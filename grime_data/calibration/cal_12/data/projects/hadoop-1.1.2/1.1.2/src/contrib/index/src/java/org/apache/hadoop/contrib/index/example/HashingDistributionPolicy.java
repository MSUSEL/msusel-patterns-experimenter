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
package org.apache.hadoop.contrib.index.example;

import org.apache.hadoop.contrib.index.mapred.DocumentID;
import org.apache.hadoop.contrib.index.mapred.IDistributionPolicy;
import org.apache.hadoop.contrib.index.mapred.Shard;

/**
 * Choose a shard for each insert or delete based on document id hashing. Do
 * NOT use this distribution policy when the number of shards changes.
 */
public class HashingDistributionPolicy implements IDistributionPolicy {

  private int numShards;

  /* (non-Javadoc)
   * @see org.apache.hadoop.contrib.index.mapred.IDistributionPolicy#init(org.apache.hadoop.contrib.index.mapred.Shard[])
   */
  public void init(Shard[] shards) {
    numShards = shards.length;
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.contrib.index.mapred.IDistributionPolicy#chooseShardForInsert(org.apache.hadoop.contrib.index.mapred.DocumentID)
   */
  public int chooseShardForInsert(DocumentID key) {
    int hashCode = key.hashCode();
    return hashCode >= 0 ? hashCode % numShards : (-hashCode) % numShards;
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.contrib.index.mapred.IDistributionPolicy#chooseShardForDelete(org.apache.hadoop.contrib.index.mapred.DocumentID)
   */
  public int chooseShardForDelete(DocumentID key) {
    int hashCode = key.hashCode();
    return hashCode >= 0 ? hashCode % numShards : (-hashCode) % numShards;
  }

}
