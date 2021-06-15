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
import java.io.InputStream;

import org.apache.hadoop.fs.PositionedReadable;
import org.apache.hadoop.fs.Seekable;
/**
 * A compression input stream.
 *
 * <p>Implementations are assumed to be buffered.  This permits clients to
 * reposition the underlying input stream then call {@link #resetState()},
 * without having to also synchronize client buffers.
 */

public abstract class CompressionInputStream extends InputStream implements Seekable {
  /**
   * The input stream to be compressed. 
   */
  protected final InputStream in;
  protected long maxAvailableData = 0L;

  /**
   * Create a compression input stream that reads
   * the decompressed bytes from the given stream.
   * 
   * @param in The input stream to be compressed.
   * @throws IOException
   */
  protected CompressionInputStream(InputStream in) throws IOException {
    if (!(in instanceof Seekable) || !(in instanceof PositionedReadable)) {
        this.maxAvailableData = in.available();
    }
    this.in = in;
  }

  public void close() throws IOException {
    in.close();
  }
  
  /**
   * Read bytes from the stream.
   * Made abstract to prevent leakage to underlying stream.
   */
  public abstract int read(byte[] b, int off, int len) throws IOException;

  /**
   * Reset the decompressor to its initial state and discard any buffered data,
   * as the underlying stream may have been repositioned.
   */
  public abstract void resetState() throws IOException;
  
  /**
   * This method returns the current position in the stream.
   *
   * @return Current position in stream as a long
   */
  public long getPos() throws IOException {
    if (!(in instanceof Seekable) || !(in instanceof PositionedReadable)){
      //This way of getting the current position will not work for file
      //size which can be fit in an int and hence can not be returned by
      //available method.
      return (this.maxAvailableData - this.in.available());
    }
    else{
      return ((Seekable)this.in).getPos();
    }

  }

  /**
   * This method is current not supported.
   *
   * @throws UnsupportedOperationException
   */

  public void seek(long pos) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * This method is current not supported.
   *
   * @throws UnsupportedOperationException
   */
  public boolean seekToNewSource(long targetPos) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }
}
