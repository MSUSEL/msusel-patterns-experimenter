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
package org.apache.hadoop.hdfs.server.datanode;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.DataChecksum;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;


/**
 * BlockMetadataHeader manages metadata for data blocks on Datanodes.
 * This is not related to the Block related functionality in Namenode.
 * The biggest part of data block metadata is CRC for the block.
 */
@InterfaceAudience.Private
@InterfaceStability.Evolving
public class BlockMetadataHeader {

  static final short METADATA_VERSION = FSDataset.METADATA_VERSION;
  
  /**
   * Header includes everything except the checksum(s) themselves.
   * Version is two bytes. Following it is the DataChecksum
   * that occupies 5 bytes. 
   */
  private short version;
  private DataChecksum checksum = null;
    
  BlockMetadataHeader(short version, DataChecksum checksum) {
    this.checksum = checksum;
    this.version = version;
  }
  
  /** Get the version */
  public short getVersion() {
    return version;
  }

  /** Get the version */
  public DataChecksum getChecksum() {
    return checksum;
  }

 
  /**
   * This reads all the fields till the beginning of checksum.
   * @param in 
   * @return Metadata Header
   * @throws IOException
   */
  public static BlockMetadataHeader readHeader(DataInputStream in) throws IOException {
    return readHeader(in.readShort(), in);
  }
  
  /**
   * Reads header at the top of metadata file and returns the header.
   * 
   * @param dataset
   * @param block
   * @return
   * @throws IOException
   */
  static BlockMetadataHeader readHeader(File file) throws IOException {
    DataInputStream in = null;
    try {
      in = new DataInputStream(new BufferedInputStream(
                               new FileInputStream(file)));
      return readHeader(in);
    } finally {
      IOUtils.closeStream(in);
    }
  }
  
  // Version is already read.
  private static BlockMetadataHeader readHeader(short version, DataInputStream in) 
                                   throws IOException {
    DataChecksum checksum = DataChecksum.newDataChecksum(in);
    return new BlockMetadataHeader(version, checksum);
  }
  
  /**
   * This writes all the fields till the beginning of checksum.
   * @param out DataOutputStream
   * @param header 
   * @return 
   * @throws IOException
   */
  private static void writeHeader(DataOutputStream out, 
                                  BlockMetadataHeader header) 
                                  throws IOException {
    out.writeShort(header.getVersion());
    header.getChecksum().writeHeader(out);
  }
  
  /**
   * Writes all the fields till the beginning of checksum.
   * @param out
   * @param checksum
   * @throws IOException
   */
  static void writeHeader(DataOutputStream out, DataChecksum checksum)
                         throws IOException {
    writeHeader(out, new BlockMetadataHeader(METADATA_VERSION, checksum));
  }

  /**
   * Returns the size of the header
   */
  static int getHeaderSize() {
    return Short.SIZE/Byte.SIZE + DataChecksum.getChecksumHeaderSize();
  }
}

