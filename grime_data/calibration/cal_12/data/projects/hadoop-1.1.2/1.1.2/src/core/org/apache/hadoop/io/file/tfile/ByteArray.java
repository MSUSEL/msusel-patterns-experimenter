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

import org.apache.hadoop.io.BytesWritable;

/**
 * Adaptor class to wrap byte-array backed objects (including java byte array)
 * as RawComparable objects.
 */
public final class ByteArray implements RawComparable {
  private final byte[] buffer;
  private final int offset;
  private final int len;

  /**
   * Constructing a ByteArray from a {@link BytesWritable}.
   * 
   * @param other
   */
  public ByteArray(BytesWritable other) {
    this(other.get(), 0, other.getSize());
  }

  /**
   * Wrap a whole byte array as a RawComparable.
   * 
   * @param buffer
   *          the byte array buffer.
   */
  public ByteArray(byte[] buffer) {
    this(buffer, 0, buffer.length);
  }

  /**
   * Wrap a partial byte array as a RawComparable.
   * 
   * @param buffer
   *          the byte array buffer.
   * @param offset
   *          the starting offset
   * @param len
   *          the length of the consecutive bytes to be wrapped.
   */
  public ByteArray(byte[] buffer, int offset, int len) {
    if ((offset | len | (buffer.length - offset - len)) < 0) {
      throw new IndexOutOfBoundsException();
    }
    this.buffer = buffer;
    this.offset = offset;
    this.len = len;
  }

  /**
   * @return the underlying buffer.
   */
  @Override
  public byte[] buffer() {
    return buffer;
  }

  /**
   * @return the offset in the buffer.
   */
  @Override
  public int offset() {
    return offset;
  }

  /**
   * @return the size of the byte array.
   */
  @Override
  public int size() {
    return len;
  }
}
