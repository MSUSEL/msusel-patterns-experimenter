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
package org.hibernate.engine.jdbc.spi;
import java.sql.Connection;

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.JDBCException;

/**
 * The "internal" contract for LogicalConnection
 *
 * @author Steve Ebersole
 * @author Brett Meyer
 */
public interface LogicalConnectionImplementor extends LogicalConnection {
	/**
	 * Obtains the JDBC services associated with this logical connection.
	 *
	 * @return JDBC services
	 */
	public JdbcServices getJdbcServices();

	/**
	 * Add an observer interested in notification of connection events.
	 *
	 * @param observer The observer.
	 */
	public void addObserver(ConnectionObserver observer);

	/**
	 * Remove an observer
	 *
	 * @param connectionObserver The observer to remove.
	 */
	public void removeObserver(ConnectionObserver connectionObserver);

	/**
	 * The release mode under which this logical connection is operating.
	 *
	 * @return the release mode.
	 */
	public ConnectionReleaseMode getConnectionReleaseMode();

	/**
	 * Manually disconnect the underlying JDBC Connection.  The assumption here
	 * is that the manager will be reconnected at a later point in time.
	 *
	 * @return The connection maintained here at time of disconnect.  Null if
	 * there was no connection cached internally.
	 */
	public Connection manualDisconnect();

	/**
	 * Manually reconnect the underlying JDBC Connection.  Should be called at some point after manualDisconnect().
	 *
	 * @param suppliedConnection For user supplied connection strategy the user needs to hand us the connection
	 * with which to reconnect.  It is an error to pass a connection in the other strategies.
	 */
	public void manualReconnect(Connection suppliedConnection);
	
	public void aggressiveRelease();
	
	public void releaseConnection() throws JDBCException;

	public boolean isAutoCommit();

	public void notifyObserversStatementPrepared();
	
	public boolean isUserSuppliedConnection();
}
