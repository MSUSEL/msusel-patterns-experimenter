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
package org.apache.hadoop.mapreduce.lib.partition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BinaryComparable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.util.ReflectionUtils;

import junit.framework.TestCase;

public class TestBinaryPartitioner extends TestCase {

  public void testDefaultOffsets() {
    Configuration conf = new Configuration();
    BinaryPartitioner<?> partitioner = 
      ReflectionUtils.newInstance(BinaryPartitioner.class, conf);
    
    BinaryComparable key1 = new BytesWritable(new byte[] { 1, 2, 3, 4, 5 }); 
    BinaryComparable key2 = new BytesWritable(new byte[] { 1, 2, 3, 4, 5 });
    int partition1 = partitioner.getPartition(key1, null, 10);
    int partition2 = partitioner.getPartition(key2, null, 10);
    assertEquals(partition1, partition2);
    
    key1 = new BytesWritable(new byte[] { 1, 2, 3, 4, 5 }); 
    key2 = new BytesWritable(new byte[] { 6, 2, 3, 4, 5 });
    partition1 = partitioner.getPartition(key1, null, 10);
    partition2 = partitioner.getPartition(key2, null, 10);
    assertTrue(partition1 != partition2);
    
    key1 = new BytesWritable(new byte[] { 1, 2, 3, 4, 5 }); 
    key2 = new BytesWritable(new byte[] { 1, 2, 3, 4, 6 });
    partition1 = partitioner.getPartition(key1, null, 10);
    partition2 = partitioner.getPartition(key2, null, 10);
    assertTrue(partition1 != partition2);
  }
  
  public void testCustomOffsets() {
    Configuration conf = new Configuration();
    BinaryComparable key1 = new BytesWritable(new byte[] { 1, 2, 3, 4, 5 }); 
    BinaryComparable key2 = new BytesWritable(new byte[] { 6, 2, 3, 7, 8 });
    
    BinaryPartitioner.setOffsets(conf, 1, -3);
    BinaryPartitioner<?> partitioner = 
      ReflectionUtils.newInstance(BinaryPartitioner.class, conf);
    int partition1 = partitioner.getPartition(key1, null, 10);
    int partition2 = partitioner.getPartition(key2, null, 10);
    assertEquals(partition1, partition2);
    
    BinaryPartitioner.setOffsets(conf, 1, 2);
    partitioner = ReflectionUtils.newInstance(BinaryPartitioner.class, conf);
    partition1 = partitioner.getPartition(key1, null, 10);
    partition2 = partitioner.getPartition(key2, null, 10);
    assertEquals(partition1, partition2);
    
    BinaryPartitioner.setOffsets(conf, -4, -3);
    partitioner = ReflectionUtils.newInstance(BinaryPartitioner.class, conf);
    partition1 = partitioner.getPartition(key1, null, 10);
    partition2 = partitioner.getPartition(key2, null, 10);
    assertEquals(partition1, partition2);
  }
  
  public void testLowerBound() {
    Configuration conf = new Configuration();
    BinaryPartitioner.setLeftOffset(conf, 0);
    BinaryPartitioner<?> partitioner = 
      ReflectionUtils.newInstance(BinaryPartitioner.class, conf);
    BinaryComparable key1 = new BytesWritable(new byte[] { 1, 2, 3, 4, 5 }); 
    BinaryComparable key2 = new BytesWritable(new byte[] { 6, 2, 3, 4, 5 });
    int partition1 = partitioner.getPartition(key1, null, 10);
    int partition2 = partitioner.getPartition(key2, null, 10);
    assertTrue(partition1 != partition2);
  }
  
  public void testUpperBound() {
    Configuration conf = new Configuration();
    BinaryPartitioner.setRightOffset(conf, 4);
    BinaryPartitioner<?> partitioner = 
      ReflectionUtils.newInstance(BinaryPartitioner.class, conf);
    BinaryComparable key1 = new BytesWritable(new byte[] { 1, 2, 3, 4, 5 }); 
    BinaryComparable key2 = new BytesWritable(new byte[] { 1, 2, 3, 4, 6 });
    int partition1 = partitioner.getPartition(key1, null, 10);
    int partition2 = partitioner.getPartition(key2, null, 10);
    assertTrue(partition1 != partition2);
  }
  
}
