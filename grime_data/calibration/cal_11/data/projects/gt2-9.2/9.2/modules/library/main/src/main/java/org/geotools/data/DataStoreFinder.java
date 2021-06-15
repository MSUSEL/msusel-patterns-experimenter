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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;


/**
 * Enable programs to find all available datastore implementations.
 *
 * <p>
 * In order to be located by this finder datasources must provide an
 * implementation of the {@link DataStoreFactorySpi} interface.
 * </p>
 *
 * <p>
 * In addition to implementing this interface datasouces should have a services
 * file:<br/><code>META-INF/services/org.geotools.data.DataStoreFactorySpi</code>
 * </p>
 *
 * <p>
 * The file should contain a single line which gives the full name of the
 * implementing class.
 * </p>
 *
 * <p>
 * Example:<br/><code>org.geotools.data.mytype.MyTypeDataStoreFacotry</code>
 * </p>
 *
 *
 * @source $URL$
 */
public final class DataStoreFinder {
	/** The logger for the filter module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data");

	/**
	 * The service registry for this manager. Will be initialized only when
	 * first needed.
	 */
	private static FactoryRegistry registry;

	//Singleton pattern
    private DataStoreFinder() {
    }

    /**
     * Checks each available datasource implementation in turn and returns the
     * first one which claims to support the resource identified by the params
     * object.
     *
     * @param params A Map object which contains a defenition of the resource
     *        to connect to. for file based resources the property 'url'
     *        should be set within this Map.
     *
     * @return The first datasource which claims to process the required
     *         resource, returns null if none can be found.
     *
     * @throws IOException If a suitable loader can be found, but it can not be
     *         attached to the specified resource without errors.
     */
    public static synchronized DataStore getDataStore(Map params) throws IOException {
        Iterator<DataStoreFactorySpi> ps = getAvailableDataStores();
        DataAccess<? extends FeatureType, ? extends Feature> dataStore;
        dataStore = DataAccessFinder.getDataStore(params, ps);
        return (DataStore) dataStore;
    }

    /**
     * Finds all implemtaions of DataStoreFactory which have registered using
     * the services mechanism, regardless weather it has the appropriate
     * libraries on the classpath.
     *
     * @return An iterator over all discovered datastores which have registered
     *         factories
     */
    public static synchronized Iterator<DataStoreFactorySpi> getAllDataStores() {
        return DataAccessFinder.getAllDataStores(getServiceRegistry(), DataStoreFactorySpi.class);
    }

    /**
     * Finds all implemtaions of DataStoreFactory which have registered using
     * the services mechanism, and that have the appropriate libraries on the
     * classpath.
     *
     * @return An iterator over all discovered datastores which have registered
     *         factories, and whose available method returns true.
     */
    public static synchronized Iterator<DataStoreFactorySpi> getAvailableDataStores() {
        Set<DataStoreFactorySpi> availableDS;
        FactoryRegistry serviceRegistry = getServiceRegistry();
        availableDS = DataAccessFinder.getAvailableDataStores(serviceRegistry, DataStoreFactorySpi.class);
        return availableDS.iterator();
    }

	/**
	 * Returns the service registry. The registry will be created the first time
	 * this method is invoked.
	 */
	private static FactoryRegistry getServiceRegistry() {
		assert Thread.holdsLock(DataStoreFinder.class);
		if (registry == null) {
			registry = new FactoryCreator(Arrays
					.asList(new Class<?>[] { DataStoreFactorySpi.class }));
		}
		return registry;
	}

	/**
	 * Scans for factory plug-ins on the application class path. This method is
	 * needed because the application class path can theoretically change, or
	 * additional plug-ins may become available. Rather than re-scanning the
	 * classpath on every invocation of the API, the class path is scanned
	 * automatically only on the first invocation. Clients can call this method
	 * to prompt a re-scan. Thus this method need only be invoked by
	 * sophisticated applications which dynamically make new plug-ins available
	 * at runtime.
	 */
	public static synchronized void scanForPlugins() {

		getServiceRegistry().scanForPlugins();

	}
	
	/**
	     * Resets the factory finder and prepares for a new full scan of the SPI subsystems
	     */
	    public static void reset() {
	        FactoryRegistry copy = registry;
	        registry = null;
	        if(copy != null) {
	            copy.deregisterAll();
	        }
	    }
    
}
