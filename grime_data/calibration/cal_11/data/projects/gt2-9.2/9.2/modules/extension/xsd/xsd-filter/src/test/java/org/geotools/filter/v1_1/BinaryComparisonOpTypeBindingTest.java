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
package org.geotools.filter.v1_1;

import org.w3c.dom.Document;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.geotools.xml.Binding;


/**
 * 
 *
 * @source $URL$
 */
public class BinaryComparisonOpTypeBindingTest extends org.geotools.filter.v1_0.BinaryComparisonOpTypeBindingTest {
    public void testBinaryComparisonOpType() {
        assertEquals(BinaryComparisonOperator.class, binding(OGC.BinaryComparisonOpType).getType());
    }

    public void testPropertyIsEqualToType() {
        assertEquals(PropertyIsEqualTo.class, binding(OGC.PropertyIsEqualTo).getType());
    }

    public void testPropertyIsEqualToExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.PropertyIsEqualTo).getExecutionMode());
    }

    public void testPropertyIsEqualToParse() throws Exception {
        FilterMockData.propertyIsEqualTo(document, document);

        PropertyIsEqualTo equalTo = (PropertyIsEqualTo) parse();
        assertNotNull(equalTo);

        assertNotNull(equalTo.getExpression1());
        assertNotNull(equalTo.getExpression2());
    }

    public void testPropertyIsEqualToEncode() throws Exception {
        PropertyIsEqualTo equalTo = FilterMockData.propertyIsEqualTo();

        Document dom = encode(equalTo, OGC.PropertyIsEqualTo);
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }

    public void testPropertyIsNotEqualToType() {
        assertEquals(PropertyIsNotEqualTo.class, binding(OGC.PropertyIsNotEqualTo).getType());
    }

    public void testPropertyIsNotEqualToExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.PropertyIsNotEqualTo).getExecutionMode());
    }

    public void testPropertyIsNotEqualToParse() throws Exception {
        FilterMockData.propertyIsNotEqualTo(document, document);

        PropertyIsNotEqualTo equalTo = (PropertyIsNotEqualTo) parse();
        assertNotNull(equalTo);

        assertNotNull(equalTo.getExpression1());
        assertNotNull(equalTo.getExpression2());
    }

    public void testPropertyIsNotEqualToEncode() throws Exception {
        PropertyIsNotEqualTo equalTo = FilterMockData.propertyIsNotEqualTo();

        Document dom = encode(equalTo, OGC.PropertyIsNotEqualTo);
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }

    public void testPropertyIsLessThanType() {
        assertEquals(PropertyIsLessThan.class, binding(OGC.PropertyIsLessThan).getType());
    }

    public void testPropertyIsLessThanExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.PropertyIsLessThan).getExecutionMode());
    }

    public void testPropertyIsLessThanParse() throws Exception {
        FilterMockData.propertyIsLessThan(document, document);

        PropertyIsLessThan equalTo = (PropertyIsLessThan) parse();
        assertNotNull(equalTo);

        assertNotNull(equalTo.getExpression1());
        assertNotNull(equalTo.getExpression2());
    }

    public void testPropertyIsLessThanEncode() throws Exception {
        PropertyIsLessThan equalTo = FilterMockData.propertyIsLessThan();

        Document dom = encode(equalTo, OGC.PropertyIsLessThan);
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }

    public void testPropertyIsLessThanOrEqualToType() {
        assertEquals(PropertyIsLessThanOrEqualTo.class,
            binding(OGC.PropertyIsLessThanOrEqualTo).getType());
    }

    public void testPropertyIsLessThanOrEqualToExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.PropertyIsLessThanOrEqualTo).getExecutionMode());
    }

    public void testPropertyIsLessThanOrEqualToParse()
        throws Exception {
        FilterMockData.propertyIsLessThanOrEqualTo(document, document);

        PropertyIsLessThanOrEqualTo equalTo = (PropertyIsLessThanOrEqualTo) parse();
        assertNotNull(equalTo);

        assertNotNull(equalTo.getExpression1());
        assertNotNull(equalTo.getExpression2());
    }

    public void testPropertyIsLessThanOrEqualToEncode()
        throws Exception {
        PropertyIsLessThanOrEqualTo equalTo = FilterMockData.propertyIsLessThanOrEqualTo();

        Document dom = encode(equalTo, OGC.PropertyIsLessThanOrEqualTo);
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }

    public void testPropertyIsGreaterThanType() {
        assertEquals(PropertyIsGreaterThan.class, binding(OGC.PropertyIsGreaterThan).getType());
    }

    public void testPropertyIsGreaterThanExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.PropertyIsGreaterThan).getExecutionMode());
    }

    public void testPropertyIsGreaterThanParse() throws Exception {
        FilterMockData.propertyIsGreaterThan(document, document);

        PropertyIsGreaterThan equalTo = (PropertyIsGreaterThan) parse();
        assertNotNull(equalTo);

        assertNotNull(equalTo.getExpression1());
        assertNotNull(equalTo.getExpression2());
    }

    public void testPropertyIsGreaterThanEncode() throws Exception {
        PropertyIsGreaterThan equalTo = FilterMockData.propertyIsGreaterThan();

        Document dom = encode(equalTo, OGC.PropertyIsGreaterThan);
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }

    public void testPropertyIsGreaterThanOrEqualToType() {
        assertEquals(PropertyIsGreaterThanOrEqualTo.class,
            binding(OGC.PropertyIsGreaterThanOrEqualTo).getType());
    }

    public void testPropertyIsGreaterThanOrEqualToExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.PropertyIsGreaterThanOrEqualTo).getExecutionMode());
    }

    public void testPropertyIsGreaterThanOrEqualToParse()
        throws Exception {
        FilterMockData.propertyIsGreaterThanOrEqualTo(document, document);

        PropertyIsGreaterThanOrEqualTo equalTo = (PropertyIsGreaterThanOrEqualTo) parse();
        assertNotNull(equalTo);

        assertNotNull(equalTo.getExpression1());
        assertNotNull(equalTo.getExpression2());
    }

    public void testPropertyIsGreaterThanOrEqualToEncode()
        throws Exception {
        PropertyIsGreaterThanOrEqualTo equalTo = FilterMockData.propertyIsGreaterThanOrEqualTo();

        Document dom = encode(equalTo, OGC.PropertyIsGreaterThanOrEqualTo);
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }
}
