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
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.util.Arrays;

import org.geotools.data.Parameter;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.text.Text;
import org.geotools.util.KVP;
import org.opengis.filter.capability.FunctionName;

/**
 * Filter function implementing the Symbology Encoding "StringPosition" function.
 * <p>
 * This function takes three arguments:
 * <ol>
 *   <li>The look up string</li>
 *   <li>The source string, ie the string to lookup from</li>
 *   <li>The search direction, frontToBack or backToFront
 * </ol>
 * </p>
 * @author Justin Deoliveira, OpenGeo
 *
 *
 *
 * @source $URL$
 */
public class FilterFunction_strPosition extends FunctionExpressionImpl {

    public static FunctionName NAME;
    static {
        Parameter<String> method = new Parameter<String>(
                "method",String.class,
                Text.text("direction"),
                Text.text("direction to search"),
                true,1,1,
                "forward",
                new KVP(Parameter.OPTIONS,Arrays.asList(new String[]{"forward","backToFront"}))
        );
        NAME = new FunctionNameImpl("strPosition",
            parameter("string", String.class),
            parameter("string", String.class),
            parameter("lookup", String.class),
            method);
    }
    public FilterFunction_strPosition() {
        super(NAME);
    }
    
    @Override
    public Object evaluate(Object object) {
        String lookup = getExpression(0).evaluate(object, String.class);
        String string = getExpression(1).evaluate(object, String.class);
        String dir = getExpression(2).evaluate(object, String.class);

        if ("backToFront".equalsIgnoreCase(dir)) {
            return new StringBuffer(string).lastIndexOf(lookup) + 1;
        }
        else {
            return string.indexOf(lookup) + 1;
        }
    }
}
