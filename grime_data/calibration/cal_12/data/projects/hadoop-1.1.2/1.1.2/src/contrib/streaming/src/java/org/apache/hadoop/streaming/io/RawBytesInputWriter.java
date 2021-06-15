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
package org.apache.hadoop.streaming.io;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.streaming.PipeMapRed;

/**
 * InputWriter that writes the client's input as raw bytes.
 */
public class RawBytesInputWriter extends InputWriter<Writable, Writable> {

  private DataOutput clientOut;
  private ByteArrayOutputStream bufferOut;
  private DataOutputStream bufferDataOut;

  @Override
  public void initialize(PipeMapRed pipeMapRed) throws IOException {
    super.initialize(pipeMapRed);
    clientOut = pipeMapRed.getClientOutput();
    bufferOut = new ByteArrayOutputStream();
    bufferDataOut = new DataOutputStream(bufferOut);
  }
  
  @Override
  public void writeKey(Writable key) throws IOException {
    writeRawBytes(key);
  }

  @Override
  public void writeValue(Writable value) throws IOException {
    writeRawBytes(value);
  }

  private void writeRawBytes(Writable writable) throws IOException {
    if (writable instanceof BytesWritable) {
      BytesWritable bw = (BytesWritable) writable;
      byte[] bytes = bw.getBytes();
      int length = bw.getLength();
      clientOut.writeInt(length);
      clientOut.write(bytes, 0, length);
    } else {
      bufferOut.reset();
      writable.write(bufferDataOut);
      byte[] bytes = bufferOut.toByteArray();
      clientOut.writeInt(bytes.length);
      clientOut.write(bytes);
    }
  }
  
}
