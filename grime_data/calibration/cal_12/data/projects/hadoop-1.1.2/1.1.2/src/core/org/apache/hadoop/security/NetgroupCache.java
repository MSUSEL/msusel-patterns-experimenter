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
package org.apache.hadoop.security;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Class that caches the netgroups and inverts group-to-user map
 * to user-to-group map
 */
public class NetgroupCache {

  private static final Log LOG = LogFactory.getLog(NetgroupCache.class);

  private static boolean netgroupToUsersMapUpdated = true;
  private static Map<String, Set<String>> netgroupToUsersMap =
    new ConcurrentHashMap<String, Set<String>>();

  private static Map<String, Set<String>> userToNetgroupsMap =
    new ConcurrentHashMap<String, Set<String>>();


  public void getNetgroups(final String user,
      List<String> groups) {
    if(netgroupToUsersMapUpdated) {
      netgroupToUsersMapUpdated = false; // at the beginning to avoid race
      //update userToNetgroupsMap
      for(String netgroup : netgroupToUsersMap.keySet()) {
        for(String netuser : netgroupToUsersMap.get(netgroup)) {
          // add to userToNetgroupsMap
          if(!userToNetgroupsMap.containsKey(netuser)) {
            userToNetgroupsMap.put(netuser, new HashSet<String>());
          }
          userToNetgroupsMap.get(netuser).add(netgroup);
        }
      }
    }
    if(userToNetgroupsMap.containsKey(user)) {
      for(String netgroup : userToNetgroupsMap.get(user)) {
        groups.add(netgroup);
      }
    }
  }

  public List<String> getNetgroupNames() {
    return new LinkedList<String>(netgroupToUsersMap.keySet());
  }

  public boolean isCached(String group) {
    return netgroupToUsersMap.containsKey(group);
  }

  public void clear() {
    netgroupToUsersMap.clear();
  }

  public void add(String group, List<String> users) {
    if(!isCached(group)) {
      netgroupToUsersMap.put(group, new HashSet<String>());
      for(String user: users) {
        netgroupToUsersMap.get(group).add(user);
      }
    }
    netgroupToUsersMapUpdated = true; // at the end to avoid race
  }
}
