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
 *    (C) 2005-2010, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.filter.capability.FunctionNameImpl.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Formats a number into a string given a certain pattern (specified in the format accepted
 * by {@link DecimalFormat}} 
 * <p>
 * This function expands on {@link FilterFunction_numberFormat} and adds some additonal parameters 
 * such as allowing the user to explicitly specify the decimal and group separators, rather than 
 * falling back on locale based defaults. 
 * </p>
 * @author Justin Deoliveira, OpenGeo
 * 
 *
 *
 * @source $URL$
 */
public class FilterFunction_numberFormat2 extends FunctionExpressionImpl {
    
    public static FunctionName NAME = new FunctionNameImpl("numberFormat2", String.class,
            parameter("format", String.class),
            parameter("number", Number.class),
            parameter("minus", String.class),
            parameter("decimal", String.class),
            parameter("separator", String.class));
    
    public FilterFunction_numberFormat2() {
        super(NAME);
    }

    public Object evaluate(Object feature) {
        String format;
        Double number;

        try {
            // attempt to get value and perform conversion
            format  = getExpression(0).evaluate(feature, String.class);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function dateFormat argument #0 - expected type String");
        }

        try { // attempt to get value and perform conversion
            number = getExpression(1).evaluate(feature, Double.class); 
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function dateFormat argument #1 - expected type java.util.Date");
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        
        if (params.size() > 2) {
            Character neg = getExpression(2).evaluate(feature, Character.class);
            symbols.setMinusSign(neg);
        }
        
        if (params.size() > 3) {
            Character dec = getExpression(3).evaluate(feature, Character.class);
            symbols.setDecimalSeparator(dec);
        }
        
        if (params.size() > 4) {
            Character grp = getExpression(4).evaluate(feature, Character.class);
            symbols.setGroupingSeparator(grp);
        }
        
        DecimalFormat numberFormat = new DecimalFormat(format, symbols);
        return numberFormat.format(number);
    }
}
