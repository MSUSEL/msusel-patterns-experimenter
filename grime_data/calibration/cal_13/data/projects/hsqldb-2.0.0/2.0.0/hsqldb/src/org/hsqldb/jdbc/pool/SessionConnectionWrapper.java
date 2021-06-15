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

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Wraps a connection from the connection pool during a session. A session lasts from the
 * connection is taken from the pool and until it is closed and returned to the pool. Each
 * time a connection is taken from the pool it is wrapped in a new SessionConnectionWrapper instance.
 * When the connection is closed the SessionConnectionWrapper is invalidated and thus that reference
 * to the connection is invalidated. There can only be one valid SessionConnectionWrapper
 * at any time per underlying connection.
 *
 * <br/><br/>
 * The SessionConnectionWrapper makes it possible to invalidate connection references independently
 * of each other, even if the different references point to the same underlying connection. This is useful
 * if a thread by accident keeps a reference to the connection after it is closed. If the thread
 * tries to call any methods on the connection the SessionConnectionWrapper will throw an SQLException.
 * Other threads, or even the same thread, that takes the connection from the pool subsequently will
 * get a new SessionConnectionWrapper. Therefore their reference to the connection is valid
 * even if earlier references have been invalidated.
 *
 * <br/><br/>
 * The SessionConnectionWrapper is also useful when a connection pool is to reclaim abandoned
 * connections (connections that by accident have not been closed). After having been out of
 * the pool and inactive for a certain time (set by the user),
 * the pool can decide that the connection must be abandoned. The pool will then close the
 * SessionConnectionWrapper and return the underlying connection to the pool.
 * If the thread that abandoned the connection tries to call any
 * methods on the SessionConnectionWrapper it will get an SQLException. The underlying connection
 * is still valid for the next thread that takes it from the pool.
 *
 *
 * @author Jakob Jenkov
 */
public class SessionConnectionWrapper extends BaseConnectionWrapper {

    protected long       latestActivityTime = 0;
    protected Connection connection         = null;

    public SessionConnectionWrapper(Connection connection) {

        this.connection = connection;

        updateLatestActivityTime();
    }

    protected Connection getConnection() {
        return this.connection;
    }

    public synchronized void updateLatestActivityTime() {
        this.latestActivityTime = System.currentTimeMillis();
    }

    public synchronized long getLatestActivityTime() {
        return latestActivityTime;
    }

    public void close() throws SQLException {

        this.isClosed = true;

        Connection temp = this.connection;

        this.connection = null;

        temp.close();
    }
}
