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
package org.apache.hadoop.io;

import java.io.*;

/** A WritableComparable for longs in a variable-length format. Such values take
 *  between one and five bytes.  Smaller values take fewer bytes.
 *  
 *  @see org.apache.hadoop.io.WritableUtils#readVLong(DataInput)
 */
public class VLongWritable implements WritableComparable {
  private long value;

  public VLongWritable() {}

  public VLongWritable(long value) { set(value); }

  /** Set the value of this LongWritable. */
  public void set(long value) { this.value = value; }

  /** Return the value of this LongWritable. */
  public long get() { return value; }

  public void readFields(DataInput in) throws IOException {
    value = WritableUtils.readVLong(in);
  }

  public void write(DataOutput out) throws IOException {
    WritableUtils.writeVLong(out, value);
  }

  /** Returns true iff <code>o</code> is a VLongWritable with the same value. */
  public boolean equals(Object o) {
    if (!(o instanceof VLongWritable))
      return false;
    VLongWritable other = (VLongWritable)o;
    return this.value == other.value;
  }

  public int hashCode() {
    return (int)value;
  }

  /** Compares two VLongWritables. */
  public int compareTo(Object o) {
    long thisValue = this.value;
    long thatValue = ((VLongWritable)o).value;
    return (thisValue < thatValue ? -1 : (thisValue == thatValue ? 0 : 1));
  }

  public String toString() {
    return Long.toString(value);
  }

}

