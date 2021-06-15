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

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableFactories;
import org.apache.hadoop.io.WritableFactory;

/**
 * This class defines a partial listing of a directory to support
 * iterative directory listing.
 */
public class DirectoryListing implements Writable {
  static {                                      // register a ctor
    WritableFactories.setFactory
      (DirectoryListing.class,
       new WritableFactory() {
         public Writable newInstance() { return new DirectoryListing(); }
       });
  }

  private HdfsFileStatus[] partialListing;
  private int remainingEntries;
  
  /**
   * default constructor
   */
  public DirectoryListing() {
  }
  
  /**
   * constructor
   * @param partialListing a partial listing of a directory
   * @param remainingEntries number of entries that are left to be listed
   */
  public DirectoryListing(HdfsFileStatus[] partialListing, 
      int remainingEntries) {
    if (partialListing == null) {
      throw new IllegalArgumentException("partial listing should not be null");
    }
    if (partialListing.length == 0 && remainingEntries != 0) {
      throw new IllegalArgumentException("Partial listing is empty but " +
          "the number of remaining entries is not zero");
    }
    this.partialListing = partialListing;
    this.remainingEntries = remainingEntries;
  }

  /**
   * Get the partial listing of file status
   * @return the partial listing of file status
   */
  public HdfsFileStatus[] getPartialListing() {
    return partialListing;
  }
  
  /**
   * Get the number of remaining entries that are left to be listed
   * @return the number of remaining entries that are left to be listed
   */
  public int getRemainingEntries() {
    return remainingEntries;
  }
  
  /**
   * Check if there are more entries that are left to be listed
   * @return true if there are more entries that are left to be listed;
   *         return false otherwise.
   */
  public boolean hasMore() {
    return remainingEntries != 0;
  }
  
  /**
   * Get the last name in this list
   * @return the last name in the list if it is not empty; otherwise return null
   */
  public byte[] getLastName() {
    if (partialListing.length == 0) {
      return null;
    }
    return partialListing[partialListing.length-1].getLocalNameInBytes();
  }

  // Writable interface
  @Override
  public void readFields(DataInput in) throws IOException {
    int numEntries = in.readInt();
    partialListing = new HdfsFileStatus[numEntries];
    for (int i=0; i<numEntries; i++) {
      partialListing[i] = new HdfsFileStatus();
      partialListing[i].readFields(in);
    }
    remainingEntries = in.readInt();
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(partialListing.length);
    for (HdfsFileStatus fileStatus : partialListing) {
      fileStatus.write(out);
    }
    out.writeInt(remainingEntries);
  }
}
