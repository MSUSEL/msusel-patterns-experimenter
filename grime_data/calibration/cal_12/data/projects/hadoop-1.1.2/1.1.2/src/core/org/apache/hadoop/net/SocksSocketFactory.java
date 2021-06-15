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
package org.apache.hadoop.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;

/**
 * Specialized SocketFactory to create sockets with a SOCKS proxy
 */
public class SocksSocketFactory extends SocketFactory implements
    Configurable {

  private Configuration conf;

  private Proxy proxy;

  /**
   * Default empty constructor (for use with the reflection API).
   */
  public SocksSocketFactory() {
    this.proxy = Proxy.NO_PROXY;
  }

  /**
   * Constructor with a supplied Proxy
   * 
   * @param proxy the proxy to use to create sockets
   */
  public SocksSocketFactory(Proxy proxy) {
    this.proxy = proxy;
  }

  /* @inheritDoc */
  @Override
  public Socket createSocket() throws IOException {

    return new Socket(proxy);
  }

  /* @inheritDoc */
  @Override
  public Socket createSocket(InetAddress addr, int port) throws IOException {

    Socket socket = createSocket();
    socket.connect(new InetSocketAddress(addr, port));
    return socket;
  }

  /* @inheritDoc */
  @Override
  public Socket createSocket(InetAddress addr, int port,
      InetAddress localHostAddr, int localPort) throws IOException {

    Socket socket = createSocket();
    socket.bind(new InetSocketAddress(localHostAddr, localPort));
    socket.connect(new InetSocketAddress(addr, port));
    return socket;
  }

  /* @inheritDoc */
  @Override
  public Socket createSocket(String host, int port) throws IOException,
      UnknownHostException {

    Socket socket = createSocket();
    socket.connect(new InetSocketAddress(host, port));
    return socket;
  }

  /* @inheritDoc */
  @Override
  public Socket createSocket(String host, int port,
      InetAddress localHostAddr, int localPort) throws IOException,
      UnknownHostException {

    Socket socket = createSocket();
    socket.bind(new InetSocketAddress(localHostAddr, localPort));
    socket.connect(new InetSocketAddress(host, port));
    return socket;
  }

  /* @inheritDoc */
  @Override
  public int hashCode() {
    return proxy.hashCode();
  }

  /* @inheritDoc */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof SocksSocketFactory))
      return false;
    final SocksSocketFactory other = (SocksSocketFactory) obj;
    if (proxy == null) {
      if (other.proxy != null)
        return false;
    } else if (!proxy.equals(other.proxy))
      return false;
    return true;
  }

  /* @inheritDoc */
  public Configuration getConf() {
    return this.conf;
  }

  /* @inheritDoc */
  public void setConf(Configuration conf) {
    this.conf = conf;
    String proxyStr = conf.get("hadoop.socks.server");
    if ((proxyStr != null) && (proxyStr.length() > 0)) {
      setProxy(proxyStr);
    }
  }

  /**
   * Set the proxy of this socket factory as described in the string
   * parameter
   * 
   * @param proxyStr the proxy address using the format "host:port"
   */
  private void setProxy(String proxyStr) {
    String[] strs = proxyStr.split(":", 2);
    if (strs.length != 2)
      throw new RuntimeException("Bad SOCKS proxy parameter: " + proxyStr);
    String host = strs[0];
    int port = Integer.parseInt(strs[1]);
    this.proxy =
        new Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved(host,
            port));
  }
}
