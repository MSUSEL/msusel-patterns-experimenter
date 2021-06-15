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
package org.hibernate.ejb;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.LoadState;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;

import org.hibernate.ejb.util.PersistenceUtilHelper;

/**
 * Hibernate EJB3 persistence provider implementation
 *
 * @author Gavin King
 */
public class HibernatePersistence extends AvailableSettings implements PersistenceProvider {
	
	private final PersistenceUtilHelper.MetadataCache cache = new PersistenceUtilHelper.MetadataCache();
	
	/**
	 * Used for environment-supplied properties.  Ex: hibernate-osgi's
	 * HibernateBundleActivator needs to set a custom JtaPlatform.
	 */
	private Map environmentProperties;
	
	public void setEnvironmentProperties( Map environmentProperties ) {
		this.environmentProperties = environmentProperties;
	}
	
	/**
	 * Get an entity manager factory by its entity manager name, using the specified
	 * properties (they override any found in the peristence.xml file).
	 * <p/>
	 * This is the form used in JSE environments.
	 *
	 * @param persistenceUnitName entity manager name
	 * @param properties The explicit property values
	 *
	 * @return initialized EntityManagerFactory
	 */
	public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
		if ( environmentProperties != null ) {
			properties.putAll( environmentProperties );
		}
		Ejb3Configuration cfg = new Ejb3Configuration();
		Ejb3Configuration configured = cfg.configure( persistenceUnitName, properties );
		return configured != null ? configured.buildEntityManagerFactory() : null;
	}

	/**
	 * Create an entity manager factory from the given persistence unit info, using the specified
	 * properties (they override any on the PUI).
	 * <p/>
	 * This is the form used by the container in a JEE environment.
	 *
	 * @param info The persistence unit information
	 * @param properties The explicit property values
	 *
	 * @return initialized EntityManagerFactory
	 */
	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {
		if ( environmentProperties != null ) {
			properties.putAll( environmentProperties );
		}
		Ejb3Configuration cfg = new Ejb3Configuration();
		Ejb3Configuration configured = cfg.configure( info, properties );
		return configured != null ? configured.buildEntityManagerFactory() : null;
	}

	/**
	 * create a factory from a canonical version
	 * @deprecated
	 */
	@Deprecated
    public EntityManagerFactory createEntityManagerFactory(Map properties) {
		// This is used directly by JBoss so don't remove until further notice.  bill@jboss.org
		Ejb3Configuration cfg = new Ejb3Configuration();
		return cfg.createEntityManagerFactory( properties );
	}

	private final ProviderUtil providerUtil = new ProviderUtil() {
		public LoadState isLoadedWithoutReference(Object proxy, String property) {
			return PersistenceUtilHelper.isLoadedWithoutReference( proxy, property, cache );
		}

		public LoadState isLoadedWithReference(Object proxy, String property) {
			return PersistenceUtilHelper.isLoadedWithReference( proxy, property, cache );
		}

		public LoadState isLoaded(Object o) {
			return PersistenceUtilHelper.isLoaded(o);
		}
	};

	/**
	 * {@inheritDoc}
	 */
	public ProviderUtil getProviderUtil() {
		return providerUtil;
	}

}