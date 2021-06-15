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

import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.classloading.spi.ClassLoadingException;
import org.hibernate.service.jdbc.connections.spi.DataSourceBasedMultiTenantConnectionProviderImpl;
import org.hibernate.service.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.BasicServiceInitiator;
import org.hibernate.service.spi.ServiceException;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * @author Steve Ebersole
 */
public class MultiTenantConnectionProviderInitiator implements BasicServiceInitiator<MultiTenantConnectionProvider> {
	public static final MultiTenantConnectionProviderInitiator INSTANCE = new MultiTenantConnectionProviderInitiator();
	private static final Logger log = Logger.getLogger( MultiTenantConnectionProviderInitiator.class );

	@Override
	public Class<MultiTenantConnectionProvider> getServiceInitiated() {
		return MultiTenantConnectionProvider.class;
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public MultiTenantConnectionProvider initiateService(Map configurationValues, ServiceRegistryImplementor registry) {
		final MultiTenancyStrategy strategy = MultiTenancyStrategy.determineMultiTenancyStrategy(  configurationValues );
		if ( strategy == MultiTenancyStrategy.NONE || strategy == MultiTenancyStrategy.DISCRIMINATOR ) {
			// nothing to do, but given the separate hierarchies have to handle this here.
		}

		final Object configValue = configurationValues.get( AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER );
		if ( configValue == null ) {
			// if they also specified the data source *name*, then lets assume they want
			// org.hibernate.service.jdbc.connections.spi.DataSourceBasedMultiTenantConnectionProviderImpl
			final Object dataSourceConfigValue = configurationValues.get( AvailableSettings.DATASOURCE );
			if ( dataSourceConfigValue != null && String.class.isInstance( dataSourceConfigValue ) ) {
				return new DataSourceBasedMultiTenantConnectionProviderImpl();
			}

			return null;
		}

		if ( MultiTenantConnectionProvider.class.isInstance( configValue ) ) {
			return (MultiTenantConnectionProvider) configValue;
		}
		else {
			final Class<MultiTenantConnectionProvider> implClass;
			if ( Class.class.isInstance( configValue ) ) {
				implClass = (Class) configValue;
			}
			else {
				final String className = configValue.toString();
				final ClassLoaderService classLoaderService = registry.getService( ClassLoaderService.class );
				try {
					implClass = classLoaderService.classForName( className );
				}
				catch (ClassLoadingException cle) {
					log.warn( "Unable to locate specified class [" + className + "]", cle );
					throw new ServiceException( "Unable to locate specified multi-tenant connection provider [" + className + "]" );
				}
			}

			try {
				return implClass.newInstance();
			}
			catch (Exception e) {
				log.warn( "Unable to instantiate specified class [" + implClass.getName() + "]", e );
				throw new ServiceException( "Unable to instantiate specified multi-tenant connection provider [" + implClass.getName() + "]" );
			}
		}
	}
}
