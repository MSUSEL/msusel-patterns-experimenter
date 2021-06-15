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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A simplified BufferedOutputStream with borrowed buffer, and allow users to
 * see how much data have been buffered.
 */
class SimpleBufferedOutputStream extends FilterOutputStream {
  protected byte buf[]; // the borrowed buffer
  protected int count = 0; // bytes used in buffer.

  // Constructor
  public SimpleBufferedOutputStream(OutputStream out, byte[] buf) {
    super(out);
    this.buf = buf;
  }

  private void flushBuffer() throws IOException {
    if (count > 0) {
      out.write(buf, 0, count);
      count = 0;
    }
  }

  @Override
  public void write(int b) throws IOException {
    if (count >= buf.length) {
      flushBuffer();
    }
    buf[count++] = (byte) b;
  }

  @Override
  public void write(byte b[], int off, int len) throws IOException {
    if (len >= buf.length) {
      flushBuffer();
      out.write(b, off, len);
      return;
    }
    if (len > buf.length - count) {
      flushBuffer();
    }
    System.arraycopy(b, off, buf, count, len);
    count += len;
  }

  @Override
  public synchronized void flush() throws IOException {
    flushBuffer();
    out.flush();
  }

  // Get the size of internal buffer being used.
  public int size() {
    return count;
  }
}
