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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenIdentifier;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.security.token.TokenIdentifier;
import org.junit.Before;
import org.junit.Test;

public class TestHftpFileSystem {
  
  @Before
  public void resetFileSystem() throws IOException {
    // filesystem caching has a quirk/bug that it caches based on the user's
    // given uri.  the result is if a filesystem is instantiated with no port,
    // it gets the default port.  then if the default port is changed,
    // and another filesystem is instantiated with no port, the prior fs
    // is returned, not a new one using the changed port.  so let's flush
    // the cache between tests...
    FileSystem.closeAll();
  }
  
  @Test
  public void testHftpDefaultPorts() throws IOException {
    Configuration conf = new Configuration();
    URI uri = URI.create("hftp://localhost");
    HftpFileSystem fs = (HftpFileSystem) FileSystem.get(uri, conf);

    assertEquals(DFSConfigKeys.DFS_NAMENODE_HTTP_PORT_DEFAULT,
        fs.getDefaultPort());

    URI fsUri = fs.getUri();
    assertEquals(uri.getHost(), fsUri.getHost());
    assertEquals(DFSConfigKeys.DFS_NAMENODE_HTTP_PORT_DEFAULT, fsUri.getPort());
    
    assertEquals(
        "127.0.0.1:"+DFSConfigKeys.DFS_NAMENODE_HTTP_PORT_DEFAULT,
        fs.getCanonicalServiceName()
    );
  }
  
  @Test
  public void testHftpCustomDefaultPorts() throws IOException {
    Configuration conf = new Configuration();
    conf.setInt("dfs.http.port", 123);
    conf.setInt("dfs.https.port", 456);

    URI uri = URI.create("hftp://localhost");
    HftpFileSystem fs = (HftpFileSystem) FileSystem.get(uri, conf);

    assertEquals(123, fs.getDefaultPort());
    
    URI fsUri = fs.getUri();
    assertEquals(uri.getHost(), fsUri.getHost());
    assertEquals(123, fsUri.getPort());
    
    assertEquals(
        "127.0.0.1:123",
        fs.getCanonicalServiceName()
    );
  }

  @Test
  public void testHftpCustomUriPortWithDefaultPorts() throws IOException {
    Configuration conf = new Configuration();
    URI uri = URI.create("hftp://localhost:123");
    HftpFileSystem fs = (HftpFileSystem) FileSystem.get(uri, conf);

    assertEquals(DFSConfigKeys.DFS_NAMENODE_HTTP_PORT_DEFAULT, fs.getDefaultPort());

    URI fsUri = fs.getUri();
    assertEquals(uri.getHost(), fsUri.getHost());
    assertEquals(uri.getPort(), fsUri.getPort());
    
    assertEquals("127.0.0.1:123",
                 fs.getCanonicalServiceName()
    );
  }

  @Test
  public void testHftpCustomUriPortWithCustomDefaultPorts() throws IOException {
    Configuration conf = new Configuration();
    conf.setInt("dfs.http.port", 123);
    conf.setInt("dfs.https.port", 456);

    URI uri = URI.create("hftp://localhost:789");
    HftpFileSystem fs = (HftpFileSystem) FileSystem.get(uri, conf);

    assertEquals(123, fs.getDefaultPort());
    
    URI fsUri = fs.getUri();
    assertEquals(uri.getHost(), fsUri.getHost());
    assertEquals(789, fsUri.getPort());
    
    assertEquals("127.0.0.1:789",
                 fs.getCanonicalServiceName()
    );
  }

  ///

  @Test
  public void testHsftpDefaultPorts() throws IOException {
    Configuration conf = new Configuration();
    URI uri = URI.create("hsftp://localhost");
    HsftpFileSystem fs = (HsftpFileSystem) FileSystem.get(uri, conf);

    assertEquals(DFSConfigKeys.DFS_NAMENODE_HTTPS_PORT_DEFAULT, fs.getDefaultPort());

    URI fsUri = fs.getUri();
    assertEquals(uri.getHost(), fsUri.getHost());
    assertEquals(DFSConfigKeys.DFS_NAMENODE_HTTPS_PORT_DEFAULT, fsUri.getPort());
    
    assertEquals(
        "127.0.0.1:"+DFSConfigKeys.DFS_NAMENODE_HTTPS_PORT_DEFAULT,
        fs.getCanonicalServiceName()
    );
  }

  @Test
  public void testHsftpCustomDefaultPorts() throws IOException {
    Configuration conf = new Configuration();
    conf.setInt("dfs.http.port", 123);
    conf.setInt("dfs.https.port", 456);

    URI uri = URI.create("hsftp://localhost");
    HsftpFileSystem fs = (HsftpFileSystem) FileSystem.get(uri, conf);

    assertEquals(456, fs.getDefaultPort());
    
    URI fsUri = fs.getUri();
    assertEquals(uri.getHost(), fsUri.getHost());
    assertEquals(456, fsUri.getPort());
    
    assertEquals(
        "127.0.0.1:456",
        fs.getCanonicalServiceName()
    );
  }

  @Test
  public void testHsftpCustomUriPortWithDefaultPorts() throws IOException {
    Configuration conf = new Configuration();
    URI uri = URI.create("hsftp://localhost:123");
    HsftpFileSystem fs = (HsftpFileSystem) FileSystem.get(uri, conf);

    assertEquals(DFSConfigKeys.DFS_NAMENODE_HTTPS_PORT_DEFAULT, fs.getDefaultPort());

    URI fsUri = fs.getUri();
    assertEquals(uri.getHost(), fsUri.getHost());
    assertEquals(uri.getPort(), fsUri.getPort());
    
    assertEquals(
        "127.0.0.1:123",
        fs.getCanonicalServiceName()
    );
  }

  @Test
  public void testHsftpCustomUriPortWithCustomDefaultPorts() throws IOException {
    Configuration conf = new Configuration();
    conf.setInt("dfs.http.port", 123);
    conf.setInt("dfs.https.port", 456);

    URI uri = URI.create("hsftp://localhost:789");
    HsftpFileSystem fs = (HsftpFileSystem) FileSystem.get(uri, conf);

    assertEquals(456, fs.getDefaultPort());
    
    URI fsUri = fs.getUri();
    assertEquals(uri.getHost(), fsUri.getHost());
    assertEquals(789, fsUri.getPort());
    
    assertEquals(
        "127.0.0.1:789",
        fs.getCanonicalServiceName()
    );
  }

  Token<DelegationTokenIdentifier> hftpToken;
  Token<DelegationTokenIdentifier> hdfsToken;
  Token<DelegationTokenIdentifier> gotToken;
  
  class StubbedHftpFileSystem extends HftpFileSystem {
    @Override
    protected Token<DelegationTokenIdentifier> selectHftpDelegationToken() {
      return hftpToken;
    }
    
    @Override
    protected Token<DelegationTokenIdentifier> selectHdfsDelegationToken() {
      return hdfsToken;
    }
    
    @Override
    public Token<DelegationTokenIdentifier> getDelegationToken(String renewer) {
      return makeDummyToken("new");
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends TokenIdentifier> void setDelegationToken(Token<T> token) {
      gotToken = (Token<DelegationTokenIdentifier>) token;
    }
  }
  
  static Token<DelegationTokenIdentifier> makeDummyToken(String kind) {
    Token<DelegationTokenIdentifier> token = new Token<DelegationTokenIdentifier>();
    token.setKind(new Text(kind));
    return token;
  }
  
  @Before
  public void resetTokens() {
    hftpToken = hdfsToken = gotToken = null;
  }
  
  @Test
  public void testHftpWithNoTokens() throws IOException {
    new StubbedHftpFileSystem().initDelegationToken();
    assertNotNull(gotToken);
    assertEquals(new Text("new"), gotToken.getKind());
    
  }
  @Test
  public void testHftpWithHftpToken() throws IOException {
    hftpToken = makeDummyToken("hftp");
    new StubbedHftpFileSystem().initDelegationToken();
    assertNotNull(gotToken);
    assertEquals(gotToken, hftpToken);
  }
  
  @Test
  public void testHftpWithHdfsToken() throws IOException {
    hdfsToken = makeDummyToken("hdfs");
    new StubbedHftpFileSystem().initDelegationToken();
    assertNotNull(gotToken);
    assertEquals(gotToken, hdfsToken);
  }

  @Test
  public void testHftpWithHftpAndHdfsToken() throws IOException {
    hftpToken = makeDummyToken("hftp");
    hdfsToken = makeDummyToken("hdfs");
    new StubbedHftpFileSystem().initDelegationToken();
    assertNotNull(gotToken);
    assertEquals(gotToken, hftpToken);
  }
}
