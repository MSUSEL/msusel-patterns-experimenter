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
package org.apache.hadoop.hdfs.util;

/**
 * A {@link GSet} is set,
 * which supports the {@link #get(Object)} operation.
 * The {@link #get(Object)} operation uses a key to lookup an element.
 * 
 * Null element is not supported.
 * 
 * @param <K> The type of the keys.
 * @param <E> The type of the elements, which must be a subclass of the keys.
 */
public interface GSet<K, E extends K> extends Iterable<E> {
  /**
   * @return The size of this set.
   */
  int size();

  /**
   * Does this set contain an element corresponding to the given key?
   * @param key The given key.
   * @return true if the given key equals to a stored element.
   *         Otherwise, return false.
   * @throws NullPointerException if key == null.
   */
  boolean contains(K key);

  /**
   * Return the stored element which is equal to the given key.
   * This operation is similar to {@link java.util.Map#get(Object)}.
   * @param key The given key.
   * @return The stored element if it exists.
   *         Otherwise, return null.
   * @throws NullPointerException if key == null.
   */
  E get(K key);

  /**
   * Add/replace an element.
   * If the element does not exist, add it to the set.
   * Otherwise, replace the existing element.
   *
   * Note that this operation
   * is similar to {@link java.util.Map#put(Object, Object)}
   * but is different from {@link java.util.Set#add(Object)}
   * which does not replace the existing element if there is any.
   *
   * @param element The element being put.
   * @return the previous stored element if there is any.
   *         Otherwise, return null.
   * @throws NullPointerException if element == null.
   */
  E put(E element);

  /**
   * Remove the element corresponding to the given key. 
   * This operation is similar to {@link java.util.Map#remove(Object)}.
   * @param key The key of the element being removed.
   * @return If such element exists, return it.
   *         Otherwise, return null. 
    * @throws NullPointerException if key == null.
  */
  E remove(K key);
}