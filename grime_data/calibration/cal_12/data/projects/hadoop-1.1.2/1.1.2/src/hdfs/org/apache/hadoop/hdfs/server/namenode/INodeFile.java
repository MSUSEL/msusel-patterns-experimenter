/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.apache.hadoop.hdfs.server.namenode;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.fs.permission.PermissionStatus;
import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.hdfs.server.namenode.BlocksMap.BlockInfo;

class INodeFile extends INode {
  static final FsPermission UMASK = FsPermission.createImmutable((short)0111);

  //Number of bits for Block size
  static final short BLOCKBITS = 48;

  //Header mask 64-bit representation
  //Format: [16 bits for replication][48 bits for PreferredBlockSize]
  static final long HEADERMASK = 0xffffL << BLOCKBITS;

  protected long header;

  protected BlockInfo blocks[] = null;

  INodeFile(PermissionStatus permissions,
            int nrBlocks, short replication, long modificationTime,
            long atime, long preferredBlockSize) {
    this(permissions, new BlockInfo[nrBlocks], replication,
        modificationTime, atime, preferredBlockSize);
  }

  protected INodeFile() {
    blocks = null;
    header = 0;
  }

  protected INodeFile(PermissionStatus permissions, BlockInfo[] blklist,
                      short replication, long modificationTime,
                      long atime, long preferredBlockSize) {
    super(permissions, modificationTime, atime);
    this.setReplication(replication);
    this.setPreferredBlockSize(preferredBlockSize);
    blocks = blklist;
  }

  /**
   * Set the {@link FsPermission} of this {@link INodeFile}.
   * Since this is a file,
   * the {@link FsAction#EXECUTE} action, if any, is ignored.
   */
  protected void setPermission(FsPermission permission) {
    super.setPermission(permission.applyUMask(UMASK));
  }

  public boolean isDirectory() {
    return false;
  }

  /**
   * Get block replication for the file 
   * @return block replication value
   */
  public short getReplication() {
    return (short) ((header & HEADERMASK) >> BLOCKBITS);
  }

  public void setReplication(short replication) {
    if(replication <= 0)
       throw new IllegalArgumentException("Unexpected value for the replication");
    header = ((long)replication << BLOCKBITS) | (header & ~HEADERMASK);
  }

  /**
   * Get preferred block size for the file
   * @return preferred block size in bytes
   */
  public long getPreferredBlockSize() {
        return header & ~HEADERMASK;
  }

  public void setPreferredBlockSize(long preferredBlkSize)
  {
    if((preferredBlkSize < 0) || (preferredBlkSize > ~HEADERMASK ))
       throw new IllegalArgumentException("Unexpected value for the block size");
    header = (header & HEADERMASK) | (preferredBlkSize & ~HEADERMASK);
  }

  /**
   * Get file blocks 
   * @return file blocks
   */
  BlockInfo[] getBlocks() {
    return this.blocks;
  }

  /**
   * add a block to the block list
   */
  void addBlock(BlockInfo newblock) {
    if (this.blocks == null) {
      this.blocks = new BlockInfo[1];
      this.blocks[0] = newblock;
    } else {
      int size = this.blocks.length;
      BlockInfo[] newlist = new BlockInfo[size + 1];
      System.arraycopy(this.blocks, 0, newlist, 0, size);
      newlist[size] = newblock;
      this.blocks = newlist;
    }
  }

  /**
   * Set file block
   */
  void setBlock(int idx, BlockInfo blk) {
    this.blocks[idx] = blk;
  }

  int collectSubtreeBlocksAndClear(List<Block> v) {
    parent = null;
    for (BlockInfo blk : blocks) {
      v.add(blk);
      blk.setINode(null);
    }
    blocks = null;
    return 1;
  }

  /** {@inheritDoc} */
  long[] computeContentSummary(long[] summary) {
    long bytes = 0;
    for(Block blk : blocks) {
      bytes += blk.getNumBytes();
    }
    summary[0] += bytes;
    summary[1]++;
    summary[3] += diskspaceConsumed();
    return summary;
  }

  

  @Override
  DirCounts spaceConsumedInTree(DirCounts counts) {
    counts.nsCount += 1;
    counts.dsCount += diskspaceConsumed();
    return counts;
  }

  long diskspaceConsumed() {
    return diskspaceConsumed(blocks);
  }
  
  long diskspaceConsumed(Block[] blkArr) {
    long size = 0;
    for (Block blk : blkArr) {
      if (blk != null) {
        size += blk.getNumBytes();
      }
    }
    /* If the last block is being written to, use prefferedBlockSize
     * rather than the actual block size.
     */
    if (blkArr.length > 0 && blkArr[blkArr.length-1] != null && 
        isUnderConstruction()) {
      size += getPreferredBlockSize() - blocks[blocks.length-1].getNumBytes();
    }
    return size * getReplication();
  }
  
  /**
   * Return the penultimate allocated block for this file.
   */
  Block getPenultimateBlock() {
    if (blocks == null || blocks.length <= 1) {
      return null;
    }
    return blocks[blocks.length - 2];
  }

  INodeFileUnderConstruction toINodeFileUnderConstruction(
      String clientName, String clientMachine, DatanodeDescriptor clientNode
      ) throws IOException {
    if (isUnderConstruction()) {
      return (INodeFileUnderConstruction)this;
    }
    return new INodeFileUnderConstruction(name,
        getReplication(), modificationTime, getPreferredBlockSize(),
        blocks, getPermissionStatus(),
        clientName, clientMachine, clientNode);
  }

  /**
   * Return the last block in this file, or null if there are no blocks.
   */
  Block getLastBlock() {
    if (this.blocks == null || this.blocks.length == 0)
      return null;
    return this.blocks[this.blocks.length - 1];
  }
}
