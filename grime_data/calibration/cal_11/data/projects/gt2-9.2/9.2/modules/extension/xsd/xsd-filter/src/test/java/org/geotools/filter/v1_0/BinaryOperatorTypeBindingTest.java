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
package org.geotools.filter.v1_0;

import org.w3c.dom.Document;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.Subtract;


/**
 * 
 *
 * @source $URL$
 */
public class BinaryOperatorTypeBindingTest extends FilterTestSupport {
    public void testBinaryOperatorType() {
        assertEquals(BinaryExpression.class, binding(OGC.BinaryOperatorType).getType());
    }

    public void testAddType() {
        assertEquals(Add.class, binding(OGC.Add).getType());
    }

    public void testAddParse() throws Exception {
        FilterMockData.add(document, document);

        Add add = (Add) parse();

        assertNotNull(add.getExpression1());
        assertNotNull(add.getExpression2());
    }

    public void testAddEncode() throws Exception {
        Document dom = encode(FilterMockData.add(), OGC.Add);

        assertEquals(2,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }

    public void testSubType() {
        assertEquals(Subtract.class, binding(OGC.Sub).getType());
    }

    public void testSubParse() throws Exception {
        FilterMockData.sub(document, document);

        Subtract sub = (Subtract) parse();

        assertNotNull(sub.getExpression1());
        assertNotNull(sub.getExpression2());
    }

    public void testSubEncode() throws Exception {
        Document dom = encode(FilterMockData.sub(), OGC.Sub);
        assertEquals(2,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }

    public void testDivType() {
        assertEquals(Divide.class, binding(OGC.Div).getType());
    }

    public void testDivParse() throws Exception {
        FilterMockData.div(document, document);

        Divide div = (Divide) parse();

        assertNotNull(div.getExpression1());
        assertNotNull(div.getExpression2());
    }

    public void testDivEncode() throws Exception {
        Document dom = encode(FilterMockData.div(), OGC.Div);
        assertEquals(2,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }

    public void testMulType() {
        assertEquals(Multiply.class, binding(OGC.Mul).getType());
    }

    public void testMulParse() throws Exception {
        FilterMockData.mul(document, document);

        Multiply mul = (Multiply) parse();

        assertNotNull(mul.getExpression1());
        assertNotNull(mul.getExpression2());
    }

    public void testMulEncode() throws Exception {
        Document dom = encode(FilterMockData.mul(), OGC.Mul);
        assertEquals(2,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }
}
