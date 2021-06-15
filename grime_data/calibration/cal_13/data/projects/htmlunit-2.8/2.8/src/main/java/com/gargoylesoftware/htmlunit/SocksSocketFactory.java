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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.http.HttpHost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * SOCKS {@link SocksSocketFactory}.
 *
 * @version $Revision: 5746 $
 * @author Ahmed Ashour
 */
class SocksSocketFactory implements SocketFactory {

    private HttpHost socksProxy_;

    void setSocksProxy(final HttpHost socksProxy) {
        socksProxy_ = socksProxy;
    }

    /**
     * {@inheritDoc}
     */
    public Socket createSocket() {
        if (socksProxy_ != null) {
            final InetSocketAddress address = new InetSocketAddress(socksProxy_.getHostName(), socksProxy_.getPort());
            final Proxy proxy = new Proxy(Proxy.Type.SOCKS, address);
            return new Socket(proxy);
        }
        else {
            return new Socket();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Socket connectSocket(Socket sock, final String host, final int port, final InetAddress localAddress,
            int localPort, final HttpParams params)
        throws IOException {

        if (host == null) {
            throw new IllegalArgumentException("Target host may not be null.");
        }
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null.");
        }

        if (sock == null) {
            sock = createSocket();
        }

        if (localAddress != null || localPort > 0) {
            if (localPort < 0) {
                localPort = 0; // indicates "any"
            }

            sock.bind(new InetSocketAddress(localAddress, localPort));
        }

        final int timeout = HttpConnectionParams.getConnectionTimeout(params);

        final InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
        try {
            sock.connect(remoteAddress, timeout);
        }
        catch (final SocketTimeoutException ex) {
            throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
        }
        return sock;
    }

    /**
     * Checks whether a socket connection is secure.
     * This factory creates plain socket connections which are not considered secure.
     *
     * @param sock the connected socket
     * @return <code>false</code>
     * @throws IllegalArgumentException if the argument is invalid
     */
    public final boolean isSecure(final Socket sock) throws IllegalArgumentException {
        if (sock == null) {
            throw new IllegalArgumentException("Socket may not be null.");
        }
        // This check is performed last since it calls a method implemented
        // by the argument object. getClass() is final in java.lang.Object.
        if (sock.isClosed()) {
            throw new IllegalArgumentException("Socket is closed.");
        }
        return false;
    }

}
