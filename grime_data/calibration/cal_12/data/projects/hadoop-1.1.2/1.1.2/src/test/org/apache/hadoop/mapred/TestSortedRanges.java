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
package org.apache.hadoop.mapred;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapred.SortedRanges.Range;

public class TestSortedRanges extends TestCase {
  private static final Log LOG = 
    LogFactory.getLog(TestSortedRanges.class);
  
  public void testAdd() {
    SortedRanges sr = new SortedRanges();
    sr.add(new Range(2,9));
    assertEquals(9, sr.getIndicesCount());
    
    sr.add(new SortedRanges.Range(3,5));
    assertEquals(9, sr.getIndicesCount());
    
    sr.add(new SortedRanges.Range(7,1));
    assertEquals(9, sr.getIndicesCount());
    
    sr.add(new Range(1,12));
    assertEquals(12, sr.getIndicesCount());
    
    sr.add(new Range(7,9));
    assertEquals(15, sr.getIndicesCount());
    
    sr.add(new Range(31,10));
    sr.add(new Range(51,10));
    sr.add(new Range(66,10));
    assertEquals(45, sr.getIndicesCount());
    
    sr.add(new Range(21,50));
    assertEquals(70, sr.getIndicesCount());
    
    LOG.debug(sr);
    
    Iterator<Long> it = sr.skipRangeIterator();
    int i = 0;
    assertEquals(i, it.next().longValue());
    for(i=16;i<21;i++) {
      assertEquals(i, it.next().longValue());
    }
    assertEquals(76, it.next().longValue());
    assertEquals(77, it.next().longValue());
    
  }
  
  public void testRemove() {
    SortedRanges sr = new SortedRanges();
    sr.add(new Range(2,19));
    assertEquals(19, sr.getIndicesCount());
    
    sr.remove(new SortedRanges.Range(15,8));
    assertEquals(13, sr.getIndicesCount());
    
    sr.remove(new SortedRanges.Range(6,5));
    assertEquals(8, sr.getIndicesCount());
    
    sr.remove(new SortedRanges.Range(8,4));
    assertEquals(7, sr.getIndicesCount());
    
    sr.add(new Range(18,5));
    assertEquals(12, sr.getIndicesCount());
    
    sr.add(new Range(25,1));
    assertEquals(13, sr.getIndicesCount());
    
    sr.remove(new SortedRanges.Range(7,24));
    assertEquals(4, sr.getIndicesCount());
    
    sr.remove(new SortedRanges.Range(5,1));
    assertEquals(3, sr.getIndicesCount());
    
    LOG.debug(sr);
  }

}
