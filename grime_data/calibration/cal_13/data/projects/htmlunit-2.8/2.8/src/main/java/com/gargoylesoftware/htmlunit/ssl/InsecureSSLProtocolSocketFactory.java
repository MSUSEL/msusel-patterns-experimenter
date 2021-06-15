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
package com.gargoylesoftware.htmlunit.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpParams;

/**
 * A completely insecure (yet very easy to use) SSL socket factory. This socket factory will
 * establish connections to any server from any client, regardless of credentials or the lack
 * thereof. This is especially useful when you are trying to connect to a server with expired or
 * corrupt certificates... this class doesn't care!
 *
 * @version $Revision: 5724 $
 * @author Daniel Gredler
 * @author Nicolas Belisle
 * @see com.gargoylesoftware.htmlunit.WebClient#setUseInsecureSSL(boolean)
 */
public class InsecureSSLProtocolSocketFactory implements SocketFactory {

    private SSLSocketFactory decoratedSSLSocketFactory_;

    /**
     * Creates a new insecure SSL protocol socket factory.
     *
     * @throws GeneralSecurityException if a security error occurs
     */
    public InsecureSSLProtocolSocketFactory() throws GeneralSecurityException {
        final SSLContext context = SSLContext.getInstance("SSL");
        context.init(null, new TrustManager[] {new InsecureTrustManager()}, null);
        decoratedSSLSocketFactory_ = new SSLSocketFactory(SSLContext.getInstance("SSL"));
    }

    /**
     * {@inheritDoc}
     */
    public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose)
        throws IOException, UnknownHostException {
        return decoratedSSLSocketFactory_.createSocket(socket, host, port, autoClose);
    }

    /**
     * {@inheritDoc}
     */
    public Socket connectSocket(final Socket sock, final String host, final int port, final InetAddress localAddress,
        final int localPort, final HttpParams params)
        throws IOException, UnknownHostException, ConnectTimeoutException {
        return decoratedSSLSocketFactory_.connectSocket(sock, host, port, localAddress, localPort, params);
    }

    /**
     * {@inheritDoc}
     */
    public Socket createSocket() throws IOException {
        return decoratedSSLSocketFactory_.createSocket();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSecure(final Socket sock) throws IllegalArgumentException {
        return decoratedSSLSocketFactory_.isSecure(sock);
    }

}
