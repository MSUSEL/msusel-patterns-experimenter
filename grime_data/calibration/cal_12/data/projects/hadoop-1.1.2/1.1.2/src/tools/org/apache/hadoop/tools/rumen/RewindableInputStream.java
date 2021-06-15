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
package org.apache.hadoop.tools.rumen;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A simple wrapper class to make any input stream "rewindable". It could be
 * made more memory efficient by grow the internal buffer adaptively.
 */
public class RewindableInputStream extends InputStream {
  private InputStream input;

  /**
   * Constructor.
   * 
   * @param input
   */
  public RewindableInputStream(InputStream input) {
    this(input, 1024 * 1024);
  }

  /**
   * Constructor
   * 
   * @param input
   *          input stream.
   * @param maxBytesToRemember
   *          Maximum number of bytes we need to remember at the beginning of
   *          the stream. If {@link #rewind()} is called after so many bytes are
   *          read from the stream, {@link #rewind()} would fail.
   */
  public RewindableInputStream(InputStream input, int maxBytesToRemember) {
    this.input = new BufferedInputStream(input, maxBytesToRemember);
    this.input.mark(maxBytesToRemember);
  }

  @Override
  public int read() throws IOException {
    return input.read();
  }

  @Override
  public int read(byte[] buffer, int offset, int length) throws IOException {
    return input.read(buffer, offset, length);
  }

  @Override
  public void close() throws IOException {
    input.close();
  }

  public InputStream rewind() throws IOException {
    try {
      input.reset();
      return this;
    } catch (IOException e) {
      throw new IOException("Unable to rewind the stream", e);
    }
  }
}
