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
package org.geotools.gml2.bindings;

import org.geotools.gml2.GML;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


/**
 * 
 *
 * @source $URL$
 */
public class GMLPointTypeBindingTest extends AbstractGMLBindingTest {
    MutablePicoContainer container;
    ElementInstance point;
    ElementInstance coord;
    ElementInstance coords;

    protected void setUp() throws Exception {
        super.setUp();

        point = createElement(GML.NAMESPACE, "myPoint", GML.POINTTYPE, null);
        coord = createElement(GML.NAMESPACE, "coord", GML.COORDTYPE, null);
        coords = createElement(GML.NAMESPACE, "coordinates", GML.COORDINATESTYPE, null);

        container = new DefaultPicoContainer();
        container.registerComponentImplementation(GeometryFactory.class);
        container.registerComponentImplementation(GMLPointTypeBinding.class);
    }

    public void testParseCoordinate() throws Exception {
        Node node = createNode(point, new ElementInstance[] { coord },
                new Object[] { new Coordinate(12.34, 56.78) }, null, null);

        GMLPointTypeBinding strategy = (GMLPointTypeBinding) container.getComponentInstanceOfType(GMLPointTypeBinding.class);

        Point p = (Point) strategy.parse(point, node, null);
        assertNotNull(p);
        assertEquals(p.getX(), 12.34, 0d);
        assertEquals(p.getY(), 56.78, 0d);
    }

    public void testParseCoordinates() throws Exception {
        Node node = createNode(point, new ElementInstance[] { coords },
                new Object[] { createCoordinateSequence(new Coordinate(12.34, 56.78)) }, null, null);

        GMLPointTypeBinding strategy = (GMLPointTypeBinding) container.getComponentInstanceOfType(GMLPointTypeBinding.class);

        Point p = (Point) strategy.parse(point, node, null);
        assertNotNull(p);
        assertEquals(p.getX(), 12.34, 0d);
        assertEquals(p.getY(), 56.78, 0d);
    }

    public void testParseMultiCoordinates() throws Exception {
        Node node = createNode(point, new ElementInstance[] { coords },
                new Object[] {
                    createCoordinateSequence(
                        new Coordinate[] { new Coordinate(12.34, 56.78), new Coordinate(9.10, 11.12) })
                }, null, null);

        GMLPointTypeBinding strategy = (GMLPointTypeBinding) container.getComponentInstanceOfType(GMLPointTypeBinding.class);

        try {
            Point p = (Point) strategy.parse(point, node, null);
            fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            //ok
        }
    }
}
