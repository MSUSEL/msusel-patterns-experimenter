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
package org.apache.hadoop.record;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.UnsupportedEncodingException;

/**
 */
public class CsvRecordInput implements RecordInput {
    
  private PushbackReader stream;
    
  private class CsvIndex implements Index {
    public boolean done() {
      char c = '\0';
      try {
        c = (char) stream.read();
        stream.unread(c);
      } catch (IOException ex) {
      }
      return (c == '}') ? true : false;
    }
    public void incr() {}
  }
    
  private void throwExceptionOnError(String tag) throws IOException {
    throw new IOException("Error deserializing "+tag);
  }
    
  private String readField(String tag) throws IOException {
    try {
      StringBuffer buf = new StringBuffer();
      while (true) {
        char c = (char) stream.read();
        switch (c) {
        case ',':
          return buf.toString();
        case '}':
        case '\n':
        case '\r':
          stream.unread(c);
          return buf.toString();
        default:
          buf.append(c);
        }
      }
    } catch (IOException ex) {
      throw new IOException("Error reading "+tag);
    }
  }
    
  /** Creates a new instance of CsvRecordInput */
  public CsvRecordInput(InputStream in) {
    try {
      stream = new PushbackReader(new InputStreamReader(in, "UTF-8"));
    } catch (UnsupportedEncodingException ex) {
      throw new RuntimeException(ex);
    }
  }
    
  public byte readByte(String tag) throws IOException {
    return (byte) readLong(tag);
  }
    
  public boolean readBool(String tag) throws IOException {
    String sval = readField(tag);
    return "T".equals(sval) ? true : false;
  }
    
  public int readInt(String tag) throws IOException {
    return (int) readLong(tag);
  }
    
  public long readLong(String tag) throws IOException {
    String sval = readField(tag);
    try {
      long lval = Long.parseLong(sval);
      return lval;
    } catch (NumberFormatException ex) {
      throw new IOException("Error deserializing "+tag);
    }
  }
    
  public float readFloat(String tag) throws IOException {
    return (float) readDouble(tag);
  }
    
  public double readDouble(String tag) throws IOException {
    String sval = readField(tag);
    try {
      double dval = Double.parseDouble(sval);
      return dval;
    } catch (NumberFormatException ex) {
      throw new IOException("Error deserializing "+tag);
    }
  }
    
  public String readString(String tag) throws IOException {
    String sval = readField(tag);
    return Utils.fromCSVString(sval);
  }
    
  public Buffer readBuffer(String tag) throws IOException {
    String sval = readField(tag);
    return Utils.fromCSVBuffer(sval);
  }
    
  public void startRecord(String tag) throws IOException {
    if (tag != null && !"".equals(tag)) {
      char c1 = (char) stream.read();
      char c2 = (char) stream.read();
      if (c1 != 's' || c2 != '{') {
        throw new IOException("Error deserializing "+tag);
      }
    }
  }
    
  public void endRecord(String tag) throws IOException {
    char c = (char) stream.read();
    if (tag == null || "".equals(tag)) {
      if (c != '\n' && c != '\r') {
        throw new IOException("Error deserializing record.");
      } else {
        return;
      }
    }
        
    if (c != '}') {
      throw new IOException("Error deserializing "+tag);
    }
    c = (char) stream.read();
    if (c != ',') {
      stream.unread(c);
    }
        
    return;
  }
    
  public Index startVector(String tag) throws IOException {
    char c1 = (char) stream.read();
    char c2 = (char) stream.read();
    if (c1 != 'v' || c2 != '{') {
      throw new IOException("Error deserializing "+tag);
    }
    return new CsvIndex();
  }
    
  public void endVector(String tag) throws IOException {
    char c = (char) stream.read();
    if (c != '}') {
      throw new IOException("Error deserializing "+tag);
    }
    c = (char) stream.read();
    if (c != ',') {
      stream.unread(c);
    }
    return;
  }
    
  public Index startMap(String tag) throws IOException {
    char c1 = (char) stream.read();
    char c2 = (char) stream.read();
    if (c1 != 'm' || c2 != '{') {
      throw new IOException("Error deserializing "+tag);
    }
    return new CsvIndex();
  }
    
  public void endMap(String tag) throws IOException {
    char c = (char) stream.read();
    if (c != '}') {
      throw new IOException("Error deserializing "+tag);
    }
    c = (char) stream.read();
    if (c != ',') {
      stream.unread(c);
    }
    return;
  }
}
