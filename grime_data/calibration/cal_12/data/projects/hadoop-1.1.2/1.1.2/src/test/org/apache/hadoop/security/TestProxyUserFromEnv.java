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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class TestProxyUserFromEnv {
  /** Test HADOOP_PROXY_USER for impersonation */
  @Test
  public void testProxyUserFromEnvironment() throws IOException {
    String proxyUser = "foo.bar";
    System.setProperty(UserGroupInformation.HADOOP_PROXY_USER, proxyUser);
    UserGroupInformation ugi = UserGroupInformation.getLoginUser();
    assertEquals(proxyUser, ugi.getUserName());

    UserGroupInformation realUgi = ugi.getRealUser();
    assertNotNull(realUgi);
    // get the expected real user name
    Process pp = Runtime.getRuntime().exec("whoami");
    BufferedReader br = new BufferedReader
                          (new InputStreamReader(pp.getInputStream()));
    String realUser = br.readLine().trim();
    assertEquals(realUser, realUgi.getUserName());
  }
}
