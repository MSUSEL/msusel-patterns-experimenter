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
package org.hibernate.testing.jta;

import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.cfg.Environment;
import org.hibernate.engine.transaction.internal.jta.JtaStatusHelper;
import org.hibernate.service.jdbc.connections.internal.ConnectionProviderInitiator;
import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Stoppable;

/**
 * A {@link DataSource} implementation intended for testing Hibernate/JTA interaction.  In that limited scope we
 * only ever have one single resource (the database connection) so we do not at all care about full-blown XA
 * semantics.  This class behaves accordingly.  This class also assumes usage of and access to JBossTS/Arjuna.
 *
 * @author Steve Ebersole
 * @author Jonathan Halliday
 */
public class JtaAwareConnectionProviderImpl implements ConnectionProvider, Configurable, Stoppable {
	private static final String CONNECTION_KEY = "_database_connection";

	private DriverManagerConnectionProviderImpl delegate;

	private List<Connection> nonEnlistedConnections = new ArrayList<Connection>();

	@Override
	public void configure(Map configurationValues) {
		Properties connectionSettings = new Properties();
		transferSetting( Environment.DRIVER, configurationValues, connectionSettings );
		transferSetting( Environment.URL, configurationValues, connectionSettings );
		transferSetting( Environment.USER, configurationValues, connectionSettings );
		transferSetting( Environment.PASS, configurationValues, connectionSettings );
		transferSetting( Environment.ISOLATION, configurationValues, connectionSettings );
		Properties passThroughSettings = ConnectionProviderInitiator.getConnectionProperties( configurationValues );
		if ( passThroughSettings != null ) {
			for ( String setting : passThroughSettings.stringPropertyNames() ) {
				transferSetting( Environment.CONNECTION_PREFIX + '.' + setting, configurationValues, connectionSettings );
			}
		}
		connectionSettings.setProperty( Environment.AUTOCOMMIT, "false" );

		delegate = new DriverManagerConnectionProviderImpl();
		delegate.configure( connectionSettings );
	}

	@SuppressWarnings("unchecked")
	private static void transferSetting(String settingName, Map source, Map target) {
		Object value = source.get( settingName );
		if ( value != null ) {
			target.put( settingName, value );
		}
	}

	@Override
	public void stop() {
		delegate.stop();
	}

	@Override
	public Connection getConnection() throws SQLException {
		Transaction currentTransaction = findCurrentTransaction();

		try {
			if ( currentTransaction == null ) {
				// this block handles non enlisted connections ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				Connection connection = delegate.getConnection();
				nonEnlistedConnections.add( connection );
				return connection;
			}

			// this portion handles enlisted connections ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			Connection connection = (Connection) TestingJtaPlatformImpl.synchronizationRegistry().getResource(
					CONNECTION_KEY
			);
			if ( connection == null ) {
				connection = delegate.getConnection();
				TestingJtaPlatformImpl.synchronizationRegistry().putResource( CONNECTION_KEY, connection );

				XAResourceWrapper xaResourceWrapper = new XAResourceWrapper( this, connection );
				currentTransaction.enlistResource( xaResourceWrapper );
			}
			return connection;
		}
		catch (SQLException e) {
			throw e;
		}
		catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public void closeConnection(Connection conn) throws SQLException {
		if ( conn == null ) {
			return;
		}

		if ( nonEnlistedConnections.contains( conn ) ) {
			nonEnlistedConnections.remove( conn );
			delegate.closeConnection( conn );
		}
		else {
			// do nothing.  part of the enlistment contract here is that the XAResource wrapper
			// takes that responsibility.
		}
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return true;
	}

	protected Transaction findCurrentTransaction() {
		try {
			return TestingJtaPlatformImpl.transactionManager().getTransaction();
		}
		catch (SystemException e) {
			throw new IllegalStateException( "Could not locate current transaction" );
		}
	}

	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return delegate.isUnwrappableAs( unwrapType );
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		return delegate.unwrap( unwrapType );
	}

	private void delist(Connection connection) {
		// todo : verify the incoming connection is the currently enlisted one?
		TestingJtaPlatformImpl.synchronizationRegistry().putResource( CONNECTION_KEY, null );
		try {
			delegate.closeConnection( connection );
		}
		catch (SQLException e) {
			System.err.println( "!!!Error trying to close JDBC connection from delist callbacks!!!" );
		}
	}

	public static class XAResourceWrapper implements XAResource {
		private final JtaAwareConnectionProviderImpl pool;
		private final Connection connection;
		private int transactionTimeout;

		public XAResourceWrapper(JtaAwareConnectionProviderImpl pool, Connection connection) {
			this.pool = pool;
			this.connection = connection;
		}

		@Override
		public int prepare(Xid xid) throws XAException {
			throw new RuntimeException("this should never be called");
		}

		@Override
		public void commit(Xid xid, boolean onePhase) throws XAException {
			if (!onePhase) {
				throw new IllegalArgumentException( "must be one phase" );
			}

			try {
				connection.commit();
			}
			catch(SQLException e) {
				throw new XAException( e.toString() );
			}
			finally {
				try {
					pool.delist( connection );
				}
				catch (Exception ignore) {
				}
			}
		}

		@Override
		public void rollback(Xid xid) throws XAException {

			try {
				connection.rollback();
			}
			catch(SQLException e) {
				throw new XAException( e.toString() );
			}
			finally {
				try {
					pool.delist( connection );
				}
				catch (Exception ignore) {
				}
			}
		}

		@Override
		public void end(Xid xid, int i) throws XAException {
			// noop
		}

		@Override
		public void start(Xid xid, int i) throws XAException {
			// noop
		}


		@Override
		public void forget(Xid xid) throws XAException {
			// noop
		}

		@Override
		public int getTransactionTimeout() throws XAException {
			return transactionTimeout;
		}

		@Override
		public boolean setTransactionTimeout(int i) throws XAException {
			transactionTimeout = i;
			return true;
		}

		@Override
		public boolean isSameRM(XAResource xaResource) throws XAException {
			return xaResource != null && xaResource == this;
		}

		@Override
		public Xid[] recover(int i) throws XAException {
			return new Xid[0];
		}
	}
}
