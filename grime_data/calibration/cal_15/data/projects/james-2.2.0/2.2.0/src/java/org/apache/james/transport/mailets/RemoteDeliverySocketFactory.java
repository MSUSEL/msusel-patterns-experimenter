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
package org.apache.james.transport.mailets;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;

/**
 * It is used by RemoteDelivery in order to make possible to bind the client
 * socket to a specific ip address.
 *
 * This is not a nice solution because the ip address must be shared by all 
 * RemoteDelivery instances. It would be better to modify JavaMail 
 * (current version 1.3) to support a corresonding property, e.g.
 * mail.smtp.bindAdress.
 * 
 * It should be a javax.net.SocketFactory descendant, but 
 * 1. it is not necessary because JavaMail 1.2 uses reflection when accessing
 * this class;
 * 2. it is not desirable because it would require java 1.4.
 */
public class RemoteDeliverySocketFactory {
    
    /**
     * @param addr the ip address or host name the delivery socket will bind to
     */
    static void setBindAdress(String addr) throws UnknownHostException {
        if (addr == null) bindAddress = null;
        else bindAddress = InetAddress.getByName(addr);
    }
    
    /**
     * the same as the similarly named javax.net.SocketFactory operation.
     */
    public static RemoteDeliverySocketFactory getDefault() {
        return new RemoteDeliverySocketFactory();
    }
    
    /**
     * the same as the similarly named javax.net.SocketFactory operation.
     * Just to be safe, it is not used by JavaMail 1.3.
     */
    public Socket createSocket() throws IOException {
        throw new IOException("Incompatible JavaMail version, " +
                "cannot bound socket");
    }
    
    /**
     * the same as the similarly named javax.net.SocketFactory operation.
     * This is the one which is used by JavaMail 1.3.
     */
    public Socket createSocket(String host, int port)
                            throws IOException, UnknownHostException {
        return new Socket(host, port, bindAddress, 0);
    }
    
    /**
     * the same as the similarly named javax.net.SocketFactory operation.
     * Just to be safe, it is not used by JavaMail 1.3.
     */
    public Socket createSocket(String host,
                                    int port,
                                    InetAddress clientHost,
                                    int clientPort)
                                    throws IOException,
                                    UnknownHostException {
        return new Socket(host, port, 
                clientHost == null ? bindAddress : clientHost, clientPort);
    }
    
    /**
     * the same as the similarly named javax.net.SocketFactory operation.
     * Just to be safe, it is not used by JavaMail 1.3.
     */
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return new Socket(host, port, bindAddress, 0);
    }
    
    /**
     * the same as the similarly named javax.net.SocketFactory operation.
     * Just to be safe, it is not used by JavaMail 1.3.
     */
    public Socket createSocket(InetAddress address,
                                    int port,
                                    InetAddress clientAddress,
                                    int clientPort)
                             throws IOException {
        return new Socket(address, port, 
                clientAddress == null ? bindAddress : clientAddress, 
                clientPort);
    }
    
    /**
     * it should be set by setBindAdress(). Null means the socket is bind to 
     * the default address.
     */
    private static InetAddress bindAddress;
}
