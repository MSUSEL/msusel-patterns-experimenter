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
package org.geotools.kml.bindings;

import javax.xml.namespace.QName;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;


/**
 * 
 *
 * @source $URL$
 */
public class PolygonTypeBindingTest extends KMLTestSupport {
    public void testType() {
        assertEquals(Polygon.class, binding(KML.PolygonType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(KML.PolygonType).getExecutionMode());
    }

    public void testParse() throws Exception {
        String xml = "<Polygon>" + "<outerBoundaryIs>"
            + "<LinearRing><coordinates>1,1 2,2 3,3 1,1</coordinates></LinearRing>"
            + "</outerBoundaryIs>" + "<innerBoundaryIs>"
            + "<LinearRing><coordinates>1,1 2,2 3,3 1,1</coordinates></LinearRing>"
            + "</innerBoundaryIs>" + "</Polygon>";

        buildDocument(xml);

        Polygon p = (Polygon) parse();

        assertEquals(1, p.getNumInteriorRing());
    }
    
    public void testEncode() throws Exception {
        Polygon p = new GeometryFactory().createPolygon(
            new GeometryFactory().createLinearRing(
                new Coordinate[]{ new Coordinate(1,1), new Coordinate(2,2), 
                    new Coordinate(3,3), new Coordinate(1,1) }
            ), new LinearRing[] {
                new GeometryFactory().createLinearRing(
                        new Coordinate[]{ new Coordinate(1,1), new Coordinate(2,2), 
                            new Coordinate(3,3), new Coordinate(1,1) }
                )
            }
        );
        Document dom = encode( p, KML.Polygon );
        
        assertNotNull( getElementByQName(dom, new QName( KML.NAMESPACE, "outerBoundaryIs") ) );
        assertNotNull( getElementByQName(dom, new QName( KML.NAMESPACE, "innerBoundaryIs") ) );
    }
}
