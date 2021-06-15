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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.util.NativeCodeLoader;

/**
 * A JNI-based implementation of {@link GroupMappingServiceProvider} 
 * that invokes libC calls to get the group
 * memberships of a given user.
 */
public class JniBasedUnixGroupsMapping implements GroupMappingServiceProvider {
  
  private static final Log LOG = LogFactory.getLog(
    JniBasedUnixGroupsMapping.class);
  
  native String[] getGroupForUser(String user);
  
  static {
    if (!NativeCodeLoader.isNativeCodeLoaded()) {
      LOG.info("Bailing out since native library couldn't be loaded");
      throw new RuntimeException();
    }
    LOG.info("Using JniBasedUnixGroupsMapping for Group resolution");
  }

  @Override
  public List<String> getGroups(String user) throws IOException {
    String[] groups = null;
    try {
      groups = getGroupForUser(user);
    } catch (Exception e) {
      LOG.warn("Got exception while trying to obtain the groups for user " + user);
    }
    if (groups != null && groups.length != 0) {
      return Arrays.asList(groups);
    }
    return Arrays.asList(new String[0]);
  }
  @Override
  public void cacheGroupsRefresh() throws IOException {
    // does nothing in this provider of user to groups mapping
  }

  @Override
  public void cacheGroupsAdd(List<String> groups) throws IOException {
    // does nothing in this provider of user to groups mapping
  }
}
