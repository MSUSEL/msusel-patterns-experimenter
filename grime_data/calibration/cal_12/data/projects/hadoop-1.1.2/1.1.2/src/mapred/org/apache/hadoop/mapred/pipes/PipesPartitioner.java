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
package org.apache.hadoop.mapred.pipes;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Partitioner;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * This partitioner is one that can either be set manually per a record or it
 * can fall back onto a Java partitioner that was set by the user.
 */
class PipesPartitioner<K extends WritableComparable,
                       V extends Writable>
  implements Partitioner<K, V> {
  
  private static ThreadLocal<Integer> cache = new ThreadLocal<Integer>();
  private Partitioner<K, V> part = null;
  
  @SuppressWarnings("unchecked")
  public void configure(JobConf conf) {
    part =
      ReflectionUtils.newInstance(Submitter.getJavaPartitioner(conf), conf);
  }

  /**
   * Set the next key to have the given partition.
   * @param newValue the next partition value
   */
  static void setNextPartition(int newValue) {
    cache.set(newValue);
  }

  /**
   * If a partition result was set manually, return it. Otherwise, we call
   * the Java partitioner.
   * @param key the key to partition
   * @param value the value to partition
   * @param numPartitions the number of reduces
   */
  public int getPartition(K key, V value, 
                          int numPartitions) {
    Integer result = cache.get();
    if (result == null) {
      return part.getPartition(key, value, numPartitions);
    } else {
      return result;
    }
  }

}
