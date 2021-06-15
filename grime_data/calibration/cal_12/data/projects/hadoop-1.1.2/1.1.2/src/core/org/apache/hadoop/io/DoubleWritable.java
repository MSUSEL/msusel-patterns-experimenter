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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Writable for Double values.
 */
public class DoubleWritable implements WritableComparable {

  private double value = 0.0;
  
  public DoubleWritable() {
    
  }
  
  public DoubleWritable(double value) {
    set(value);
  }
  
  public void readFields(DataInput in) throws IOException {
    value = in.readDouble();
  }

  public void write(DataOutput out) throws IOException {
    out.writeDouble(value);
  }
  
  public void set(double value) { this.value = value; }
  
  public double get() { return value; }

  /**
   * Returns true iff <code>o</code> is a DoubleWritable with the same value.
   */
  public boolean equals(Object o) {
    if (!(o instanceof DoubleWritable)) {
      return false;
    }
    DoubleWritable other = (DoubleWritable)o;
    return this.value == other.value;
  }
  
  public int hashCode() {
    return (int)Double.doubleToLongBits(value);
  }
  
  public int compareTo(Object o) {
    DoubleWritable other = (DoubleWritable)o;
    return (value < other.value ? -1 : (value == other.value ? 0 : 1));
  }
  
  public String toString() {
    return Double.toString(value);
  }

  /** A Comparator optimized for DoubleWritable. */ 
  public static class Comparator extends WritableComparator {
    public Comparator() {
      super(DoubleWritable.class);
    }

    public int compare(byte[] b1, int s1, int l1,
                       byte[] b2, int s2, int l2) {
      double thisValue = readDouble(b1, s1);
      double thatValue = readDouble(b2, s2);
      return (thisValue < thatValue ? -1 : (thisValue == thatValue ? 0 : 1));
    }
  }

  static {                                        // register this comparator
    WritableComparator.define(DoubleWritable.class, new Comparator());
  }

}

