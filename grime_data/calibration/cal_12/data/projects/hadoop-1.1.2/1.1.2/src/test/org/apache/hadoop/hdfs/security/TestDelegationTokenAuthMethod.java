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
package org.apache.hadoop.hdfs.security;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;

import junit.framework.Assert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.UserGroupInformation.AuthenticationMethod;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenIdentifier;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenSecretManager;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDelegationTokenAuthMethod {
  private MiniDFSCluster cluster;
  Configuration config;
  
  @Before
  public void setUp() throws Exception {
    config = new Configuration();
    FileSystem.setDefaultUri(config, "hdfs://localhost:" + "0");
    cluster = new MiniDFSCluster(0, config, 1, true, true, true,  null, null, null, null);
    cluster.waitActive();
    cluster.getNameNode().getNamesystem().getDelegationTokenSecretManager().startThreads();
  }

  @After
  public void tearDown() throws Exception {
    if(cluster!=null) {
      cluster.shutdown();
    }
  }
  
  private Token<DelegationTokenIdentifier> generateDelegationToken(
      String owner, String renewer) {
    DelegationTokenSecretManager dtSecretManager = cluster.getNameNode().getNamesystem()
        .getDelegationTokenSecretManager();
    DelegationTokenIdentifier dtId = new DelegationTokenIdentifier(new Text(
        owner), new Text(renewer), null);
    return new Token<DelegationTokenIdentifier>(dtId, dtSecretManager);
  }
  
  @Test
  public void testDelegationTokenNamesystemApi() throws Exception {
    final FSNamesystem namesys = cluster.getNameNode().getNamesystem();
    final UserGroupInformation ugi = UserGroupInformation.getCurrentUser();
    ugi.setAuthenticationMethod(AuthenticationMethod.KERBEROS);
    config.set(DFSConfigKeys.HADOOP_SECURITY_AUTHENTICATION, "kerberos");
    //Set conf again so that namesystem finds security enabled
    UserGroupInformation.setConfiguration(config);
    ugi.doAs(new PrivilegedExceptionAction<Object>() {
      public Object run() throws Exception {
        try {
          Token<DelegationTokenIdentifier> token = namesys
              .getDelegationToken(new Text(ugi.getShortUserName()));
          namesys.renewDelegationToken(token);
          namesys.cancelDelegationToken(token);
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
    final FSNamesystem namesys = cluster.getNameNode().getNamesystem();
    UserGroupInformation ugi = UserGroupInformation.getCurrentUser();
    ugi.setAuthenticationMethod(AuthenticationMethod.TOKEN);
    config.set(DFSConfigKeys.HADOOP_SECURITY_AUTHENTICATION, "kerberos");
    //Set conf again so that namesystem finds security enabled
    UserGroupInformation.setConfiguration(config);
    ugi.doAs(new PrivilegedExceptionAction<Object>() {
      public Object run() throws Exception {
        try {
          namesys.getDelegationToken(new Text("arenewer"));
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
    final FSNamesystem namesys = cluster.getNameNode().getNamesystem();
    UserGroupInformation ugi = UserGroupInformation.getCurrentUser();
    ugi.setAuthenticationMethod(AuthenticationMethod.TOKEN);
    config.set(DFSConfigKeys.HADOOP_SECURITY_AUTHENTICATION, "kerberos");
    //Set conf again so that namesystem finds security enabled
    UserGroupInformation.setConfiguration(config);
    final Token<DelegationTokenIdentifier> token = generateDelegationToken(
        "owner", ugi.getShortUserName());
    ugi.doAs(new PrivilegedExceptionAction<Object>() {
      public Object run() throws Exception {
        try {
          namesys.renewDelegationToken(token);
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
