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
import java.util.Stack;

/**
 * XML Serializer.
 */
public class XmlRecordOutput implements RecordOutput {

  private PrintStream stream;
    
  private int indent = 0;
    
  private Stack<String> compoundStack;
    
  private void putIndent() {
    StringBuffer sb = new StringBuffer("");
    for (int idx = 0; idx < indent; idx++) {
      sb.append("  ");
    }
    stream.print(sb.toString());
  }
    
  private void addIndent() {
    indent++;
  }
    
  private void closeIndent() {
    indent--;
  }
    
  private void printBeginEnvelope(String tag) {
    if (!compoundStack.empty()) {
      String s = compoundStack.peek();
      if ("struct".equals(s)) {
        putIndent();
        stream.print("<member>\n");
        addIndent();
        putIndent();
        stream.print("<name>"+tag+"</name>\n");
        putIndent();
        stream.print("<value>");
      } else if ("vector".equals(s)) {
        stream.print("<value>");
      } else if ("map".equals(s)) {
        stream.print("<value>");
      }
    } else {
      stream.print("<value>");
    }
  }
    
  private void printEndEnvelope(String tag) {
    if (!compoundStack.empty()) {
      String s = compoundStack.peek();
      if ("struct".equals(s)) {
        stream.print("</value>\n");
        closeIndent();
        putIndent();
        stream.print("</member>\n");
      } else if ("vector".equals(s)) {
        stream.print("</value>\n");
      } else if ("map".equals(s)) {
        stream.print("</value>\n");
      }
    } else {
      stream.print("</value>\n");
    }
  }
    
  private void insideVector(String tag) {
    printBeginEnvelope(tag);
    compoundStack.push("vector");
  }
    
  private void outsideVector(String tag) throws IOException {
    String s = compoundStack.pop();
    if (!"vector".equals(s)) {
      throw new IOException("Error serializing vector.");
    }
    printEndEnvelope(tag);
  }
    
  private void insideMap(String tag) {
    printBeginEnvelope(tag);
    compoundStack.push("map");
  }
    
  private void outsideMap(String tag) throws IOException {
    String s = compoundStack.pop();
    if (!"map".equals(s)) {
      throw new IOException("Error serializing map.");
    }
    printEndEnvelope(tag);
  }
    
  private void insideRecord(String tag) {
    printBeginEnvelope(tag);
    compoundStack.push("struct");
  }
    
  private void outsideRecord(String tag) throws IOException {
    String s = compoundStack.pop();
    if (!"struct".equals(s)) {
      throw new IOException("Error serializing record.");
    }
    printEndEnvelope(tag);
  }
    
  /** Creates a new instance of XmlRecordOutput */
  public XmlRecordOutput(OutputStream out) {
    try {
      stream = new PrintStream(out, true, "UTF-8");
      compoundStack = new Stack<String>();
    } catch (UnsupportedEncodingException ex) {
      throw new RuntimeException(ex);
    }
  }
    
  public void writeByte(byte b, String tag) throws IOException {
    printBeginEnvelope(tag);
    stream.print("<ex:i1>");
    stream.print(Byte.toString(b));
    stream.print("</ex:i1>");
    printEndEnvelope(tag);
  }
    
  public void writeBool(boolean b, String tag) throws IOException {
    printBeginEnvelope(tag);
    stream.print("<boolean>");
    stream.print(b ? "1" : "0");
    stream.print("</boolean>");
    printEndEnvelope(tag);
  }
    
  public void writeInt(int i, String tag) throws IOException {
    printBeginEnvelope(tag);
    stream.print("<i4>");
    stream.print(Integer.toString(i));
    stream.print("</i4>");
    printEndEnvelope(tag);
  }
    
  public void writeLong(long l, String tag) throws IOException {
    printBeginEnvelope(tag);
    stream.print("<ex:i8>");
    stream.print(Long.toString(l));
    stream.print("</ex:i8>");
    printEndEnvelope(tag);
  }
    
  public void writeFloat(float f, String tag) throws IOException {
    printBeginEnvelope(tag);
    stream.print("<ex:float>");
    stream.print(Float.toString(f));
    stream.print("</ex:float>");
    printEndEnvelope(tag);
  }
    
  public void writeDouble(double d, String tag) throws IOException {
    printBeginEnvelope(tag);
    stream.print("<double>");
    stream.print(Double.toString(d));
    stream.print("</double>");
    printEndEnvelope(tag);
  }
    
  public void writeString(String s, String tag) throws IOException {
    printBeginEnvelope(tag);
    stream.print("<string>");
    stream.print(Utils.toXMLString(s));
    stream.print("</string>");
    printEndEnvelope(tag);
  }
    
  public void writeBuffer(Buffer buf, String tag)
    throws IOException {
    printBeginEnvelope(tag);
    stream.print("<string>");
    stream.print(Utils.toXMLBuffer(buf));
    stream.print("</string>");
    printEndEnvelope(tag);
  }
    
  public void startRecord(Record r, String tag) throws IOException {
    insideRecord(tag);
    stream.print("<struct>\n");
    addIndent();
  }
    
  public void endRecord(Record r, String tag) throws IOException {
    closeIndent();
    putIndent();
    stream.print("</struct>");
    outsideRecord(tag);
  }
    
  public void startVector(ArrayList v, String tag) throws IOException {
    insideVector(tag);
    stream.print("<array>\n");
    addIndent();
  }
    
  public void endVector(ArrayList v, String tag) throws IOException {
    closeIndent();
    putIndent();
    stream.print("</array>");
    outsideVector(tag);
  }
    
  public void startMap(TreeMap v, String tag) throws IOException {
    insideMap(tag);
    stream.print("<array>\n");
    addIndent();
  }
    
  public void endMap(TreeMap v, String tag) throws IOException {
    closeIndent();
    putIndent();
    stream.print("</array>");
    outsideMap(tag);
  }

}
