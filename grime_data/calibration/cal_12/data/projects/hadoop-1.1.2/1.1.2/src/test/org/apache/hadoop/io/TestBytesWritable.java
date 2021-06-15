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
package org.apache.hadoop.io;

import junit.framework.TestCase;

/**
 * This is the unit test for BytesWritable.
 */
public class TestBytesWritable extends TestCase {

  public void testSizeChange() throws Exception {
    byte[] hadoop = "hadoop".getBytes();
    BytesWritable buf = new BytesWritable(hadoop);
    int size = buf.getLength();
    int orig_capacity = buf.getCapacity();
    buf.setSize(size*2);
    int new_capacity = buf.getCapacity();
    System.arraycopy(buf.getBytes(), 0, buf.getBytes(), size, size);
    assertTrue(new_capacity >= size * 2);
    assertEquals(size * 2, buf.getLength());
    assertTrue(new_capacity != orig_capacity);
    buf.setSize(size*4);
    assertTrue(new_capacity != buf.getCapacity());
    for(int i=0; i < size*2; ++i) {
      assertEquals(hadoop[i%size], buf.getBytes()[i]);
    }
    // shrink the buffer
    buf.setCapacity(1);
    // make sure the size has been cut down too
    assertEquals(1, buf.getLength());
    // but that the data is still there
    assertEquals(hadoop[0], buf.getBytes()[0]);
  }
  
  public void testHash() throws Exception {
    byte[] owen = "owen".getBytes();
    BytesWritable buf = new BytesWritable(owen);
    assertEquals(4347922, buf.hashCode());
    buf.setCapacity(10000);
    assertEquals(4347922, buf.hashCode());
    buf.setSize(0);
    assertEquals(1, buf.hashCode());
  }
  
  public void testCompare() throws Exception {
    byte[][] values = new byte[][]{"abc".getBytes(), 
                                   "ad".getBytes(),
                                   "abcd".getBytes(),
                                   "".getBytes(),
                                   "b".getBytes()};
    BytesWritable[] buf = new BytesWritable[values.length];
    for(int i=0; i < values.length; ++i) {
      buf[i] = new BytesWritable(values[i]);
    }
    // check to make sure the compare function is symetric and reflexive
    for(int i=0; i < values.length; ++i) {
      for(int j=0; j < values.length; ++j) {
        assertTrue(buf[i].compareTo(buf[j]) == -buf[j].compareTo(buf[i]));
        assertTrue((i == j) == (buf[i].compareTo(buf[j]) == 0));
      }
    }
    assertTrue(buf[0].compareTo(buf[1]) < 0);
    assertTrue(buf[1].compareTo(buf[2]) > 0);
    assertTrue(buf[2].compareTo(buf[3]) > 0);
    assertTrue(buf[3].compareTo(buf[4]) < 0);
  }
  
  private void checkToString(byte[] input, String expected) {
    String actual = new BytesWritable(input).toString();
    assertEquals(expected, actual);
  }

  public void testToString() {
    checkToString(new byte[]{0,1,2,0x10}, "00 01 02 10");
    checkToString(new byte[]{-0x80, -0x7f, -0x1, -0x2, 1, 0}, 
                  "80 81 ff fe 01 00");
  }
}

