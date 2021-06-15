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
package org.apache.hadoop.mapred.join;

import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapred.JobConf;

/**
 * Prefer the &quot;rightmost&quot; data source for this key.
 * For example, <tt>override(S1,S2,S3)</tt> will prefer values
 * from S3 over S2, and values from S2 over S1 for all keys
 * emitted from all sources.
 */
public class OverrideRecordReader<K extends WritableComparable,
                                  V extends Writable>
    extends MultiFilterRecordReader<K,V> {

  OverrideRecordReader(int id, JobConf conf, int capacity,
      Class<? extends WritableComparator> cmpcl) throws IOException {
    super(id, conf, capacity, cmpcl);
  }

  /**
   * Emit the value with the highest position in the tuple.
   */
  @SuppressWarnings("unchecked") // No static typeinfo on Tuples
  protected V emit(TupleWritable dst) {
    return (V) dst.iterator().next();
  }

  /**
   * Instead of filling the JoinCollector with iterators from all
   * data sources, fill only the rightmost for this key.
   * This not only saves space by discarding the other sources, but
   * it also emits the number of key-value pairs in the preferred
   * RecordReader instead of repeating that stream n times, where
   * n is the cardinality of the cross product of the discarded
   * streams for the given key.
   */
  protected void fillJoinCollector(K iterkey) throws IOException {
    final PriorityQueue<ComposableRecordReader<K,?>> q = getRecordReaderQueue();
    if (!q.isEmpty()) {
      int highpos = -1;
      ArrayList<ComposableRecordReader<K,?>> list =
        new ArrayList<ComposableRecordReader<K,?>>(kids.length);
      q.peek().key(iterkey);
      final WritableComparator cmp = getComparator();
      while (0 == cmp.compare(q.peek().key(), iterkey)) {
        ComposableRecordReader<K,?> t = q.poll();
        if (-1 == highpos || list.get(highpos).id() < t.id()) {
          highpos = list.size();
        }
        list.add(t);
        if (q.isEmpty())
          break;
      }
      ComposableRecordReader<K,?> t = list.remove(highpos);
      t.accept(jc, iterkey);
      for (ComposableRecordReader<K,?> rr : list) {
        rr.skip(iterkey);
      }
      list.add(t);
      for (ComposableRecordReader<K,?> rr : list) {
        if (rr.hasNext()) {
          q.add(rr);
        }
      }
    }
  }

}
