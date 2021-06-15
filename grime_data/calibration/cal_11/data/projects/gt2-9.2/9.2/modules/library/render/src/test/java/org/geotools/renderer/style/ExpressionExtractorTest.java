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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 * 
 *
 * @source $URL$
 */
public class ExpressionExtractorTest extends TestCase {
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    
    public void testLiteral() {
        final String exp = "I'm a plain string";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        assertEquals(1, result.size());
        assertEquals(ff.literal("I'm a plain string"), result.get(0));
    }
    
    public void testLiteralEscapes() {
        final String exp = "I'm a plain string \\\\ \\${bla bla\\}";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        assertEquals(1, result.size());
        assertEquals(ff.literal("I'm a plain string \\ ${bla bla}"), result.get(0));
    }
    
    public void testSimpleExpressions() {
        final String exp = "I'm a ${name} expression of type ${type}";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        assertEquals(4, result.size());
        assertEquals(ff.literal("I'm a "), result.get(0));
        assertEquals(ff.property("name"), result.get(1));
        assertEquals(ff.literal(" expression of type "), result.get(2));
        assertEquals(ff.property("type"), result.get(3));
    }
    
    public void testDoubleOpen() {
        try {
            ExpressionExtractor.splitCqlExpressions("I'm a plain string ${bla ${bla}");
            fail("Double cql exp open, should have failed");
        } catch(IllegalArgumentException e) {
            // fine
        }
    }
    
    public void testOpenLingering() {
        try {
            ExpressionExtractor.splitCqlExpressions("I'm a plain string ${bla");
            fail("Double cql exp open, should have failed");
        } catch(IllegalArgumentException e) {
            // fine
        }
    }
    
    public void testDoubleClose() {
        try {
            ExpressionExtractor.splitCqlExpressions("I'm a plain string ${bla}}");
            fail("Double cql exp open, should have failed");
        } catch(IllegalArgumentException e) {
            // fine
        }
    }
    
    public void testEscapesInside() {
        final String exp = "${strLength('\\\\\\${\\}')}";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        assertEquals(1, result.size());
        assertEquals(ff.function("strLength", ff.literal("\\${}")), result.get(0));
    }
    
    public void testTwoExpressions() {
        final String exp = "${name}${age}";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        assertEquals(2, result.size());
        assertEquals(ff.property("name"), result.get(0));
        assertEquals(ff.property("age"), result.get(1));
    }
    
    public void testCatenateOne() {
        Expression l = ff.literal("http://test?param=");
        Expression cat = ExpressionExtractor.catenateExpressions(Arrays.asList(l));
        assertEquals(l, cat);
    }
    
    public void testCatenateTwo() {
        Literal l = ff.literal("http://test?param=");
        PropertyName pn = ff.property("intAttribute");
        Expression cat = ExpressionExtractor.catenateExpressions(Arrays.asList(l, pn));
        assertTrue(cat instanceof Function);
        Function f = (Function) cat;
        assertEquals("strConcat", f.getName());
        assertEquals(l, f.getParameters().get(0));
        assertEquals(pn, f.getParameters().get(1));
    }
}
