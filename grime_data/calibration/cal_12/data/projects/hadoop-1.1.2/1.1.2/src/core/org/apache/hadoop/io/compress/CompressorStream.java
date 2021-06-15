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

import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.Compressor;

public class CompressorStream extends CompressionOutputStream {
  protected Compressor compressor;
  protected byte[] buffer;
  protected boolean closed = false;
  
  public CompressorStream(OutputStream out, Compressor compressor, int bufferSize) {
    super(out);

    if (out == null || compressor == null) {
      throw new NullPointerException();
    } else if (bufferSize <= 0) {
      throw new IllegalArgumentException("Illegal bufferSize");
    }

    this.compressor = compressor;
    buffer = new byte[bufferSize];
  }

  public CompressorStream(OutputStream out, Compressor compressor) {
    this(out, compressor, 512);
  }
  
  /**
   * Allow derived classes to directly set the underlying stream.
   * 
   * @param out Underlying output stream.
   */
  protected CompressorStream(OutputStream out) {
    super(out);
  }

  public void write(byte[] b, int off, int len) throws IOException {
    // Sanity checks
    if (compressor.finished()) {
      throw new IOException("write beyond end of stream");
    }
    if ((off | len | (off + len) | (b.length - (off + len))) < 0) {
      throw new IndexOutOfBoundsException();
    } else if (len == 0) {
      return;
    }

    compressor.setInput(b, off, len);
    while (!compressor.needsInput()) {
      compress();
    }
  }

  protected void compress() throws IOException {
    int len = compressor.compress(buffer, 0, buffer.length);
    if (len > 0) {
      out.write(buffer, 0, len);
    }
  }

  public void finish() throws IOException {
    if (!compressor.finished()) {
      compressor.finish();
      while (!compressor.finished()) {
        compress();
      }
    }
  }

  public void resetState() throws IOException {
    compressor.reset();
  }
  
  public void close() throws IOException {
    if (!closed) {
      finish();
      out.close();
      closed = true;
    }
  }

  private byte[] oneByte = new byte[1];
  public void write(int b) throws IOException {
    oneByte[0] = (byte)(b & 0xff);
    write(oneByte, 0, oneByte.length);
  }

}
