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

import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Thrown when {@link FactoryRegistry} is invoked recursively for the same category. This exception
 * is often the result of a programming error. It happen typically when an implementation of some
 * {@code FooFactory} interface queries in their constructor, directly or indirectly,
 * {@link FactoryRegistry#getServiceProvider getServiceProvider} for the same category (namely
 * {@code FooFactory.class}). Factories implemented as wrappers around other factories of the same
 * kind are the most likely to fall in this canvas. If this {@code RecursiveSearchException}
 * was not throw, the application would typically dies with a {@link StackOverflowError}.
 * <p>
 * A workaround for this exception is to invoke {@code getServiceProvider} outside the constuctor,
 * when a method first need it.
 *
 * @since 2.3
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class RecursiveSearchException extends FactoryRegistryException {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -2028654588882874110L;

    /**
     * Creates a new exception with a default message determined from the specified category.
     */
    public RecursiveSearchException(final Class<?> category) {
        super(Errors.format(ErrorKeys.RECURSIVE_CALL_$1, category));
    }

    /**
     * Creates a new exception with the specified detail message.
     */
    public RecursiveSearchException(final String message) {
        super(message);
    }
}
