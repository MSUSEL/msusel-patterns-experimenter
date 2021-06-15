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
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;

public class TestLRUCache extends TestCase {
  private static final Log LOG = 
    LogFactory.getLog(TestLRUCache.class);
  
  public void testPut() {
    TaskTracker.LRUCache<String, Path> cache = new TaskTracker.LRUCache<String, Path>(200);
  
    for(int i=0;i<200;i++) {
       cache.put(i+"", new Path("/foo"+i));
    }
    
    Iterator<Map.Entry<String, Path>> iterator = cache.getIterator();
    int i=0;
    while(iterator.hasNext()) {
      Map.Entry<String, Path> entry = iterator.next();
      String key = entry.getKey();
      Path val = entry.getValue();
      assertEquals(i+"", key);
      i++;
    }
    LOG.info("Completed testPut");
  }
  
  
  public void testGet() {
    TaskTracker.LRUCache<String, Path> cache = new TaskTracker.LRUCache<String, Path>(200);
  
    for(int i=0;i<200;i++) {
      cache.put(i+"", new Path("/foo"+i));
    }
    
    for(int i=0;i<200;i++) {
      Path path = cache.get(i+"");
      assertEquals(path.toString(), (new Path("/foo"+i)).toString());
    }
    LOG.info("Completed testGet");
  }
  
  
  /**
   * Test if cache can be cleared properly
   */
  public void testClear() {
    TaskTracker.LRUCache<String, Path> cache = new TaskTracker.LRUCache<String, Path>(200);
  
    for(int i=0;i<200;i++) {
      cache.put(i+"", new Path("/foo"+i));
    }
    
    cache.clear();
    assertTrue(cache.size() == 0);
    LOG.info("Completed testClear");
  }
  
  /**
   * Test for cache overflow condition
   */
  public void testOverFlow() {
    TaskTracker.LRUCache<String, Path> cache = new TaskTracker.LRUCache<String, Path>(200);
  
    int SIZE = 5000;
  
    for(int i=0;i<SIZE;i++) {
      cache.put(i+"", new Path("/foo"+i));
    }
    
    //Check if the items are removed properly when the cache size is exceeded
    for(int i=SIZE-1;i>=SIZE-200;i--) {
      Path path = cache.get(i+"");
      assertEquals(path.toString(), (new Path("/foo"+i)).toString());
    }
    
    LOG.info("Completed testOverFlow");
  }
}