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
package org.apache.hadoop.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A cached implementation of DNSToSwitchMapping that takes an
 * raw DNSToSwitchMapping and stores the resolved network location in 
 * a cache. The following calls to a resolved network location
 * will get its location from the cache. 
 *
 */
public class CachedDNSToSwitchMapping implements DNSToSwitchMapping {
  private Map<String, String> cache = new ConcurrentHashMap<String, String>();
  protected DNSToSwitchMapping rawMapping;
  
  public CachedDNSToSwitchMapping(DNSToSwitchMapping rawMapping) {
    this.rawMapping = rawMapping;
  }
  
  
  /**
   * Returns the hosts from 'names' that have not been cached previously
   */
  private List<String> getUncachedHosts(List<String> names) {
    // find out all names without cached resolved location
    List<String> unCachedHosts = new ArrayList<String>(names.size());
    for (String name : names) {
      if (cache.get(name) == null) {
        unCachedHosts.add(name);
      } 
    }
    return unCachedHosts;
  }
  
  /**
   * Caches the resolved hosts
   */
  private void cacheResolvedHosts(List<String> uncachedHosts, 
      List<String> resolvedHosts) {
    // Cache the result
    if (resolvedHosts != null) {
      for (int i=0; i<uncachedHosts.size(); i++) {
        cache.put(uncachedHosts.get(i), resolvedHosts.get(i));
      }
    }
  }
  
  /**
   * Returns the cached resolution of the list of hostnames/addresses.
   * Returns null if any of the names are not currently in the cache
   */
  private List<String> getCachedHosts(List<String> names) {
    List<String> result = new ArrayList<String>(names.size());
    // Construct the result
    for (String name : names) {
      String networkLocation = cache.get(name);
      if (networkLocation != null) {
        result.add(networkLocation);
      } else {
        return null;
      }
    }
    return result;
  }

  public List<String> resolve(List<String> names) {
    // normalize all input names to be in the form of IP addresses
    names = NetUtils.normalizeHostNames(names);

    List <String> result = new ArrayList<String>(names.size());
    if (names.isEmpty()) {
      return result;
    }

    List<String> uncachedHosts = this.getUncachedHosts(names);

    // Resolve the uncached hosts
    List<String> resolvedHosts = rawMapping.resolve(uncachedHosts);
    this.cacheResolvedHosts(uncachedHosts, resolvedHosts);
    return this.getCachedHosts(names);

  }
}
