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

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Checksum;

/**
 * This is a generic output stream for generating checksums for
 * data before it is written to the underlying stream
 */

abstract public class FSOutputSummer extends OutputStream {
  // data checksum
  private Checksum sum;
  // internal buffer for storing data before it is checksumed
  private byte buf[];
  // internal buffer for storing checksum
  private byte checksum[];
  // The number of valid bytes in the buffer.
  private int count;
  
  protected FSOutputSummer(Checksum sum, int maxChunkSize, int checksumSize) {
    this.sum = sum;
    this.buf = new byte[maxChunkSize];
    this.checksum = new byte[checksumSize];
    this.count = 0;
  }
  
  /* write the data chunk in <code>b</code> staring at <code>offset</code> with
   * a length of <code>len</code>, and its checksum
   */
  protected abstract void writeChunk(byte[] b, int offset, int len, byte[] checksum)
  throws IOException;

  /** Write one byte */
  public synchronized void write(int b) throws IOException {
    sum.update(b);
    buf[count++] = (byte)b;
    if(count == buf.length) {
      flushBuffer();
    }
  }

  /**
   * Writes <code>len</code> bytes from the specified byte array 
   * starting at offset <code>off</code> and generate a checksum for
   * each data chunk.
   *
   * <p> This method stores bytes from the given array into this
   * stream's buffer before it gets checksumed. The buffer gets checksumed 
   * and flushed to the underlying output stream when all data 
   * in a checksum chunk are in the buffer.  If the buffer is empty and
   * requested length is at least as large as the size of next checksum chunk
   * size, this method will checksum and write the chunk directly 
   * to the underlying output stream.  Thus it avoids uneccessary data copy.
   *
   * @param      b     the data.
   * @param      off   the start offset in the data.
   * @param      len   the number of bytes to write.
   * @exception  IOException  if an I/O error occurs.
   */
  public synchronized void write(byte b[], int off, int len)
  throws IOException {
    if (off < 0 || len < 0 || off > b.length - len) {
      throw new ArrayIndexOutOfBoundsException();
    }

    for (int n=0;n<len;n+=write1(b, off+n, len-n)) {
    }
  }
  
  /**
   * Write a portion of an array, flushing to the underlying
   * stream at most once if necessary.
   */
  private int write1(byte b[], int off, int len) throws IOException {
    if(count==0 && len>=buf.length) {
      // local buffer is empty and user data has one chunk
      // checksum and output data
      final int length = buf.length;
      sum.update(b, off, length);
      writeChecksumChunk(b, off, length, false);
      return length;
    }
    
    // copy user data to local buffer
    int bytesToCopy = buf.length-count;
    bytesToCopy = (len<bytesToCopy) ? len : bytesToCopy;
    sum.update(b, off, bytesToCopy);
    System.arraycopy(b, off, buf, count, bytesToCopy);
    count += bytesToCopy;
    if (count == buf.length) {
      // local buffer is full
      flushBuffer();
    } 
    return bytesToCopy;
  }

  /* Forces any buffered output bytes to be checksumed and written out to
   * the underlying output stream. 
   */
  protected synchronized void flushBuffer() throws IOException {
    flushBuffer(false);
  }

  /* Forces any buffered output bytes to be checksumed and written out to
   * the underlying output stream.  If keep is true, then the state of 
   * this object remains intact.
   */
  protected synchronized void flushBuffer(boolean keep) throws IOException {
    if (count != 0) {
      int chunkLen = count;
      count = 0;
      writeChecksumChunk(buf, 0, chunkLen, keep);
      if (keep) {
        count = chunkLen;
      }
    }
  }
  
  /** Generate checksum for the data chunk and output data chunk & checksum
   * to the underlying output stream. If keep is true then keep the
   * current checksum intact, do not reset it.
   */
  private void writeChecksumChunk(byte b[], int off, int len, boolean keep)
  throws IOException {
    int tempChecksum = (int)sum.getValue();
    if (!keep) {
      sum.reset();
    }
    int2byte(tempChecksum, checksum);
    writeChunk(b, off, len, checksum);
  }

  /**
   * Converts a checksum integer value to a byte stream
   */
  static public byte[] convertToByteStream(Checksum sum, int checksumSize) {
    return int2byte((int)sum.getValue(), new byte[checksumSize]);
  }

  static byte[] int2byte(int integer, byte[] bytes) {
    bytes[0] = (byte)((integer >>> 24) & 0xFF);
    bytes[1] = (byte)((integer >>> 16) & 0xFF);
    bytes[2] = (byte)((integer >>>  8) & 0xFF);
    bytes[3] = (byte)((integer >>>  0) & 0xFF);
    return bytes;
  }

  /**
   * Resets existing buffer with a new one of the specified size.
   */
  protected synchronized void resetChecksumChunk(int size) {
    sum.reset();
    this.buf = new byte[size];
    this.count = 0;
  }
}
