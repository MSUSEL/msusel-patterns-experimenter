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
package org.apache.hadoop.hdfs.server.common;

import java.io.*;
import org.apache.hadoop.io.*;

/****************************************************************
 * A GenerationStamp is a Hadoop FS primitive, identified by a long.
 ****************************************************************/
public class GenerationStamp implements WritableComparable<GenerationStamp> {
  public static final long WILDCARD_STAMP = 1;
  public static final long FIRST_VALID_STAMP = 1000L;

  static {                                      // register a ctor
    WritableFactories.setFactory
      (GenerationStamp.class,
       new WritableFactory() {
         public Writable newInstance() { return new GenerationStamp(0); }
       });
  }

  long genstamp;

  /**
   * Create a new instance, initialized to FIRST_VALID_STAMP.
   */
  public GenerationStamp() {this(GenerationStamp.FIRST_VALID_STAMP);}

  /**
   * Create a new instance, initialized to the specified value.
   */
  GenerationStamp(long stamp) {this.genstamp = stamp;}

  /**
   * Returns the current generation stamp
   */
  public long getStamp() {
    return this.genstamp;
  }

  /**
   * Sets the current generation stamp
   */
  public void setStamp(long stamp) {
    this.genstamp = stamp;
  }

  /**
   * First increments the counter and then returns the stamp 
   */
  public synchronized long nextStamp() {
    this.genstamp++;
    return this.genstamp;
  }

  /////////////////////////////////////
  // Writable
  /////////////////////////////////////
  public void write(DataOutput out) throws IOException {
    out.writeLong(genstamp);
  }

  public void readFields(DataInput in) throws IOException {
    this.genstamp = in.readLong();
    if (this.genstamp < 0) {
      throw new IOException("Bad Generation Stamp: " + this.genstamp);
    }
  }

  /////////////////////////////////////
  // Comparable
  /////////////////////////////////////
  public static int compare(long x, long y) {
    return x < y? -1: x == y? 0: 1;
  }

  /** {@inheritDoc} */
  public int compareTo(GenerationStamp that) {
    return compare(this.genstamp, that.genstamp);
  }

  /** {@inheritDoc} */
  public boolean equals(Object o) {
    if (!(o instanceof GenerationStamp)) {
      return false;
    }
    return genstamp == ((GenerationStamp)o).genstamp;
  }

  public static boolean equalsWithWildcard(long x, long y) {
    return x == y || x == WILDCARD_STAMP || y == WILDCARD_STAMP;  
  }

  /** {@inheritDoc} */
  public int hashCode() {
    return 37 * 17 + (int) (genstamp^(genstamp>>>32));
  }
}
