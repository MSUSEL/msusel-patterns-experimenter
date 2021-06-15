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
package org.apache.hadoop.mapreduce.security.token.delegation;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;

import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobTracker;
import org.apache.hadoop.mapred.MiniMRCluster;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.UserGroupInformation.AuthenticationMethod;
import org.apache.hadoop.security.token.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestDelegationTokenAuthMethod {
  private MiniMRCluster cluster;
  private JobConf config;

  @Before
  public void setup() throws Exception {
    config = new JobConf();
    cluster = new MiniMRCluster(0, 0, 1, "file:///", 1, null, null, null,
        config);
  }

  private Token<DelegationTokenIdentifier> generateDelegationToken(
      String owner, String renewer) {
    DelegationTokenSecretManager dtSecretManager = cluster
        .getJobTrackerRunner().getJobTracker()
        .getDelegationTokenSecretManager();
    DelegationTokenIdentifier dtId = new DelegationTokenIdentifier(new Text(
        owner), new Text(renewer), null);
    return new Token<DelegationTokenIdentifier>(dtId, dtSecretManager);
  }

  @Test
  public void testDelegationToken() throws Exception {
    final JobTracker jt = cluster.getJobTrackerRunner().getJobTracker();
    final UserGroupInformation ugi = UserGroupInformation.getCurrentUser();
    ugi.setAuthenticationMethod(AuthenticationMethod.KERBEROS);
    config.set(CommonConfigurationKeys.HADOOP_SECURITY_AUTHENTICATION,
        "kerberos");
    // Set configuration again so that job tracker finds security enabled
    UserGroupInformation.setConfiguration(config);
    ugi.doAs(new PrivilegedExceptionAction<Object>() {
      public Object run() throws Exception {
        try {
          Token<DelegationTokenIdentifier> token = jt
              .getDelegationToken(new Text(ugi.getShortUserName()));
          jt.renewDelegationToken(token);
          jt.cancelDelegationToken(token);
        } catch (IOException e) {
          e.printStackTrace();
          throw e;
        }
        return null;
      }
    });
  }
  
  @Test
  public void testGetDelegationTokenWithoutKerberos() throws Exception {
    final JobTracker jt = cluster.getJobTrackerRunner().getJobTracker();
    UserGroupInformation ugi = UserGroupInformation.getCurrentUser();
    ugi.setAuthenticationMethod(AuthenticationMethod.TOKEN);
    config.set(CommonConfigurationKeys.HADOOP_SECURITY_AUTHENTICATION,
        "kerberos");
    // Set configuration again so that job tracker finds security enabled
    UserGroupInformation.setConfiguration(config);
    Assert.assertTrue(UserGroupInformation.isSecurityEnabled());
    ugi.doAs(new PrivilegedExceptionAction<Object>() {
      public Object run() throws Exception {
        try {
          Token<DelegationTokenIdentifier> token = jt
              .getDelegationToken(new Text("arenewer"));
          Assert.assertTrue(token != null);
          Assert
              .fail("Delegation token should not be issued without Kerberos authentication");
        } catch (IOException e) {
          // success
        }
        return null;
      }
    });
  }

  @Test
  public void testRenewDelegationTokenWithoutKerberos() throws Exception {
    final JobTracker jt = cluster.getJobTrackerRunner().getJobTracker();
    UserGroupInformation ugi = UserGroupInformation.getCurrentUser();
    ugi.setAuthenticationMethod(AuthenticationMethod.TOKEN);
    config.set(CommonConfigurationKeys.HADOOP_SECURITY_AUTHENTICATION,
        "kerberos");
    // Set configuration again so that job tracker finds security enabled
    UserGroupInformation.setConfiguration(config);
    Assert.assertTrue(UserGroupInformation.isSecurityEnabled());
    final Token<DelegationTokenIdentifier> token = generateDelegationToken(
        "owner", ugi.getShortUserName());
    ugi.doAs(new PrivilegedExceptionAction<Object>() {
      public Object run() throws Exception {
        try {
          jt.renewDelegationToken(token);
          Assert
              .fail("Delegation token should not be renewed without Kerberos authentication");
        } catch (IOException e) {
          // success
        }
        return null;
      }
    });
  }
}
