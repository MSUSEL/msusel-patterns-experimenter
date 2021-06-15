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
 * A {@link org.apache.hadoop.io.compress.CompressorStream} which works
 * with 'block-based' based compression algorithms, as opposed to 
 * 'stream-based' compression algorithms.
 *
 * It should be noted that this wrapper does not guarantee that blocks will
 * be sized for the compressor. If the
 * {@link org.apache.hadoop.io.compress.Compressor} requires buffering to
 * effect meaningful compression, it is responsible for it.
 */
public class BlockCompressorStream extends CompressorStream {

  // The 'maximum' size of input data to be compressed, to account
  // for the overhead of the compression algorithm.
  private final int MAX_INPUT_SIZE;

  /**
   * Create a {@link BlockCompressorStream}.
   * 
   * @param out stream
   * @param compressor compressor to be used
   * @param bufferSize size of buffer
   * @param compressionOverhead maximum 'overhead' of the compression 
   *                            algorithm with given bufferSize
   */
  public BlockCompressorStream(OutputStream out, Compressor compressor, 
                               int bufferSize, int compressionOverhead) {
    super(out, compressor, bufferSize);
    MAX_INPUT_SIZE = bufferSize - compressionOverhead;
  }

  /**
   * Create a {@link BlockCompressorStream} with given output-stream and 
   * compressor.
   * Use default of 512 as bufferSize and compressionOverhead of 
   * (1% of bufferSize + 12 bytes) =  18 bytes (zlib algorithm).
   * 
   * @param out stream
   * @param compressor compressor to be used
   */
  public BlockCompressorStream(OutputStream out, Compressor compressor) {
    this(out, compressor, 512, 18);
  }

  /**
   * Write the data provided to the compression codec, compressing no more
   * than the buffer size less the compression overhead as specified during
   * construction for each block.
   *
   * Each block contains the uncompressed length for the block, followed by
   * one or more length-prefixed blocks of compressed data.
   */
  public void write(byte[] b, int off, int len) throws IOException {
    // Sanity checks
    if (compressor.finished()) {
      throw new IOException("write beyond end of stream");
    }
    if (b == null) {
      throw new NullPointerException();
    } else if ((off < 0) || (off > b.length) || (len < 0) ||
               ((off + len) > b.length)) {
      throw new IndexOutOfBoundsException();
    } else if (len == 0) {
      return;
    }

    long limlen = compressor.getBytesRead();
    if (len + limlen > MAX_INPUT_SIZE && limlen > 0) {
      // Adding this segment would exceed the maximum size.
      // Flush data if we have it.
      finish();
      compressor.reset();
    }

    if (len > MAX_INPUT_SIZE) {
      // The data we're given exceeds the maximum size. Any data
      // we had have been flushed, so we write out this chunk in segments
      // not exceeding the maximum size until it is exhausted.
      rawWriteInt(len);
      do {
        int bufLen = Math.min(len, MAX_INPUT_SIZE);
        
        compressor.setInput(b, off, bufLen);
        compressor.finish();
        while (!compressor.finished()) {
          compress();
        }
        compressor.reset();
        off += bufLen;
        len -= bufLen;
      } while (len > 0);
      return;
    }

    // Give data to the compressor
    compressor.setInput(b, off, len);
    if (!compressor.needsInput()) {
      // compressor buffer size might be smaller than the maximum
      // size, so we permit it to flush if required.
      rawWriteInt((int)compressor.getBytesRead());
      do {
        compress();
      } while (!compressor.needsInput());
    }
  }

  public void finish() throws IOException {
    if (!compressor.finished()) {
      rawWriteInt((int)compressor.getBytesRead());
      compressor.finish();
      while (!compressor.finished()) {
        compress();
      }
    }
  }

  protected void compress() throws IOException {
    int len = compressor.compress(buffer, 0, buffer.length);
    if (len > 0) {
      // Write out the compressed chunk
      rawWriteInt(len);
      out.write(buffer, 0, len);
    }
  }
  
  private void rawWriteInt(int v) throws IOException {
    out.write((v >>> 24) & 0xFF);
    out.write((v >>> 16) & 0xFF);
    out.write((v >>>  8) & 0xFF);
    out.write((v >>>  0) & 0xFF);
  }

}
