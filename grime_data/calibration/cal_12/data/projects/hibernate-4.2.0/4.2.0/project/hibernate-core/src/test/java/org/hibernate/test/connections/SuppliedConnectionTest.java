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
package org.hibernate.test.connections;

import java.sql.Connection;
import java.sql.ResultSet;

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.service.jdbc.connections.internal.UserSuppliedConnectionProviderImpl;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.Stoppable;
import org.hibernate.testing.AfterClassOnce;
import org.hibernate.testing.BeforeClassOnce;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.env.ConnectionProviderBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * Implementation of SuppliedConnectionTest.
 *
 * @author Steve Ebersole
 */
@RequiresDialect(H2Dialect.class)
public class SuppliedConnectionTest extends ConnectionManagementTestCase {
	private ConnectionProvider cp = ConnectionProviderBuilder.buildConnectionProvider();
	private Connection connectionUnderTest;

	@BeforeClassOnce
	private void prepareConnectionProvider() {
		cp = ConnectionProviderBuilder.buildConnectionProvider();
	}

	@AfterClassOnce
	private void releaseConnectionProvider() {
		try {
			if ( cp instanceof Stoppable ) {
					( ( Stoppable ) cp ).stop();
			}
			cp = null;
		}
		catch( Throwable ignore ) {
		}
	}

	@Override
	protected Session getSessionUnderTest() throws Throwable {
		connectionUnderTest = cp.getConnection();
		return sessionFactory().withOptions().connection( connectionUnderTest ).openSession();
	}

	@Override
	protected void reconnect(Session session) {
		session.reconnect( connectionUnderTest );
	}

	@Override
	protected void done() throws Throwable {
		cp.closeConnection( connectionUnderTest );
	}

	@Override
	public void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.RELEASE_CONNECTIONS, ConnectionReleaseMode.ON_CLOSE.toString() );
		cfg.setProperty( Environment.CONNECTION_PROVIDER, UserSuppliedConnectionProviderImpl.class.getName() );
		boolean supportsScroll = true;
		Connection conn = null;
		try {
			conn = cp.getConnection();
			supportsScroll = conn.getMetaData().supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		}
		catch( Throwable ignore ) {
		}
		finally {
			if ( conn != null ) {
				try {
					conn.close();
				}
				catch( Throwable ignore ) {
					// ignore it...
				}
			}
		}
		cfg.setProperty( Environment.USE_SCROLLABLE_RESULTSET, "" + supportsScroll );
	}

	@Override
	public boolean createSchema() {
		return false;
	}

	@Override
	public boolean rebuildSessionFactoryOnError() {
		return false;
	}

	@Override
	protected void prepareTest() throws Exception {
		super.prepareTest();
		Connection conn = cp.getConnection();
		try {
			new SchemaExport( configuration(), conn ).create( false, true );
		}
		finally {
			if ( conn != null ) {
				try {
					cp.closeConnection( conn );
				}
				catch( Throwable ignore ) {
				}
			}
		}
	}

	@Override
	protected void cleanupTest() throws Exception {
		Connection conn = cp.getConnection();
		try {
			new SchemaExport( configuration(), conn ).drop( false, true );
		}
		finally {
			if ( conn != null ) {
				try {
					cp.closeConnection( conn );
				}
				catch( Throwable ignore ) {
				}
			}
		}
		super.cleanupTest();
	}
}
