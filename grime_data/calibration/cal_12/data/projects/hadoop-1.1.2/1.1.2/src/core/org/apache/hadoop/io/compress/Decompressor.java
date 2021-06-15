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

/**
 * Specification of a stream-based 'de-compressor' which can be  
 * plugged into a {@link CompressionInputStream} to compress data.
 * This is modelled after {@link java.util.zip.Inflater}
 * 
 */
public interface Decompressor {
  /**
   * Sets input data for decompression. 
   * This should be called if and only if {@link #needsInput()} returns 
   * <code>true</code> indicating that more input data is required.
   * (Both native and non-native versions of various Decompressors require
   * that the data passed in via <code>b[]</code> remain unmodified until
   * the caller is explicitly notified--via {@link #needsInput()}--that the
   * buffer may be safely modified.  With this requirement, an extra
   * buffer-copy can be avoided.)
   * 
   * @param b Input data
   * @param off Start offset
   * @param len Length
   */
  public void setInput(byte[] b, int off, int len);
  
  /**
   * Returns true if the input data buffer is empty and 
   * {@link #setInput(byte[], int, int)} should be called to
   * provide more input. 
   * 
   * @return <code>true</code> if the input data buffer is empty and 
   * {@link #setInput(byte[], int, int)} should be called in
   * order to provide more input.
   */
  public boolean needsInput();
  
  /**
   * Sets preset dictionary for compression. A preset dictionary
   * is used when the history buffer can be predetermined. 
   *
   * @param b Dictionary data bytes
   * @param off Start offset
   * @param len Length
   */
  public void setDictionary(byte[] b, int off, int len);
  
  /**
   * Returns <code>true</code> if a preset dictionary is needed for decompression.
   * @return <code>true</code> if a preset dictionary is needed for decompression
   */
  public boolean needsDictionary();

  /**
   * Returns true if the end of the decompressed 
   * data output stream has been reached.
   * @return <code>true</code> if the end of the decompressed
   * data output stream has been reached.
   */
  public boolean finished();
  
  /**
   * Fills specified buffer with uncompressed data. Returns actual number
   * of bytes of uncompressed data. A return value of 0 indicates that
   * {@link #needsInput()} should be called in order to determine if more
   * input data is required.
   * 
   * @param b Buffer for the compressed data
   * @param off Start offset of the data
   * @param len Size of the buffer
   * @return The actual number of bytes of compressed data.
   * @throws IOException
   */
  public int decompress(byte[] b, int off, int len) throws IOException;

  /**
   * Returns the number of bytes remaining in the compressed-data buffer;
   * typically called after the decompressor has finished decompressing
   * the current gzip stream (a.k.a. "member").
   */
  public int getRemaining();

  /**
   * Resets decompressor and input and output buffers so that a new set of
   * input data can be processed.
   */
  public void reset();

  /**
   * Closes the decompressor and discards any unprocessed input.
   */
  public void end(); 
}
