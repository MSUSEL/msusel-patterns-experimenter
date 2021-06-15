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
package org.hibernate.test.cache.infinispan.tm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.Stoppable;
import org.hibernate.testing.env.ConnectionProviderBuilder;

/**
 * XaConnectionProvider.
 *
 * @author Galder Zamarreño
 * @since 3.5
 */
public class XaConnectionProvider implements ConnectionProvider {
	private static ConnectionProvider actualConnectionProvider = ConnectionProviderBuilder.buildConnectionProvider();
	private boolean isTransactional;

	public static ConnectionProvider getActualConnectionProvider() {
		return actualConnectionProvider;
	}

	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return XaConnectionProvider.class.isAssignableFrom( unwrapType ) ||
				ConnectionProvider.class.equals( unwrapType ) ||
				actualConnectionProvider.getClass().isAssignableFrom( unwrapType );
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public <T> T unwrap(Class<T> unwrapType) {
		if ( XaConnectionProvider.class.isAssignableFrom( unwrapType ) ) {
			return (T) this;
		}
		else if ( ConnectionProvider.class.isAssignableFrom( unwrapType ) ||
				actualConnectionProvider.getClass().isAssignableFrom( unwrapType ) ) {
			return (T) getActualConnectionProvider();
		}
		else {
			throw new UnknownUnwrapTypeException( unwrapType );
		}
	}

	public void configure(Properties props) throws HibernateException {
	}

	public Connection getConnection() throws SQLException {
		XaTransactionImpl currentTransaction = XaTransactionManagerImpl.getInstance().getCurrentTransaction();
		if ( currentTransaction == null ) {
			isTransactional = false;
			return actualConnectionProvider.getConnection();
		}
		else {
			isTransactional = true;
			Connection connection = currentTransaction.getEnlistedConnection();
			if ( connection == null ) {
				connection = actualConnectionProvider.getConnection();
				currentTransaction.enlistConnection( connection );
			}
			return connection;
		}
	}

	public void closeConnection(Connection conn) throws SQLException {
		if ( !isTransactional ) {
			conn.close();
		}
	}

	public void close() throws HibernateException {
		if ( actualConnectionProvider instanceof Stoppable ) {
			((Stoppable) actualConnectionProvider).stop();
		}
	}

	public boolean supportsAggressiveRelease() {
		return true;
	}
}
