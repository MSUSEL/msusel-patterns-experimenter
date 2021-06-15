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

/**
 * Interface supported by {@link org.apache.hadoop.io.WritableComparable}
 * types supporting ordering/permutation by a representative set of bytes.
 */
public abstract class BinaryComparable implements Comparable<BinaryComparable> {

  /**
   * Return n st bytes 0..n-1 from {#getBytes()} are valid.
   */
  public abstract int getLength();

  /**
   * Return representative byte array for this instance.
   */
  public abstract byte[] getBytes();

  /**
   * Compare bytes from {#getBytes()}.
   * @see org.apache.hadoop.io.WritableComparator#compareBytes(byte[],int,int,byte[],int,int)
   */
  public int compareTo(BinaryComparable other) {
    if (this == other)
      return 0;
    return WritableComparator.compareBytes(getBytes(), 0, getLength(),
             other.getBytes(), 0, other.getLength());
  }

  /**
   * Compare bytes from {#getBytes()} to those provided.
   */
  public int compareTo(byte[] other, int off, int len) {
    return WritableComparator.compareBytes(getBytes(), 0, getLength(),
             other, off, len);
  }

  /**
   * Return true if bytes from {#getBytes()} match.
   */
  public boolean equals(Object other) {
    if (!(other instanceof BinaryComparable))
      return false;
    BinaryComparable that = (BinaryComparable)other;
    if (this.getLength() != that.getLength())
      return false;
    return this.compareTo(that) == 0;
  }

  /**
   * Return a hash of the bytes returned from {#getBytes()}.
   * @see org.apache.hadoop.io.WritableComparator#hashBytes(byte[],int)
   */
  public int hashCode() {
    return WritableComparator.hashBytes(getBytes(), getLength());
  }

}
