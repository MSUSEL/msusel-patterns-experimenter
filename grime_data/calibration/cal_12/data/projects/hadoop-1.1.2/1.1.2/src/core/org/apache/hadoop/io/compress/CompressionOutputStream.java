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
package org.apache.hadoop.io.compress;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A compression output stream.
 */
public abstract class CompressionOutputStream extends OutputStream {
  /**
   * The output stream to be compressed. 
   */
  protected final OutputStream out;
  
  /**
   * Create a compression output stream that writes
   * the compressed bytes to the given stream.
   * @param out
   */
  protected CompressionOutputStream(OutputStream out) {
    this.out = out;
  }
  
  public void close() throws IOException {
    finish();
    out.close();
  }
  
  public void flush() throws IOException {
    out.flush();
  }
  
  /**
   * Write compressed bytes to the stream.
   * Made abstract to prevent leakage to underlying stream.
   */
  public abstract void write(byte[] b, int off, int len) throws IOException;

  /**
   * Finishes writing compressed data to the output stream 
   * without closing the underlying stream.
   */
  public abstract void finish() throws IOException;
  
  /**
   * Reset the compression to the initial state. 
   * Does not reset the underlying stream.
   */
  public abstract void resetState() throws IOException;

}
