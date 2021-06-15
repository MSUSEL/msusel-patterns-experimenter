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
package org.geotools.filter.function;

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

import static org.geotools.filter.capability.FunctionNameImpl.*;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.Converters;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.VolatileFunction;

/**
 * Extracts a property from a feature, taking the property name as a parameter
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 * @source $URL$
 */
public class FilterFunction_property extends FunctionExpressionImpl implements VolatileFunction {

    FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    public static FunctionName NAME = new FunctionNameImpl("property", parameter("propertyValue",
            Object.class), parameter("propertyName", String.class));

    /**
     * Cache the last PropertyName used in a thead safe way
     */
    volatile PropertyName lastPropertyName;

    public FilterFunction_property() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object object, Class context) {
        Object result = evaluate(object);
        if (result == null) {
            return null;
        } else {
            return Converters.convert(result, context);
        }
    }

    public Object evaluate(Object feature) {
        String name = getExpression(0).evaluate(feature, String.class);

        if (name == null) {
            return null;
        }

        PropertyName pn = lastPropertyName;
        if (pn != null && pn.getPropertyName().equals(name)) {
            return pn.evaluate(feature);
        } else {
            pn = FF.property(name);
            Object result = pn.evaluate(feature);
            lastPropertyName = pn;
            return result;
        }
    }
}
