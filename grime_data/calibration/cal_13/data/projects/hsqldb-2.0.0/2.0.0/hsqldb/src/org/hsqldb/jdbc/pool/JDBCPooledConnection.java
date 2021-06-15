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

import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;

import java.sql.Connection;
import java.sql.SQLException;

//#ifdef JAVA6
import javax.sql.StatementEventListener;

//#endif JAVA6
// boucherb@users 20051207 - patch 1.8.0.x initial JDBC 4.0 support work

/**
 * @author Jakob Jenkov
 */
public class JDBCPooledConnection implements PooledConnection {

    private LifeTimeConnectionWrapper connectionWrapper = null;

    public JDBCPooledConnection(LifeTimeConnectionWrapper connectionWrapper) {
        this.connectionWrapper = connectionWrapper;
    }

    public void close() throws SQLException {

        connectionWrapper.closePhysically();

        this.connectionWrapper = null;
    }

    public Connection getConnection() throws SQLException {
        return this.connectionWrapper;
    }

    public void addConnectionEventListener(ConnectionEventListener listener) {
        this.connectionWrapper.addConnectionEventListener(listener);
    }

    public void removeConnectionEventListener(
            ConnectionEventListener listener) {
        this.connectionWrapper.removeConnectionEventListener(listener);
    }

/** @todo - implement methods */

//#ifdef JAVA6
    public void addStatementEventListener(StatementEventListener listener) {}

    public void removeStatementEventListener(
            StatementEventListener listener) {}

//#endif JAVA6
}
