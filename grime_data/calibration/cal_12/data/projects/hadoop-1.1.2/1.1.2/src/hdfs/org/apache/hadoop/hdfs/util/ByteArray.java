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
package org.apache.hadoop.hdfs.util;

import java.util.Arrays;

/** 
 * Wrapper for byte[] to use byte[] as key in HashMap
 */
public class ByteArray {
  private int hash = 0; // cache the hash code
  private final byte[] bytes;
  
  public ByteArray(byte[] bytes) {
    this.bytes = bytes;
  }
  
  public byte[] getBytes() {
    return bytes;
  }
  
  @Override
  public int hashCode() {
    if (hash == 0) {
      hash = Arrays.hashCode(bytes);
    }
    return hash;
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ByteArray)) {
      return false;
    }
    return Arrays.equals(bytes, ((ByteArray)o).bytes);
  }
}
