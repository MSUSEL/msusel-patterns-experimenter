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
package org.apache.hadoop.util;

public class UTF8ByteArrayUtils {
  /**
   * Find the first occurrence of the given byte b in a UTF-8 encoded string
   * @param utf a byte array containing a UTF-8 encoded string
   * @param start starting offset
   * @param end ending position
   * @param b the byte to find
   * @return position that first byte occures otherwise -1
   */
  public static int findByte(byte [] utf, int start, int end, byte b) {
    for(int i=start; i<end; i++) {
      if (utf[i]==b) {
        return i;
      }
    }
    return -1;      
  }

  /**
   * Find the first occurrence of the given bytes b in a UTF-8 encoded string
   * @param utf a byte array containing a UTF-8 encoded string
   * @param start starting offset
   * @param end ending position
   * @param b the bytes to find
   * @return position that first byte occures otherwise -1
   */
  public static int findBytes(byte [] utf, int start, int end, byte[] b) {
    int matchEnd = end - b.length;
    for(int i=start; i<=matchEnd; i++) {
      boolean matched = true;
      for(int j=0; j<b.length; j++) {
        if (utf[i+j] != b[j]) {
          matched = false;
          break;
        }
      }
      if (matched) {
        return i;
      }
    }
    return -1;      
  }
    
  /**
   * Find the nth occurrence of the given byte b in a UTF-8 encoded string
   * @param utf a byte array containing a UTF-8 encoded string
   * @param start starting offset
   * @param length the length of byte array
   * @param b the byte to find
   * @param n the desired occurrence of the given byte
   * @return position that nth occurrence of the given byte if exists; otherwise -1
   */
  public static int findNthByte(byte [] utf, int start, int length, byte b, int n) {
    int pos = -1;
    int nextStart = start;
    for (int i = 0; i < n; i++) {
      pos = findByte(utf, nextStart, length, b);
      if (pos < 0) {
        return pos;
      }
      nextStart = pos + 1;
    }
    return pos;      
  }
  
  /**
   * Find the nth occurrence of the given byte b in a UTF-8 encoded string
   * @param utf a byte array containing a UTF-8 encoded string
   * @param b the byte to find
   * @param n the desired occurrence of the given byte
   * @return position that nth occurrence of the given byte if exists; otherwise -1
   */
  public static int findNthByte(byte [] utf, byte b, int n) {
    return findNthByte(utf, 0, utf.length, b, n);      
  }

}

