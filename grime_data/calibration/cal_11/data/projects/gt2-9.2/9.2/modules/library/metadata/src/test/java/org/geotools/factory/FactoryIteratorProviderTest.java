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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Arrays;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link FactoryIteratorProvider} addition in {@link FactoryIteratorProviders}.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class FactoryIteratorProviderTest {
    /**
     * The registry to use for testing purpose.
     */
    private FactoryRegistry registry;

    /**
     * Creates a new, initially empty, factory registry.
     */
    @Before
    public void createRegistry() {
        registry = new FactoryRegistry(Arrays.asList(new Class<?>[] {
            DummyFactory.class
        }));
    }

    /**
     * Returns a factory from the specified registry.
     *
     * @throws FactoryNotFoundException if no factory was found.
     * @throws FactoryRegistryException if a factory can't be returned for some other reason.
     */
    private static DummyFactory getFactory(final FactoryRegistry registry,
                                           final Class<? extends DummyFactory> type)
            throws FactoryRegistryException
    {
        Hints hints = null;
        if (type != null) {
            hints = new Hints(DummyFactory.DUMMY_FACTORY, type);
        }
        return registry.getServiceProvider(DummyFactory.class,
                null, hints, DummyFactory.DUMMY_FACTORY);
    }

    /**
     * Tests the registration of {@link DummyFactory} instances from
     * {@link DummyFactoryIteratorProvider}.
     */
    @Test
    public void testRegistration() {
        /*
         * Tests the initially empty factory.
         */
        try {
            assertNotNull(getFactory(registry, null));
            fail("No factory should have been found.");
        } catch (FactoryNotFoundException e) {
            // This is the expected exception.
        }
        /*
         * Tests after the addition of a new provider.
         */
        final FactoryIteratorProvider provider1 = new DummyFactoryIteratorProvider(true);
        try {
            FactoryIteratorProviders.addFactoryIteratorProvider(provider1);
            assertNotNull(getFactory(registry, null));
            DummyFactory factory;

            factory = getFactory(registry, DummyFactory.Example1.class);
            assertEquals(DummyFactory.Example1.class, factory.getClass());
            factory = getFactory(registry, DummyFactory.Example2.class);
            assertEquals(DummyFactory.Example2.class, factory.getClass());
            try {
                assertNotNull(getFactory(registry, DummyFactory.Example3.class));
                fail("Example #3 should not be registered.");
            } catch (FactoryNotFoundException e) {
                // This is the expected exception.
            }
            try {
                assertNotNull(getFactory(registry, DummyFactory.Example4.class));
                fail("Example #4 should not be registered.");
            } catch (FactoryNotFoundException e) {
                // This is the expected exception.
            }
            /*
             * Add yet an other provider, and test again.
             */
            final FactoryIteratorProvider provider2 = new DummyFactoryIteratorProvider(false);
            try {
                FactoryIteratorProviders.addFactoryIteratorProvider(provider2);
                factory = getFactory(registry, DummyFactory.Example1.class);
                assertEquals(DummyFactory.Example1.class, factory.getClass());
                factory = getFactory(registry, DummyFactory.Example2.class);
                assertEquals(DummyFactory.Example2.class, factory.getClass());
                factory = getFactory(registry, DummyFactory.Example3.class);
                assertEquals(DummyFactory.Example3.class, factory.getClass());
                factory = getFactory(registry, DummyFactory.Example4.class);
                assertEquals(DummyFactory.Example4.class, factory.getClass());
            } finally {
                FactoryIteratorProviders.removeFactoryIteratorProvider(provider2);
            }
        } finally {
            FactoryIteratorProviders.removeFactoryIteratorProvider(provider1);
        }
        /*
         * Tests with a new registry, which should be empty.
         */
        createRegistry();
        try {
            assertNotNull(getFactory(registry, null));
            fail("The factory should be empty again.");
        } catch (FactoryNotFoundException e) {
            // This is the expected exception.
        }
    }
}
