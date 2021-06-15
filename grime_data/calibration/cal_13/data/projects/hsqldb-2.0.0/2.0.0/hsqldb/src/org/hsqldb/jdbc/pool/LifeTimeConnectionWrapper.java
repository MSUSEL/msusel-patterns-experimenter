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

package org.hsqldb.jdbc.pool;

import org.hsqldb.jdbc.JDBCConnection;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

// boucherb@users 20051207 - patch 1.8.0.x initial JDBC 4.0 support work

/**
 * A simple wrapper around a regular <code>java.sql.Connection</code>.
 * Redirects all calls to the encapsulated connection except <code>close()</code>.
 * Calling <code>close()</code> on this wrapper marks the wrappers as closed,
 * and puts the encapsulated connection back in the queue of idle connections.
 *
 * <br>
 * This version doesn't wrap Statement, PreparedStatement, CallableStatement
 * and ResultSet instances. In order to behave 100% correctly it probably should.
 * That way this wrapper can close all of these instances when the wrapper is closed,
 * before the connection is given back to the pool. Normally the wrapped connection
 * would do this, but since it's never closed... someone has to do it.
 * <br>
 * The connection.reset() call is to reset the JDBC Connection,
 * the Statement and ResultSet objects. It also resets the session settings
 * to its initial state.
 *
 *
 * @author Jakob Jenkov
 */
public class LifeTimeConnectionWrapper extends BaseConnectionWrapper {

    protected JDBCConnection     connection          = null;
    protected PooledConnection   pooledConnection    = null;
    protected Set                connectionListeners = new HashSet();
    protected ConnectionDefaults connectionDefaults  = null;

    public LifeTimeConnectionWrapper(
            JDBCConnection connection,
            ConnectionDefaults connectionDefaults) throws SQLException {

        this.connection = connection;

        if (connectionDefaults != null) {
            this.connectionDefaults = connectionDefaults;

            this.connectionDefaults.setDefaults(connection);
        } else {
            this.connectionDefaults = new ConnectionDefaults(connection);
        }
    }

    public LifeTimeConnectionWrapper(
            JDBCConnection connection) throws SQLException {
        this(connection, null);
    }

    public void setPooledConnection(JDBCPooledConnection pooledConnection) {
        this.pooledConnection = pooledConnection;
    }

    public void addConnectionEventListener(ConnectionEventListener listener) {
        connectionListeners.add(listener);
    }

    public void removeConnectionEventListener(
            ConnectionEventListener listener) {
        connectionListeners.remove(listener);
    }

    protected void finalize() throws Throwable {
        closePhysically();
    }

    protected Connection getConnection() {
        return this.connection;
    }

    /**
     * Rolls the connection back, resets the connection back to defaults, clears warnings,
     * resets the connection on the server side, and returns the connection to the pool.
     * @throws SQLException
     */
    public void close() throws SQLException {

        validate();

        try {
            this.connection.rollback();
            this.connection.clearWarnings();
            this.connection.reset();
            this.connectionDefaults.setDefaults(this.connection);
            fireCloseEvent();
        } catch (SQLException e) {
            fireSqlExceptionEvent(e);

            throw e;
        }
    }

    /**
     * Closes the connection physically. The pool is not notified of this.
     * @throws SQLException If something goes wrong during the closing of the wrapped JDBCConnection.
     */
    public void closePhysically() throws SQLException {

        SQLException exception = null;

        if (!isClosed && this.connection != null
                && !this.connection.isClosed()) {
            try {
                this.connection.close();
            } catch (SQLException e) {

                //catch and hold so that the rest of the finalizer is run too. Throw at the end if present.
                exception = e;
            }
        }
        this.isClosed           = true;
        this.pooledConnection   = null;
        this.connection         = null;
        this.connectionDefaults = null;

        this.connectionListeners.clear();

        this.connectionListeners = null;

        if (exception != null) {
            throw exception;
        }
    }

    protected void fireSqlExceptionEvent(SQLException e) {

        ConnectionEvent event = new ConnectionEvent(this.pooledConnection, e);

        for (Iterator iterator = connectionListeners.iterator();
                iterator.hasNext(); ) {
            ConnectionEventListener connectionEventListener =
                (ConnectionEventListener) iterator.next();

            connectionEventListener.connectionErrorOccurred(event);
        }
    }

    protected void fireCloseEvent() {

        ConnectionEvent connectionEvent =
            new ConnectionEvent(this.pooledConnection);

        for (Iterator iterator = connectionListeners.iterator();
                iterator.hasNext(); ) {
            ConnectionEventListener connectionListener =
                (ConnectionEventListener) iterator.next();

            connectionListener.connectionClosed(connectionEvent);
        }
    }
}
