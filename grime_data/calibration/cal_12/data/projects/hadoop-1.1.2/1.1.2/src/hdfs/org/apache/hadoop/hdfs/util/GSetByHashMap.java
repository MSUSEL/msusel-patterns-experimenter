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

import java.util.HashMap;
import java.util.Iterator;

/**
 * A {@link GSet} implementation by {@link HashMap}.
 */
public class GSetByHashMap<K, E extends K> implements GSet<K, E> {
  private final HashMap<K, E> m;

  public GSetByHashMap(int initialCapacity, float loadFactor) {
    m = new HashMap<K, E>(initialCapacity, loadFactor);
  }

  @Override
  public int size() {
    return m.size();
  }

  @Override
  public boolean contains(K k) {
    return m.containsKey(k);
  }

  @Override
  public E get(K k) {
    return m.get(k);
  }

  @Override
  public E put(E element) {
    if (element == null) {
      throw new UnsupportedOperationException("Null element is not supported.");
    }
    return m.put(element, element);
  }

  @Override
  public E remove(K k) {
    return m.remove(k);
  }

  @Override
  public Iterator<E> iterator() {
    return m.values().iterator();
  }
}
