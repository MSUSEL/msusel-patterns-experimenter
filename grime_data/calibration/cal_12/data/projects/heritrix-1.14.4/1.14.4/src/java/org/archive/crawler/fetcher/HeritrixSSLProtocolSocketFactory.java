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
package org.archive.crawler.fetcher;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.archive.crawler.datamodel.ServerCache;
import org.archive.httpclient.ConfigurableX509TrustManager;


/**
 * Implementation of the commons-httpclient SSLProtocolSocketFactory so we
 * can return SSLSockets whose trust manager is
 * {@link org.archive.httpclient.ConfigurableX509TrustManager}.
 * 
 * We also go to the heritrix cache to get IPs to use making connection.
 * To this, we have dependency on {@link HeritrixProtocolSocketFactory};
 * its assumed this class and it are used together.
 * See {@link HeritrixProtocolSocketFactory#getHostAddress(ServerCache,String)}.
 *
 * @author stack
 * @version $Id: HeritrixSSLProtocolSocketFactory.java 4553 2006-08-29 22:47:03Z stack-sf $
 * @see org.archive.httpclient.ConfigurableX509TrustManager
 */
public class HeritrixSSLProtocolSocketFactory
implements SecureProtocolSocketFactory {
    /***
     * Socket factory with default trust manager installed.
     */
    private SSLSocketFactory sslDefaultFactory = null;
    
    /**
     * Shutdown constructor.
     * @throws KeyManagementException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     */
    public HeritrixSSLProtocolSocketFactory()
    throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException{
        // Get an SSL context and initialize it.
        SSLContext context = SSLContext.getInstance("SSL");

        // I tried to get the default KeyManagers but doesn't work unless you
        // point at a physical keystore. Passing null seems to do the right
        // thing so we'll go w/ that.
        context.init(null, new TrustManager[] {
            new ConfigurableX509TrustManager(
                ConfigurableX509TrustManager.DEFAULT)}, null);
        this.sslDefaultFactory = context.getSocketFactory();
    }

    public Socket createSocket(String host, int port, InetAddress clientHost,
        int clientPort)
    throws IOException, UnknownHostException {
    	return this.sslDefaultFactory.createSocket(host, port,
    	    clientHost, clientPort);
    }

    public Socket createSocket(String host, int port)
    throws IOException, UnknownHostException {
        return this.sslDefaultFactory.createSocket(host, port);
    }

    public synchronized Socket createSocket(String host, int port,
    	InetAddress localAddress, int localPort, HttpConnectionParams params)
    throws IOException, UnknownHostException {
        // Below code is from the DefaultSSLProtocolSocketFactory#createSocket
        // method only it has workarounds to deal with pre-1.4 JVMs.  I've
        // cut these out.
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        Socket socket = null;
        int timeout = params.getConnectionTimeout();
        if (timeout == 0) {
            socket = createSocket(host, port, localAddress, localPort);
        } else {
        	SSLSocketFactory factory = (SSLSocketFactory)params.
                getParameter(FetchHTTP.SSL_FACTORY_KEY);
        	SSLSocketFactory f = (factory != null)? factory: this.sslDefaultFactory;
            socket = f.createSocket();
            ServerCache cache = (ServerCache)params.
                getParameter(FetchHTTP.SERVER_CACHE_KEY);
            InetAddress hostAddress = (cache !=  null)?
                HeritrixProtocolSocketFactory.getHostAddress(cache, host): null;
            InetSocketAddress address = (hostAddress != null)?
                    new InetSocketAddress(hostAddress, port):
                    new InetSocketAddress(host, port);
            socket.bind(new InetSocketAddress(localAddress, localPort));
            try {
                socket.connect(address, timeout);
            } catch (SocketTimeoutException e) {
                // Add timeout info. to the exception.
                throw new SocketTimeoutException(e.getMessage() +
                    ": timeout set at " + Integer.toString(timeout) + "ms.");
            }
            assert socket.isConnected(): "Socket not connected " + host;
        }
        return socket;
    }
    
	public Socket createSocket(Socket socket, String host, int port,
        boolean autoClose)
    throws IOException, UnknownHostException {
        return this.sslDefaultFactory.createSocket(socket, host,
            port, autoClose);
	}
    
    public boolean equals(Object obj) {
        return ((obj != null) && obj.getClass().
            equals(HeritrixSSLProtocolSocketFactory.class));
    }

    public int hashCode() {
        return HeritrixSSLProtocolSocketFactory.class.hashCode();
    }
}