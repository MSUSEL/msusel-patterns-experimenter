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

import javax.sql.XADataSource;

import java.sql.SQLException;

import org.hsqldb.lib.HashMap;
import org.hsqldb.lib.HashSet;
import org.hsqldb.lib.Iterator;

import javax.transaction.xa.Xid;
import javax.sql.PooledConnection;

import org.hsqldb.jdbc.JDBCConnection;

import java.sql.DriverManager;

import javax.sql.XAConnection;

// @(#)$Id: JDBCXADataSource.java 3495 2010-03-06 14:20:44Z fredt $

/**
 * Connection factory for JDBCXAConnections.
 * For use by XA data source factories, not by end users.
 *
 * @since HSQLDB v. 1.9.0
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 * @see javax.sql.XADataSource
 * @see org.hsqldb.jdbc.pool.JDBCXAConnection
 */
public class JDBCXADataSource extends JDBCConnectionPoolDataSource implements XADataSource {

    /** @todo:  Break off code used here and in JDBCConnectionPoolDataSource
     *        into an abstract class, and have these classes extend the
     *        abstract class.  This class should NOT extend
     *        JDBCConnectionPoolDataSource (notice the masked
     *        pool-specific methods below).
     */
    private HashMap resources = new HashMap();

    public void addResource(Xid xid, JDBCXAResource xaResource) {
        resources.put(xid, xaResource);
    }

    public JDBCXAResource removeResource(Xid xid) {
        return (JDBCXAResource) resources.remove(xid);
    }

    /**
     * Return the list of transactions currently <I>in prepared or
     * heuristically completed states</I>.
     * Need to find out what non-prepared states they are considering
     * <I>heuristically completed</I>.
     *
     * @see javax.transaction.xa.XAResource#recover(int)
     */
    Xid[] getPreparedXids() {

        Iterator it = resources.keySet().iterator();
        Xid      curXid;
        HashSet  preparedSet = new HashSet();

        while (it.hasNext()) {
            curXid = (Xid) it.next();

            if (((JDBCXAResource) resources.get(curXid)).state
                    == JDBCXAResource.XA_STATE_PREPARED) {
                preparedSet.add(curXid);
            }
        }

        return (Xid[]) preparedSet.toArray(new Xid[0]);
    }

    /**
     * This is needed so that XAResource.commit() and
     * XAResource.rollback() may be applied to the right Connection
     * (which is not necessarily that associated with that XAResource
     * object).
     *
     * @see javax.transaction.xa.XAResource#commit(Xid, boolean)
     * @see javax.transaction.xa.XAResource#rollback(Xid)
     */
    JDBCXAResource getResource(Xid xid) {
        return (JDBCXAResource) resources.get(xid);
    }

    /**
     * Get new PHYSICAL connection, to be managed by a connection manager.
     */
    public XAConnection getXAConnection() throws SQLException {

        // Comment out before public release:
/*
        System.err.print("Executing " + getClass().getName()
                         + ".getXAConnection()...");
*/
        try {
            Class.forName(driver).newInstance();
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error opening connection: "
                                   + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new SQLException("Error opening connection: "
                                   + e.getMessage());
        } catch (InstantiationException e) {
            throw new SQLException("Error opening connection: "
                                   + e.getMessage());
        }

        JDBCConnection connection =
            (JDBCConnection) DriverManager.getConnection(url, connProperties);

        // Comment out before public release:
/*
        System.err.print("New phys:  " + connection);
*/
        JDBCXAResource xaResource = new JDBCXAResource(connection, this);
        JDBCXAConnectionWrapper xaWrapper =
            new JDBCXAConnectionWrapper(connection, xaResource,
                                        connectionDefaults);
        JDBCXAConnection xaConnection = new JDBCXAConnection(xaWrapper,
            xaResource);

        xaWrapper.setPooledConnection(xaConnection);

        return xaConnection;
    }

    /**
     * Gets a new physical connection after validating the given username
     * and password.
     *
     * @param user String which must match the 'user' configured for this
     *             JDBCXADataSource.
     * @param password  String which must match the 'password' configured
     *                  for this JDBCXADataSource.
     *
     * @see #getXAConnection()
     */
    public XAConnection getXAConnection(String user,
                                        String password) throws SQLException {

        validateSpecifiedUserAndPassword(user, password);

        return getXAConnection();
    }

    public PooledConnection getPooledConnection() throws SQLException {

        throw new SQLException(
            "Use the getXAConnections to get XA Connections.\n"
            + "Use the class JDBCConnectionPoolDataSource for non-XA data sources.");
    }

    public PooledConnection getPooledConnection(String user,
            String password) throws SQLException {

        throw new SQLException(
            "Use the getXAConnections to get XA Connections.\n"
            + "Use the class JDBCConnectionPoolDataSource for non-XA data sources.");
    }
}
