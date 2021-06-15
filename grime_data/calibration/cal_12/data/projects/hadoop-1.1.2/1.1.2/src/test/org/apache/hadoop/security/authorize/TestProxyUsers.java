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
package org.apache.hadoop.security.authorize;

import java.util.Arrays;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.security.UserGroupInformation;

import junit.framework.TestCase;

public class TestProxyUsers extends TestCase {
  private static final String REAL_USER_NAME = "proxier";
  private static final String PROXY_USER_NAME = "proxied_user";
  private static final String[] GROUP_NAMES =
    new String[] { "foo_group" };
  private static final String[] OTHER_GROUP_NAMES =
    new String[] { "bar_group" };
  private static final String PROXY_IP = "1.2.3.4";
  
  public void testProxyUsers() throws Exception {
    Configuration conf = new Configuration();
    conf.set(
      ProxyUsers.getProxySuperuserGroupConfKey(REAL_USER_NAME),
      StringUtils.join(",", Arrays.asList(GROUP_NAMES)));
    conf.set(
      ProxyUsers.getProxySuperuserIpConfKey(REAL_USER_NAME),
      PROXY_IP);
    ProxyUsers.refreshSuperUserGroupsConfiguration(conf);


    // First try proxying a group that's allowed
    UserGroupInformation realUserUgi = UserGroupInformation
        .createRemoteUser(REAL_USER_NAME);
    UserGroupInformation proxyUserUgi = UserGroupInformation.createProxyUserForTesting(
        PROXY_USER_NAME, realUserUgi, GROUP_NAMES);

    // From good IP
    assertAuthorized(proxyUserUgi, "1.2.3.4");
    // From bad IP
    assertNotAuthorized(proxyUserUgi, "1.2.3.5");

    // Now try proxying a group that's not allowed
    realUserUgi = UserGroupInformation.createRemoteUser(REAL_USER_NAME);
    proxyUserUgi = UserGroupInformation.createProxyUserForTesting(
        PROXY_USER_NAME, realUserUgi, OTHER_GROUP_NAMES);
    
    // From good IP
    assertNotAuthorized(proxyUserUgi, "1.2.3.4");
    // From bad IP
    assertNotAuthorized(proxyUserUgi, "1.2.3.5");
  }

  public void testWildcardGroup() {
    Configuration conf = new Configuration();
    conf.set(
      ProxyUsers.getProxySuperuserGroupConfKey(REAL_USER_NAME),
      "*");
    conf.set(
      ProxyUsers.getProxySuperuserIpConfKey(REAL_USER_NAME),
      PROXY_IP);
    ProxyUsers.refreshSuperUserGroupsConfiguration(conf);

    // First try proxying a group that's allowed
    UserGroupInformation realUserUgi = UserGroupInformation
        .createRemoteUser(REAL_USER_NAME);
    UserGroupInformation proxyUserUgi = UserGroupInformation.createProxyUserForTesting(
        PROXY_USER_NAME, realUserUgi, GROUP_NAMES);

    // From good IP
    assertAuthorized(proxyUserUgi, "1.2.3.4");
    // From bad IP
    assertNotAuthorized(proxyUserUgi, "1.2.3.5");

    // Now try proxying a different group (just to make sure we aren't getting spill over
    // from the other test case!)
    realUserUgi = UserGroupInformation.createRemoteUser(REAL_USER_NAME);
    proxyUserUgi = UserGroupInformation.createProxyUserForTesting(
        PROXY_USER_NAME, realUserUgi, OTHER_GROUP_NAMES);
    
    // From good IP
    assertAuthorized(proxyUserUgi, "1.2.3.4");
    // From bad IP
    assertNotAuthorized(proxyUserUgi, "1.2.3.5");
  }

  public void testWildcardIP() {
    Configuration conf = new Configuration();
    conf.set(
      ProxyUsers.getProxySuperuserGroupConfKey(REAL_USER_NAME),
      StringUtils.join(",", Arrays.asList(GROUP_NAMES)));
    conf.set(
      ProxyUsers.getProxySuperuserIpConfKey(REAL_USER_NAME),
      "*");
    ProxyUsers.refreshSuperUserGroupsConfiguration(conf);

    // First try proxying a group that's allowed
    UserGroupInformation realUserUgi = UserGroupInformation
        .createRemoteUser(REAL_USER_NAME);
    UserGroupInformation proxyUserUgi = UserGroupInformation.createProxyUserForTesting(
        PROXY_USER_NAME, realUserUgi, GROUP_NAMES);

    // From either IP should be fine
    assertAuthorized(proxyUserUgi, "1.2.3.4");
    assertAuthorized(proxyUserUgi, "1.2.3.5");

    // Now set up an unallowed group
    realUserUgi = UserGroupInformation.createRemoteUser(REAL_USER_NAME);
    proxyUserUgi = UserGroupInformation.createProxyUserForTesting(
        PROXY_USER_NAME, realUserUgi, OTHER_GROUP_NAMES);
    
    // Neither IP should be OK
    assertNotAuthorized(proxyUserUgi, "1.2.3.4");
    assertNotAuthorized(proxyUserUgi, "1.2.3.5");
  }

  private void assertNotAuthorized(UserGroupInformation proxyUgi, String host) {
    try {
      ProxyUsers.authorize(proxyUgi, host, null);
      fail("Allowed authorization of " + proxyUgi + " from " + host);
    } catch (AuthorizationException e) {
      // Expected
    }
  }
  
  private void assertAuthorized(UserGroupInformation proxyUgi, String host) {
    try {
      ProxyUsers.authorize(proxyUgi, host, null);
    } catch (AuthorizationException e) {
      fail("Did not allowed authorization of " + proxyUgi + " from " + host);
    }
  }
}
