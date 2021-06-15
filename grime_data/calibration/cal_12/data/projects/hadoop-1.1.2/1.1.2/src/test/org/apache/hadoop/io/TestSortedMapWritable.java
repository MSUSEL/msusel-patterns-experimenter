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

import java.util.Map;

import junit.framework.TestCase;

/**
 * Tests SortedMapWritable
 */
public class TestSortedMapWritable extends TestCase {
  /** the test */
  @SuppressWarnings("unchecked")
  public void testSortedMapWritable() {
    Text[] keys = {
        new Text("key1"),
        new Text("key2"),
        new Text("key3"),
    };
    
    BytesWritable[] values = {
        new BytesWritable("value1".getBytes()),
        new BytesWritable("value2".getBytes()),
        new BytesWritable("value3".getBytes())
    };

    SortedMapWritable inMap = new SortedMapWritable();
    for (int i = 0; i < keys.length; i++) {
      inMap.put(keys[i], values[i]);
    }
    
    assertEquals(0, inMap.firstKey().compareTo(keys[0]));
    assertEquals(0, inMap.lastKey().compareTo(keys[2]));

    SortedMapWritable outMap = new SortedMapWritable(inMap);
    assertEquals(inMap.size(), outMap.size());
    
    for (Map.Entry<WritableComparable, Writable> e: inMap.entrySet()) {
      assertTrue(outMap.containsKey(e.getKey()));
      assertEquals(0, ((WritableComparable) outMap.get(e.getKey())).compareTo(
          e.getValue()));
    }
    
    // Now for something a little harder...
    
    Text[] maps = {
        new Text("map1"),
        new Text("map2")
    };
    
    SortedMapWritable mapOfMaps = new SortedMapWritable();
    mapOfMaps.put(maps[0], inMap);
    mapOfMaps.put(maps[1], outMap);
    
    SortedMapWritable copyOfMapOfMaps = new SortedMapWritable(mapOfMaps);
    for (int i = 0; i < maps.length; i++) {
      assertTrue(copyOfMapOfMaps.containsKey(maps[i]));

      SortedMapWritable a = (SortedMapWritable) mapOfMaps.get(maps[i]);
      SortedMapWritable b = (SortedMapWritable) copyOfMapOfMaps.get(maps[i]);
      assertEquals(a.size(), b.size());
      for (Writable key: a.keySet()) {
        assertTrue(b.containsKey(key));
        
        // This will work because we know what we put into each set
        
        WritableComparable aValue = (WritableComparable) a.get(key);
        WritableComparable bValue = (WritableComparable) b.get(key);
        assertEquals(0, aValue.compareTo(bValue));
      }
    }
  }
  
  /**
   * Test that number of "unknown" classes is propagated across multiple copies.
   */
  @SuppressWarnings("deprecation")
  public void testForeignClass() {
    SortedMapWritable inMap = new SortedMapWritable();
    inMap.put(new Text("key"), new UTF8("value"));
    inMap.put(new Text("key2"), new UTF8("value2"));
    SortedMapWritable outMap = new SortedMapWritable(inMap);
    SortedMapWritable copyOfCopy = new SortedMapWritable(outMap);
    assertEquals(1, copyOfCopy.getNewClasses());
  }
}
