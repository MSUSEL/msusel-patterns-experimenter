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


/**
 * An optional factory that may not be available in all configurations.
 * <p>
 * Such factories often need some external resources. For example the default
 * {@linkplain org.geotools.referencing.factory.epsg.AccessDialectEpsgFactory EPSG factory}
 * need a MS-Access database installed on the client machine. This database is not bundle in
 * Geotools distribution; if the user have not installed it, the factory can't work.
 * <p>
 * This interface is <strong>not</strong> a manager for automatic download of external resources.
 * It is just a way to tell to {@link FactoryFinder} that this factory exists, but can't do its
 * job for whatever reasons (usually a missing resource that the user shall download and install
 * himself), so {@code FactoryFinder} has to choose an other factory. In other words, the
 * {@code OptionalFactory} interface is used as a filter, nothing else. The process is as follows:
 * <p>
 * <ul>
 *   <li>When {@link FactoryRegistry#getServiceProvider} is invoked, it starts to iterate over all
 *       registered factories. If an {@linkplain FactoryRegistry#setOrdering(Class,Object,Object)
 *       ordering is set}, it is taken in account for the iteration order.</li>
 *   <li>If no suitable factory was found before the iterator reachs this optional factory, then
 *       {@link #isAvailable} is invoked. If it returns {@code true}, then this optional factory
 *       is processed like any other factories. Otherwise it is ignored.</li>
 * </ul>
 * <p>
 * <strong>NOTE:</strong> {@code OptionalFactory} is not designed for factories with intermittent
 * state (i.e. return value of {@link #isAvailable} varying in an unpredictable way). The behavior
 * is undetermined if the {@code isAvailable()} state changes with time.
 *
 * @since 2.0
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @see org.geotools.data.DataStoreFactorySpi#isAvailable
 */
public interface OptionalFactory extends Factory {
    /**
     * Returns {@code true} if this factory is ready for use.
     * An optional factory may returns {@code false} for now but returns {@code true} later.
     * However, the converse is not recommended.
     */
    boolean isAvailable();
}
