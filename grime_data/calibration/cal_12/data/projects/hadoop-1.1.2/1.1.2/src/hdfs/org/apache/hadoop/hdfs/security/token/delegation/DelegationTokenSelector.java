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
package org.apache.hadoop.hdfs.security.token.delegation;

//import org.apache.hadoop.classification.InterfaceAudience;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.server.namenode.NameNode;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.security.SecurityUtil;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.security.token.delegation.AbstractDelegationTokenSelector;

/**
 * A delegation token that is specialized for HDFS
 */
//@InterfaceAudience.Private
public class DelegationTokenSelector
    extends AbstractDelegationTokenSelector<DelegationTokenIdentifier>{
  private static final String SERVICE_NAME_KEY = "hdfs.service.host_";

  private static final DelegationTokenSelector INSTANCE = new DelegationTokenSelector();

  /** Select the delegation token for hdfs from the ugi. */
  public static Token<DelegationTokenIdentifier> selectHdfsDelegationToken(
      final InetSocketAddress nnAddr, final UserGroupInformation ugi,
      final Configuration conf) {
    // this guesses the remote cluster's rpc service port.
    // the current token design assumes it's the same as the local cluster's
    // rpc port unless a config key is set.  there should be a way to automatic
    // and correctly determine the value
    final String key = SERVICE_NAME_KEY + SecurityUtil.buildTokenService(nnAddr);
    final String nnServiceName = conf.get(key);
    
    int nnRpcPort = NameNode.DEFAULT_PORT;
    if (nnServiceName != null) {
      nnRpcPort = NetUtils.createSocketAddr(nnServiceName, nnRpcPort).getPort(); 
    }
    
    final Text serviceName = SecurityUtil.buildTokenService(
        NetUtils.makeSocketAddr(nnAddr.getHostName(), nnRpcPort));
    return INSTANCE.selectToken(serviceName, ugi.getTokens());
  }

  public DelegationTokenSelector() {
    super(DelegationTokenIdentifier.HDFS_DELEGATION_KIND);
  }
}
