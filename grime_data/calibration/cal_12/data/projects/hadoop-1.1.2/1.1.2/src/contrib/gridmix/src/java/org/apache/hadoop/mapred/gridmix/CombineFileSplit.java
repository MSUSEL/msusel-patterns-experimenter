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
package org.apache.hadoop.mapred.gridmix;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;

/**
 * A sub-collection of input files. 
 * 
 * Unlike {@link FileSplit}, CombineFileSplit class does not represent 
 * a split of a file, but a split of input files into smaller sets. 
 * A split may contain blocks from different file but all 
 * the blocks in the same split are probably local to some rack <br> 
 * CombineFileSplit can be used to implement {@link RecordReader}'s, 
 * with reading one record per file.
 * 
 * @see FileSplit
 * @see CombineFileInputFormat 
 */
public class CombineFileSplit extends InputSplit implements Writable {

  private Path[] paths;
  private long[] startoffset;
  private long[] lengths;
  private String[] locations;
  private long totLength;

  /**
   * default constructor
   */
  public CombineFileSplit() {}
  public CombineFileSplit(Path[] files, long[] start, 
                          long[] lengths, String[] locations) {
    initSplit(files, start, lengths, locations);
  }

  public CombineFileSplit(Path[] files, long[] lengths) {
    long[] startoffset = new long[files.length];
    for (int i = 0; i < startoffset.length; i++) {
      startoffset[i] = 0;
    }
    String[] locations = new String[files.length];
    for (int i = 0; i < locations.length; i++) {
      locations[i] = "";
    }
    initSplit(files, startoffset, lengths, locations);
  }
  
  private void initSplit(Path[] files, long[] start, 
                         long[] lengths, String[] locations) {
    this.startoffset = start;
    this.lengths = lengths;
    this.paths = files;
    this.totLength = 0;
    this.locations = locations;
    for(long length : lengths) {
      totLength += length;
    }
  }

  /**
   * Copy constructor
   */
  public CombineFileSplit(CombineFileSplit old) throws IOException {
    this(old.getPaths(), old.getStartOffsets(),
         old.getLengths(), old.getLocations());
  }

  public long getLength() {
    return totLength;
  }

  /** Returns an array containing the start offsets of the files in the split*/ 
  public long[] getStartOffsets() {
    return startoffset;
  }
  
  /** Returns an array containing the lengths of the files in the split*/ 
  public long[] getLengths() {
    return lengths;
  }

  /** Returns the start offset of the i<sup>th</sup> Path */
  public long getOffset(int i) {
    return startoffset[i];
  }
  
  /** Returns the length of the i<sup>th</sup> Path */
  public long getLength(int i) {
    return lengths[i];
  }
  
  /** Returns the number of Paths in the split */
  public int getNumPaths() {
    return paths.length;
  }

  /** Returns the i<sup>th</sup> Path */
  public Path getPath(int i) {
    return paths[i];
  }
  
  /** Returns all the Paths in the split */
  public Path[] getPaths() {
    return paths;
  }

  /** Returns all the Paths where this input-split resides */
  public String[] getLocations() throws IOException {
    return locations;
  }

  public void readFields(DataInput in) throws IOException {
    totLength = in.readLong();
    int arrLength = in.readInt();
    lengths = new long[arrLength];
    for(int i=0; i<arrLength;i++) {
      lengths[i] = in.readLong();
    }
    int filesLength = in.readInt();
    paths = new Path[filesLength];
    for(int i=0; i<filesLength;i++) {
      paths[i] = new Path(Text.readString(in));
    }
    arrLength = in.readInt();
    startoffset = new long[arrLength];
    for(int i=0; i<arrLength;i++) {
      startoffset[i] = in.readLong();
    }
  }

  public void write(DataOutput out) throws IOException {
    out.writeLong(totLength);
    out.writeInt(lengths.length);
    for(long length : lengths) {
      out.writeLong(length);
    }
    out.writeInt(paths.length);
    for(Path p : paths) {
      Text.writeString(out, p.toString());
    }
    out.writeInt(startoffset.length);
    for(long length : startoffset) {
      out.writeLong(length);
    }
  }
  
  @Override
 public String toString() {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < paths.length; i++) {
      if (i == 0 ) {
        sb.append("Paths:");
      }
      sb.append(paths[i].toUri().getPath() + ":" + startoffset[i] +
                "+" + lengths[i]);
      if (i < paths.length -1) {
        sb.append(",");
      }
    }
    if (locations != null) {
      String locs = "";
      StringBuffer locsb = new StringBuffer();
      for (int i = 0; i < locations.length; i++) {
        locsb.append(locations[i] + ":");
      }
      locs = locsb.toString();
      sb.append(" Locations:" + locs + "; ");
    }
    return sb.toString();
  }
}
