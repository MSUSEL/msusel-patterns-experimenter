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
package org.apache.hadoop.hdfs.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableFactories;
import org.apache.hadoop.io.WritableFactory;

/**
 * Collection of blocks with their locations and the file length.
 */
public class LocatedBlocks implements Writable {
  private long fileLength;
  private List<LocatedBlock> blocks; // array of blocks with prioritized locations
  private boolean underConstruction;

  LocatedBlocks() {
    fileLength = 0;
    blocks = null;
    underConstruction = false;
  }
  
  public LocatedBlocks(long flength, List<LocatedBlock> blks, boolean isUnderConstuction) {

    fileLength = flength;
    blocks = blks;
    underConstruction = isUnderConstuction;
  }
  
  /**
   * Get located blocks.
   */
  public List<LocatedBlock> getLocatedBlocks() {
    return blocks;
  }
  
  /**
   * Get located block.
   */
  public LocatedBlock get(int index) {
    return blocks.get(index);
  }
  
  /**
   * Get number of located blocks.
   */
  public int locatedBlockCount() {
    return blocks == null ? 0 : blocks.size();
  }

  /**
   * 
   */
  public long getFileLength() {
    return this.fileLength;
  }

  /**
   * Return ture if file was under construction when 
   * this LocatedBlocks was constructed, false otherwise.
   */
  public boolean isUnderConstruction() {
    return underConstruction;
  }

  /**
   * Sets the file length of the file.
   */
  public void setFileLength(long length) {
    this.fileLength = length;
  }
  
  /**
   * Find block containing specified offset.
   * 
   * @return block if found, or null otherwise.
   */
  public int findBlock(long offset) {
    // create fake block of size 1 as a key
    LocatedBlock key = new LocatedBlock();
    key.setStartOffset(offset);
    key.getBlock().setNumBytes(1);
    Comparator<LocatedBlock> comp = 
      new Comparator<LocatedBlock>() {
        // Returns 0 iff a is inside b or b is inside a
        public int compare(LocatedBlock a, LocatedBlock b) {
          long aBeg = a.getStartOffset();
          long bBeg = b.getStartOffset();
          long aEnd = aBeg + a.getBlockSize();
          long bEnd = bBeg + b.getBlockSize();
          if(aBeg <= bBeg && bEnd <= aEnd 
              || bBeg <= aBeg && aEnd <= bEnd)
            return 0; // one of the blocks is inside the other
          if(aBeg < bBeg)
            return -1; // a's left bound is to the left of the b's
          return 1;
        }
      };
    return Collections.binarySearch(blocks, key, comp);
  }
  
  public void insertRange(int blockIdx, List<LocatedBlock> newBlocks) {
    int oldIdx = blockIdx;
    int insStart = 0, insEnd = 0;
    for(int newIdx = 0; newIdx < newBlocks.size() && oldIdx < blocks.size(); 
                                                        newIdx++) {
      long newOff = newBlocks.get(newIdx).getStartOffset();
      long oldOff = blocks.get(oldIdx).getStartOffset();
      if(newOff < oldOff) {
        insEnd++;
      } else if(newOff == oldOff) {
        // replace old cached block by the new one
        blocks.set(oldIdx, newBlocks.get(newIdx));
        if(insStart < insEnd) { // insert new blocks
          blocks.addAll(oldIdx, newBlocks.subList(insStart, insEnd));
          oldIdx += insEnd - insStart;
        }
        insStart = insEnd = newIdx+1;
        oldIdx++;
      } else {  // newOff > oldOff
        assert false : "List of LocatedBlock must be sorted by startOffset";
      }
    }
    insEnd = newBlocks.size();
    if(insStart < insEnd) { // insert new blocks
      blocks.addAll(oldIdx, newBlocks.subList(insStart, insEnd));
    }
  }
  
  public static int getInsertIndex(int binSearchResult) {
    return binSearchResult >= 0 ? binSearchResult : -(binSearchResult+1);
  }

  //////////////////////////////////////////////////
  // Writable
  //////////////////////////////////////////////////
  static {                                      // register a ctor
    WritableFactories.setFactory
      (LocatedBlocks.class,
       new WritableFactory() {
         public Writable newInstance() { return new LocatedBlocks(); }
       });
  }

  public void write(DataOutput out) throws IOException {
    out.writeLong(this.fileLength);
    out.writeBoolean(underConstruction);
    // write located blocks
    int nrBlocks = locatedBlockCount();
    out.writeInt(nrBlocks);
    if (nrBlocks == 0) {
      return;
    }
    for (LocatedBlock blk : this.blocks) {
      blk.write(out);
    }
  }
  
  public void readFields(DataInput in) throws IOException {
    this.fileLength = in.readLong();
    underConstruction = in.readBoolean();
    // read located blocks
    int nrBlocks = in.readInt();
    this.blocks = new ArrayList<LocatedBlock>(nrBlocks);
    for (int idx = 0; idx < nrBlocks; idx++) {
      LocatedBlock blk = new LocatedBlock();
      blk.readFields(in);
      this.blocks.add(blk);
    }
  }
}
