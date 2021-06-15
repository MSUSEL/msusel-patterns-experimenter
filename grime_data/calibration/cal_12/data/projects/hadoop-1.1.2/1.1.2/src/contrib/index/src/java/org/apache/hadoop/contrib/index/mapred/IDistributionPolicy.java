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

/**
 * A distribution policy decides, given a document with a document id, which
 * one shard the request should be sent to if the request is an insert, and
 * which shard(s) the request should be sent to if the request is a delete.
 */
public interface IDistributionPolicy {

  /**
   * Initialization. It must be called before any chooseShard() is called.
   * @param shards
   */
  void init(Shard[] shards);

  /**
   * Choose a shard to send an insert request.
   * @param key
   * @return the index of the chosen shard
   */
  int chooseShardForInsert(DocumentID key);

  /**
   * Choose a shard or all shards to send a delete request. E.g. a round-robin
   * distribution policy would send a delete request to all the shards.
   * -1 represents all the shards.
   * @param key
   * @return the index of the chosen shard, -1 if all the shards are chosen
   */
  int chooseShardForDelete(DocumentID key);

}
