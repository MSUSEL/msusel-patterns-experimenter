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

package org.hsqldb.server;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Base class for producing the Socket objects used by HSQLDB.
 *
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 * @author boucherb@users
 * @version 1.7.2
 * @since 1.7.2
 */
public class HsqlSocketFactory {

// ----------------------------- static members ---------------------------------
    private static HsqlSocketFactory plainImpl;
    private static HsqlSocketFactory sslImpl;

// ------------------------------ constructors ---------------------------------

    /**
     * External construction disabled.  New factory instances are retreived
     * through the newHsqlSocketFactory method instead.
     */
    protected HsqlSocketFactory() throws Exception {}

// ------------------------- factory builder method ----------------------------

    /**
     * Retrieves an HsqlSocketFactory whose subclass and attributes are
     * determined by the specified argument, tls.
     *
     * @param tls whether to retrieve a factory producing SSL sockets
     * @throws Exception if the new factory cannot be constructed or is
     *      of the wrong type
     * @return a new factory
     */
    public static HsqlSocketFactory getInstance(boolean tls)
    throws Exception {
        return tls ? getSSLImpl()
                   : getPlainImpl();
    }

// -------------------------- public instance methods --------------------------
    public void configureSocket(Socket socket) {

        // default: do nothing
    }

    /**
     * Returns a server socket bound to the specified port.
     * The socket is configured with the socket options
     * given to this factory.
     *
     * @return the ServerSocket
     * @param port the port to which to bind the ServerSocket
     * @throws Exception if a network error occurs
     */
    public ServerSocket createServerSocket(int port) throws Exception {
        return new ServerSocket(port);
    }

    /**
     * Returns a server socket bound to the specified port.
     * The socket is configured with the socket options
     * given to this factory.
     *
     * @return the ServerSocket
     * @param port the port to which to bind the ServerSocket
     * @throws Exception if a network error occurs
     */
    public ServerSocket createServerSocket(int port,
                                           String address) throws Exception {
        return new ServerSocket(port, 128, InetAddress.getByName(address));
    }

    /**
     * Creates a socket and connects it to the specified remote host at the
     * specified remote port. This socket is configured using the socket options
     * established for this factory.
     *
     * @return the socket
     * @param host the server host
     * @param port the server port
     * @throws Exception if a network error occurs
     */
    public Socket createSocket(String host, int port) throws Exception {
        return new Socket(host, port);
    }

    /**
     * Retrieves whether this factory produces secure sockets.
     *
     * @return true if this factory produces secure sockets
     */
    public boolean isSecure() {
        return false;
    }

// ------------------------ static utility methods -----------------------------
    private static HsqlSocketFactory getPlainImpl() throws Exception {

        synchronized (HsqlSocketFactory.class) {
            if (plainImpl == null) {
                plainImpl = new HsqlSocketFactory();
            }
        }

        return plainImpl;
    }

    private static HsqlSocketFactory getSSLImpl() throws Exception {

        synchronized (HsqlSocketFactory.class) {
            if (sslImpl == null) {
                sslImpl = newFactory("org.hsqldb.server.HsqlSocketFactorySecure");
            }
        }

        return sslImpl;
    }

    /**
     * Retrieves a new HsqlSocketFactory whose class
     * is determined by the implClass argument. The basic contract here
     * is that implementations constructed by this method should return
     * true upon calling isSecure() iff they actually create secure sockets.
     * There is no way to guarantee this directly here, so it is simply
     * trusted that an  implementation is secure if it returns true
     * for calls to isSecure();
     *
     * @return a new secure socket factory
     * @param implClass the fully qaulified name of the desired
     *      class to construct
     * @throws Exception if a new secure socket factory cannot
     *      be constructed
     */
    private static HsqlSocketFactory newFactory(String implClass)
    throws Exception {

        Class       clazz;
        Constructor ctor;
        Class[]     ctorParm;
        Object[]    ctorArg;
        Object      factory;

        clazz    = Class.forName(implClass);
        ctorParm = new Class[0];

        // protected constructor
        ctor    = clazz.getDeclaredConstructor(ctorParm);
        ctorArg = new Object[0];

        try {
            factory = ctor.newInstance(ctorArg);
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();

            throw (t instanceof Exception) ? ((Exception) t)
                                           : new RuntimeException(
                                               t.toString());
        }

        return (HsqlSocketFactory) factory;
    }

// --
}
