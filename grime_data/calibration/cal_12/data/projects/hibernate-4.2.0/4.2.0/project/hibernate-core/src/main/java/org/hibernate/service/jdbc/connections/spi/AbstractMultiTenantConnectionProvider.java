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
package org.hibernate.service.jdbc.connections.spi;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.service.UnknownUnwrapTypeException;

/**
 * Basic support for {@link MultiTenantConnectionProvider} implementations using
 * individual {@link ConnectionProvider} instances per tenant behind the scenes.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractMultiTenantConnectionProvider implements MultiTenantConnectionProvider {
	protected abstract ConnectionProvider getAnyConnectionProvider();
	protected abstract ConnectionProvider selectConnectionProvider(String tenantIdentifier);

	@Override
	public Connection getAnyConnection() throws SQLException {
		return getAnyConnectionProvider().getConnection();
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		getAnyConnectionProvider().closeConnection( connection );
	}

	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
		return selectConnectionProvider( tenantIdentifier ).getConnection();
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		selectConnectionProvider( tenantIdentifier ).closeConnection( connection );
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return getAnyConnectionProvider().supportsAggressiveRelease();
	}

	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return ConnectionProvider.class.equals( unwrapType ) ||
				MultiTenantConnectionProvider.class.equals( unwrapType ) ||
				AbstractMultiTenantConnectionProvider.class.isAssignableFrom( unwrapType );
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public <T> T unwrap(Class<T> unwrapType) {
		if ( isUnwrappableAs( unwrapType ) ) {
			return (T) this;
		}
		else {
			throw new UnknownUnwrapTypeException( unwrapType );
		}
	}
}
