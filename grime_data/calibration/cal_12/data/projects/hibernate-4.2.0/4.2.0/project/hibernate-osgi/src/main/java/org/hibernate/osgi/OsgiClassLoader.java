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
package org.hibernate.osgi;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;

/**
 * Custom OSGI ClassLoader helper which knows all the "interesting" bundles and
 * encapsulates the OSGi related capabilities.
 * 
 * @author Brett Meyer
 */
public class OsgiClassLoader extends ClassLoader {

	private Map<String, CachedBundle> bundles = new HashMap<String, CachedBundle>();
	
	private Map<String, Class<?>> classCache = new HashMap<String, Class<?>>();
	
	private Map<String, URL> resourceCache = new HashMap<String, URL>();
	
	private Map<String, Enumeration<URL>> resourceListCache = new HashMap<String, Enumeration<URL>>();

	/**
	 * Load the class and break on first found match.
	 * TODO: Should this throw a different exception or warn if multiple
	 * classes were found? Naming collisions can and do happen in OSGi...
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if ( classCache.containsKey( name ) ) {
			return classCache.get( name );
		}
		
		for ( CachedBundle bundle : bundles.values() ) {
			try {
				Class clazz = bundle.loadClass( name );
				if ( clazz != null ) {
					classCache.put( name, clazz );
					return clazz;
				}
			}
			catch ( Exception ignore ) {
			}
		}

		throw new ClassNotFoundException( "Could not load requested class : " + name );
	}

	/**
	 * Load the class and break on first found match.
	 * TODO: Should this throw a different exception or warn if multiple
	 * classes were found? Naming collisions can and do happen in OSGi...
	 */
	@Override
	protected URL findResource(String name) {
		if ( resourceCache.containsKey( name ) ) {
			return resourceCache.get( name );
		}
		
		for ( CachedBundle bundle : bundles.values() ) {
			try {
				URL resource = bundle.getResource( name );
				if ( resource != null ) {
					resourceCache.put( name, resource );
					return resource;
				}
			}
			catch ( Exception ignore ) {
			}
		}
		// TODO: Error?
		return null;
	}

	/**
	 * Load the class and break on first found match.
	 * TODO: Should this throw a different exception or warn if multiple
	 * classes were found? Naming collisions can and do happen in OSGi...
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Enumeration<URL> findResources(String name) {
		if ( resourceListCache.containsKey( name ) ) {
			return resourceListCache.get( name );
		}
		
		for ( CachedBundle bundle : bundles.values() ) {
			try {
				Enumeration<URL> resources = bundle.getResources( name );
				if ( resources != null ) {
					resourceListCache.put( name, resources );
					return resources;
				}
			}
			catch ( Exception ignore ) {
			}
		}
		// TODO: Error?
		return null;
	}

	/**
	 * Register the bundle with this class loader
	 */
	public void registerBundle(Bundle bundle) {
		if ( bundle != null ) {
			synchronized ( bundles ) {
				String key = getBundleKey( bundle );
				if ( !bundles.containsKey( key ) ) {
					bundles.put( key, new CachedBundle( bundle, key ) );
				}
			}
		}
	}

	/**
	 * Unregister the bundle from this class loader
	 */
	public void unregisterBundle(Bundle bundle) {
		if ( bundle != null ) {
			synchronized ( bundles ) {
				String key = getBundleKey( bundle );
				if ( bundles.containsKey( key ) ) {
					CachedBundle cachedBundle = bundles.remove( key );
					clearCache( classCache, cachedBundle.getClassNames() );
					clearCache( resourceCache, cachedBundle.getResourceNames() );
					clearCache( resourceListCache, cachedBundle.getResourceListNames() );
				}
			}
		}
	}
	
	private void clearCache( Map cache, List<String> names ) {
		for ( String name : names ) {
			cache.remove( name );
		}
	}
	
	public void clear() {
		bundles.clear();
		classCache.clear();
		resourceCache.clear();
		resourceListCache.clear();
	}

	protected static String getBundleKey(Bundle bundle) {
		return bundle.getSymbolicName() + " " + bundle.getVersion().toString();
	}

}
