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
 * A factory that can not determines all its dependencies at construction time.
 * Because this factory may creates new factories at any time, it must keep all
 * user-supplied hints. The general contract of {@link #hints} said that we can't
 * use this field for creating new factories. But instances of this classe are special cases
 * where we can, because this class retains all user-supplied hints.
 *
 * <p>Extends this class only if you really need to keep all user-supplied hints, because
 * doing so has a cost:</p>
 * <ul>
 *   <li><p>{@link FactoryRegistry} is less likely to reuse existing factory instances, because
 *       any value found in {@link #hints} that do not agree with a user-supplied hint will cause
 *       {@code FactoryRegistry} to discarts this instance, even if the hint was actually
 *       irrelevant to this factory.</p></li>
 *   <li><p>The user-supplied hints may contain references to big objects (for example a coordinate
 *       transform backed by a grid), and some of them may not be relevant to this factory.
 *       Retaining all hints will prevents their garbage collection.<p></li>
 * </ul>
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @deprecated This class should be a marker interface instead of an {@code AbstractFactory}
 *             subclass. We will make this change in a future version. When this change is
 *             done, {@link org.geotools.referencing.factory.AllAuthoritiesFactory} should
 *             implement this interface.
 */
public interface FactoryUsingVolatileDependencies extends Factory {
}
