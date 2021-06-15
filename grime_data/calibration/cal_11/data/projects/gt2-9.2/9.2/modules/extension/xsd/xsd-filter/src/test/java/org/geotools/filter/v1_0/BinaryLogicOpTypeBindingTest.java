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
import org.w3c.dom.Element;
import org.opengis.filter.And;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.geotools.xml.Binding;


/**
 * 
 *
 * @source $URL$
 */
public class BinaryLogicOpTypeBindingTest extends FilterTestSupport {
    public void testBinaryLogicOpType() {
        assertEquals(BinaryLogicOperator.class, binding(OGC.BinaryLogicOpType).getType());
    }

    public void testAndType() {
        assertEquals(And.class, binding(OGC.And).getType());
    }

    public void testAndExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.And).getExecutionMode());
    }

    public void testAndParse() throws Exception {
        FilterMockData.and(document, document);

        And and = (And) parse();

        assertEquals(2, and.getChildren().size());
    }

    public void testAndEncode() throws Exception {
        Document dom = encode(FilterMockData.and(), OGC.And);
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyIsEqualTo.getLocalPart())
               .getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyIsNotEqualTo.getLocalPart())
               .getLength());
    }

    public void testOrType() {
        assertEquals(Or.class, binding(OGC.Or).getType());
    }

    public void testOrExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.Or).getExecutionMode());
    }

    public void testOrParse() throws Exception {
        FilterMockData.or(document, document);

        Or or = (Or) parse();

        assertEquals(2, or.getChildren().size());
    }

    public void testOrEncode() throws Exception {
        Document dom = encode(FilterMockData.or(), OGC.Or);
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyIsEqualTo.getLocalPart())
               .getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyIsNotEqualTo.getLocalPart())
               .getLength());
    }
    
    public void testAndWithLikeParse() throws Exception {
        Element e = FilterMockData.and(document, document, true);
        FilterMockData.propertyIsLike(document, e);
        FilterMockData.propertyIsLike(document, e);
        
        And and = (And) parse();
        assertEquals( 2, and.getChildren().size() );
        assertTrue( and.getChildren().get(0) instanceof PropertyIsLike );
        assertTrue( and.getChildren().get(1) instanceof PropertyIsLike );
    }
    
    public void testAndWithLikeEncode() throws Exception {
        FilterFactory f = FilterMockData.f;
        And and = f.and( FilterMockData.propertyIsLike(), FilterMockData.propertyIsLike());
        
        Document dom = encode(and,OGC.And);
        assertEquals( 2, getElementsByQName(dom, OGC.PropertyIsLike).getLength() );
    }
    
    public void testAndWithNullParse() throws Exception {
        Element e = FilterMockData.and(document, document, true);
        FilterMockData.propertyisNull(document, e);
        FilterMockData.propertyisNull(document, e);
        
        And and = (And) parse();
        assertEquals( 2, and.getChildren().size() );
        assertTrue( and.getChildren().get(0) instanceof PropertyIsNull );
        assertTrue( and.getChildren().get(1) instanceof PropertyIsNull );
    }
    
    public void testAndWithNullEncode() throws Exception {
        FilterFactory f = FilterMockData.f;
        And and = f.and( FilterMockData.propertyIsNull(), FilterMockData.propertyIsNull());
        
        Document dom = encode(and,OGC.And);
        assertEquals( 2, getElementsByQName(dom, OGC.PropertyIsNull).getLength() );
    }
    
    public void testAndWithBetweenParse() throws Exception {
        Element e = FilterMockData.and(document, document, true);
        FilterMockData.propertyIsBetween(document, e);
        FilterMockData.propertyIsBetween(document, e);
        
        And and = (And) parse();
        assertEquals( 2, and.getChildren().size() );
        assertTrue( and.getChildren().get(0) instanceof PropertyIsBetween );
        assertTrue( and.getChildren().get(1) instanceof PropertyIsBetween );
    }
    
    public void testAndWithBetweenEncode() throws Exception {
        FilterFactory f = FilterMockData.f;
        And and = f.and( FilterMockData.propertyIsBetween(), FilterMockData.propertyIsBetween());
        
        Document dom = encode(and,OGC.And);
        assertEquals( 2, getElementsByQName(dom, OGC.PropertyIsBetween).getLength() );
    }
}
