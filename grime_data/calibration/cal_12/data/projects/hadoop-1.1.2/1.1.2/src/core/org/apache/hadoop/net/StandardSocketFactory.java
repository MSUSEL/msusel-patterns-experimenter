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
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import javax.net.SocketFactory;

/**
 * Specialized SocketFactory to create sockets with a SOCKS proxy
 */
public class StandardSocketFactory extends SocketFactory {

  /**
   * Default empty constructor (for use with the reflection API).
   */
  public StandardSocketFactory() {
  }

  /* @inheritDoc */
  @Override
  public Socket createSocket() throws IOException {
    /*
     * NOTE: This returns an NIO socket so that it has an associated 
     * SocketChannel. As of now, this unfortunately makes streams returned
     * by Socket.getInputStream() and Socket.getOutputStream() unusable
     * (because a blocking read on input stream blocks write on output stream
     * and vice versa).
     * 
     * So users of these socket factories should use 
     * NetUtils.getInputStream(socket) and 
     * NetUtils.getOutputStream(socket) instead.
     * 
     * A solution for hiding from this from user is to write a 
     * 'FilterSocket' on the lines of FilterInputStream and extend it by
     * overriding getInputStream() and getOutputStream().
     */
    return SocketChannel.open().socket();
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
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof StandardSocketFactory))
      return false;
    return true;
  }

  /* @inheritDoc */
  @Override
  public int hashCode() {
    // Dummy hash code (to make find bugs happy)
    return 47;
  } 
  
}
