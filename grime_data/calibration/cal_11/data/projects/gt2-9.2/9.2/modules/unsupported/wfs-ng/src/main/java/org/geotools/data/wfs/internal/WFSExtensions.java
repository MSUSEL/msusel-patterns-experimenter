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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.spi.ServiceRegistry;

import org.geotools.data.wfs.impl.WFSDataStoreFactory;
import org.geotools.factory.FactoryNotFoundException;

/**
 * Utility class to look up for a parser that can deal with a given WFS response and process it.
 * <p>
 * This class uses the usual GeoTools SPI (Service Provider Interface) mechanism to find out a
 * {@link WFSResponseFactory} for a given {@link WFSResponse}. As such, {@link WFSResponseFactory}
 * implementation may live outside this plugin as long as they're declared in it's own {code
 * /META-INF/services/org.geotools.data.wfs.protocol.wfs.WFSResponseParserFactory} text file.
 * </p>
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id$
 * @since 2.6
 * 
 * 
 * 
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/wfs/src/main/java/org/geotools
 *         /data/wfs/protocol/wfs/WFSExtensions.java $
 */
@SuppressWarnings("nls")
public class WFSExtensions {
    /**
     * The service registry for this manager. Will be initialized only when first needed.
     */
    private static Set<WFSResponseFactory> registry;

    /**
     * Processes the result of a WFS operation and returns the parsed object.
     * <p>
     * The result can either be:
     * <ul>
     * <li>a {@link WFSException} exception if the WFS response was an exception report
     * <li>a {@link GetFeatureParser} if the WFS returned a FeatureCollection
     * </p>
     * 
     * @param request
     *            the WFS request that originated the given response
     * @param response
     *            the handle to the WFS response contents
     * @return
     * @throws IOException
     */
    // public static Object process(WFSResponse response) throws IOException {
    //
    // WFSRequest originatingRequest = response.getOriginatingRequest();
    // WFSResponseFactory pf = findParserFactory(originatingRequest);
    //
    // WFSResponseParser parser = pf.createParser(response);
    //
    // Object result = parser.parse(response);
    // return result;
    // }

    /**
     * @param contentType
     * @param requestType
     * @param outputFormat
     * @return
     * @throws FactoryNotFoundException
     */
    public static WFSResponseFactory findResponseFactory(final WFSRequest originatingRequest,
            final String contentType) {

        Iterator<WFSResponseFactory> serviceProviders;
        serviceProviders = getServiceProviders();

        WFSResponseFactory factory;
        while (serviceProviders.hasNext()) {
            factory = serviceProviders.next();
            if (factory.isAvailable()) {
                if (factory.canProcess(originatingRequest, contentType)) {
                    return factory;
                }
            }
        }
        throw new FactoryNotFoundException("Can't find a response parser factory for "
                + originatingRequest.getOperation() + "/'" + contentType + "'");
    }

    public static List<WFSResponseFactory> findResponseFactories(final WFSOperationType operation) {

        Iterator<WFSResponseFactory> serviceProviders = getServiceProviders();

        List<WFSResponseFactory> matches = new ArrayList<WFSResponseFactory>(5);

        while (serviceProviders.hasNext()) {
            WFSResponseFactory factory = serviceProviders.next();
            if (factory.isAvailable()) {
                if (factory.canProcess(operation)) {
                    matches.add(factory);
                }
            }
        }

        return matches;
    }

    private static Iterator<WFSResponseFactory> getServiceProviders() {
        if (registry == null) {
            synchronized (WFSExtensions.class) {
                if (registry == null) {
                    /*
                     * Set the current thread's class loader to the one that actually loaded the
                     * WDSDataStore and related classes for while the factory lookup is performed.
                     * This way the module is friendlier to crazy class loader hierarchies like
                     * OSGI/Eclipse
                     */
                    final ClassLoader current = Thread.currentThread().getContextClassLoader();
                    try {
                        final ClassLoader tempClassLoader = WFSDataStoreFactory.class
                                .getClassLoader();
                        Thread.currentThread().setContextClassLoader(tempClassLoader);
                        /*
                         * Now that we're on the correct classloader lets perform the lookup
                         */
                        Iterator<WFSResponseFactory> providers;
                        providers = ServiceRegistry.lookupProviders(WFSResponseFactory.class);
                        registry = new HashSet<WFSResponseFactory>();
                        while (providers.hasNext()) {
                            WFSResponseFactory provider = providers.next();
                            registry.add(provider);
                        }
                    } finally {
                        /*
                         * And finally restore the original thread's class loader
                         */
                        Thread.currentThread().setContextClassLoader(current);
                    }
                }
            }
        }
        return registry.iterator();
    }

}
