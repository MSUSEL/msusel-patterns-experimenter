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

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableFactories;
import org.apache.hadoop.io.WritableFactory;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;


/**
 * A block and the full path information to the block data file and
 * the metadata file stored on the local file system.
 */
@InterfaceAudience.Private
@InterfaceStability.Evolving
public class BlockLocalPathInfo implements Writable {
  static final WritableFactory FACTORY = new WritableFactory() {
    public Writable newInstance() { return new BlockLocalPathInfo(); }
  };
  static {                                      // register a ctor
    WritableFactories.setFactory(BlockLocalPathInfo.class, FACTORY);
  }

  private Block block;
  private String localBlockPath = "";  // local file storing the data
  private String localMetaPath = "";   // local file storing the checksum

  public BlockLocalPathInfo() {}

  /**
   * Constructs BlockLocalPathInfo.
   * @param b The block corresponding to this lock path info.
   * @param file Block data file.
   * @param metafile Metadata file for the block.
   */
  public BlockLocalPathInfo(Block b, String file, String metafile) {
    block = b;
    localBlockPath = file;
    localMetaPath = metafile;
  }

  /**
   * Get the Block data file.
   * @return Block data file.
   */
  public String getBlockPath() {return localBlockPath;}
  
  /**
   * Get the Block metadata file.
   * @return Block metadata file.
   */
  public String getMetaPath() {return localMetaPath;}

  @Override
  public void write(DataOutput out) throws IOException {
    block.write(out);
    Text.writeString(out, localBlockPath);
    Text.writeString(out, localMetaPath);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    block = new Block();
    block.readFields(in);
    localBlockPath = Text.readString(in);
    localMetaPath = Text.readString(in);
  }
  
  /**
   * Get number of bytes in the block.
   * @return Number of bytes in the block.
   */
  public long getNumBytes() {
    return block.getNumBytes();
  }
}
