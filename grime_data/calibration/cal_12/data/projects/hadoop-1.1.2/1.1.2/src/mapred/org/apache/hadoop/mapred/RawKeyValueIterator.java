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
package org.apache.hadoop.mapred;

import java.io.IOException;

import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.util.Progress;

/**
 * <code>RawKeyValueIterator</code> is an iterator used to iterate over
 * the raw keys and values during sort/merge of intermediate data. 
 */
public interface RawKeyValueIterator {
  /** 
   * Gets the current raw key.
   * 
   * @return Gets the current raw key as a DataInputBuffer
   * @throws IOException
   */
  DataInputBuffer getKey() throws IOException;
  
  /** 
   * Gets the current raw value.
   * 
   * @return Gets the current raw value as a DataInputBuffer 
   * @throws IOException
   */
  DataInputBuffer getValue() throws IOException;
  
  /** 
   * Sets up the current key and value (for getKey and getValue).
   * 
   * @return <code>true</code> if there exists a key/value, 
   *         <code>false</code> otherwise. 
   * @throws IOException
   */
  boolean next() throws IOException;
  
  /** 
   * Closes the iterator so that the underlying streams can be closed.
   * 
   * @throws IOException
   */
  void close() throws IOException;
  
  /** Gets the Progress object; this has a float (0.0 - 1.0) 
   * indicating the bytes processed by the iterator so far
   */
  Progress getProgress();
}
