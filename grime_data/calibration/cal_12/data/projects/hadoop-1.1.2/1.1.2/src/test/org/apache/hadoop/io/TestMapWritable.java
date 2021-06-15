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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Tests MapWritable
 */
public class TestMapWritable extends TestCase {
  /** the test */
  @SuppressWarnings("unchecked")
  public void testMapWritable() {
    Text[] keys = {
        new Text("key1"),
        new Text("key2"),
        new Text("Key3"),
    };
    
    BytesWritable[] values = {
        new BytesWritable("value1".getBytes()),
        new BytesWritable("value2".getBytes()),
        new BytesWritable("value3".getBytes())
    };

    MapWritable inMap = new MapWritable();
    for (int i = 0; i < keys.length; i++) {
      inMap.put(keys[i], values[i]);
    }

    MapWritable outMap = new MapWritable(inMap);
    assertEquals(inMap.size(), outMap.size());
    
    for (Map.Entry<Writable, Writable> e: inMap.entrySet()) {
      assertTrue(outMap.containsKey(e.getKey()));
      assertEquals(0, ((WritableComparable) outMap.get(e.getKey())).compareTo(
          e.getValue()));
    }
    
    // Now for something a little harder...
    
    Text[] maps = {
        new Text("map1"),
        new Text("map2")
    };
    
    MapWritable mapOfMaps = new MapWritable();
    mapOfMaps.put(maps[0], inMap);
    mapOfMaps.put(maps[1], outMap);
    
    MapWritable copyOfMapOfMaps = new MapWritable(mapOfMaps);
    for (int i = 0; i < maps.length; i++) {
      assertTrue(copyOfMapOfMaps.containsKey(maps[i]));
      MapWritable a = (MapWritable) mapOfMaps.get(maps[i]);
      MapWritable b = (MapWritable) copyOfMapOfMaps.get(maps[i]);
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
    MapWritable inMap = new MapWritable();
    inMap.put(new Text("key"), new UTF8("value"));
    inMap.put(new Text("key2"), new UTF8("value2"));
    MapWritable outMap = new MapWritable(inMap);
    MapWritable copyOfCopy = new MapWritable(outMap);
    assertEquals(1, copyOfCopy.getNewClasses());
  }
  
  /**
   * Assert MapWritable does not grow across calls to readFields.
   * @throws Exception
   * @see <a href="https://issues.apache.org/jira/browse/HADOOP-2244">HADOOP-2244</a>
   */
  public void testMultipleCallsToReadFieldsAreSafe() throws Exception {
    // Create an instance and add a key/value.
    MapWritable m = new MapWritable();
    final Text t = new Text(getName());
    m.put(t, t);
    // Get current size of map.  Key values are 't'.
    int count = m.size();
    // Now serialize... save off the bytes.
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    m.write(dos);
    dos.close();
    // Now add new values to the MapWritable.
    m.put(new Text("key1"), new Text("value1"));
    m.put(new Text("key2"), new Text("value2"));
    // Now deserialize the original MapWritable.  Ensure count and key values
    // match original state.
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    DataInputStream dis = new DataInputStream(bais);
    m.readFields(dis);
    assertEquals(count, m.size());
    assertTrue(m.get(t).equals(t));
    dis.close();
  }
}
