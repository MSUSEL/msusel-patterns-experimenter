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
package org.hibernate.service.config.internal;

import java.util.Collections;
import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.classloading.spi.ClassLoadingException;
import org.hibernate.service.config.spi.ConfigurationService;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * @author Steve Ebersole
 */
public class ConfigurationServiceImpl implements ConfigurationService, ServiceRegistryAwareService {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			ConfigurationServiceImpl.class.getName()
	);
	private final Map settings;
	private ServiceRegistryImplementor serviceRegistry;

	@SuppressWarnings( "unchecked" )
	public ConfigurationServiceImpl(Map settings) {
		this.settings = Collections.unmodifiableMap( settings );
	}

	@Override
	public Map getSettings() {
		return settings;
	}

	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	public <T> T getSetting(String name, Converter<T> converter) {
		return getSetting( name, converter, null );
	}

	@Override
	public <T> T getSetting(String name, Converter<T> converter, T defaultValue) {
		final Object value = settings.get( name );
		if ( value == null ) {
			return defaultValue;
		}

		return converter.convert( value );
	}
	@Override
	public <T> T getSetting(String name, Class<T> expected, T defaultValue) {
		Object value = settings.get( name );
		T target = cast( expected, value );
		return target !=null ? target : defaultValue;
	}
	@Override
	public <T> T cast(Class<T> expected, Object candidate){
		if(candidate == null) return null;
		if ( expected.isInstance( candidate ) ) {
			return (T) candidate;
		}
		Class<T> target;
		if ( Class.class.isInstance( candidate ) ) {
			target = Class.class.cast( candidate );
		}
		else {
			try {
				target = serviceRegistry.getService( ClassLoaderService.class ).classForName( candidate.toString() );
			}
			catch ( ClassLoadingException e ) {
				LOG.debugf( "Unable to locate %s implementation class %s", expected.getName(), candidate.toString() );
				target = null;
			}
		}
		if ( target != null ) {
			try {
				return target.newInstance();
			}
			catch ( Exception e ) {
				LOG.debugf(
						"Unable to instantiate %s class %s", expected.getName(),
						target.getName()
				);
			}
		}
		return null;
	}


}
