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
package org.apache.hadoop.util;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Contains utility methods for dealing with Java Generics. 
 */
public class GenericsUtil {

  /**
   * Returns the Class object (of type <code>Class&lt;T&gt;</code>) of the  
   * argument of type <code>T</code>. 
   * @param <T> The type of the argument
   * @param t the object to get it class
   * @return <code>Class&lt;T&gt;</code>
   */
  public static <T> Class<T> getClass(T t) {
    @SuppressWarnings("unchecked")
    Class<T> clazz = (Class<T>)t.getClass();
    return clazz;
  }

  /**
   * Converts the given <code>List&lt;T&gt;</code> to a an array of 
   * <code>T[]</code>.
   * @param c the Class object of the items in the list
   * @param list the list to convert
   */
  public static <T> T[] toArray(Class<T> c, List<T> list)
  {
    @SuppressWarnings("unchecked")
    T[] ta= (T[])Array.newInstance(c, list.size());

    for (int i= 0; i<list.size(); i++)
      ta[i]= list.get(i);
    return ta;
  }


  /**
   * Converts the given <code>List&lt;T&gt;</code> to a an array of 
   * <code>T[]</code>. 
   * @param list the list to convert
   * @throws ArrayIndexOutOfBoundsException if the list is empty. 
   * Use {@link #toArray(Class, List)} if the list may be empty.
   */
  public static <T> T[] toArray(List<T> list) {
    return toArray(getClass(list.get(0)), list);
  }

}
