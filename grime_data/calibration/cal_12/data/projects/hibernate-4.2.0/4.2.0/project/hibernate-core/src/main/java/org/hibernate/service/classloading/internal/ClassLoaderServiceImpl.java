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
package org.hibernate.service.classloading.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.internal.util.ClassLoaderHelper;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.classloading.spi.ClassLoadingException;
import org.jboss.logging.Logger;

/**
 * Standard implementation of the service for interacting with class loaders
 *
 * @author Steve Ebersole
 */
public class ClassLoaderServiceImpl implements ClassLoaderService {
	private static final Logger log = Logger.getLogger( ClassLoaderServiceImpl.class );

	private final ClassLoader aggregatedClassLoader;

	public ClassLoaderServiceImpl() {
		this( ClassLoaderServiceImpl.class.getClassLoader() );
	}

	public ClassLoaderServiceImpl(ClassLoader classLoader) {
		this( Collections.singletonList( classLoader ) );
	}

	public ClassLoaderServiceImpl(List<ClassLoader> providedClassLoaders) {
		final LinkedHashSet<ClassLoader> orderedClassLoaderSet = new LinkedHashSet<ClassLoader>();

		// first add all provided class loaders, if any
		if ( providedClassLoaders != null ) {
			for ( ClassLoader classLoader : providedClassLoaders ) {
				if ( classLoader != null ) {
					orderedClassLoaderSet.add( classLoader );
				}
			}
		}

		// normalize adding known class-loaders...
		// first, the "overridden" classloader provided by an environment (OSGi, etc.)
		if ( ClassLoaderHelper.overridenClassLoader != null ) {
			orderedClassLoaderSet.add( ClassLoaderHelper.overridenClassLoader );
		}
		// then the Hibernate class loader
		orderedClassLoaderSet.add( ClassLoaderServiceImpl.class.getClassLoader() );
		// then the TCCL, if one...
		final ClassLoader tccl = locateTCCL();
		if ( tccl != null ) {
			orderedClassLoaderSet.add( tccl );
		}
		// finally the system classloader
		final ClassLoader sysClassLoader = locateSystemClassLoader();
		if ( sysClassLoader != null ) {
			orderedClassLoaderSet.add( sysClassLoader );
		}

		// now build the aggregated class loader...
		this.aggregatedClassLoader = new AggregatedClassLoader( orderedClassLoaderSet );
	}

	@SuppressWarnings({"UnusedDeclaration", "unchecked", "deprecation"})
	@Deprecated
	public static ClassLoaderServiceImpl fromConfigSettings(Map configVales) {
		final List<ClassLoader> providedClassLoaders = new ArrayList<ClassLoader>();

		final Collection<ClassLoader> classLoaders = (Collection<ClassLoader>) configVales.get( AvailableSettings.CLASSLOADERS );
		if ( classLoaders != null ) {
			for ( ClassLoader classLoader : classLoaders ) {
				providedClassLoaders.add( classLoader );
			}
		}

		addIfSet( providedClassLoaders, AvailableSettings.APP_CLASSLOADER, configVales );
		addIfSet( providedClassLoaders, AvailableSettings.RESOURCES_CLASSLOADER, configVales );
		addIfSet( providedClassLoaders, AvailableSettings.HIBERNATE_CLASSLOADER, configVales );
		addIfSet( providedClassLoaders, AvailableSettings.ENVIRONMENT_CLASSLOADER, configVales );

		return new ClassLoaderServiceImpl( providedClassLoaders );
	}

	private static void addIfSet(List<ClassLoader> providedClassLoaders, String name, Map configVales) {
		final ClassLoader providedClassLoader = (ClassLoader) configVales.get( name );
		if ( providedClassLoader != null ) {
			providedClassLoaders.add( providedClassLoader );
		}
	}
	private static ClassLoader locateSystemClassLoader() {
		try {
			return ClassLoader.getSystemClassLoader();
		}
		catch ( Exception e ) {
			return null;
		}
	}

	private static ClassLoader locateTCCL() {
		try {
			return Thread.currentThread().getContextClassLoader();
		}
		catch ( Exception e ) {
			return null;
		}
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public <T> Class<T> classForName(String className) {
		try {
			return (Class<T>) Class.forName( className, true, aggregatedClassLoader );
		}
		catch (Exception e) {
			throw new ClassLoadingException( "Unable to load class [" + className + "]", e );
		}
	}

	@Override
	public URL locateResource(String name) {
		// first we try name as a URL
		try {
			return new URL( name );
		}
		catch ( Exception ignore ) {
		}

		try {
			return aggregatedClassLoader.getResource( name );
		}
		catch ( Exception ignore ) {
		}

		return null;
	}

	@Override
	public InputStream locateResourceStream(String name) {
		// first we try name as a URL
		try {
			log.tracef( "trying via [new URL(\"%s\")]", name );
			return new URL( name ).openStream();
		}
		catch ( Exception ignore ) {
		}

		try {
			log.tracef( "trying via [ClassLoader.getResourceAsStream(\"%s\")]", name );
			InputStream stream =  aggregatedClassLoader.getResourceAsStream( name );
			if ( stream != null ) {
				return stream;
			}
		}
		catch ( Exception ignore ) {
		}

		final String stripped = name.startsWith( "/" ) ? name.substring(1) : null;

		if ( stripped != null ) {
			try {
				log.tracef( "trying via [new URL(\"%s\")]", stripped );
				return new URL( stripped ).openStream();
			}
			catch ( Exception ignore ) {
			}

			try {
				log.tracef( "trying via [ClassLoader.getResourceAsStream(\"%s\")]", stripped );
				InputStream stream = aggregatedClassLoader.getResourceAsStream( stripped );
				if ( stream != null ) {
					return stream;
				}
			}
			catch ( Exception ignore ) {
			}
		}

		return null;
	}

	@Override
	public List<URL> locateResources(String name) {
		ArrayList<URL> urls = new ArrayList<URL>();
		try {
			Enumeration<URL> urlEnumeration = aggregatedClassLoader.getResources( name );
			if ( urlEnumeration != null && urlEnumeration.hasMoreElements() ) {
				while ( urlEnumeration.hasMoreElements() ) {
					urls.add( urlEnumeration.nextElement() );
				}
			}
		}
		catch ( Exception ignore ) {
		}

		return urls;
	}

	@Override
	public <S> LinkedHashSet<S> loadJavaServices(Class<S> serviceContract) {
		final ServiceLoader<S> loader = ServiceLoader.load( serviceContract, aggregatedClassLoader );
		final LinkedHashSet<S> services = new LinkedHashSet<S>();
		for ( S service : loader ) {
			services.add( service );
		}

		return services;
	}
	private static class AggregatedClassLoader extends ClassLoader {
		private final ClassLoader[] individualClassLoaders;

		private AggregatedClassLoader(final LinkedHashSet<ClassLoader> orderedClassLoaderSet) {
			super( null );
			individualClassLoaders = orderedClassLoaderSet.toArray( new ClassLoader[ orderedClassLoaderSet.size() ] );
		}

		@Override
		public Enumeration<URL> getResources(String name) throws IOException {
			final HashSet<URL> resourceUrls = new HashSet<URL>();

			for ( ClassLoader classLoader : individualClassLoaders ) {
				final Enumeration<URL> urls = classLoader.getResources( name );
				while ( urls.hasMoreElements() ) {
					resourceUrls.add( urls.nextElement() );
				}
			}

			return new Enumeration<URL>() {
				final Iterator<URL> resourceUrlIterator = resourceUrls.iterator();
				@Override
				public boolean hasMoreElements() {
					return resourceUrlIterator.hasNext();
				}

				@Override
				public URL nextElement() {
					return resourceUrlIterator.next();
				}
			};
		}

		@Override
		protected URL findResource(String name) {
			for ( ClassLoader classLoader : individualClassLoaders ) {
				final URL resource = classLoader.getResource( name );
				if ( resource != null ) {
					return resource;
				}
			}
			return super.findResource( name );
		}

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			for ( ClassLoader classLoader : individualClassLoaders ) {
				try {
					return classLoader.loadClass( name );
				}
				catch (Exception ignore) {
				}
			}

			throw new ClassNotFoundException( "Could not load requested class : " + name );
		}
	}

}
