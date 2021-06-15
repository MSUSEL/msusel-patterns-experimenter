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

import org.apache.hadoop.io.TestWritable;
import junit.framework.TestCase;
import java.security.MessageDigest;
import java.util.Random;

/** Unit tests for MD5Hash. */
public class TestMD5Hash extends TestCase {
  public TestMD5Hash(String name) { super(name); }

  private static final Random RANDOM = new Random();

  public static MD5Hash getTestHash() throws Exception {
    MessageDigest digest = MessageDigest.getInstance("MD5");
    byte[] buffer = new byte[1024];
    RANDOM.nextBytes(buffer);
    digest.update(buffer);
    return new MD5Hash(digest.digest());
  }

  protected static byte[] D00 = new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  protected static byte[] DFF = new byte[] {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}; 
  
  public void testMD5Hash() throws Exception {
    MD5Hash md5Hash = getTestHash();

    final MD5Hash md5Hash00
      = new MD5Hash(D00);

    final MD5Hash md5HashFF
      = new MD5Hash(DFF);
    
    MD5Hash orderedHash = new MD5Hash(new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,
                                                 13,14,15,16});
    MD5Hash backwardHash = new MD5Hash(new byte[]{-1,-2,-3,-4,-5,-6,-7,-8,
                                                  -9,-10,-11,-12, -13, -14,
                                                  -15,-16});
    MD5Hash closeHash1 = new MD5Hash(new byte[]{-1,0,0,0,0,0,0,0,
                                                0,0,0,0,0,0,0,0});
    MD5Hash closeHash2 = new MD5Hash(new byte[]{-1,1,0,0,0,0,0,0,
                                                0,0,0,0,0,0,0,0});

    // test i/o
    TestWritable.testWritable(md5Hash);
    TestWritable.testWritable(md5Hash00);
    TestWritable.testWritable(md5HashFF);

    // test equals()
    assertEquals(md5Hash, md5Hash);
    assertEquals(md5Hash00, md5Hash00);
    assertEquals(md5HashFF, md5HashFF);

    // test compareTo()
    assertTrue(md5Hash.compareTo(md5Hash) == 0);
    assertTrue(md5Hash00.compareTo(md5Hash) < 0);
    assertTrue(md5HashFF.compareTo(md5Hash) > 0);

    // test toString and string ctor
    assertEquals(md5Hash, new MD5Hash(md5Hash.toString()));
    assertEquals(md5Hash00, new MD5Hash(md5Hash00.toString()));
    assertEquals(md5HashFF, new MD5Hash(md5HashFF.toString()));

    assertEquals(0x01020304, orderedHash.quarterDigest());
    assertEquals(0xfffefdfc, backwardHash.quarterDigest());
    
    assertEquals(0x0102030405060708L, orderedHash.halfDigest());
    assertEquals(0xfffefdfcfbfaf9f8L, backwardHash.halfDigest());
    assertTrue("hash collision", 
               closeHash1.hashCode() != closeHash2.hashCode());
     
    Thread t1 = new Thread() {      
      public void run() {
        for (int i = 0; i < 100; i++) {
          MD5Hash hash = new MD5Hash(DFF);
          assertEquals(hash, md5HashFF);
        }        
      }
    };
    
    Thread t2 = new Thread() {
      public void run() {
        for (int i = 0; i < 100; i++) {
          MD5Hash hash = new MD5Hash(D00);
          assertEquals(hash, md5Hash00);
        }
      }      
    };
    
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    
  }
	
}
