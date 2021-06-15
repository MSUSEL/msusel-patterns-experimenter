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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import junit.framework.TestCase;

public class TestBloomMapFile extends TestCase {
  private static Configuration conf = new Configuration();
  
  public void testMembershipTest() throws Exception {
    // write the file
    Path dirName = new Path(System.getProperty("test.build.data",".") +
        getName() + ".bloommapfile"); 
    FileSystem fs = FileSystem.getLocal(conf);
    Path qualifiedDirName = fs.makeQualified(dirName);
    conf.setInt("io.mapfile.bloom.size", 2048);
    BloomMapFile.Writer writer = new BloomMapFile.Writer(conf, fs,
      qualifiedDirName.toString(), IntWritable.class, Text.class);
    IntWritable key = new IntWritable();
    Text value = new Text();
    for (int i = 0; i < 2000; i += 2) {
      key.set(i);
      value.set("00" + i);
      writer.append(key, value);
    }
    writer.close();
    
    BloomMapFile.Reader reader = new BloomMapFile.Reader(fs,
        qualifiedDirName.toString(), conf);
    // check false positives rate
    int falsePos = 0;
    int falseNeg = 0;
    for (int i = 0; i < 2000; i++) {
      key.set(i);
      boolean exists = reader.probablyHasKey(key);
      if (i % 2 == 0) {
        if (!exists) falseNeg++;
      } else {
        if (exists) falsePos++;
      }
    }
    reader.close();
    fs.delete(qualifiedDirName, true);
    System.out.println("False negatives: " + falseNeg);
    assertEquals(0, falseNeg);
    System.out.println("False positives: " + falsePos);
    assertTrue(falsePos < 2);
  }

  private void checkMembershipVaryingSizedKeys(String name, List<Text> keys) throws Exception {
    Path dirName = new Path(System.getProperty("test.build.data",".") +
        name + ".bloommapfile"); 
    FileSystem fs = FileSystem.getLocal(conf);
    Path qualifiedDirName = fs.makeQualified(dirName);
    BloomMapFile.Writer writer = new BloomMapFile.Writer(conf, fs,
      qualifiedDirName.toString(), Text.class, NullWritable.class);
    for (Text key : keys) {
      writer.append(key, NullWritable.get());
    }
    writer.close();

    // will check for membership in the opposite order of how keys were inserted
    BloomMapFile.Reader reader = new BloomMapFile.Reader(fs,
        qualifiedDirName.toString(), conf);
    Collections.reverse(keys);
    for (Text key : keys) {
      assertTrue("False negative for existing key " + key, reader.probablyHasKey(key));
    }
    reader.close();
    fs.delete(qualifiedDirName, true);
  }

  public void testMembershipVaryingSizedKeysTest1() throws Exception {
    ArrayList<Text> list = new ArrayList<Text>();
    list.add(new Text("A"));
    list.add(new Text("BB"));
    checkMembershipVaryingSizedKeys(getName(), list);
  }

  public void testMembershipVaryingSizedKeysTest2() throws Exception {
    ArrayList<Text> list = new ArrayList<Text>();
    list.add(new Text("AA"));
    list.add(new Text("B"));
    checkMembershipVaryingSizedKeys(getName(), list);
  }

}
