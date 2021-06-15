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
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.filter;

import java.util.Map;

import org.apache.commons.jxpath.JXPathContext;
import org.geotools.factory.Hints;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;

/**
 * A {@link PropertyAccessorFactory} that returns a {@link PropertyAccessor} capable of evaluating
 * single attribute names from a {@link Map}.
 * 
 * @author Gabriel Roldan
 * 
 *
 *
 *
 *
 * @source $URL$
 */
public class MapPropertyAccessorFactory implements PropertyAccessorFactory {

    /**
     * Creates a property accessor for a particular class.
     * 
     * @param type
     *                The type of object to be accessed.
     * @param xpath
     *                The xpath expression to evaluate.
     * @param target
     *                The kind of result we are expecting (ie Geometry)
     * @param hints
     *                Hints to be used when creatign the accessor.
     * 
     * @return The property accessor, or <code>null</code> if this factory cannot create an
     *         accessor for the specified type.
     */
    public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target,
            Hints hints) {
        if (Map.class.isAssignableFrom(type)) {
            return MAP_ACCESSOR;
        }
        return null;
    }

    private static PropertyAccessor MAP_ACCESSOR = new PropertyAccessor() {

        public boolean canHandle(Object object, String xpath, Class target) {
            return object instanceof Map;
        }

        public Object get(Object object, String xpath, Class target)
                throws IllegalArgumentException {
            JXPathContext context = JXPathContext.newContext(object);
            context.setLenient(true);
            Object value = context.getValue(xpath);
            return value;
        }

        public void set(Object object, String xpath, Object value, Class target)
                throws IllegalAttributeException, IllegalArgumentException {
            throw new IllegalAttributeException("not implemented");
        }

    };
}
