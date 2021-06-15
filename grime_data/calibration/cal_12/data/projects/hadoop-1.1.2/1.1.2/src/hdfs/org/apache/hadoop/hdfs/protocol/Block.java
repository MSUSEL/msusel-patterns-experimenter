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

import java.io.*;

import org.apache.hadoop.hdfs.server.common.GenerationStamp;
import org.apache.hadoop.io.*;

/**************************************************
 * A Block is a Hadoop FS primitive, identified by a 
 * long.
 *
 **************************************************/
public class Block implements Writable, Comparable<Block> {

  static {                                      // register a ctor
    WritableFactories.setFactory
      (Block.class,
       new WritableFactory() {
         public Writable newInstance() { return new Block(); }
       });
  }

  // generation stamp of blocks that pre-date the introduction of
  // a generation stamp.
  public static final long GRANDFATHER_GENERATION_STAMP = 0;

  /**
   */
  public static boolean isBlockFilename(File f) {
    String name = f.getName();
    if ( name.startsWith( "blk_" ) && 
        name.indexOf( '.' ) < 0 ) {
      return true;
    } else {
      return false;
    }
  }

  static long filename2id(String name) {
    return Long.parseLong(name.substring("blk_".length()));
  }

  private long blockId;
  private long numBytes;
  private long generationStamp;

  public Block() {this(0, 0, 0);}

  public Block(final long blkid, final long len, final long generationStamp) {
    set(blkid, len, generationStamp);
  }

  public Block(final long blkid) {this(blkid, 0, GenerationStamp.WILDCARD_STAMP);}

  public Block(Block blk) {this(blk.blockId, blk.numBytes, blk.generationStamp);}

  /**
   * Find the blockid from the given filename
   */
  public Block(File f, long len, long genstamp) {
    this(filename2id(f.getName()), len, genstamp);
  }

  public void set(long blkid, long len, long genStamp) {
    this.blockId = blkid;
    this.numBytes = len;
    this.generationStamp = genStamp;
  }
  /**
   */
  public long getBlockId() {
    return blockId;
  }
  
  public void setBlockId(long bid) {
    blockId = bid;
  }

  /**
   */
  public String getBlockName() {
    return "blk_" + String.valueOf(blockId);
  }

  /**
   */
  public long getNumBytes() {
    return numBytes;
  }
  public void setNumBytes(long len) {
    this.numBytes = len;
  }

  public long getGenerationStamp() {
    return generationStamp;
  }
  
  public void setGenerationStamp(long stamp) {
    generationStamp = stamp;
  }

  public Block getWithWildcardGS() {
    return new Block(blockId, numBytes, GenerationStamp.WILDCARD_STAMP);
  }

  /**
   */
  public String toString() {
    return getBlockName() + "_" + getGenerationStamp();
  }

  /////////////////////////////////////
  // Writable
  /////////////////////////////////////
  public void write(DataOutput out) throws IOException {
    out.writeLong(blockId);
    out.writeLong(numBytes);
    out.writeLong(generationStamp);
  }

  public void readFields(DataInput in) throws IOException {
    this.blockId = in.readLong();
    this.numBytes = in.readLong();
    this.generationStamp = in.readLong();
    if (numBytes < 0) {
      throw new IOException("Unexpected block size: " + numBytes);
    }
  }

  /////////////////////////////////////
  // Comparable
  /////////////////////////////////////
  static void validateGenerationStamp(long generationstamp) {
    if (generationstamp == GenerationStamp.WILDCARD_STAMP) {
      throw new IllegalStateException("generationStamp (=" + generationstamp
          + ") == GenerationStamp.WILDCARD_STAMP");
    }    
  }

  /** {@inheritDoc} */
  public int compareTo(Block b) {
    //Wildcard generationStamp is NOT ALLOWED here
    validateGenerationStamp(this.generationStamp);
    validateGenerationStamp(b.generationStamp);

    if (blockId < b.blockId) {
      return -1;
    } else if (blockId == b.blockId) {
      return GenerationStamp.compare(generationStamp, b.generationStamp);
    } else {
      return 1;
    }
  }

  /** {@inheritDoc} */
  public boolean equals(Object o) {
    if (!(o instanceof Block)) {
      return false;
    }
    final Block that = (Block)o;
    //Wildcard generationStamp is ALLOWED here
    return this.blockId == that.blockId
      && GenerationStamp.equalsWithWildcard(
          this.generationStamp, that.generationStamp);
  }

  /** {@inheritDoc} */
  public int hashCode() {
    //GenerationStamp is IRRELEVANT and should not be used here
    return 37 * 17 + (int) (blockId^(blockId>>>32));
  }
}
