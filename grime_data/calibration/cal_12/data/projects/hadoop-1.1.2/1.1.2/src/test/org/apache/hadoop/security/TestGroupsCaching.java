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
import java.util.Iterator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.hadoop.security.ShellBasedUnixGroupsMapping;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.Groups;


public class TestGroupsCaching {
  public static final Log LOG = LogFactory.getLog(TestGroupsCaching.class);
  private static Configuration conf = new Configuration();
  private static String[] myGroups = {"grp1", "grp2"};

  static {
    conf.setClass(CommonConfigurationKeys.HADOOP_SECURITY_GROUP_MAPPING,
      FakeGroupMapping.class,
      ShellBasedUnixGroupsMapping.class);
  }

  public static class FakeGroupMapping extends ShellBasedUnixGroupsMapping {
    // any to n mapping
    private static Set<String> allGroups = new HashSet<String>();
    private static Set<String> blackList = new HashSet<String>();

    public List<String> getGroups(String user) throws IOException {
      LOG.info("Getting groups for " + user);
      if (blackList.contains(user)) {
        return new LinkedList<String>();
      }
      return new LinkedList<String>(allGroups);
    }

    public void cacheGroupsRefresh() throws IOException {
      LOG.info("Cache is being refreshed.");
      clearBlackList();
      return;
    }

    public static void clearBlackList() throws IOException {
      LOG.info("Clearing the blacklist");
      blackList.clear();
    }

    public void cacheGroupsAdd(List<String> groups) throws IOException {
      LOG.info("Adding " + groups + " to groups.");
      allGroups.addAll(groups);
    }

    public static void addToBlackList(String user) throws IOException {
      LOG.info("Adding " + user + " to the blacklist");
      blackList.add(user);
    }
  }

  @Test
  public void TestGroupsCachingDefault() throws Exception {
    Groups groups = new Groups(conf);
    groups.cacheGroupsAdd(Arrays.asList(myGroups));
    groups.refresh();
    FakeGroupMapping.clearBlackList();
    FakeGroupMapping.addToBlackList("user1");

    // regular entry
    assertTrue(groups.getGroups("me").size() == 2);

    // this must be cached. blacklisting should have no effect.
    FakeGroupMapping.addToBlackList("me");
    assertTrue(groups.getGroups("me").size() == 2);

    // ask for a negative entry
    try {
      LOG.error("We are not supposed to get here." + groups.getGroups("user1").toString());
      fail();
    } catch (IOException ioe) {
      if(!ioe.getMessage().startsWith("No groups found")) {
        LOG.error("Got unexpected exception: " + ioe.getMessage());
        fail();
      }
    }

    // this shouldn't be cached. remove from the black list and retry.
    FakeGroupMapping.clearBlackList();
    assertTrue(groups.getGroups("user1").size() == 2);
  }
}
