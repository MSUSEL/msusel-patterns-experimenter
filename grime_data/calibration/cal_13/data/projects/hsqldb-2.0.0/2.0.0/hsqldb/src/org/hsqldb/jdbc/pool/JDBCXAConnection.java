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

import javax.sql.XAConnection;

import java.sql.SQLException;

import javax.transaction.xa.XAResource;

// @(#)$Id: JDBCXAConnection.java 3481 2010-02-26 18:05:06Z fredt $

/**
 * HSQLDB Connection objects for use by a global transaction service manager.
 *
 * Other than the xaResource, this class deals with Connection Wrappers
 * not the real thing, but these correspond 1:1 with physical
 * JDBCConnections.
 *
 * @since HSQLDB v. 1.9.0
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 * @see javax.sql.XAConnection
 */
public class JDBCXAConnection extends JDBCPooledConnection implements XAConnection {

    JDBCXAResource xaResource;

    public JDBCXAConnection(JDBCXAConnectionWrapper xaConnectionWrapper,
                            JDBCXAResource xaResource) {

        super(xaConnectionWrapper);

        this.xaResource = xaResource;
    }

    public XAResource getXAResource() throws SQLException {
        return xaResource;
    }
}
