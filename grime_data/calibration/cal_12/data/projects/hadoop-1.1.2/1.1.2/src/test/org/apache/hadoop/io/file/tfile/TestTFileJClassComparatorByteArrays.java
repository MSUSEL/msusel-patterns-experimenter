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
package org.apache.hadoop.io.file.tfile;

import java.io.IOException;

import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.WritableComparator;

/**
 * 
 * Byte arrays test case class using GZ compression codec, base class of none
 * and LZO compression classes.
 * 
 */

public class TestTFileJClassComparatorByteArrays extends TestTFileByteArrays {
  /**
   * Test non-compression codec, using the same test cases as in the ByteArrays.
   */
  @Override
  public void setUp() throws IOException {
    init(Compression.Algorithm.GZ.getName(),
        "jclass: org.apache.hadoop.io.file.tfile.MyComparator",
        "TFileTestJClassComparator", 4480, 4263);
    super.setUp();
  }
}

class MyComparator implements RawComparator<byte[]> {

  @Override
  public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
    return WritableComparator.compareBytes(b1, s1, l1, b2, s2, l2);
  }

  @Override
  public int compare(byte[] o1, byte[] o2) {
    return WritableComparator.compareBytes(o1, 0, o1.length, o2, 0, o2.length);
  }
  
}

