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
package org.hibernate.service.jdbc.connections.internal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.jndi.spi.JndiService;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.InjectService;
import org.hibernate.service.spi.Stoppable;

/**
 * A {@link ConnectionProvider} that manages connections from an underlying {@link DataSource}.
 * <p/>
 * The {@link DataSource} to use may be specified by either:<ul>
 * <li>injection via {@link #setDataSource}</li>
 * <li>decaring the {@link DataSource} instance using the {@link Environment#DATASOURCE} config property</li>
 * <li>decaring the JNDI name under which the {@link DataSource} can be found via {@link Environment#DATASOURCE} config property</li>
 * </ul>
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class DatasourceConnectionProviderImpl implements ConnectionProvider, Configurable, Stoppable {

	private DataSource dataSource;
	private String user;
	private String pass;
	private boolean useCredentials;
	private JndiService jndiService;

	private boolean available;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@InjectService( required = false )
	public void setJndiService(JndiService jndiService) {
		this.jndiService = jndiService;
	}

	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return ConnectionProvider.class.equals( unwrapType ) ||
				DatasourceConnectionProviderImpl.class.isAssignableFrom( unwrapType ) ||
				DataSource.class.isAssignableFrom( unwrapType );
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public <T> T unwrap(Class<T> unwrapType) {
		if ( ConnectionProvider.class.equals( unwrapType ) ||
				DatasourceConnectionProviderImpl.class.isAssignableFrom( unwrapType ) ) {
			return (T) this;
		}
		else if ( DataSource.class.isAssignableFrom( unwrapType ) ) {
			return (T) getDataSource();
		}
		else {
			throw new UnknownUnwrapTypeException( unwrapType );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void configure(Map configValues) {
		if ( this.dataSource == null ) {
			final Object dataSource = configValues.get( Environment.DATASOURCE );
			if ( DataSource.class.isInstance( dataSource ) ) {
				this.dataSource = (DataSource) dataSource;
			}
			else {
				final String dataSourceJndiName = (String) dataSource;
				if ( dataSourceJndiName == null ) {
					throw new HibernateException(
							"DataSource to use was not injected nor specified by [" + Environment.DATASOURCE
									+ "] configuration property"
					);
				}
				if ( jndiService == null ) {
					throw new HibernateException( "Unable to locate JndiService to lookup Datasource" );
				}
				this.dataSource = (DataSource) jndiService.locate( dataSourceJndiName );
			}
		}
		if ( this.dataSource == null ) {
			throw new HibernateException( "Unable to determine appropriate DataSource to use" );
		}

		user = (String) configValues.get( Environment.USER );
		pass = (String) configValues.get( Environment.PASS );
		useCredentials = user != null || pass != null;
		available = true;
	}

	public void stop() {
		available = false;
		dataSource = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Connection getConnection() throws SQLException {
		if ( !available ) {
			throw new HibernateException( "Provider is closed!" );
		}
		return useCredentials ? dataSource.getConnection( user, pass ) : dataSource.getConnection();
	}

	/**
	 * {@inheritDoc}
	 */
	public void closeConnection(Connection connection) throws SQLException {
		connection.close();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsAggressiveRelease() {
		return true;
	}
}
