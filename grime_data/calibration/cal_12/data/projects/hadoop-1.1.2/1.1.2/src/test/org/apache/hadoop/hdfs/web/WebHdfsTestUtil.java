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
package org.apache.hadoop.hdfs.web;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.PrivilegedExceptionAction;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.web.resources.HttpOpParam;
import org.apache.hadoop.hdfs.web.resources.Param;
import org.apache.hadoop.security.UserGroupInformation;
import org.junit.Assert;

public class WebHdfsTestUtil {
  public static final Log LOG = LogFactory.getLog(WebHdfsTestUtil.class);

  public static Configuration createConf() {
    final Configuration conf = new Configuration();
    conf.setBoolean(DFSConfigKeys.DFS_WEBHDFS_ENABLED_KEY, true);
    return conf;
  }

  public static WebHdfsFileSystem getWebHdfsFileSystem(final Configuration conf
      ) throws IOException, URISyntaxException {
    final String uri = WebHdfsFileSystem.SCHEME  + "://"
        + conf.get("dfs.http.address");
    return (WebHdfsFileSystem)FileSystem.get(new URI(uri), conf);
  }

  public static WebHdfsFileSystem getWebHdfsFileSystemAs(
      final UserGroupInformation ugi, final Configuration conf
      ) throws IOException, InterruptedException {
    return ugi.doAs(new PrivilegedExceptionAction<WebHdfsFileSystem>() {
      @Override
      public WebHdfsFileSystem run() throws Exception {
        return getWebHdfsFileSystem(conf);
      }
    });
  }

  public static URL toUrl(final WebHdfsFileSystem webhdfs,
      final HttpOpParam.Op op, final Path fspath,
      final Param<?,?>... parameters) throws IOException {
    final URL url = webhdfs.toUrl(op, fspath, parameters);
    WebHdfsTestUtil.LOG.info("url=" + url);
    return url;
  }

  public static Map<?, ?> connectAndGetJson(final HttpURLConnection conn,
      final int expectedResponseCode) throws IOException {
    conn.connect();
    Assert.assertEquals(expectedResponseCode, conn.getResponseCode());
    return WebHdfsFileSystem.jsonParse(conn, false);
  }
  
  public static HttpURLConnection twoStepWrite(final WebHdfsFileSystem webhdfs,
    final HttpOpParam.Op op, HttpURLConnection conn) throws IOException {
    return webhdfs.new Runner(op, conn).twoStepWrite();
  }

  public static FSDataOutputStream write(final WebHdfsFileSystem webhdfs,
      final HttpOpParam.Op op, final HttpURLConnection conn,
      final int bufferSize) throws IOException {
    return webhdfs.write(op, conn, bufferSize);
  }
}
