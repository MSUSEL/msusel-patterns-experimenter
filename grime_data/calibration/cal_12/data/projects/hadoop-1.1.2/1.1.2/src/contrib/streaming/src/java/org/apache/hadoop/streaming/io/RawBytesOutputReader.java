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

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.streaming.PipeMapRed;

/**
 * OutputReader that reads the client's output as raw bytes.
 */
public class RawBytesOutputReader 
  extends OutputReader<BytesWritable, BytesWritable> {

  private DataInput clientIn;
  private byte[] bytes;
  private BytesWritable key;
  private BytesWritable value;

  @Override
  public void initialize(PipeMapRed pipeMapRed) throws IOException {
    super.initialize(pipeMapRed);
    clientIn = pipeMapRed.getClientInput();
    key = new BytesWritable();
    value = new BytesWritable();
  }
  
  @Override
  public boolean readKeyValue() throws IOException {
    int length = readLength();
    if (length < 0) {
      return false;
    }
    key.set(readBytes(length), 0, length);
    length = readLength();
    value.set(readBytes(length), 0, length);
    return true;
  }
  
  @Override
  public BytesWritable getCurrentKey() throws IOException {
    return key;
  }
  
  @Override
  public BytesWritable getCurrentValue() throws IOException {
    return value;
  }

  @Override
  public String getLastOutput() {
    return new BytesWritable(bytes).toString();
  }

  private int readLength() throws IOException {
    try {
      return clientIn.readInt();
    } catch (EOFException eof) {
      return -1;
    }
  }
  
  private byte[] readBytes(int length) throws IOException {
    bytes = new byte[length];
    clientIn.readFully(bytes);
    return bytes;
  }
  
}
