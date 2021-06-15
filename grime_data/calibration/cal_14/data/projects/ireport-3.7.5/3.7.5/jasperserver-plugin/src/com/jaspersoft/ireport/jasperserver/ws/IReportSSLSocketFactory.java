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
package com.jaspersoft.ireport.jasperserver.ws;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

/**
 *
 * @version $Id: IReportSSLSocketFactory.java 0 2010-07-19 19:59:15 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IReportSSLSocketFactory implements org.apache.commons.httpclient.protocol.ProtocolSocketFactory  {



    private SSLContext sslcontext = null;


    /**

     * Constructor for EasySSLProtocolSocketFactory.

     */

    public IReportSSLSocketFactory() {

        super();

    }


    private static SSLContext createIRSSLContext() {

        try {

            SSLContext context = SSLContext.getInstance("SSL");

            context.init(null, new TrustManager[] {new IReportTrustManager()}, null);

            return context;

        } catch (Exception ex) {

            ex.printStackTrace();

            throw new HttpClientError(ex.toString());

        }

    }


    private SSLContext getSSLContext() {

        if (this.sslcontext == null) {

            this.sslcontext = createIRSSLContext();

        }

        return this.sslcontext;

    }


    /**

     * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int,java.net.InetAddress,int)

     */

    public Socket createSocket(

        String host,

        int port,

        InetAddress clientHost,

        int clientPort)

        throws IOException, UnknownHostException {


        return getSSLContext().getSocketFactory().createSocket(

            host,

            port,

            clientHost,

            clientPort

        );

    }


    /**

     * Attempts to get a new socket connection to the given host within the given time limit.

     * <p>

     * To circumvent the limitations of older JREs that do not support connect timeout a

     * controller thread is executed. The controller thread attempts to create a new socket

     * within the given limit of time. If socket constructor does not return until the

     * timeout expires, the controller terminates and throws an {@link ConnectTimeoutException}

     * </p>

     *

     * @param host the host name/IP

     * @param port the port on the host

     * @param clientHost the local host name/IP to bind the socket to

     * @param clientPort the port on the local machine

     * @param params {@link HttpConnectionParams Http connection parameters}

     *

     * @return Socket a new socket

     *

     * @throws IOException if an I/O error occurs while creating the socket

     * @throws UnknownHostException if the IP address of the host cannot be

     * determined

     */

    public Socket createSocket(

        final String host,

        final int port,

        final InetAddress localAddress,

        final int localPort,

        final HttpConnectionParams params

    ) throws IOException, UnknownHostException, ConnectTimeoutException {

        if (params == null) {

            throw new IllegalArgumentException("Parameters may not be null");

        }

        int timeout = params.getConnectionTimeout();

        SocketFactory socketfactory = getSSLContext().getSocketFactory();

        if (timeout == 0) {

            return socketfactory.createSocket(host, port, localAddress, localPort);

        } else {

            Socket socket = socketfactory.createSocket();

            SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);

            SocketAddress remoteaddr = new InetSocketAddress(host, port);

            socket.bind(localaddr);

            socket.connect(remoteaddr, timeout);

            return socket;

        }

    }


    /**

     * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int)

     */

    public Socket createSocket(String host, int port)

        throws IOException, UnknownHostException {

        return getSSLContext().getSocketFactory().createSocket(

            host,

            port

        );

    }


    /**

     * @see SecureProtocolSocketFactory#createSocket(java.net.Socket,java.lang.String,int,boolean)

     */

    public Socket createSocket(

        Socket socket,

        String host,

        int port,

        boolean autoClose)

        throws IOException, UnknownHostException {

        return getSSLContext().getSocketFactory().createSocket(

            socket,

            host,

            port,

            autoClose

        );

    }


    public boolean equals(Object obj) {

        return ((obj != null) && obj.getClass().equals(IReportSSLSocketFactory.class));

    }


    public int hashCode() {

        return IReportSSLSocketFactory.class.hashCode();

    }







}
