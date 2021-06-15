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
package org.apache.hadoop.io.serializer;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * Provides a facility for deserializing objects of type <T> from an
 * {@link InputStream}.
 * </p>
 * 
 * <p>
 * Deserializers are stateful, but must not buffer the input since
 * other producers may read from the input between calls to
 * {@link #deserialize(Object)}.
 * </p>
 * @param <T>
 */
public interface Deserializer<T> {
  /**
   * <p>Prepare the deserializer for reading.</p>
   */
  void open(InputStream in) throws IOException;
  
  /**
   * <p>
   * Deserialize the next object from the underlying input stream.
   * If the object <code>t</code> is non-null then this deserializer
   * <i>may</i> set its internal state to the next object read from the input
   * stream. Otherwise, if the object <code>t</code> is null a new
   * deserialized object will be created.
   * </p>
   * @return the deserialized object
   */
  T deserialize(T t) throws IOException;
  
  /**
   * <p>Close the underlying input stream and clear up any resources.</p>
   */
  void close() throws IOException;
}
