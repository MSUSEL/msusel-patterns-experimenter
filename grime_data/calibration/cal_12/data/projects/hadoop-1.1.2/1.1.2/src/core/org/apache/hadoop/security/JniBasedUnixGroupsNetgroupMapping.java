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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.util.NativeCodeLoader;

import org.apache.hadoop.security.NetgroupCache;

/**
 * A JNI-based implementation of {@link GroupMappingServiceProvider} 
 * that invokes libC calls to get the group
 * memberships of a given user.
 */
public class JniBasedUnixGroupsNetgroupMapping
  extends JniBasedUnixGroupsMapping {
  
  private static final Log LOG = LogFactory.getLog(
    JniBasedUnixGroupsNetgroupMapping.class);

  private static final NetgroupCache netgroupCache = new NetgroupCache();

  native String[] getUsersForNetgroupJNI(String group);
  
  /**
   * Gets unix groups and netgroups for the user.
   *
   * It gets all unix groups as returned by id -Gn but it
   * only returns netgroups that are used in ACLs (there is
   * no way to get all netgroups for a given user, see
   * documentation for getent netgroup)
   */
  @Override
  public List<String> getGroups(String user) throws IOException {
    // parent gets unix groups
    List<String> groups = new LinkedList<String>(super.getGroups(user));
    netgroupCache.getNetgroups(user, groups);
    return groups;
  }

  @Override
  public void cacheGroupsRefresh() throws IOException {
    List<String> groups = netgroupCache.getNetgroupNames();
    netgroupCache.clear();
    cacheGroupsAdd(groups);
  }

  @Override
  public void cacheGroupsAdd(List<String> groups) throws IOException {
    for(String group: groups) {
      if(group.length() == 0) {
        // better safe than sorry (should never happen)
      } else if(group.charAt(0) == '@') {
        if(!netgroupCache.isCached(group)) {
          netgroupCache.add(group, getUsersForNetgroup(group));
        }
      } else {
        // unix group, not caching
      }
    }
  }

  /**
   * Calls JNI function to get users for a netgroup, since C functions
   * are not reentrant we need to make this synchronized (see
   * documentation for setnetgrent, getnetgrent and endnetgrent)
   */
  protected synchronized List<String> getUsersForNetgroup(String netgroup) {
    String[] users = null;
    try {
      // JNI code does not expect '@' at the begining of the group name
      users = getUsersForNetgroupJNI(netgroup.substring(1));
    } catch (Exception e) {
      LOG.warn("Got exception while trying to obtain the users for netgroup ["
        + netgroup + "] [" + e + "]");
    }
    if (users != null && users.length != 0) {
      return Arrays.asList(users);
    }
    return new LinkedList<String>();
  }
}
