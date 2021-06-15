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



import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenIdentifier;
import org.apache.hadoop.hdfs.server.namenode.web.resources.NamenodeWebHdfsMethods;
import org.apache.hadoop.hdfs.web.WebHdfsFileSystem;
import org.apache.hadoop.hdfs.web.WebHdfsTestUtil;
import org.apache.hadoop.hdfs.web.resources.DoAsParam;
import org.apache.hadoop.hdfs.web.resources.ExceptionHandler;
import org.apache.hadoop.hdfs.web.resources.GetOpParam;
import org.apache.hadoop.hdfs.web.resources.PostOpParam;
import org.apache.hadoop.hdfs.web.resources.PutOpParam;
import org.apache.hadoop.security.TestDoAsEffectiveUser;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.authorize.ProxyUsers;
import org.apache.hadoop.security.token.Token;
import org.apache.log4j.Level;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestDelegationTokenForProxyUser {
  private MiniDFSCluster cluster;
  Configuration config;
  final private static String GROUP1_NAME = "group1";
  final private static String GROUP2_NAME = "group2";
  final private static String[] GROUP_NAMES = new String[] { GROUP1_NAME,
      GROUP2_NAME };
  final private static String REAL_USER = "RealUser";
  final private static String PROXY_USER = "ProxyUser";
  
  private static final Log LOG = LogFactory.getLog(TestDoAsEffectiveUser.class);
  
  private void configureSuperUserIPAddresses(Configuration conf,
      String superUserShortName) throws IOException {
    ArrayList<String> ipList = new ArrayList<String>();
    Enumeration<NetworkInterface> netInterfaceList = NetworkInterface
        .getNetworkInterfaces();
    while (netInterfaceList.hasMoreElements()) {
      NetworkInterface inf = netInterfaceList.nextElement();
      Enumeration<InetAddress> addrList = inf.getInetAddresses();
      while (addrList.hasMoreElements()) {
        InetAddress addr = addrList.nextElement();
        ipList.add(addr.getHostAddress());
      }
    }
    StringBuilder builder = new StringBuilder();
    for (String ip : ipList) {
      builder.append(ip);
      builder.append(',');
    }
    builder.append("127.0.1.1,");
    builder.append(InetAddress.getLocalHost().getCanonicalHostName());
    LOG.info("Local Ip addresses: " + builder.toString());
    conf.setStrings(ProxyUsers.getProxySuperuserIpConfKey(superUserShortName),
        builder.toString());
  }
  
  @Before
  public void setUp() throws Exception {
    config = new Configuration();
    config.setBoolean(DFSConfigKeys.DFS_WEBHDFS_ENABLED_KEY, true);
    config.setBoolean("dfs.support.broken.append", true);
    config.setLong(
        DFSConfigKeys.DFS_NAMENODE_DELEGATION_TOKEN_MAX_LIFETIME_KEY, 10000);
    config.setLong(
        DFSConfigKeys.DFS_NAMENODE_DELEGATION_TOKEN_RENEW_INTERVAL_KEY, 5000);
    config.setStrings(ProxyUsers.getProxySuperuserGroupConfKey(REAL_USER),
        "group1");
    configureSuperUserIPAddresses(config, REAL_USER);
    FileSystem.setDefaultUri(config, "hdfs://localhost:" + "0");
    cluster = new MiniDFSCluster(0, config, 1, true, true, true, null, null,
        null, null);
    cluster.waitActive();
    cluster.getNameNode().getNamesystem().getDelegationTokenSecretManager().startThreads();
    ProxyUsers.refreshSuperUserGroupsConfiguration(config);
  }

  @After
  public void tearDown() throws Exception {
    if(cluster!=null) {
      cluster.shutdown();
    }
  }
 
  @Test
  public void testDelegationTokenWithRealUser() throws IOException {
    UserGroupInformation ugi = UserGroupInformation
        .createRemoteUser(REAL_USER);
    final UserGroupInformation proxyUgi = UserGroupInformation
        .createProxyUserForTesting(PROXY_USER, ugi, GROUP_NAMES);
    try {
      Token<DelegationTokenIdentifier> token = proxyUgi
          .doAs(new PrivilegedExceptionAction<Token<DelegationTokenIdentifier>>() {
            public Token<DelegationTokenIdentifier> run() throws IOException {
              DistributedFileSystem dfs = (DistributedFileSystem) cluster
                  .getFileSystem();
              return dfs.getDelegationToken("RenewerUser");
            }
          });
      DelegationTokenIdentifier identifier = new DelegationTokenIdentifier();
      byte[] tokenId = token.getIdentifier();
      identifier.readFields(new DataInputStream(new ByteArrayInputStream(
          tokenId)));
      Assert.assertEquals(identifier.getUser().getUserName(), PROXY_USER);
      Assert.assertEquals(identifier.getUser().getRealUser().getUserName(),
          REAL_USER);
    } catch (InterruptedException e) {
      //Do Nothing
    }
  }
  
  @Test
  public void testWebHdfsDoAs() throws Exception {
    WebHdfsTestUtil.LOG.info("START: testWebHdfsDoAs()");
    ((Log4JLogger)NamenodeWebHdfsMethods.LOG).getLogger().setLevel(Level.ALL);
    ((Log4JLogger)ExceptionHandler.LOG).getLogger().setLevel(Level.ALL);
    final UserGroupInformation ugi = UserGroupInformation.createRemoteUser(REAL_USER);
    WebHdfsTestUtil.LOG.info("ugi.getShortUserName()=" + ugi.getShortUserName());
    final WebHdfsFileSystem webhdfs = WebHdfsTestUtil.getWebHdfsFileSystemAs(ugi, config);
    
    final Path root = new Path("/");
    cluster.getFileSystem().setPermission(root, new FsPermission((short)0777));

    {
      //test GETHOMEDIRECTORY with doAs
      final URL url = WebHdfsTestUtil.toUrl(webhdfs,
          GetOpParam.Op.GETHOMEDIRECTORY,  root, new DoAsParam(PROXY_USER));
      final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      final Map<?, ?> m = WebHdfsTestUtil.connectAndGetJson(conn, HttpServletResponse.SC_OK);
      conn.disconnect();
  
      final Object responsePath = m.get(Path.class.getSimpleName());
      WebHdfsTestUtil.LOG.info("responsePath=" + responsePath);
      Assert.assertEquals("/user/" + PROXY_USER, responsePath);
    }

    {
      //test GETHOMEDIRECTORY with DOas
      final URL url = WebHdfsTestUtil.toUrl(webhdfs,
          GetOpParam.Op.GETHOMEDIRECTORY,  root, new DoAsParam(PROXY_USER) {
            @Override
            public String getName() {
              return "DOas";
            }
      });
      final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      final Map<?, ?> m = WebHdfsTestUtil.connectAndGetJson(conn, HttpServletResponse.SC_OK);
      conn.disconnect();
  
      final Object responsePath = m.get(Path.class.getSimpleName());
      WebHdfsTestUtil.LOG.info("responsePath=" + responsePath);
      Assert.assertEquals("/user/" + PROXY_USER, responsePath);
    }

    final Path f = new Path("/testWebHdfsDoAs/a.txt");
    {
      //test create file with doAs
      final PutOpParam.Op op = PutOpParam.Op.CREATE;
      final URL url = WebHdfsTestUtil.toUrl(webhdfs, op,  f, new DoAsParam(PROXY_USER));
      WebHdfsTestUtil.LOG.info("url=" + url);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn = WebHdfsTestUtil.twoStepWrite(webhdfs, op, conn);
      final FSDataOutputStream out = WebHdfsTestUtil.write(webhdfs, op, conn, 4096);
      out.write("Hello, webhdfs user!".getBytes());
      out.close();
  
      final FileStatus status = webhdfs.getFileStatus(f);
      WebHdfsTestUtil.LOG.info("status.getOwner()=" + status.getOwner());
      Assert.assertEquals(PROXY_USER, status.getOwner());
    }

    {
      //test append file with doAs
      final PostOpParam.Op op = PostOpParam.Op.APPEND;
      final URL url = WebHdfsTestUtil.toUrl(webhdfs, op,  f, new DoAsParam(PROXY_USER));
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn = WebHdfsTestUtil.twoStepWrite(webhdfs, op, conn);
      final FSDataOutputStream out = WebHdfsTestUtil.write(webhdfs, op, conn, 4096);
      out.write("\nHello again!".getBytes());
      out.close();
  
      final FileStatus status = webhdfs.getFileStatus(f);
      WebHdfsTestUtil.LOG.info("status.getOwner()=" + status.getOwner());
      WebHdfsTestUtil.LOG.info("status.getLen()  =" + status.getLen());
      Assert.assertEquals(PROXY_USER, status.getOwner());
    }
  }
}
