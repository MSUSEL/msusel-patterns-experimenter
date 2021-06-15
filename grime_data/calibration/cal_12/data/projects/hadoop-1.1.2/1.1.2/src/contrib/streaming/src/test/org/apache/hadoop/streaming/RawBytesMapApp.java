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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.io.IntWritable;

public class RawBytesMapApp {
  private String find;
  private DataOutputStream dos;

  public RawBytesMapApp(String find) {
    this.find = find;
    dos = new DataOutputStream(System.out);
  }

  public void go() throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String line;
    while ((line = in.readLine()) != null) {
      for (String part : line.split(find)) {
        writeString(part);  // write key
        writeInt(1);        // write value
      }
    }
    System.out.flush();
  }
  
  public static void main(String[] args) throws IOException {
    RawBytesMapApp app = new RawBytesMapApp(args[0].replace(".","\\."));
    app.go();
  }
  
  private void writeString(String str) throws IOException {
    byte[] bytes = str.getBytes("UTF-8");
    dos.writeInt(bytes.length);
    dos.write(bytes);
  }
  
  private void writeInt(int i) throws IOException {
    dos.writeInt(4);
    IntWritable iw = new IntWritable(i);
    iw.write(dos);
  }
}
