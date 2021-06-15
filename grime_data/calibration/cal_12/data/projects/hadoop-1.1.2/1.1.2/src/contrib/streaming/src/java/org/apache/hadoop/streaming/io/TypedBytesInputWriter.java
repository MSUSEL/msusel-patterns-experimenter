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

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.streaming.PipeMapRed;
import org.apache.hadoop.typedbytes.TypedBytesOutput;
import org.apache.hadoop.typedbytes.TypedBytesWritableOutput;

/**
 * InputWriter that writes the client's input as typed bytes.
 */
public class TypedBytesInputWriter extends InputWriter<Object, Object> {

  private TypedBytesOutput tbOut;
  private TypedBytesWritableOutput tbwOut;

  @Override
  public void initialize(PipeMapRed pipeMapRed) throws IOException {
    super.initialize(pipeMapRed);
    DataOutput clientOut = pipeMapRed.getClientOutput();
    tbOut = new TypedBytesOutput(clientOut);
    tbwOut = new TypedBytesWritableOutput(clientOut);
  }

  @Override
  public void writeKey(Object key) throws IOException {
    writeTypedBytes(key);
  }

  @Override
  public void writeValue(Object value) throws IOException {
    writeTypedBytes(value);
  }
  
  private void writeTypedBytes(Object value) throws IOException {
    if (value instanceof Writable) {
      tbwOut.write((Writable) value);
    } else {
      tbOut.write(value);
    }
  }
  
}
