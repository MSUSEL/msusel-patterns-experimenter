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
package org.hibernate.testing.env;

import java.util.Properties;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl;

/**
 * Defines the JDBC connection information (currently H2) used by Hibernate for unit (not functional!) tests
 *
 * @author Steve Ebersole
 */
public class ConnectionProviderBuilder {
	public static final String DRIVER = "org.h2.Driver";
	public static final String URL = "jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;MVCC=TRUE";
	public static final String USER = "sa";
	public static final String PASS = "";

	public static Properties getConnectionProviderProperties(String dbName) {
		Properties props = new Properties( null );
		props.put( Environment.DRIVER, DRIVER );
		props.put( Environment.URL, String.format( URL, dbName ) );
		props.put( Environment.USER, USER );
		props.put( Environment.PASS, PASS );
		return props;
	}

	public static Properties getConnectionProviderProperties() {
		return getConnectionProviderProperties( "db1" );
	}

	public static DriverManagerConnectionProviderImpl buildConnectionProvider() {
		return buildConnectionProvider( false );
	}

	public static DriverManagerConnectionProviderImpl buildConnectionProvider(String dbName) {
		return buildConnectionProvider( getConnectionProviderProperties( dbName ), false );
	}

	public static DriverManagerConnectionProviderImpl buildConnectionProvider(final boolean allowAggressiveRelease) {
		return buildConnectionProvider( getConnectionProviderProperties( "db1" ), allowAggressiveRelease );
	}

	private static DriverManagerConnectionProviderImpl buildConnectionProvider(Properties props, final boolean allowAggressiveRelease) {
		DriverManagerConnectionProviderImpl connectionProvider = new DriverManagerConnectionProviderImpl() {
			public boolean supportsAggressiveRelease() {
				return allowAggressiveRelease;
			}
		};
		connectionProvider.configure( props );
		return connectionProvider;
	}

	public static Dialect getCorrespondingDialect() {
		return new H2Dialect();
	}
}
