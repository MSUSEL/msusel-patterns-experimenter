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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Iterator;
import static org.junit.Assert.*;


/**
 * An implementation of {@link FactoryIteratorProvider} over the {@link DummyFactory}.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class DummyFactoryIteratorProvider implements FactoryIteratorProvider {
    /**
     * {@code true} for iterating over the first half or examples, or {@code false}
     * for iterating over the second half.
     */
    private final boolean firstHalf;

    /**
     * Creates a new instance of the dummy factory iterator provider.
     */
    public DummyFactoryIteratorProvider(final boolean firstHalf) {
        this.firstHalf = firstHalf;
    }

    /**
     * Returns an iterator over all {@link DummyFactory}.
     */
    @SuppressWarnings("unchecked")
    public <T> Iterator<T> iterator(final Class<T> category) {
        assertEquals(DummyFactory.class, category);
        final DummyFactory[] factories;
        if (firstHalf) {
            factories = new DummyFactory[] {
                new DummyFactory.Example1(),
                new DummyFactory.Example2(),
            };
        } else {
            factories = new DummyFactory[] {
                new DummyFactory.Example3(),
                new DummyFactory.Example4()
            };
        }
        return (Iterator) Arrays.asList(factories).iterator();
    }
}
