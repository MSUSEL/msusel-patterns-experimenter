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

import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.streaming.PipeMapRed;

/**
 * InputWriter that writes the client's input as text.
 */
public class TextInputWriter extends InputWriter<Object, Object> {
  
  private DataOutput clientOut;
  private byte[] inputSeparator;
  
  @Override
  public void initialize(PipeMapRed pipeMapRed) throws IOException {
    super.initialize(pipeMapRed);
    clientOut = pipeMapRed.getClientOutput();
    inputSeparator = pipeMapRed.getInputSeparator();
  }
  
  @Override
  public void writeKey(Object key) throws IOException {
    writeUTF8(key);
    clientOut.write(inputSeparator);
  }

  @Override
  public void writeValue(Object value) throws IOException {
    writeUTF8(value);
    clientOut.write('\n');
  }
  
  // Write an object to the output stream using UTF-8 encoding
  private void writeUTF8(Object object) throws IOException {
    byte[] bval;
    int valSize;
    if (object instanceof BytesWritable) {
      BytesWritable val = (BytesWritable) object;
      bval = val.getBytes();
      valSize = val.getLength();
    } else if (object instanceof Text) {
      Text val = (Text) object;
      bval = val.getBytes();
      valSize = val.getLength();
    } else {
      String sval = object.toString();
      bval = sval.getBytes("UTF-8");
      valSize = bval.length;
    }
    clientOut.write(bval, 0, valSize);
  }
  
}
