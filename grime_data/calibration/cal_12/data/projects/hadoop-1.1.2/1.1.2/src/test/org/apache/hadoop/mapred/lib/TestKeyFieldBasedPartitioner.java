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
package org.apache.hadoop.mapred.lib;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner;

import junit.framework.TestCase;

public class TestKeyFieldBasedPartitioner extends TestCase {

  /**
   * Test is key-field-based partitioned works with empty key.
   */
  public void testEmptyKey() throws Exception {
    int numReducers = 10;
    KeyFieldBasedPartitioner<Text, Text> kfbp = 
      new KeyFieldBasedPartitioner<Text, Text>();
    JobConf conf = new JobConf();
    conf.setInt("num.key.fields.for.partition", 10);
    kfbp.configure(conf);
    assertEquals("Empty key should map to 0th partition", 
                 0, kfbp.getPartition(new Text(), new Text(), numReducers));
    
    // check if the hashcode is correct when no keyspec is specified
    kfbp = new KeyFieldBasedPartitioner<Text, Text>();
    conf = new JobConf();
    kfbp.configure(conf);
    String input = "abc\tdef\txyz";
    int hashCode = input.hashCode();
    int expectedPartition = kfbp.getPartition(hashCode, numReducers);
    assertEquals("Partitioner doesnt work as expected", expectedPartition, 
                 kfbp.getPartition(new Text(input), new Text(), numReducers));
    
    // check if the hashcode is correct with specified keyspec
    kfbp = new KeyFieldBasedPartitioner<Text, Text>();
    conf = new JobConf();
    conf.set("mapred.text.key.partitioner.options", "-k2,2");
    kfbp.configure(conf);
    String expectedOutput = "def";
    byte[] eBytes = expectedOutput.getBytes();
    hashCode = kfbp.hashCode(eBytes, 0, eBytes.length - 1, 0);
    expectedPartition = kfbp.getPartition(hashCode, numReducers);
    assertEquals("Partitioner doesnt work as expected", expectedPartition, 
                 kfbp.getPartition(new Text(input), new Text(), numReducers));
    
    // test with invalid end index in keyspecs
    kfbp = new KeyFieldBasedPartitioner<Text, Text>();
    conf = new JobConf();
    conf.set("mapred.text.key.partitioner.options", "-k2,5");
    kfbp.configure(conf);
    expectedOutput = "def\txyz";
    eBytes = expectedOutput.getBytes();
    hashCode = kfbp.hashCode(eBytes, 0, eBytes.length - 1, 0);
    expectedPartition = kfbp.getPartition(hashCode, numReducers);
    assertEquals("Partitioner doesnt work as expected", expectedPartition, 
                 kfbp.getPartition(new Text(input), new Text(), numReducers));
    
    // test with 0 end index in keyspecs
    kfbp = new KeyFieldBasedPartitioner<Text, Text>();
    conf = new JobConf();
    conf.set("mapred.text.key.partitioner.options", "-k2");
    kfbp.configure(conf);
    expectedOutput = "def\txyz";
    eBytes = expectedOutput.getBytes();
    hashCode = kfbp.hashCode(eBytes, 0, eBytes.length - 1, 0);
    expectedPartition = kfbp.getPartition(hashCode, numReducers);
    assertEquals("Partitioner doesnt work as expected", expectedPartition, 
                 kfbp.getPartition(new Text(input), new Text(), numReducers));
    
    // test with invalid keyspecs
    kfbp = new KeyFieldBasedPartitioner<Text, Text>();
    conf = new JobConf();
    conf.set("mapred.text.key.partitioner.options", "-k10");
    kfbp.configure(conf);
    assertEquals("Partitioner doesnt work as expected", 0, 
                 kfbp.getPartition(new Text(input), new Text(), numReducers));
    
    // test with multiple keyspecs
    kfbp = new KeyFieldBasedPartitioner<Text, Text>();
    conf = new JobConf();
    conf.set("mapred.text.key.partitioner.options", "-k2,2 -k4,4");
    kfbp.configure(conf);
    input = "abc\tdef\tpqr\txyz";
    expectedOutput = "def";
    eBytes = expectedOutput.getBytes();
    hashCode = kfbp.hashCode(eBytes, 0, eBytes.length - 1, 0);
    expectedOutput = "xyz";
    eBytes = expectedOutput.getBytes();
    hashCode = kfbp.hashCode(eBytes, 0, eBytes.length - 1, hashCode);
    expectedPartition = kfbp.getPartition(hashCode, numReducers);
    assertEquals("Partitioner doesnt work as expected", expectedPartition, 
                 kfbp.getPartition(new Text(input), new Text(), numReducers));
    
    // test with invalid start index in keyspecs
    kfbp = new KeyFieldBasedPartitioner<Text, Text>();
    conf = new JobConf();
    conf.set("mapred.text.key.partitioner.options", "-k2,2 -k30,21 -k4,4 -k5");
    kfbp.configure(conf);
    expectedOutput = "def";
    eBytes = expectedOutput.getBytes();
    hashCode = kfbp.hashCode(eBytes, 0, eBytes.length - 1, 0);
    expectedOutput = "xyz";
    eBytes = expectedOutput.getBytes();
    hashCode = kfbp.hashCode(eBytes, 0, eBytes.length - 1, hashCode);
    expectedPartition = kfbp.getPartition(hashCode, numReducers);
    assertEquals("Partitioner doesnt work as expected", expectedPartition, 
                 kfbp.getPartition(new Text(input), new Text(), numReducers));
  }
}
