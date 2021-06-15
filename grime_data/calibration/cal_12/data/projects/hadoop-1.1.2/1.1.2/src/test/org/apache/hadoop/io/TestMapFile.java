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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import junit.framework.TestCase;

public class TestMapFile extends TestCase {
  private static Configuration conf = new Configuration();

  /**
   * Test getClosest feature.
   * @throws Exception
   */
  public void testGetClosest() throws Exception {
    // Write a mapfile of simple data: keys are 
    Path dirName = new Path(System.getProperty("test.build.data",".") +
      getName() + ".mapfile"); 
    FileSystem fs = FileSystem.getLocal(conf);
    Path qualifiedDirName = fs.makeQualified(dirName);
    // Make an index entry for every third insertion.
    MapFile.Writer.setIndexInterval(conf, 3);
    MapFile.Writer writer = new MapFile.Writer(conf, fs,
      qualifiedDirName.toString(), Text.class, Text.class);
    // Assert that the index interval is 1
    assertEquals(3, writer.getIndexInterval());
    // Add entries up to 100 in intervals of ten.
    final int FIRST_KEY = 10;
    for (int i = FIRST_KEY; i < 100; i += 10) {
      String iStr = Integer.toString(i);
      Text t = new Text("00".substring(iStr.length()) + iStr);
      writer.append(t, t);
    }
    writer.close();
    // Now do getClosest on created mapfile.
    MapFile.Reader reader = new MapFile.Reader(fs, qualifiedDirName.toString(),
      conf);
    Text key = new Text("55");
    Text value = new Text();
    Text closest = (Text)reader.getClosest(key, value);
    // Assert that closest after 55 is 60
    assertEquals(new Text("60"), closest);
    // Get closest that falls before the passed key: 50
    closest = (Text)reader.getClosest(key, value, true);
    assertEquals(new Text("50"), closest);
    // Test get closest when we pass explicit key
    final Text TWENTY = new Text("20");
    closest = (Text)reader.getClosest(TWENTY, value);
    assertEquals(TWENTY, closest);
    closest = (Text)reader.getClosest(TWENTY, value, true);
    assertEquals(TWENTY, closest);
    // Test what happens at boundaries.  Assert if searching a key that is
    // less than first key in the mapfile, that the first key is returned.
    key = new Text("00");
    closest = (Text)reader.getClosest(key, value);
    assertEquals(FIRST_KEY, Integer.parseInt(closest.toString()));
    
    // If we're looking for the first key before, and we pass in a key before 
    // the first key in the file, we should get null
    closest = (Text)reader.getClosest(key, value, true);
    assertNull(closest);
    
    // Assert that null is returned if key is > last entry in mapfile.
    key = new Text("99");
    closest = (Text)reader.getClosest(key, value);
    assertNull(closest);

    // If we were looking for the key before, we should get the last key
    closest = (Text)reader.getClosest(key, value, true);
    assertEquals(new Text("90"), closest);
  }
}
