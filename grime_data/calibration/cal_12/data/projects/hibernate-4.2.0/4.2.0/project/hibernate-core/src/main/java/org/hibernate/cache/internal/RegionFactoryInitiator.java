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
package org.hibernate.cache.internal;

import java.util.Map;

import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.spi.BasicServiceInitiator;
import org.hibernate.service.spi.ServiceException;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * Initiator for the {@link RegionFactory} service.
 *
 * @author Hardy Ferentschik
 */
public class RegionFactoryInitiator implements BasicServiceInitiator<RegionFactory> {
	public static final RegionFactoryInitiator INSTANCE = new RegionFactoryInitiator();

	/**
	 * Property name to use to configure the full qualified class name for the {@code RegionFactory}
	 */
	public static final String IMPL_NAME = "hibernate.cache.region.factory_class";

	@Override
	public Class<RegionFactory> getServiceInitiated() {
		return RegionFactory.class;
	}

	@Override
	@SuppressWarnings( { "unchecked" })
	public RegionFactory initiateService(Map configurationValues, ServiceRegistryImplementor registry) {
		final Object impl = configurationValues.get( IMPL_NAME );
		if ( impl == null ) {
			return new NoCachingRegionFactory();
		}

		if ( getServiceInitiated().isInstance( impl ) ) {
			return (RegionFactory) impl;
		}

		Class<? extends RegionFactory> customImplClass = null;
		if ( Class.class.isInstance( impl ) ) {
			customImplClass = (Class<? extends RegionFactory>) impl;
		}
		else {
			customImplClass = registry.getService( ClassLoaderService.class )
					.classForName( mapLegacyNames( impl.toString() ) );
		}

		try {
			return customImplClass.newInstance();
		}
		catch ( Exception e ) {
			throw new ServiceException(
					"Could not initialize custom RegionFactory impl [" + customImplClass.getName() + "]", e
			);
		}
	}

	// todo this shouldn't be public (nor really static):
	// hack for org.hibernate.cfg.SettingsFactory.createRegionFactory()
	public static String mapLegacyNames(final String name) {
		if ( "org.hibernate.cache.EhCacheRegionFactory".equals( name ) ) {
			return "org.hibernate.cache.ehcache.EhCacheRegionFactory";
		}

		if ( "org.hibernate.cache.SingletonEhCacheRegionFactory".equals( name ) ) {
			return "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory";
		}

		return name;
	}
}
