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
package org.apache.hadoop.hdfs.server.namenode;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Caches frequently used names to facilitate reuse.
 * (example: byte[] representation of the file name in {@link INode}).
 * 
 * This class is used by initially adding all the file names. Cache
 * tracks the number of times a name is used in a transient map. It promotes 
 * a name used more than {@code useThreshold} to the cache.
 * 
 * One all the names are added, {@link #initialized()} should be called to
 * finish initialization. The transient map where use count is tracked is
 * discarded and cache is ready for use.
 * 
 * <p>
 * This class must be synchronized externally.
 * 
 * @param <K> name to be added to the cache
 */
class NameCache<K> {
  /**
   * Class for tracking use count of a name
   */
  private class UseCount {
    int count;
    final K value;  // Internal value for the name

    UseCount(final K value) {
      count = 1;
      this.value = value;
    }
    
    void increment() {
      count++;
    }
    
    int get() {
      return count;
    }
  }

  static final Log LOG = LogFactory.getLog(NameCache.class.getName());

  /** indicates initialization is in progress */
  private boolean initialized = false;

  /** names used more than {@code useThreshold} is added to the cache */
  private final int useThreshold;

  /** of times a cache look up was successful */
  private int lookups = 0;

  /** Cached names */
  final HashMap<K, K> cache = new HashMap<K, K>();

  /** Names and with number of occurrences tracked during initialization */
  Map<K, UseCount> transientMap = new HashMap<K, UseCount>();

  /**
   * Constructor
   * @param useThreshold names occurring more than this is promoted to the
   *          cache
   */
  NameCache(int useThreshold) {
    this.useThreshold = useThreshold;
  }
  
  /**
   * Add a given name to the cache or track use count.
   * exist. If the name already exists, then the internal value is returned.
   * 
   * @param name name to be looked up
   * @return internal value for the name if found; otherwise null
   */
  K put(final K name) {
    K internal = cache.get(name);
    if (internal != null) {
      lookups++;
      return internal;
    }

    // Track the usage count only during initialization
    if (!initialized) {
      UseCount useCount = transientMap.get(name);
      if (useCount != null) {
        useCount.increment();
        if (useCount.get() >= useThreshold) {
          promote(name);
        }
        return useCount.value;
      }
      useCount = new UseCount(name);
      transientMap.put(name, useCount);
    }
    return null;
  }
  
  /**
   * Lookup count when a lookup for a name returned cached object
   * @return number of successful lookups
   */
  int getLookupCount() {
    return lookups;
  }

  /**
   * Size of the cache
   * @return Number of names stored in the cache
   */
  int size() {
    return cache.size();
  }

  /**
   * Mark the name cache as initialized. The use count is no longer tracked
   * and the transient map used for initializing the cache is discarded to
   * save heap space.
   */
  void initialized() {
    LOG.info("initialized with " + size() + " entries " + lookups + " lookups");
    this.initialized = true;
    transientMap.clear();
    transientMap = null;
  }
  
  /** Promote a frequently used name to the cache */
  private void promote(final K name) {
    transientMap.remove(name);
    cache.put(name, name);
    lookups += useThreshold;
  }
}
