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
package org.apache.hadoop.typedbytes;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.hadoop.record.Buffer;
import org.apache.hadoop.record.Record;
import org.apache.hadoop.record.RecordOutput;

/**
 * Deserialized for records that reads typed bytes.
 */
public class TypedBytesRecordOutput implements RecordOutput {

  private TypedBytesOutput out;

  private TypedBytesRecordOutput() {}

  private void setTypedBytesOutput(TypedBytesOutput out) {
    this.out = out;
  }

  private static ThreadLocal tbOut = new ThreadLocal() {
    protected synchronized Object initialValue() {
      return new TypedBytesRecordOutput();
    }
  };

  /**
   * Get a thread-local typed bytes record input for the supplied
   * {@link TypedBytesOutput}.
   * 
   * @param out typed bytes output object
   * @return typed bytes record output corresponding to the supplied
   *         {@link TypedBytesOutput}.
   */
  public static TypedBytesRecordOutput get(TypedBytesOutput out) {
    TypedBytesRecordOutput bout = (TypedBytesRecordOutput) tbOut.get();
    bout.setTypedBytesOutput(out);
    return bout;
  }

  /**
   * Get a thread-local typed bytes record output for the supplied
   * {@link DataOutput}.
   * 
   * @param out data output object
   * @return typed bytes record output corresponding to the supplied
   *         {@link DataOutput}.
   */
  public static TypedBytesRecordOutput get(DataOutput out) {
    return get(TypedBytesOutput.get(out));
  }

  /** Creates a new instance of TypedBytesRecordOutput. */
  public TypedBytesRecordOutput(TypedBytesOutput out) {
    this.out = out;
  }

  /** Creates a new instance of TypedBytesRecordOutput. */
  public TypedBytesRecordOutput(DataOutput out) {
    this(new TypedBytesOutput(out));
  }

  public void writeBool(boolean b, String tag) throws IOException {
    out.writeBool(b);
  }

  public void writeBuffer(Buffer buf, String tag) throws IOException {
    out.writeBytes(buf.get());
  }

  public void writeByte(byte b, String tag) throws IOException {
    out.writeByte(b);
  }

  public void writeDouble(double d, String tag) throws IOException {
    out.writeDouble(d);
  }

  public void writeFloat(float f, String tag) throws IOException {
    out.writeFloat(f);
  }

  public void writeInt(int i, String tag) throws IOException {
    out.writeInt(i);
  }

  public void writeLong(long l, String tag) throws IOException {
    out.writeLong(l);
  }

  public void writeString(String s, String tag) throws IOException {
    out.writeString(s);
  }

  public void startRecord(Record r, String tag) throws IOException {
    out.writeListHeader();
  }

  public void startVector(ArrayList v, String tag) throws IOException {
    out.writeVectorHeader(v.size());
  }

  public void startMap(TreeMap m, String tag) throws IOException {
    out.writeMapHeader(m.size());
  }

  public void endRecord(Record r, String tag) throws IOException {
    out.writeListFooter();
  }

  public void endVector(ArrayList v, String tag) throws IOException {}

  public void endMap(TreeMap m, String tag) throws IOException {}

}
