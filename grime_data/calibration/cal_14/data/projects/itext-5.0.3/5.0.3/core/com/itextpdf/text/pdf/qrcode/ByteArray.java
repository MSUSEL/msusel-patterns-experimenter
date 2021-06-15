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
package com.itextpdf.text.pdf.qrcode;

/**
 * This class implements an array of unsigned bytes.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @since 5.0.2
 */
public final class ByteArray {

  private static final int INITIAL_SIZE = 32;

  private byte[] bytes;
  private int size;

  public ByteArray() {
    bytes = null;
    size = 0;
  }

  public ByteArray(int size) {
    bytes = new byte[size];
    this.size = size;
  }

  public ByteArray(byte[] byteArray) {
    bytes = byteArray;
    size = bytes.length;
  }

  /**
   * Access an unsigned byte at location index.
   * @param index The index in the array to access.
   * @return The unsigned value of the byte as an int.
   */
  public int at(int index) {
    return bytes[index] & 0xff;
  }

  public void set(int index, int value) {
    bytes[index] = (byte) value;
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public void appendByte(int value) {
    if (size == 0 || size >= bytes.length) {
      int newSize = Math.max(INITIAL_SIZE, size << 1);
      reserve(newSize);
    }
    bytes[size] = (byte) value;
    size++;
  }

  public void reserve(int capacity) {
    if (bytes == null || bytes.length < capacity) {
      byte[] newArray = new byte[capacity];
      if (bytes != null) {
        System.arraycopy(bytes, 0, newArray, 0, bytes.length);
      }
      bytes = newArray;
    }
  }

  // Copy count bytes from array source starting at offset.
  public void set(byte[] source, int offset, int count) {
    bytes = new byte[count];
    size = count;
    for (int x = 0; x < count; x++) {
      bytes[x] = source[offset + x];
    }
  }

}
