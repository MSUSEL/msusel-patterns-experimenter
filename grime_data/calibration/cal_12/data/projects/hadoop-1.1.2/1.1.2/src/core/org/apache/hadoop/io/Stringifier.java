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
package org.apache.hadoop.io;

import java.io.IOException;

/**
 * Stringifier interface offers two methods to convert an object 
 * to a string representation and restore the object given its 
 * string representation.
 * @param <T> the class of the objects to stringify
 */
public interface Stringifier<T> extends java.io.Closeable {

  /**
   * Converts the object to a string representation
   * @param obj the object to convert
   * @return the string representation of the object
   * @throws IOException if the object cannot be converted
   */
  public String toString(T obj)  throws IOException;
  
  /**
   * Restores the object from its string representation.
   * @param str the string representation of the object
   * @return restored object
   * @throws IOException if the object cannot be restored
   */
  public T fromString(String str) throws IOException;
  
  
  /** 
   * Closes this object. 
   * @throws IOException if an I/O error occurs 
   * */
  public void close() throws IOException;
  
}
