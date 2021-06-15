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

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.RecordReader;

/**
 * Additional operations required of a RecordReader to participate in a join.
 */
public interface ComposableRecordReader<K extends WritableComparable,
                                 V extends Writable>
    extends RecordReader<K,V>, Comparable<ComposableRecordReader<K,?>> {

  /**
   * Return the position in the collector this class occupies.
   */
  int id();

  /**
   * Return the key this RecordReader would supply on a call to next(K,V)
   */
  K key();

  /**
   * Clone the key at the head of this RecordReader into the object provided.
   */
  void key(K key) throws IOException;

  /**
   * Returns true if the stream is not empty, but provides no guarantee that
   * a call to next(K,V) will succeed.
   */
  boolean hasNext();

  /**
   * Skip key-value pairs with keys less than or equal to the key provided.
   */
  void skip(K key) throws IOException;

  /**
   * While key-value pairs from this RecordReader match the given key, register
   * them with the JoinCollector provided.
   */
  void accept(CompositeRecordReader.JoinCollector jc, K key) throws IOException;
}
