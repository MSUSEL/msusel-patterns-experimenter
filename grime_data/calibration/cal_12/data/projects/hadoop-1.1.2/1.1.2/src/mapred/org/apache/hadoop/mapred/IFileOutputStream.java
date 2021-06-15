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
package org.apache.hadoop.mapred;

import java.io.IOException;
import java.io.OutputStream; 
import java.io.FilterOutputStream;

import org.apache.hadoop.util.DataChecksum;
/**
 * A Checksum output stream.
 * Checksum for the contents of the file is calculated and
 * appended to the end of the file on close of the stream.
 * Used for IFiles
 */
class IFileOutputStream extends FilterOutputStream {
  /**
   * The output stream to be checksummed. 
   */
  private final DataChecksum sum;
  private byte[] barray;
  private boolean closed = false;
  private boolean finished = false;

  /**
   * Create a checksum output stream that writes
   * the bytes to the given stream.
   * @param out
   */
  public IFileOutputStream(OutputStream out) {
    super(out);
    sum = DataChecksum.newDataChecksum(DataChecksum.CHECKSUM_CRC32,
        Integer.MAX_VALUE);
    barray = new byte[sum.getChecksumSize()];
  }
  
  @Override
  public void close() throws IOException {
    if (closed) {
      return;
    }
    closed = true;
    finish();
    out.close();
  }

  /**
   * Finishes writing data to the output stream, by writing
   * the checksum bytes to the end. The underlying stream is not closed.
   * @throws IOException
   */
  public void finish() throws IOException {
    if (finished) {
      return;
    }
    finished = true;
    sum.writeValue(barray, 0, false);
    out.write (barray, 0, sum.getChecksumSize());
    out.flush();
  }

  /**
   * Write bytes to the stream.
   */
  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    sum.update(b, off,len);
    out.write(b,off,len);
  }
 
  @Override
  public void write(int b) throws IOException {
    barray[0] = (byte) (b & 0xFF);
    write(barray,0,1);
  }

}
