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
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 */
public class BinaryRecordOutput implements RecordOutput {
    
  private DataOutput out;
    
  private BinaryRecordOutput() {}
    
  private void setDataOutput(DataOutput out) {
    this.out = out;
  }
    
  private static ThreadLocal bOut = new ThreadLocal() {
      protected synchronized Object initialValue() {
        return new BinaryRecordOutput();
      }
    };
    
  /**
   * Get a thread-local record output for the supplied DataOutput.
   * @param out data output stream
   * @return binary record output corresponding to the supplied DataOutput.
   */
  public static BinaryRecordOutput get(DataOutput out) {
    BinaryRecordOutput bout = (BinaryRecordOutput) bOut.get();
    bout.setDataOutput(out);
    return bout;
  }
    
  /** Creates a new instance of BinaryRecordOutput */
  public BinaryRecordOutput(OutputStream out) {
    this.out = new DataOutputStream(out);
  }
    
  /** Creates a new instance of BinaryRecordOutput */
  public BinaryRecordOutput(DataOutput out) {
    this.out = out;
  }
    
    
  public void writeByte(byte b, String tag) throws IOException {
    out.writeByte(b);
  }
    
  public void writeBool(boolean b, String tag) throws IOException {
    out.writeBoolean(b);
  }
    
  public void writeInt(int i, String tag) throws IOException {
    Utils.writeVInt(out, i);
  }
    
  public void writeLong(long l, String tag) throws IOException {
    Utils.writeVLong(out, l);
  }
    
  public void writeFloat(float f, String tag) throws IOException {
    out.writeFloat(f);
  }
    
  public void writeDouble(double d, String tag) throws IOException {
    out.writeDouble(d);
  }
    
  public void writeString(String s, String tag) throws IOException {
    Utils.toBinaryString(out, s);
  }
    
  public void writeBuffer(Buffer buf, String tag)
    throws IOException {
    byte[] barr = buf.get();
    int len = buf.getCount();
    Utils.writeVInt(out, len);
    out.write(barr, 0, len);
  }
    
  public void startRecord(Record r, String tag) throws IOException {}
    
  public void endRecord(Record r, String tag) throws IOException {}
    
  public void startVector(ArrayList v, String tag) throws IOException {
    writeInt(v.size(), tag);
  }
    
  public void endVector(ArrayList v, String tag) throws IOException {}
    
  public void startMap(TreeMap v, String tag) throws IOException {
    writeInt(v.size(), tag);
  }
    
  public void endMap(TreeMap v, String tag) throws IOException {}
    
}
