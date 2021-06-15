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

import junit.framework.*;

/**
 * A Unit test for Record I/O Buffer class
 */
public class TestBuffer extends TestCase {
  
  public TestBuffer(String testName) {
    super(testName);
  }
  
  /**
   * Test of set method, of class org.apache.hadoop.record.Buffer.
   */
  public void testSet() {
    final byte[] bytes = new byte[10];
    final Buffer instance = new Buffer();
    
    instance.set(bytes);
    
    assertEquals("set failed", bytes, instance.get());
  }
  
  /**
   * Test of copy method, of class org.apache.hadoop.record.Buffer.
   */
  public void testCopy() {
    final byte[] bytes = new byte[10];
    final int offset = 6;
    final int length = 3;
    for (int idx = 0; idx < 10; idx ++) {
      bytes[idx] = (byte) idx;
    }
    final Buffer instance = new Buffer();
    
    instance.copy(bytes, offset, length);
    
    assertEquals("copy failed", 3, instance.getCapacity());
    assertEquals("copy failed", 3, instance.get().length);
    for (int idx = 0; idx < 3; idx++) {
      assertEquals("Buffer content corrupted", idx+6, instance.get()[idx]);
    }
  }
  
  /**
   * Test of getCount method, of class org.apache.hadoop.record.Buffer.
   */
  public void testGetCount() {
    final Buffer instance = new Buffer();
    
    final int expResult = 0;
    final int result = instance.getCount();
    assertEquals("getSize failed", expResult, result);
  }
  
  /**
   * Test of getCapacity method, of class org.apache.hadoop.record.Buffer.
   */
  public void testGetCapacity() {
    final Buffer instance = new Buffer();
    
    final int expResult = 0;
    final int result = instance.getCapacity();
    assertEquals("getCapacity failed", expResult, result);
    
    instance.setCapacity(100);
    assertEquals("setCapacity failed", 100, instance.getCapacity());
  }
  
  /**
   * Test of truncate method, of class org.apache.hadoop.record.Buffer.
   */
  public void testTruncate() {
    final Buffer instance = new Buffer();
    instance.setCapacity(100);
    assertEquals("setCapacity failed", 100, instance.getCapacity());
    
    instance.truncate();
    assertEquals("truncate failed", 0, instance.getCapacity());
  }
  
  /**
   * Test of append method, of class org.apache.hadoop.record.Buffer.
   */
  public void testAppend() {
    final byte[] bytes = new byte[100];
    final int offset = 0;
    final int length = 100;
    for (int idx = 0; idx < 100; idx++) {
      bytes[idx] = (byte) (100-idx);
    }
    
    final Buffer instance = new Buffer();
    
    instance.append(bytes, offset, length);
    
    assertEquals("Buffer size mismatch", 100, instance.getCount());
    
    for (int idx = 0; idx < 100; idx++) {
      assertEquals("Buffer contents corrupted", 100-idx, instance.get()[idx]);
    }
    
  }
}
