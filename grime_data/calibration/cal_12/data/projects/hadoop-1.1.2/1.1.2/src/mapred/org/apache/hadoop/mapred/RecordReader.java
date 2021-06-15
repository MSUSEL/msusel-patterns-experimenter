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
import java.io.DataInput;

/**
 * <code>RecordReader</code> reads &lt;key, value&gt; pairs from an 
 * {@link InputSplit}.
 *   
 * <p><code>RecordReader</code>, typically, converts the byte-oriented view of 
 * the input, provided by the <code>InputSplit</code>, and presents a 
 * record-oriented view for the {@link Mapper} & {@link Reducer} tasks for 
 * processing. It thus assumes the responsibility of processing record 
 * boundaries and presenting the tasks with keys and values.</p>
 * 
 * @see InputSplit
 * @see InputFormat
 */
public interface RecordReader<K, V> {
  /** 
   * Reads the next key/value pair from the input for processing.
   *
   * @param key the key to read data into
   * @param value the value to read data into
   * @return true iff a key/value was read, false if at EOF
   */      
  boolean next(K key, V value) throws IOException;
  
  /**
   * Create an object of the appropriate type to be used as a key.
   * 
   * @return a new key object.
   */
  K createKey();
  
  /**
   * Create an object of the appropriate type to be used as a value.
   * 
   * @return a new value object.
   */
  V createValue();

  /** 
   * Returns the current position in the input.
   * 
   * @return the current position in the input.
   * @throws IOException
   */
  long getPos() throws IOException;

  /** 
   * Close this {@link InputSplit} to future operations.
   * 
   * @throws IOException
   */ 
  public void close() throws IOException;

  /**
   * How much of the input has the {@link RecordReader} consumed i.e.
   * has been processed by?
   * 
   * @return progress from <code>0.0</code> to <code>1.0</code>.
   * @throws IOException
   */
  float getProgress() throws IOException;
}
