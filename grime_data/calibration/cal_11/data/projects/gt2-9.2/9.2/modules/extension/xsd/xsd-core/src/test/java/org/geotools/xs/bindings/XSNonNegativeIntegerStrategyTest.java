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
package org.geotools.xs.bindings;

import java.math.BigInteger;
import javax.xml.namespace.QName;
import org.geotools.xs.TestSchema;
import org.geotools.xs.XS;


/**
 * 
 *
 * @source $URL$
 */
public class XSNonNegativeIntegerStrategyTest extends TestSchema {
    public void validateValues(String text, Number expected)
        throws Exception {
        Object value = new BigInteger(text.trim());

        Object result = strategy.parse(element(text, qname), value);

        if (!(result instanceof BigInteger) && result instanceof Number) {
            result = BigInteger.valueOf(((Number) result).longValue());
        }

        assertEquals(integer(expected), integer(result));
    }

    public BigInteger integer(Object value) {
        return (value instanceof BigInteger) ? ((BigInteger) value)
                                             : BigInteger.valueOf(((Number) value).longValue());
    }

    public Number number(String number) {
        return BigInteger.valueOf(Integer.valueOf(number).longValue());
    }

    /*
     * Test method for 'org.geotools.xml.strategies.xs.XSNonPositiveIntegerStrategy.parse(Element, Node[], Object)'
     */
    public void testNegativeOne() throws Exception {
        try {
            validateValues("-1", number("-1"));
        } catch (IllegalArgumentException e) {
            // yeah!
        }
    }

    public void testZero() throws Exception {
        validateValues("0", number("0"));
    }

    public void testLargePositiveNumber() throws Exception {
        validateValues("12678967543233", new BigInteger("12678967543233"));
    }

    public void testPositiveNumber() throws Exception {
        validateValues("1000", new Integer("1000"));
    }

    protected QName getQName() {
        return XS.NONNEGATIVEINTEGER;
    }
}
