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
package org.apache.hadoop.fs;

import java.io.BufferedInputStream;
import java.io.IOException;


/**
 * A class optimizes reading from FSInputStream by bufferring
 */


public class BufferedFSInputStream extends BufferedInputStream
implements Seekable, PositionedReadable {
  /**
   * Creates a <code>BufferedFSInputStream</code>
   * with the specified buffer size,
   * and saves its  argument, the input stream
   * <code>in</code>, for later use.  An internal
   * buffer array of length  <code>size</code>
   * is created and stored in <code>buf</code>.
   *
   * @param   in     the underlying input stream.
   * @param   size   the buffer size.
   * @exception IllegalArgumentException if size <= 0.
   */
  public BufferedFSInputStream(FSInputStream in, int size) {
    super(in, size);
  }

  public long getPos() throws IOException {
    return ((FSInputStream)in).getPos()-(count-pos);
  }

  public long skip(long n) throws IOException {
    if (n <= 0) {
      return 0;
    }

    seek(getPos()+n);
    return n;
  }

  public void seek(long pos) throws IOException {
    if( pos<0 ) {
      return;
    }
    // optimize: check if the pos is in the buffer
    long end = ((FSInputStream)in).getPos();
    long start = end - count;
    if( pos>=start && pos<end) {
      this.pos = (int)(pos-start);
      return;
    }

    // invalidate buffer
    this.pos = 0;
    this.count = 0;

    ((FSInputStream)in).seek(pos);
  }

  public boolean seekToNewSource(long targetPos) throws IOException {
    pos = 0;
    count = 0;
    return ((FSInputStream)in).seekToNewSource(targetPos);
  }

  public int read(long position, byte[] buffer, int offset, int length) throws IOException {
    return ((FSInputStream)in).read(position, buffer, offset, length) ;
  }

  public void readFully(long position, byte[] buffer, int offset, int length) throws IOException {
    ((FSInputStream)in).readFully(position, buffer, offset, length);
  }

  public void readFully(long position, byte[] buffer) throws IOException {
    ((FSInputStream)in).readFully(position, buffer);
  }
}
