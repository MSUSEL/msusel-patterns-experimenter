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

import java.util.*;
import java.io.*;

public class RandomDatum implements WritableComparable {
  private int length;
  private byte[] data;

  public RandomDatum() {}

  public RandomDatum(Random random) {
    length = 10 + (int) Math.pow(10.0, random.nextFloat() * 3.0);
    data = new byte[length];
    random.nextBytes(data);
  }

  public int getLength() {
    return length;
  }
  
  public void write(DataOutput out) throws IOException {
    out.writeInt(length);
    out.write(data);
  }

  public void readFields(DataInput in) throws IOException {
    length = in.readInt();
    if (data == null || length > data.length)
      data = new byte[length];
    in.readFully(data, 0, length);
  }

  public int compareTo(Object o) {
    RandomDatum that = (RandomDatum)o;
    return WritableComparator.compareBytes(this.data, 0, this.length,
                                           that.data, 0, that.length);
  }

  public boolean equals(Object o) {
    return compareTo(o) == 0;
  }

  private static final char[] HEX_DIGITS =
  {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

  /** Returns a string representation of this object. */
  public String toString() {
    StringBuffer buf = new StringBuffer(length*2);
    for (int i = 0; i < length; i++) {
      int b = data[i];
      buf.append(HEX_DIGITS[(b >> 4) & 0xf]);
      buf.append(HEX_DIGITS[b & 0xf]);
    }
    return buf.toString();
  }

  public static class Generator {
    Random random;

    private RandomDatum key;
    private RandomDatum value;
    
    public Generator() { random = new Random(); }
    public Generator(int seed) { random = new Random(seed); }

    public RandomDatum getKey() { return key; }
    public RandomDatum getValue() { return value; }

    public void next() {
      key = new RandomDatum(random);
      value = new RandomDatum(random);
    }
  }

  /** A WritableComparator optimized for RandomDatum. */
  public static class Comparator extends WritableComparator {
    public Comparator() {
      super(RandomDatum.class);
    }

    public int compare(byte[] b1, int s1, int l1,
                       byte[] b2, int s2, int l2) {
      int n1 = readInt(b1, s1);
      int n2 = readInt(b2, s2);
      return compareBytes(b1, s1+4, n1, b2, s2+4, n2);
    }
  }

}
