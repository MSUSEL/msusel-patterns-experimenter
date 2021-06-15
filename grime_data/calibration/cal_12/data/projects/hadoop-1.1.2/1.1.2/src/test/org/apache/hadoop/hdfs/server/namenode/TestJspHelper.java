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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.servlet.http.HttpServletRequest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenIdentifier;
import org.apache.hadoop.hdfs.server.namenode.NameNode;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.security.SecurityUtil;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.security.token.TokenIdentifier;
import org.apache.hadoop.security.token.delegation.AbstractDelegationTokenSecretManager;
import org.junit.Assert;
import org.junit.Test;

public class TestJspHelper {

  private Configuration conf = new Configuration();

  public static class DummySecretManager extends
      AbstractDelegationTokenSecretManager<DelegationTokenIdentifier> {

    public DummySecretManager(long delegationKeyUpdateInterval,
        long delegationTokenMaxLifetime, long delegationTokenRenewInterval,
        long delegationTokenRemoverScanInterval) {
      super(delegationKeyUpdateInterval, delegationTokenMaxLifetime,
          delegationTokenRenewInterval, delegationTokenRemoverScanInterval);
    }

    @Override
    public DelegationTokenIdentifier createIdentifier() {
      return null;
    }

    @Override
    public byte[] createPassword(DelegationTokenIdentifier dtId) {
      return new byte[1];
    }
  }

  @Test
  public void testGetUgi() throws IOException {
    conf.set(DFSConfigKeys.FS_DEFAULT_NAME_KEY, "hdfs://localhost:4321/");
    HttpServletRequest request = mock(HttpServletRequest.class);
    String user = "TheDoctor";
    Text userText = new Text(user);
    DelegationTokenIdentifier dtId = new DelegationTokenIdentifier(userText,
        userText, null);
    Token<DelegationTokenIdentifier> token = new Token<DelegationTokenIdentifier>(
        dtId, new DummySecretManager(0, 0, 0, 0));
    String tokenString = token.encodeToUrlString();
    when(request.getParameter(JspHelper.DELEGATION_PARAMETER_NAME)).thenReturn(
        tokenString);
    when(request.getRemoteUser()).thenReturn(user);

    conf.set(DFSConfigKeys.HADOOP_SECURITY_AUTHENTICATION, "kerberos");
    UserGroupInformation.setConfiguration(conf);

    InetSocketAddress serviceAddr = NameNode.getAddress(conf);
    Text tokenService = SecurityUtil.buildTokenService(serviceAddr);

    UserGroupInformation ugi = JspHelper.getUGI(request, conf);
    Token<? extends TokenIdentifier> tokenInUgi = ugi.getTokens().iterator()
        .next();
    Assert.assertEquals(tokenService, tokenInUgi.getService());
  }
  
  @Test
  public void testDelegationTokenUrlParam() {
    conf.set(DFSConfigKeys.HADOOP_SECURITY_AUTHENTICATION, "kerberos");
    UserGroupInformation.setConfiguration(conf);
    String tokenString = "xyzabc";
    String delegationTokenParam = JspHelper
        .getDelegationTokenUrlParam(tokenString);
    //Security is enabled
    Assert.assertEquals(JspHelper.SET_DELEGATION + "xyzabc",
        delegationTokenParam);
    conf.set(DFSConfigKeys.HADOOP_SECURITY_AUTHENTICATION, "simple");
    UserGroupInformation.setConfiguration(conf);
    delegationTokenParam = JspHelper
        .getDelegationTokenUrlParam(tokenString);
    //Empty string must be returned because security is disabled.
    Assert.assertEquals("", delegationTokenParam);
  }

}
