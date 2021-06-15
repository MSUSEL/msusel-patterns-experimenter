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
package org.apache.hadoop.hdfsproxy;

import org.apache.hadoop.hdfs.server.namenode.JspHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * A HTTPS/SSL proxy to HDFS, implementing certificate based access control.
 */
public class HdfsProxy {
  public static final Log LOG = LogFactory.getLog(HdfsProxy.class);

  private ProxyHttpServer server;
  private InetSocketAddress sslAddr;

  /** Construct a proxy from the given configuration */
  public HdfsProxy(Configuration conf) throws IOException {
    try {
      initialize(conf);
    } catch (IOException e) {
      this.stop();
      throw e;
    }
  }

  private void initialize(Configuration conf) throws IOException {
    sslAddr = getSslAddr(conf);
    String nn = conf.get("hdfsproxy.dfs.namenode.address");
    if (nn == null)
      throw new IOException("HDFS NameNode address is not specified");
    InetSocketAddress nnAddr = NetUtils.createSocketAddr(nn);
    LOG.info("HDFS NameNode is at: " + nnAddr.getHostName() + ":" + nnAddr.getPort());

    Configuration sslConf = new Configuration(false);
    sslConf.addResource(conf.get("hdfsproxy.https.server.keystore.resource",
        "ssl-server.xml"));
    // unit testing
    sslConf.set("proxy.http.test.listener.addr",
                conf.get("proxy.http.test.listener.addr"));

    this.server = new ProxyHttpServer(sslAddr, sslConf);
    this.server.setAttribute("proxy.https.port", server.getPort());
    this.server.setAttribute("name.node.address", nnAddr);
    this.server.setAttribute(JspHelper.CURRENT_CONF, new Configuration());
    this.server.addGlobalFilter("ProxyFilter", ProxyFilter.class.getName(), null);
    this.server.addServlet("listPaths", "/listPaths/*", ProxyListPathsServlet.class);
    this.server.addServlet("data", "/data/*", ProxyFileDataServlet.class);
    this.server.addServlet("streamFile", "/streamFile/*", ProxyStreamFile.class);
  }

  /** return the http port if any, only for testing purposes */
  int getPort() throws IOException {
    return server.getPort();
  }

  /**
   * Start the server.
   */
  public void start() throws IOException {
    this.server.start();
    LOG.info("HdfsProxy server up at: " + sslAddr.getHostName() + ":"
        + sslAddr.getPort());
  }

  /**
   * Stop all server threads and wait for all to finish.
   */
  public void stop() {
    try {
      if (server != null) {
        server.stop();
        server.join();
      }
    } catch (Exception e) {
      LOG.warn("Got exception shutting down proxy", e);
    }
  }

  /**
   * Wait for service to finish.
   * (Normally, it runs forever.)
   */
  public void join() {
    try {
      this.server.join();
    } catch (InterruptedException ie) {
    }
  }

  static InetSocketAddress getSslAddr(Configuration conf) throws IOException {
    String addr = conf.get("hdfsproxy.https.address");
    if (addr == null)
      throw new IOException("HdfsProxy address is not specified");
    return NetUtils.createSocketAddr(addr);
  }


  public static HdfsProxy createHdfsProxy(String argv[], Configuration conf)
      throws IOException {
    if (argv.length > 0) {
      System.err.println("Usage: HdfsProxy");
      return null;
    }
    if (conf == null) {
      conf = new Configuration(false);
      conf.addResource("hdfsproxy-default.xml");
    }

    StringUtils.startupShutdownMessage(HdfsProxy.class, argv, LOG);
    HdfsProxy proxy = new HdfsProxy(conf);
    proxy.start();
    return proxy;
  }

  public static void main(String[] argv) throws Exception {
    try {
      HdfsProxy proxy = createHdfsProxy(argv, null);
      if (proxy != null)
        proxy.join();
    } catch (Throwable e) {
      LOG.error(StringUtils.stringifyException(e));
      System.exit(-1);
    }
  }
}
