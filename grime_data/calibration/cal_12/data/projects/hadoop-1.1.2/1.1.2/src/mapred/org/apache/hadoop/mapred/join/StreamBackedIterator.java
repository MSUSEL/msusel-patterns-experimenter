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
package org.apache.hadoop.mapred.join;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * This class provides an implementation of ResetableIterator. This
 * implementation uses a byte array to store elements added to it.
 */
public class StreamBackedIterator<X extends Writable>
    implements ResetableIterator<X> {

  private static class ReplayableByteInputStream extends ByteArrayInputStream {
    public ReplayableByteInputStream(byte[] arr) {
      super(arr);
    }
    public void resetStream() {
      mark = 0;
      reset();
    }
  }

  private ByteArrayOutputStream outbuf = new ByteArrayOutputStream();
  private DataOutputStream outfbuf = new DataOutputStream(outbuf);
  private ReplayableByteInputStream inbuf;
  private DataInputStream infbuf;

  public StreamBackedIterator() { }

  public boolean hasNext() {
    return infbuf != null && inbuf.available() > 0;
  }

  public boolean next(X val) throws IOException {
    if (hasNext()) {
      inbuf.mark(0);
      val.readFields(infbuf);
      return true;
    }
    return false;
  }

  public boolean replay(X val) throws IOException {
    inbuf.reset();
    if (0 == inbuf.available())
      return false;
    val.readFields(infbuf);
    return true;
  }

  public void reset() {
    if (null != outfbuf) {
      inbuf = new ReplayableByteInputStream(outbuf.toByteArray());
      infbuf =  new DataInputStream(inbuf);
      outfbuf = null;
    }
    inbuf.resetStream();
  }

  public void add(X item) throws IOException {
    item.write(outfbuf);
  }

  public void close() throws IOException {
    if (null != infbuf)
      infbuf.close();
    if (null != outfbuf)
      outfbuf.close();
  }

  public void clear() {
    if (null != inbuf)
      inbuf.resetStream();
    outbuf.reset();
    outfbuf = new DataOutputStream(outbuf);
  }
}
