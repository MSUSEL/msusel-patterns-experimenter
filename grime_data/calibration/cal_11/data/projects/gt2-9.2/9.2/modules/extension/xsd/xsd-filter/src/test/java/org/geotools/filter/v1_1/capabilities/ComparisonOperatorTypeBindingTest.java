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
package org.geotools.filter.v1_1.capabilities;

import org.w3c.dom.Document;
import javax.xml.namespace.QName;
import org.opengis.filter.capability.Operator;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/ogc:ComparisonOperatorType.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;xsd:simpleType name="ComparisonOperatorType"&gt;
 *      &lt;xsd:restriction base="xsd:string"&gt;
 *          &lt;xsd:enumeration value="LessThan"/&gt;
 *          &lt;xsd:enumeration value="GreaterThan"/&gt;
 *          &lt;xsd:enumeration value="LessThanEqualTo"/&gt;
 *          &lt;xsd:enumeration value="GreaterThanEqualTo"/&gt;
 *          &lt;xsd:enumeration value="EqualTo"/&gt;
 *          &lt;xsd:enumeration value="NotEqualTo"/&gt;
 *          &lt;xsd:enumeration value="Like"/&gt;
 *          &lt;xsd:enumeration value="Between"/&gt;
 *          &lt;xsd:enumeration value="NullCheck"/&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt;
 *
 *    </code>
 *   </pre>
 * </p>
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public class ComparisonOperatorTypeBindingTest extends OGCTestSupport {
    public void testType() {
        assertEquals(Operator.class, binding(OGC.ComparisonOperatorType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.ComparisonOperatorType).getExecutionMode());
    }

    public void testParse1() throws Exception {
        FilterMockData.comparisonOperator(document, document, "LessThan");

        Operator op = (Operator) parse(OGC.ComparisonOperatorType);

        assertEquals("LessThan", op.getName());
    }

    public void testParse2() throws Exception {
        FilterMockData.comparisonOperator(document, document, "LessThanOrEqualTo");

        Operator op = (Operator) parse(OGC.ComparisonOperatorType);

        assertEquals("LessThanOrEqualTo", op.getName());
    }

    public void testParse3() throws Exception {
        FilterMockData.comparisonOperator(document, document, "GreaterThan");

        Operator op = (Operator) parse(OGC.ComparisonOperatorType);

        assertEquals("GreaterThan", op.getName());
    }

    public void testParse4() throws Exception {
        FilterMockData.comparisonOperator(document, document, "GreaterThanOrEqualTo");

        Operator op = (Operator) parse(OGC.ComparisonOperatorType);

        assertEquals("GreaterThanOrEqualTo", op.getName());
    }

    public void testParse5() throws Exception {
        FilterMockData.comparisonOperator(document, document, "EqualTo");

        Operator op = (Operator) parse(OGC.ComparisonOperatorType);

        assertEquals("EqualTo", op.getName());
    }

    public void testParse6() throws Exception {
        FilterMockData.comparisonOperator(document, document, "NotEqualTo");

        Operator op = (Operator) parse(OGC.ComparisonOperatorType);

        assertEquals("NotEqualTo", op.getName());
    }

    public void testParse7() throws Exception {
        FilterMockData.comparisonOperator(document, document, "Like");

        Operator op = (Operator) parse(OGC.ComparisonOperatorType);

        assertEquals("Like", op.getName());
    }

    public void testParse8() throws Exception {
        FilterMockData.comparisonOperator(document, document, "Between");

        Operator op = (Operator) parse(OGC.ComparisonOperatorType);

        assertEquals("Between", op.getName());
    }

    public void testParse9() throws Exception {
        FilterMockData.comparisonOperator(document, document, "NullCheck");

        Operator op = (Operator) parse(OGC.ComparisonOperatorType);

        assertEquals("NullCheck", op.getName());
    }

    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.comparisonOperator("LessThan"),
                new QName(OGC.NAMESPACE, "ComparisonOperator"), OGC.ComparisonOperatorType);
        assertEquals("LessThan", dom.getDocumentElement().getFirstChild().getNodeValue());
    }
}
