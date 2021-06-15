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

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;

/** Provide an cyclic {@link Iterator} for a {@link NavigableMap}.
 * The {@link Iterator} navigates the entries of the map
 * according to the map's ordering.
 * If the {@link Iterator} hits the last entry of the map,
 * it will then continue from the first entry.
 */
public class CyclicIteration<K, V> implements Iterable<Map.Entry<K, V>> {
  private final NavigableMap<K, V> navigablemap;
  private final NavigableMap<K, V> tailmap;

  /** Construct an {@link Iterable} object,
   * so that an {@link Iterator} can be created  
   * for iterating the given {@link NavigableMap}.
   * The iteration begins from the starting key exclusively.
   */
  public CyclicIteration(NavigableMap<K, V> navigablemap, K startingkey) {
    if (navigablemap == null || navigablemap.isEmpty()) {
      this.navigablemap = null;
      this.tailmap = null;
    }
    else {
      this.navigablemap = navigablemap;
      this.tailmap = navigablemap.tailMap(startingkey, false); 
    }
  }

  /** {@inheritDoc} */
  public Iterator<Map.Entry<K, V>> iterator() {
    return new CyclicIterator();
  }

  /** An {@link Iterator} for {@link CyclicIteration}. */
  private class CyclicIterator implements Iterator<Map.Entry<K, V>> {
    private boolean hasnext;
    private Iterator<Map.Entry<K, V>> i;
    /** The first entry to begin. */
    private final Map.Entry<K, V> first;
    /** The next entry. */
    private Map.Entry<K, V> next;
    
    private CyclicIterator() {
      hasnext = navigablemap != null;
      if (hasnext) {
        i = tailmap.entrySet().iterator();
        first = nextEntry();
        next = first;
      }
      else {
        i = null;
        first = null;
        next = null;
      }
    }

    private Map.Entry<K, V> nextEntry() {
      if (!i.hasNext()) {
        i = navigablemap.entrySet().iterator();
      }
      return i.next();
    }

    /** {@inheritDoc} */
    public boolean hasNext() {
      return hasnext;
    }

    /** {@inheritDoc} */
    public Map.Entry<K, V> next() {
      if (!hasnext) {
        throw new NoSuchElementException();
      }

      final Map.Entry<K, V> curr = next;
      next = nextEntry();
      hasnext = !next.equals(first);
      return curr;
    }

    /** Not supported */
    public void remove() {
      throw new UnsupportedOperationException("Not supported");
    }
  }
}