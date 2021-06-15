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

import org.apache.hadoop.conf.Configuration;

/**
 * Specification of a stream-based 'compressor' which can be  
 * plugged into a {@link CompressionOutputStream} to compress data.
 * This is modelled after {@link java.util.zip.Deflater}
 * 
 */
public interface Compressor {
  /**
   * Sets input data for compression. 
   * This should be called whenever #needsInput() returns 
   * <code>true</code> indicating that more input data is required.
   * 
   * @param b Input data
   * @param off Start offset
   * @param len Length
   */
  public void setInput(byte[] b, int off, int len);
  
  /**
   * Returns true if the input data buffer is empty and 
   * #setInput() should be called to provide more input. 
   * 
   * @return <code>true</code> if the input data buffer is empty and 
   * #setInput() should be called in order to provide more input.
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
   * Return number of uncompressed bytes input so far.
   */
  public long getBytesRead();

  /**
   * Return number of compressed bytes output so far.
   */
  public long getBytesWritten();

  /**
   * When called, indicates that compression should end
   * with the current contents of the input buffer.
   */
  public void finish();
  
  /**
   * Returns true if the end of the compressed 
   * data output stream has been reached.
   * @return <code>true</code> if the end of the compressed
   * data output stream has been reached.
   */
  public boolean finished();
  
  /**
   * Fills specified buffer with compressed data. Returns actual number
   * of bytes of compressed data. A return value of 0 indicates that
   * needsInput() should be called in order to determine if more input
   * data is required.
   * 
   * @param b Buffer for the compressed data
   * @param off Start offset of the data
   * @param len Size of the buffer
   * @return The actual number of bytes of compressed data.
   */
  public int compress(byte[] b, int off, int len) throws IOException;
  
  /**
   * Resets compressor so that a new set of input data can be processed.
   */
  public void reset();
  
  /**
   * Closes the compressor and discards any unprocessed input.
   */
  public void end();

  /**
   * Prepare the compressor to be used in a new stream with settings defined in
   * the given Configuration
   * 
   * @param conf Configuration from which new setting are fetched
   */
  public void reinit(Configuration conf);
}
