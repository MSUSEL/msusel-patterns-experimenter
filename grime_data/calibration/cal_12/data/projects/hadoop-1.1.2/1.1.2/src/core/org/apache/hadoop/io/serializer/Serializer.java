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
import java.io.OutputStream;

/**
 * <p>
 * Provides a facility for serializing objects of type <T> to an
 * {@link OutputStream}.
 * </p>
 * 
 * <p>
 * Serializers are stateful, but must not buffer the output since
 * other producers may write to the output between calls to
 * {@link #serialize(Object)}.
 * </p>
 * @param <T>
 */
public interface Serializer<T> {
  /**
   * <p>Prepare the serializer for writing.</p>
   */
  void open(OutputStream out) throws IOException;
  
  /**
   * <p>Serialize <code>t</code> to the underlying output stream.</p>
   */
  void serialize(T t) throws IOException;
  
  /**
   * <p>Close the underlying output stream and clear up any resources.</p>
   */  
  void close() throws IOException;
}
