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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.http.HttpServer;
import org.apache.hadoop.net.NetUtils;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.security.SslSocketConnector;

/**
 * Create a Jetty embedded server to answer http/https requests.
 */
public class ProxyHttpServer extends HttpServer {
  public static final Log LOG = LogFactory.getLog(ProxyHttpServer.class);

  public ProxyHttpServer(InetSocketAddress addr, Configuration conf)
      throws IOException {
    super("", addr.getHostName(), addr.getPort(), 0 <= addr.getPort(), conf);
  }

  /** {@inheritDoc} */
  public Connector createBaseListener(Configuration conf)
      throws IOException {
    final String sAddr;
    if (null == (sAddr = conf.get("proxy.http.test.listener.addr"))) {
      SslSocketConnector sslListener = new SslSocketConnector();
      sslListener.setKeystore(conf.get("ssl.server.keystore.location"));
      sslListener.setPassword(conf.get("ssl.server.keystore.password", ""));
      sslListener.setKeyPassword(conf.get("ssl.server.keystore.keypassword", ""));
      sslListener.setKeystoreType(conf.get("ssl.server.keystore.type", "jks"));
      sslListener.setNeedClientAuth(true);
      System.setProperty("javax.net.ssl.trustStore",
          conf.get("ssl.server.truststore.location", ""));
      System.setProperty("javax.net.ssl.trustStorePassword",
          conf.get("ssl.server.truststore.password", ""));
      System.setProperty("javax.net.ssl.trustStoreType",
          conf.get("ssl.server.truststore.type", "jks"));
      return sslListener;
    }
    // unit test
    InetSocketAddress proxyAddr = NetUtils.createSocketAddr(sAddr);
    SelectChannelConnector testlistener = new SelectChannelConnector();
    testlistener.setUseDirectBuffers(false);
    testlistener.setHost(proxyAddr.getHostName());
    testlistener.setPort(proxyAddr.getPort());
    return testlistener;
  }

}
