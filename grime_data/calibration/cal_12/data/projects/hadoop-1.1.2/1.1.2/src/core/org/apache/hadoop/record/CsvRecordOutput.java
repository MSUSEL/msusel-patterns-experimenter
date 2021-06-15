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

import java.io.IOException;
import java.util.TreeMap;
import java.util.ArrayList;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 */
public class CsvRecordOutput implements RecordOutput {

  private PrintStream stream;
  private boolean isFirst = true;
    
  private void throwExceptionOnError(String tag) throws IOException {
    if (stream.checkError()) {
      throw new IOException("Error serializing "+tag);
    }
  }
 
  private void printCommaUnlessFirst() {
    if (!isFirst) {
      stream.print(",");
    }
    isFirst = false;
  }
    
  /** Creates a new instance of CsvRecordOutput */
  public CsvRecordOutput(OutputStream out) {
    try {
      stream = new PrintStream(out, true, "UTF-8");
    } catch (UnsupportedEncodingException ex) {
      throw new RuntimeException(ex);
    }
  }
    
  public void writeByte(byte b, String tag) throws IOException {
    writeLong((long)b, tag);
  }
    
  public void writeBool(boolean b, String tag) throws IOException {
    printCommaUnlessFirst();
    String val = b ? "T" : "F";
    stream.print(val);
    throwExceptionOnError(tag);
  }
    
  public void writeInt(int i, String tag) throws IOException {
    writeLong((long)i, tag);
  }
    
  public void writeLong(long l, String tag) throws IOException {
    printCommaUnlessFirst();
    stream.print(l);
    throwExceptionOnError(tag);
  }
    
  public void writeFloat(float f, String tag) throws IOException {
    writeDouble((double)f, tag);
  }
    
  public void writeDouble(double d, String tag) throws IOException {
    printCommaUnlessFirst();
    stream.print(d);
    throwExceptionOnError(tag);
  }
    
  public void writeString(String s, String tag) throws IOException {
    printCommaUnlessFirst();
    stream.print(Utils.toCSVString(s));
    throwExceptionOnError(tag);
  }
    
  public void writeBuffer(Buffer buf, String tag)
    throws IOException {
    printCommaUnlessFirst();
    stream.print(Utils.toCSVBuffer(buf));
    throwExceptionOnError(tag);
  }
    
  public void startRecord(Record r, String tag) throws IOException {
    if (tag != null && !"".equals(tag)) {
      printCommaUnlessFirst();
      stream.print("s{");
      isFirst = true;
    }
  }
    
  public void endRecord(Record r, String tag) throws IOException {
    if (tag == null || "".equals(tag)) {
      stream.print("\n");
      isFirst = true;
    } else {
      stream.print("}");
      isFirst = false;
    }
  }
    
  public void startVector(ArrayList v, String tag) throws IOException {
    printCommaUnlessFirst();
    stream.print("v{");
    isFirst = true;
  }
    
  public void endVector(ArrayList v, String tag) throws IOException {
    stream.print("}");
    isFirst = false;
  }
    
  public void startMap(TreeMap v, String tag) throws IOException {
    printCommaUnlessFirst();
    stream.print("m{");
    isFirst = true;
  }
    
  public void endMap(TreeMap v, String tag) throws IOException {
    stream.print("}");
    isFirst = false;
  }
}
