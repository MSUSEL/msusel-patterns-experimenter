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

import org.geotools.xml.Binding;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.spatial.Intersects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * 
 *
 * @source $URL$
 */
public class FilterTypeBindingTest extends FilterTestSupport {
    public void testType() {
        assertEquals(Filter.class, binding(OGC.FilterType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.FilterType).getExecutionMode());
    }

    public void testParseSpatial() throws Exception {
        Element filterElement = FilterMockData.element(document, document, OGC.Filter);
        FilterMockData.intersects(document, filterElement);

        Filter filter = (Filter) parse();
        assertTrue(filter instanceof Intersects);
    }

    public void testEncodeSpatial() throws Exception {
        Document doc = encode(FilterMockData.intersects(), OGC.Filter);
        assertEquals("ogc:Filter", doc.getDocumentElement().getNodeName());

        assertEquals(1, doc.getElementsByTagNameNS(OGC.NAMESPACE, "Intersects").getLength());
    }

    public void testParseComparison() throws Exception {
        Element filterElement = FilterMockData.element(document, document, OGC.Filter);
        FilterMockData.propertyIsEqualTo(document, filterElement);

        Filter filter = (Filter) parse();
        assertTrue(filter instanceof PropertyIsEqualTo);
    }

    public void testEncodeComparison() throws Exception {
        Document doc = encode(FilterMockData.propertyIsEqualTo(), OGC.Filter);

        assertEquals("ogc:Filter", doc.getDocumentElement().getNodeName());
        assertEquals(1, doc.getElementsByTagNameNS(OGC.NAMESPACE, "PropertyIsEqualTo").getLength());
    }

    public void testParseLogical() throws Exception {
        Element filterElement = FilterMockData.element(document, document, OGC.Filter);
        FilterMockData.and(document, filterElement);

        Filter filter = (Filter) parse();
        assertTrue(filter instanceof And);
    }

    public void testEncodeLogical() throws Exception {
        Document doc = encode(FilterMockData.and(), OGC.Filter);

        assertEquals("ogc:Filter", doc.getDocumentElement().getNodeName());
        assertEquals(1, doc.getElementsByTagNameNS(OGC.NAMESPACE, "And").getLength());
    }
    
    public void testEncodeDateTimeLiterals() throws Exception {
        Object literal;
        String expected;

        literal = new java.util.Date(1000000);
        expected = "1970-01-01T00:16:40Z";
        testEncodeLiteral(literal, expected);
        
        literal = new java.sql.Timestamp(1000000);
        expected = "1970-01-01T00:16:40Z";
        testEncodeLiteral(literal, expected);

        literal = new java.sql.Date(1000000);
        expected = "1970-01-01Z";
        testEncodeLiteral(literal, expected);

        literal = new java.sql.Time(1000000);
        expected = "00:16:40Z";
        testEncodeLiteral(literal, expected);
    }

    private void testEncodeLiteral(final Object literal, final String expected) throws Exception{
        Document doc = encode(FilterMockData.literal(literal), OGC.Literal);

        assertEquals("ogc:Literal", doc.getDocumentElement().getNodeName());
        
        String actual = doc.getDocumentElement().getTextContent();
        assertEquals(expected, actual);
    }
}
