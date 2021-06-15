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
package org.apache.hadoop.hdfs;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenIdentifier;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenRenewer;
import org.apache.hadoop.hdfs.tools.DelegationTokenFetcher;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.security.SecurityUtil;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.security.token.delegation.AbstractDelegationTokenSelector;

/** An implementation of a protocol for accessing filesystems over HTTPS.
 * The following implementation provides a limited, read-only interface
 * to a filesystem over HTTPS.
 * @see org.apache.hadoop.hdfs.server.namenode.ListPathsServlet
 * @see org.apache.hadoop.hdfs.server.namenode.FileDataServlet
 */
public class HsftpFileSystem extends HftpFileSystem {
  public static final Text TOKEN_KIND = new Text("HSFTP delegation");

  private static final DelegationTokenRenewer<HsftpFileSystem> dtRenewer
      = new DelegationTokenRenewer<HsftpFileSystem>(HsftpFileSystem.class);
  private static final HsftpDelegationTokenSelector hftpTokenSelector =
      new HsftpDelegationTokenSelector();

  @Override
  public void initialize(URI name, Configuration conf) throws IOException {
    super.initialize(name, conf);
    DelegationTokenFetcher.setupSsl(conf);
  }


  @Override
  protected int getDefaultPort() {
    return getConf().getInt(DFSConfigKeys.DFS_NAMENODE_HTTPS_PORT_KEY,
        DFSConfigKeys.DFS_NAMENODE_HTTPS_PORT_DEFAULT);
  }

  /**
   * Return the underlying protocol that is used to talk to the namenode.
   */
  protected String getUnderlyingProtocol() {
    return "https";
  }

  @Override
  protected HttpURLConnection openConnection(String path, String query)
      throws IOException {
    try {
      query = updateQuery(query);
      final URL url = new URI(getUnderlyingProtocol(), null, 
			      nnAddr.getHostName(),
			      nnAddr.getPort(), path, query, null).toURL();
      HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
      // bypass hostname verification
      conn.setHostnameVerifier(new DummyHostnameVerifier());
      return (HttpURLConnection)conn;
    } catch (URISyntaxException e) {
      throw (IOException)new IOException().initCause(e);
    }
  }

  /**
   * Dummy hostname verifier that is used to bypass hostname checking
   */
  protected static class DummyHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  }

  protected Token<DelegationTokenIdentifier> selectHftpDelegationToken() {
    Text serviceName = SecurityUtil.buildTokenService(nnAddr);
    return hftpTokenSelector.selectToken(serviceName, ugi.getTokens());      
  }
  
  @InterfaceAudience.Private
  public static class TokenManager extends HftpFileSystem.TokenManager {

    @Override
    public boolean handleKind(Text kind) {
      return kind.equals(TOKEN_KIND);
    }

    protected String getUnderlyingProtocol() {
      return "https";
    }
  }
  
  private static class HsftpDelegationTokenSelector
  extends AbstractDelegationTokenSelector<DelegationTokenIdentifier> {

    public HsftpDelegationTokenSelector() {
      super(TOKEN_KIND);
    }
  }

}
