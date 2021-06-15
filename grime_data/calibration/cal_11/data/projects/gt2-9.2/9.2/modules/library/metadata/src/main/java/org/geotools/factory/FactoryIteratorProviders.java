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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.factory;

import java.util.Iterator;
import java.util.Set;
import java.util.LinkedHashSet;
import org.geotools.resources.XArray;


/**
 * The list of registered {@linkplain FactoryIteratorProvider factory iterator providers}.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @todo Consider removing {@link FactoryRegistry#globalConfiguration} and use listeners instead.
 */
final class FactoryIteratorProviders {
    /**
     * The system-wide configuration. This is the instance configured by
     * the public static methods provided in this class.
     */
    private static final FactoryIteratorProviders GLOBAL = new FactoryIteratorProviders();

    /**
     * Incremented every time a modification is performed.
     */
    private int modifications = 0;

    /**
     * Alternative scanning methods used by {@link FactoryRegistry#scanForPlugins(Collection,Class)}
     * in addition of the default lookup mechanism. Will be created only when first needed.
     */
    private Set<FactoryIteratorProvider> iteratorProviders;

    /**
     * Creates an initially empty set of factories.
     */
    FactoryIteratorProviders() {
    }

    /**
     * Synchronizes the content of the {@link #iteratorProviders} map with the {@linkplain #GLOBAL
     * global} one. New providers are returned for later {@linkplain FactoryRegistry#register
     * registration}. Note that this method is typically invoked in a different thread than
     * {@link FactoryIteratorProviders} public static method calls.
     *
     * @return The new iterators providers {@linkplain #addFactoryIteratorProvider added} since
     *         the last time this method was invoked, or {@code null} if none.
     */
    final FactoryIteratorProvider[] synchronizeIteratorProviders() {
        FactoryIteratorProvider[] newProviders = null;
        int count = 0;
        synchronized (GLOBAL) {
            if (modifications == GLOBAL.modifications) {
                return null;
            }
            modifications = GLOBAL.modifications;
            if (GLOBAL.iteratorProviders == null) {
                /*
                 * Should never happen. If GLOBAL.iteratorProviders was null, then every
                 * 'modifications' count should be 0 and this method should have returned 'null'.
                 */
                throw new AssertionError(modifications);
            }
            /*
             * If 'removeFactoryIteratorProvider(...)' has been invoked since the last time
             * this method was run, then synchronize 'iteratorProviders' accordingly. Current
             * implementation do not unregister the factories that were created by those iterators.
             */
            if (iteratorProviders != null) {
                iteratorProviders.retainAll(GLOBAL.iteratorProviders);
            } else if (!GLOBAL.iteratorProviders.isEmpty()) {
                iteratorProviders = new LinkedHashSet<FactoryIteratorProvider>();
            }
            /*
             * If 'addFactoryIteratorProvider(...)' has been invoked since the last time
             * this method was run, then synchronize 'iteratorProviders' accordingly. We
             * keep trace of new providers in order to allow 'FactoryRegistry' to use them
             * for a immediate scanning.
             */
            int remaining = GLOBAL.iteratorProviders.size();
            for (final Iterator it=GLOBAL.iteratorProviders.iterator(); it.hasNext();) {
                final FactoryIteratorProvider candidate = (FactoryIteratorProvider) it.next();
                if (iteratorProviders.add(candidate)) {
                    if (newProviders == null) {
                        newProviders = new FactoryIteratorProvider[remaining];
                    }
                    newProviders[count++] = candidate;
                }
                remaining--;
            }
        }
        // Note: newProviders may be null.
        return XArray.resize(newProviders, count);
    }

    /**
     * Adds an alternative way to search for factory implementations. {@link FactoryRegistry} has
     * a default mechanism bundled in it, which uses the content of all {@code META-INF/services}
     * directories found on the classpath. This {@code addFactoryIteratorProvider} method allows
     * to specify additional discovery algorithms. It may be useful in the context of some
     * frameworks that use the <cite>constructor injection</cite> pattern, like the
     * <a href="http://www.springframework.org/">Spring framework</a>.
     */
    public static void addFactoryIteratorProvider(FactoryIteratorProvider provider) {
        synchronized (GLOBAL) {
            if (GLOBAL.iteratorProviders == null) {
                GLOBAL.iteratorProviders = new LinkedHashSet<FactoryIteratorProvider>();
            }
            if (GLOBAL.iteratorProviders.add(provider)) {
                GLOBAL.modifications++;
            }
        }
    }

    /**
     * Removes a provider that was previously {@linkplain #addFactoryIteratorProvider added}.
     * Note that factories already obtained from the specified provider will not be
     * {@linkplain FactoryRegistry#deregisterServiceProvider deregistered} by this method.
     */
    public static void removeFactoryIteratorProvider(FactoryIteratorProvider provider) {
        synchronized (GLOBAL) {
            if (GLOBAL.iteratorProviders != null) {
                if (GLOBAL.iteratorProviders.remove(provider)) {
                    GLOBAL.modifications++;
                }
            }
        }
    }

    /**
     * Returns all iterator providers. This method do not returns any live collection
     * since the array will be used outside the synchronized block.
     */
    static FactoryIteratorProvider[] getIteratorProviders() {
        synchronized (GLOBAL) {
            if (GLOBAL.iteratorProviders == null) {
                return new FactoryIteratorProvider[0];
            }
            return GLOBAL.iteratorProviders.toArray(
                    new FactoryIteratorProvider[GLOBAL.iteratorProviders.size()]);
        }
    }
}
