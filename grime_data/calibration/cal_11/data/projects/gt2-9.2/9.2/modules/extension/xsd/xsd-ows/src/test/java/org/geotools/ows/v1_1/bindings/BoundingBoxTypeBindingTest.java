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
package org.geotools.ows.v1_1.bindings;

import java.math.BigInteger;
import java.util.Arrays;

import net.opengis.ows11.BoundingBoxType;
import net.opengis.ows11.Ows11Factory;

import org.geotools.ows.v1_1.OWS;
import org.geotools.ows.v1_1.OWSTestSupport;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * 
 *
 * @source $URL$
 */
public class BoundingBoxTypeBindingTest extends OWSTestSupport {
    public void testType() throws Exception {
        assertEquals(BoundingBoxType.class, binding(OWS.BoundingBoxType).getType());
    }

    public void testExecutionMode() throws Exception {
        assertEquals(Binding.OVERRIDE, binding(OWS.BoundingBoxType).getExecutionMode());
    }

    public void testParse() throws Exception {
        String xml = "<ows:BoundingBox dimensions=\"2\" crs=\"EPSG:4326\" xmlns:ows=\"http://www.opengis.net/ows\" version=\"1.1.0\">\n" + 
        		"          <ows:LowerCorner>-180.0 -90.0</ows:LowerCorner>\n" + 
        		"          <ows:UpperCorner>180.0 90.0</ows:UpperCorner>\n" + 
        		"        </ows:BoundingBox>";

        buildDocument(xml);

        BoundingBoxType box = (BoundingBoxType) parse();
        assertNotNull(box);
        
        assertEquals( new BigInteger("2"), box.getDimensions() );
        assertEquals( "EPSG:4326", box.getCrs() );
        assertEquals( Arrays.asList(-180.0, -90.0), box.getLowerCorner() );
        assertEquals( Arrays.asList(180.0, 90.0), box.getUpperCorner() );
    }
    
    public void testEncode() throws Exception {
        BoundingBoxType bbox = Ows11Factory.eINSTANCE.createBoundingBoxType();
        bbox.setLowerCorner(Arrays.asList(-180.0, -90.0));
        bbox.setUpperCorner(Arrays.asList(180.0, 90.0));
        bbox.setCrs("EPSG:4326");

        Document d = encode(bbox, OWS.BoundingBox);
        Node bboxNode = d.getChildNodes().item(0);
        assertEquals("-180.0 -90.0", bboxNode.getChildNodes().item(0).getTextContent());
        assertEquals("180.0 90.0", bboxNode.getChildNodes().item(1).getTextContent());
    }
}
