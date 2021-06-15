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

package org.apache.james.services;


import org.apache.avalon.cornerstone.services.connection.ConnectionManager;
import java.net.ServerSocket;
import org.apache.avalon.excalibur.thread.ThreadPool;
import org.apache.avalon.cornerstone.services.connection.ConnectionHandlerFactory;

/**
 * This interface extends the standard ConnectionManager interface to allow
 * connectionLimits to be specified on a per service basis
 **/
public interface JamesConnectionManager extends ConnectionManager
{
    /**
     * The component role used by components implementing this service
     */
    String ROLE = "org.apache.james.services.JamesConnectionManager";

    /**
     * Returns the default maximum number of open connections supported by this
     * SimpleConnectionManager
     *
     * @return the maximum number of connections
     */
    int getMaximumNumberOfOpenConnections();

    /**
     * Start managing a connection.
     * Management involves accepting connections and farming them out to threads
     * from pool to be handled.
     *
     * @param name the name of connection
     * @param socket the ServerSocket from which to
     * @param handlerFactory the factory from which to acquire handlers
     * @param threadPool the thread pool to use
     * @param maxOpenConnections the maximum number of open connections allowed for this server socket.
     * @exception Exception if an error occurs
     */
    void connect( String name,
                  ServerSocket socket,
                  ConnectionHandlerFactory handlerFactory,
                  ThreadPool threadPool,
                  int maxOpenConnections )
        throws Exception;
    
    /**
     * Start managing a connection.
     * This is similar to other connect method except that it uses default thread pool.
     *
     * @param name the name of connection
     * @param socket the ServerSocket from which to
     * @param handlerFactory the factory from which to acquire handlers
     * @param maxOpenConnections the maximum number of open connections allowed for this server socket.
     * @exception Exception if an error occurs
     */
    void connect( String name,
                  ServerSocket socket,
                  ConnectionHandlerFactory handlerFactory,
                  int maxOpenConnections )
        throws Exception;

}
