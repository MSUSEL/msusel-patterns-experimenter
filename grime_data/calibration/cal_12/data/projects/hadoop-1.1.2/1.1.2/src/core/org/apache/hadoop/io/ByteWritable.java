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

/** A WritableComparable for a single byte. */
public class ByteWritable implements WritableComparable {
  private byte value;

  public ByteWritable() {}

  public ByteWritable(byte value) { set(value); }

  /** Set the value of this ByteWritable. */
  public void set(byte value) { this.value = value; }

  /** Return the value of this ByteWritable. */
  public byte get() { return value; }

  public void readFields(DataInput in) throws IOException {
    value = in.readByte();
  }

  public void write(DataOutput out) throws IOException {
    out.writeByte(value);
  }

  /** Returns true iff <code>o</code> is a ByteWritable with the same value. */
  public boolean equals(Object o) {
    if (!(o instanceof ByteWritable)) {
      return false;
    }
    ByteWritable other = (ByteWritable)o;
    return this.value == other.value;
  }

  public int hashCode() {
    return (int)value;
  }

  /** Compares two ByteWritables. */
  public int compareTo(Object o) {
    int thisValue = this.value;
    int thatValue = ((ByteWritable)o).value;
    return (thisValue < thatValue ? -1 : (thisValue == thatValue ? 0 : 1));
  }

  public String toString() {
    return Byte.toString(value);
  }

  /** A Comparator optimized for ByteWritable. */ 
  public static class Comparator extends WritableComparator {
    public Comparator() {
      super(ByteWritable.class);
    }

    public int compare(byte[] b1, int s1, int l1,
                       byte[] b2, int s2, int l2) {
      byte thisValue = b1[s1];
      byte thatValue = b2[s2];
      return (thisValue < thatValue ? -1 : (thisValue == thatValue ? 0 : 1));
    }
  }

  static {                                        // register this comparator
    WritableComparator.define(ByteWritable.class, new Comparator());
  }
}

