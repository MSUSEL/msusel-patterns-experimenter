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
package org.apache.hadoop.io.file.tfile;

import java.util.Comparator;

import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.WritableComparator;

class CompareUtils {
  /**
   * Prevent the instantiation of class.
   */
  private CompareUtils() {
    // nothing
  }

  /**
   * A comparator to compare anything that implements {@link RawComparable}
   * using a customized comparator.
   */
  public static final class BytesComparator implements
      Comparator<RawComparable> {
    private RawComparator<Object> cmp;

    public BytesComparator(RawComparator<Object> cmp) {
      this.cmp = cmp;
    }

    @Override
    public int compare(RawComparable o1, RawComparable o2) {
      return compare(o1.buffer(), o1.offset(), o1.size(), o2.buffer(), o2
          .offset(), o2.size());
    }

    public int compare(byte[] a, int off1, int len1, byte[] b, int off2,
        int len2) {
      return cmp.compare(a, off1, len1, b, off2, len2);
    }
  }

  /**
   * Interface for all objects that has a single integer magnitude.
   */
  static interface Scalar {
    long magnitude();
  }

  static final class ScalarLong implements Scalar {
    private long magnitude;

    public ScalarLong(long m) {
      magnitude = m;
    }

    public long magnitude() {
      return magnitude;
    }
  }

  public static final class ScalarComparator implements Comparator<Scalar> {
    @Override
    public int compare(Scalar o1, Scalar o2) {
      long diff = o1.magnitude() - o2.magnitude();
      if (diff < 0) return -1;
      if (diff > 0) return 1;
      return 0;
    }
  }

  public static final class MemcmpRawComparator implements
      RawComparator<Object> {
    @Override
    public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
      return WritableComparator.compareBytes(b1, s1, l1, b2, s2, l2);
    }

    @Override
    public int compare(Object o1, Object o2) {
      throw new RuntimeException("Object comparison not supported");
    }
  }
}
