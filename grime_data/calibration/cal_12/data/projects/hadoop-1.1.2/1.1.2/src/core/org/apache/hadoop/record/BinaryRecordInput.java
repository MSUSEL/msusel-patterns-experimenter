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

import java.io.DataInput;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;

/**
 */
public class BinaryRecordInput implements RecordInput {
    
  private DataInput in;
    
  static private class BinaryIndex implements Index {
    private int nelems;
    private BinaryIndex(int nelems) {
      this.nelems = nelems;
    }
    public boolean done() {
      return (nelems <= 0);
    }
    public void incr() {
      nelems--;
    }
  }
    
  private BinaryRecordInput() {}
    
  private void setDataInput(DataInput inp) {
    this.in = inp;
  }
    
  private static ThreadLocal bIn = new ThreadLocal() {
      protected synchronized Object initialValue() {
        return new BinaryRecordInput();
      }
    };
    
  /**
   * Get a thread-local record input for the supplied DataInput.
   * @param inp data input stream
   * @return binary record input corresponding to the supplied DataInput.
   */
  public static BinaryRecordInput get(DataInput inp) {
    BinaryRecordInput bin = (BinaryRecordInput) bIn.get();
    bin.setDataInput(inp);
    return bin;
  }
    
  /** Creates a new instance of BinaryRecordInput */
  public BinaryRecordInput(InputStream strm) {
    this.in = new DataInputStream(strm);
  }
    
  /** Creates a new instance of BinaryRecordInput */
  public BinaryRecordInput(DataInput din) {
    this.in = din;
  }
    
  public byte readByte(final String tag) throws IOException {
    return in.readByte();
  }
    
  public boolean readBool(final String tag) throws IOException {
    return in.readBoolean();
  }
    
  public int readInt(final String tag) throws IOException {
    return Utils.readVInt(in);
  }
    
  public long readLong(final String tag) throws IOException {
    return Utils.readVLong(in);
  }
    
  public float readFloat(final String tag) throws IOException {
    return in.readFloat();
  }
    
  public double readDouble(final String tag) throws IOException {
    return in.readDouble();
  }
    
  public String readString(final String tag) throws IOException {
    return Utils.fromBinaryString(in);
  }
    
  public Buffer readBuffer(final String tag) throws IOException {
    final int len = Utils.readVInt(in);
    final byte[] barr = new byte[len];
    in.readFully(barr);
    return new Buffer(barr);
  }
    
  public void startRecord(final String tag) throws IOException {
    // no-op
  }
    
  public void endRecord(final String tag) throws IOException {
    // no-op
  }
    
  public Index startVector(final String tag) throws IOException {
    return new BinaryIndex(readInt(tag));
  }
    
  public void endVector(final String tag) throws IOException {
    // no-op
  }
    
  public Index startMap(final String tag) throws IOException {
    return new BinaryIndex(readInt(tag));
  }
    
  public void endMap(final String tag) throws IOException {
    // no-op
  }
}
