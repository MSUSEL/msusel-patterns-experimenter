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

import java.io.DataInput;
import java.io.IOException;

import org.apache.hadoop.record.Buffer;
import org.apache.hadoop.record.Index;
import org.apache.hadoop.record.RecordInput;

/**
 * Serializer for records that writes typed bytes.
 */
public class TypedBytesRecordInput implements RecordInput {

  private TypedBytesInput in;

  private TypedBytesRecordInput() {}

  private void setTypedBytesInput(TypedBytesInput in) {
    this.in = in;
  }

  private static ThreadLocal tbIn = new ThreadLocal() {
    protected synchronized Object initialValue() {
      return new TypedBytesRecordInput();
    }
  };

  /**
   * Get a thread-local typed bytes record input for the supplied
   * {@link TypedBytesInput}.
   * 
   * @param in typed bytes input object
   * @return typed bytes record input corresponding to the supplied
   *         {@link TypedBytesInput}.
   */
  public static TypedBytesRecordInput get(TypedBytesInput in) {
    TypedBytesRecordInput bin = (TypedBytesRecordInput) tbIn.get();
    bin.setTypedBytesInput(in);
    return bin;
  }

  /**
   * Get a thread-local typed bytes record input for the supplied
   * {@link DataInput}.
   * 
   * @param in data input object
   * @return typed bytes record input corresponding to the supplied
   *         {@link DataInput}.
   */
  public static TypedBytesRecordInput get(DataInput in) {
    return get(TypedBytesInput.get(in));
  }

  /** Creates a new instance of TypedBytesRecordInput. */
  public TypedBytesRecordInput(TypedBytesInput in) {
    this.in = in;
  }

  /** Creates a new instance of TypedBytesRecordInput. */
  public TypedBytesRecordInput(DataInput in) {
    this(new TypedBytesInput(in));
  }

  public boolean readBool(String tag) throws IOException {
    in.skipType();
    return in.readBool();
  }

  public Buffer readBuffer(String tag) throws IOException {
    in.skipType();
    return new Buffer(in.readBytes());
  }

  public byte readByte(String tag) throws IOException {
    in.skipType();
    return in.readByte();
  }

  public double readDouble(String tag) throws IOException {
    in.skipType();
    return in.readDouble();
  }

  public float readFloat(String tag) throws IOException {
    in.skipType();
    return in.readFloat();
  }

  public int readInt(String tag) throws IOException {
    in.skipType();
    return in.readInt();
  }

  public long readLong(String tag) throws IOException {
    in.skipType();
    return in.readLong();
  }

  public String readString(String tag) throws IOException {
    in.skipType();
    return in.readString();
  }

  public void startRecord(String tag) throws IOException {
    in.skipType();
  }

  public Index startVector(String tag) throws IOException {
    in.skipType();
    return new TypedBytesIndex(in.readVectorHeader());
  }

  public Index startMap(String tag) throws IOException {
    in.skipType();
    return new TypedBytesIndex(in.readMapHeader());
  }

  public void endRecord(String tag) throws IOException {}

  public void endVector(String tag) throws IOException {}

  public void endMap(String tag) throws IOException {}

  private static  final class TypedBytesIndex implements Index {
    private int nelems;

    private TypedBytesIndex(int nelems) {
      this.nelems = nelems;
    }

    public boolean done() {
      return (nelems <= 0);
    }

    public void incr() {
      nelems--;
    }
  }

}
