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
package org.apache.hadoop.streaming;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.hadoop.typedbytes.TypedBytesInput;
import org.apache.hadoop.typedbytes.TypedBytesOutput;

public class TypedBytesReduceApp {

  public void go() throws IOException {
    TypedBytesInput tbinput = new TypedBytesInput(new DataInputStream(System.in));
    TypedBytesOutput tboutput = new TypedBytesOutput(new DataOutputStream(System.out));
    
    Object prevKey = null;
    int sum = 0;
    Object key = tbinput.read();
    while (key != null) {
      if (prevKey != null && !key.equals(prevKey)) {
        tboutput.write(prevKey);  // write key
        tboutput.write(sum);      // write value
        sum = 0;
      }
      sum += (Integer) tbinput.read();
      prevKey = key;
      key = tbinput.read();
    }
    tboutput.write(prevKey);
    tboutput.write(sum);
    
    System.out.flush();
  }

  public static void main(String[] args) throws IOException {
    TypedBytesReduceApp app = new TypedBytesReduceApp();
    app.go();
  }
  
}
