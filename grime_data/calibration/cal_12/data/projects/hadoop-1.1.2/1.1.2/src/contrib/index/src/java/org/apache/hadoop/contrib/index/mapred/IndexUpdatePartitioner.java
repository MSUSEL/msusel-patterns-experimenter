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
package org.apache.hadoop.contrib.index.mapred;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Partitioner;

/**
 * This partitioner class puts the values of the same key - in this case the
 * same shard - in the same partition.
 */
public class IndexUpdatePartitioner implements
    Partitioner<Shard, IntermediateForm> {

  private Shard[] shards;
  private Map<Shard, Integer> map;

  /* (non-Javadoc)
   * @see org.apache.hadoop.mapred.Partitioner#getPartition(java.lang.Object, java.lang.Object, int)
   */
  public int getPartition(Shard key, IntermediateForm value, int numPartitions) {
    int partition = map.get(key).intValue();
    if (partition < numPartitions) {
      return partition;
    } else {
      return numPartitions - 1;
    }
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.mapred.JobConfigurable#configure(org.apache.hadoop.mapred.JobConf)
   */
  public void configure(JobConf job) {
    shards = Shard.getIndexShards(new IndexUpdateConfiguration(job));
    map = new HashMap<Shard, Integer>();
    for (int i = 0; i < shards.length; i++) {
      map.put(shards[i], i);
    }
  }

}
