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
package org.apache.hadoop.hdfs.server.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

/** A class to implement an array of BlockLocations
 *  It provide efficient customized serialization/deserialization methods
 *  in stead of using the default array (de)serialization provided by RPC
 */
public class BlocksWithLocations implements Writable {

  /**
   * A class to keep track of a block and its locations
   */
  public static class BlockWithLocations  implements Writable {
    Block block;
    String datanodeIDs[];
    
    /** default constructor */
    public BlockWithLocations() {
      block = new Block();
      datanodeIDs = null;
    }
    
    /** constructor */
    public BlockWithLocations(Block b, String[] datanodes) {
      block = b;
      datanodeIDs = datanodes;
    }
    
    /** get the block */
    public Block getBlock() {
      return block;
    }
    
    /** get the block's locations */
    public String[] getDatanodes() {
      return datanodeIDs;
    }
    
    /** deserialization method */
    public void readFields(DataInput in) throws IOException {
      block.readFields(in);
      int len = WritableUtils.readVInt(in); // variable length integer
      datanodeIDs = new String[len];
      for(int i=0; i<len; i++) {
        datanodeIDs[i] = Text.readString(in);
      }
    }
    
    /** serialization method */
    public void write(DataOutput out) throws IOException {
      block.write(out);
      WritableUtils.writeVInt(out, datanodeIDs.length); // variable length int
      for(String id:datanodeIDs) {
        Text.writeString(out, id);
      }
    }
  }

  private BlockWithLocations[] blocks;

  /** default constructor */
  BlocksWithLocations() {
  }

  /** Constructor with one parameter */
  public BlocksWithLocations( BlockWithLocations[] blocks ) {
    this.blocks = blocks;
  }

  /** getter */
  public BlockWithLocations[] getBlocks() {
    return blocks;
  }

  /** serialization method */
  public void write( DataOutput out ) throws IOException {
    WritableUtils.writeVInt(out, blocks.length);
    for(int i=0; i<blocks.length; i++) {
      blocks[i].write(out);
    }
  }

  /** deserialization method */
  public void readFields(DataInput in) throws IOException {
    int len = WritableUtils.readVInt(in);
    blocks = new BlockWithLocations[len];
    for(int i=0; i<len; i++) {
      blocks[i] = new BlockWithLocations();
      blocks[i].readFields(in);
    }
  }
}
