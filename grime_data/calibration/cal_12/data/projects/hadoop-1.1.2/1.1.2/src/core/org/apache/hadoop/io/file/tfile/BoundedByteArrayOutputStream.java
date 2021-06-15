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

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A byte array backed output stream with a limit. The limit should be smaller
 * than the buffer capacity. The object can be reused through <code>reset</code>
 * API and choose different limits in each round.
 */
class BoundedByteArrayOutputStream extends OutputStream {
  private final byte[] buffer;
  private int limit;
  private int count;

  public BoundedByteArrayOutputStream(int capacity) {
    this(capacity, capacity);
  }

  public BoundedByteArrayOutputStream(int capacity, int limit) {
    if ((capacity < limit) || (capacity | limit) < 0) {
      throw new IllegalArgumentException("Invalid capacity/limit");
    }
    this.buffer = new byte[capacity];
    this.limit = limit;
    this.count = 0;
  }

  @Override
  public void write(int b) throws IOException {
    if (count >= limit) {
      throw new EOFException("Reaching the limit of the buffer.");
    }
    buffer[count++] = (byte) b;
  }

  @Override
  public void write(byte b[], int off, int len) throws IOException {
    if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length)
        || ((off + len) < 0)) {
      throw new IndexOutOfBoundsException();
    } else if (len == 0) {
      return;
    }

    if (count + len > limit) {
      throw new EOFException("Reach the limit of the buffer");
    }

    System.arraycopy(b, off, buffer, count, len);
    count += len;
  }

  public void reset(int newlim) {
    if (newlim > buffer.length) {
      throw new IndexOutOfBoundsException("Limit exceeds buffer size");
    }
    this.limit = newlim;
    this.count = 0;
  }

  public void reset() {
    this.limit = buffer.length;
    this.count = 0;
  }

  public int getLimit() {
    return limit;
  }

  public byte[] getBuffer() {
    return buffer;
  }

  public int size() {
    return count;
  }
}
