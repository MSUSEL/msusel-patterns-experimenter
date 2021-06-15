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
package org.geotools.filter;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class IsEqualsToImplTest extends TestCase {

    org.opengis.filter.FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    public void testOperandsSameType() {
        Expression e1 = filterFactory.literal(1);
        Expression e2 = filterFactory.literal(1);

        PropertyIsEqualTo equal = filterFactory.equals(e1, e2);
        assertTrue(equal.evaluate(null));
    }

    public void testOperandsShort() {
        Expression literalShort42 = filterFactory.literal( (short) 42 );
        Expression literalString42 = filterFactory.literal("42");
        Expression literalDouble42 = filterFactory.literal( 42.0 );
        Expression literalLong42 = filterFactory.literal( (long) 42 );
        Expression literalFloat42 = filterFactory.literal( (float) 42 );
        Expression literalBig42 = filterFactory.literal( new BigDecimal(42));
        
        assertTrue( filterFactory.equals( literalShort42, literalShort42).evaluate( null ) );
        assertTrue( filterFactory.equals( literalShort42, literalString42).evaluate( null ) );
        assertTrue( filterFactory.equals( literalShort42, literalDouble42).evaluate( null ) );
        assertTrue( filterFactory.equals( literalShort42, literalLong42).evaluate( null ) );
        assertTrue( filterFactory.equals( literalShort42, literalFloat42).evaluate( null ) );
        assertTrue( filterFactory.equals( literalShort42, literalBig42).evaluate( null ) );
        assertTrue( filterFactory.equals( literalShort42, literalDouble42).evaluate( null ) );        
    }
    
    public void testOperandsIntString() {
        Expression e1 = filterFactory.literal(1);
        Expression e2 = filterFactory.literal("1");

        PropertyIsEqualTo equal = filterFactory.equals(e1, e2);
        assertTrue(equal.evaluate(null));
    }
    
    public void testOperandsIntFloatString() {
        Expression e1 = filterFactory.literal(1);
        Expression e2 = filterFactory.literal("1.2");

        PropertyIsEqualTo equal = filterFactory.equals(e1, e2);
        assertFalse(equal.evaluate(null));
    }

    public void testOperandsLongInt() {
        Expression e1 = filterFactory.literal(1);
        Expression e2 = filterFactory.literal(1l);

        PropertyIsEqualTo equal = filterFactory.equals(e1, e2);
        assertTrue(equal.evaluate(null));
    }

    public void testOperandsFloatInt() {
        Expression e1 = filterFactory.literal(1.0f);
        Expression e2 = filterFactory.literal(1);

        PropertyIsEqualTo equal = filterFactory.equals(e1, e2);
        assertTrue(equal.evaluate(null));
    }

    public void testOperandsDoubleLong() {
        Expression e1 = filterFactory.literal(1.0);
        Expression e2 = filterFactory.literal(1l);

        PropertyIsEqualTo equal = filterFactory.equals(e1, e2);
        assertTrue(equal.evaluate(null));
    }

    public void testOperandsDoubleLongOutOfRange() {
        Expression e1 = filterFactory.literal(new Double(Long.MAX_VALUE).doubleValue() + 10000.0);
        Expression e2 = filterFactory.literal(Long.MAX_VALUE);

        PropertyIsEqualTo equal = filterFactory.equals(e1, e2);
        assertFalse(equal.evaluate(null));
    }

    public void testCaseSensitivity() {
        Expression e1 = filterFactory.literal("foo");
        Expression e2 = filterFactory.literal("FoO");

        PropertyIsEqualTo caseSensitive = filterFactory.equal(e1, e2, true);
        assertFalse(caseSensitive.evaluate(null));

        PropertyIsEqualTo caseInsensitive = filterFactory.equal(e1, e2, false);
        assertTrue(caseInsensitive.evaluate(null));
    }

}
