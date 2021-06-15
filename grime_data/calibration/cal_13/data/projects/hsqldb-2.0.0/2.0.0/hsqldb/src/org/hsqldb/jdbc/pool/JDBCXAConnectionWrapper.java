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

import java.sql.SQLException;
import java.sql.Savepoint;

// @(#)$Id: JDBCXAConnectionWrapper.java 3481 2010-02-26 18:05:06Z fredt $

/**
 * This is a wrapper class for JDBCConnection objects (not XAConnection
 * object).
 * Purpose of this class is to intercept and handle XA-related operations
 * according to chapter 12 of the JDBC 3.0 specification, by returning this
 * wrapped JDBCConnection to end-users.
 * Global transaction services and XAResources will not use this wrapper.
 * It also supports pooling, by virtue of the parent class,
 * LifeTimeConnectionWrapper.
 * <P>
 * This class name would be very precise (the class being a wrapper for
 * XA-capable JDBC Connections), except that a "XAConnection" is an entirely
 * different thing from a JDBC java.sql.Connection.
 * I can think of no way to eliminate the ambiguity without using an
 * 80-character class name.
 * Best I think I can do is to clearly state here that
 * <b>This is a wrapper for XA-capable java.sql.Connections, not for
 *    javax.sql.XAConnection.</b>
 *
 * @since HSQLDB v. 1.9.0
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 * @see org.hsqldb.jdbc.JDBCConnection
 * @see org.hsqldb.jdbc.pool.LifeTimeConnectionWrapper
 */
public class JDBCXAConnectionWrapper extends LifeTimeConnectionWrapper {

    /*
     * A critical question:  One responsibility of this
     * class is to intercept invocations of commit(), rollback(),
     * savePoint methods, etc.  But, what about if user issues the
     * corresponding SQL commands?  What is the point to intercepting
     * Connection.commit() here if end-users can execute the SQL command
     * "COMMIT" and bypass interception?
     * Similarly, what about DDL commands that cause an explicit commit?
     *                                                - blaine
     */
    private JDBCXAResource xaResource;

    public JDBCXAConnectionWrapper(
            JDBCConnection connection, JDBCXAResource xaResource,
            ConnectionDefaults connectionDefaults) throws SQLException {

        /* Could pass in the creating XAConnection, which has methods to
         * get the connection and the xaResource, but this way cuts down
         * on the inter-dependencies a bit. */
        super(connection, connectionDefaults);

        this.xaResource = xaResource;
    }

    /**
     * Throws a SQLException if within a Global transaction.
     *
     * @throws SQLException if within a Global transaction.
     */
    private void validateNotWithinTransaction() throws SQLException {

        if (xaResource.withinGlobalTransaction()) {
            throw new SQLException(
                "Method prohibited within a global transaction");
        }
    }

    /**
     * Interceptor method, because this method is prohibited within
     * any global transaction.
     * See section 1.2.4 of the JDBC 3.0 spec.
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        validateNotWithinTransaction();
        super.setAutoCommit(autoCommit);
    }

    /**
     * Interceptor method, because this method is prohibited within
     * any global transaction.
     * See section 1.2.4 of the JDBC 3.0 spec.
     */
    public void commit() throws SQLException {
        validateNotWithinTransaction();
        super.commit();
    }

    /**
     * Interceptor method, because this method is prohibited within
     * any global transaction.
     * See section 1.2.4 of the JDBC 3.0 spec.
     */
    public void rollback() throws SQLException {
        validateNotWithinTransaction();
        super.rollback();
    }

    /**
     * Interceptor method, because this method is prohibited within
     * any global transaction.
     * See section 1.2.4 of the JDBC 3.0 spec.
     */
    public void rollback(Savepoint savepoint) throws SQLException {
        validateNotWithinTransaction();
        super.rollback(savepoint);
    }

    /**
     * Interceptor method, because this method is prohibited within
     * any global transaction.
     * See section 1.2.4 of the JDBC 3.0 spec.
     */
    public Savepoint setSavepoint() throws SQLException {

        validateNotWithinTransaction();

        return super.setSavepoint();
    }

    /**
     * Interceptor method, because this method is prohibited within
     * any global transaction.
     * See section 1.2.4 of the JDBC 3.0 spec.
     */
    public Savepoint setSavepoint(String name) throws SQLException {

        validateNotWithinTransaction();

        return super.setSavepoint(name);
    }

    /**
     * Interceptor method, because there may be XA implications to
     * calling the method within a global transaction.
     * See section 1.2.4 of the JDBC 3.0 spec.
     */
    public void setTransactionIsolation(int level) throws SQLException {

        /* Goal at this time is to get a working XA DataSource.
         * After we have multiple transaction levels working, we can
         * consider how we want to handle attempts to change the level
         * within a global transaction. */
        validateNotWithinTransaction();
        super.setTransactionIsolation(level);
    }
}
