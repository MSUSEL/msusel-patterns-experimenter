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
import java.io.EOFException;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;

public class RawBytesReduceApp {
  private DataInputStream dis;

  public RawBytesReduceApp() {
    dis = new DataInputStream(System.in);
  }
  
  public void go() throws IOException {
    String prevKey = null;
    int sum = 0;
    String key = readString();
    while (key != null) {
      if (prevKey != null && !key.equals(prevKey)) {
        System.out.println(prevKey + "\t" + sum);
        sum = 0;
      }
      sum += readInt();
      prevKey = key;
      key = readString();
    }
    System.out.println(prevKey + "\t" + sum);
    System.out.flush();
  }

  public static void main(String[] args) throws IOException {
    RawBytesReduceApp app = new RawBytesReduceApp();
    app.go();
  }
  
  private String readString() throws IOException {
    int length;
    try {
      length = dis.readInt();
    } catch (EOFException eof) {
      return null;
    }
    byte[] bytes = new byte[length];
    dis.readFully(bytes);
    return new String(bytes, "UTF-8");
  }
  
  private int readInt() throws IOException {
    dis.readInt(); // ignore (we know it's 4)
    IntWritable iw = new IntWritable();
    iw.readFields(dis);
    return iw.get();
  }
}
