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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.Context;
import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.service.config.spi.ConfigurationService;
import org.hibernate.service.jndi.spi.JndiService;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.service.spi.Stoppable;

/**
 * A concrete implementation of the {@link MultiTenantConnectionProvider} contract bases on a number of
 * reasonable assumptions.  We assume that:<ul>
 *     <li>
 *         The {@link DataSource} instances are all available from JNDI named by the tenant identifier relative
 *         to a single base JNDI context
 *     </li>
 *     <li>
 *         {@link org.hibernate.cfg.AvailableSettings#DATASOURCE} is a string naming either the {@literal any}
 *         data source or the base JNDI context.  If the latter, {@link #TENANT_IDENTIFIER_TO_USE_FOR_ANY_KEY} must
 *         also be set.
 *     </li>
 * </ul>
 *
 * @author Steve Ebersole
 */
public class DataSourceBasedMultiTenantConnectionProviderImpl
		extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl
		implements ServiceRegistryAwareService, Stoppable {

	public static final String TENANT_IDENTIFIER_TO_USE_FOR_ANY_KEY = "hibernate.multi_tenant.datasource.identifier_for_any";

	private Map<String,DataSource> dataSourceMap;
	private JndiService jndiService;
	private String tenantIdentifierForAny;
	private String baseJndiNamespace;

	@Override
	protected DataSource selectAnyDataSource() {
		return selectDataSource( tenantIdentifierForAny );
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		DataSource dataSource = dataSourceMap().get( tenantIdentifier );
		if ( dataSource == null ) {
			dataSource = (DataSource) jndiService.locate( baseJndiNamespace + '/' + tenantIdentifier );
			dataSourceMap().put( tenantIdentifier, dataSource );
		}
		return dataSource;
	}

	private Map<String,DataSource> dataSourceMap() {
		if ( dataSourceMap == null ) {
			dataSourceMap = new ConcurrentHashMap<String, DataSource>();
		}
		return dataSourceMap;
	}

	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {
		final Object dataSourceConfigValue = serviceRegistry.getService( ConfigurationService.class )
				.getSettings()
				.get( AvailableSettings.DATASOURCE );
		if ( dataSourceConfigValue == null || ! String.class.isInstance( dataSourceConfigValue ) ) {
			throw new HibernateException( "Improper set up of DataSourceBasedMultiTenantConnectionProviderImpl" );
		}
		final String jndiName = (String) dataSourceConfigValue;

		jndiService = serviceRegistry.getService( JndiService.class );
		if ( jndiService == null ) {
			throw new HibernateException( "Could not locate JndiService from DataSourceBasedMultiTenantConnectionProviderImpl" );
		}

		Object namedObject = jndiService.locate( jndiName );
		if ( namedObject == null ) {
			throw new HibernateException( "JNDI name [" + jndiName + "] could not be resolved" );
		}

		if ( DataSource.class.isInstance( namedObject ) ) {
			int loc = jndiName.lastIndexOf( "/" );
			this.baseJndiNamespace = jndiName.substring( 0, loc );
			this.tenantIdentifierForAny = jndiName.substring( loc + 1 );
			dataSourceMap().put( tenantIdentifierForAny, (DataSource) namedObject );
		}
		else if ( Context.class.isInstance( namedObject ) ) {
			this.baseJndiNamespace = jndiName;
			this.tenantIdentifierForAny = (String) serviceRegistry.getService( ConfigurationService.class )
					.getSettings()
					.get( TENANT_IDENTIFIER_TO_USE_FOR_ANY_KEY );
			if ( tenantIdentifierForAny == null ) {
				throw new HibernateException( "JNDI name named a Context, but tenant identifier to use for ANY was not specified" );
			}
		}
		else {
			throw new HibernateException(
					"Unknown object type [" + namedObject.getClass().getName() +
							"] found in JNDI location [" + jndiName + "]"
			);
		}
	}

	@Override
	public void stop() {
		if ( dataSourceMap != null ) {
			dataSourceMap.clear();
			dataSourceMap = null;
		}
	}
}
