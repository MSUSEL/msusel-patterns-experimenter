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
import org.picocontainer.defaults.DefaultPicoContainer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


/**
 * 
 *
 * @source $URL$
 */
public class GMLGeometryCollectionTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance gcol;
    ElementInstance point1;
    ElementInstance point2;
    ElementInstance line1;
    ElementInstance ring1;
    ElementInstance poly1;
    GeometryFactory gf;

    protected void setUp() throws Exception {
        super.setUp();

        point1 = createElement(GML.NAMESPACE, "myPoint", GML.POINTMEMBERTYPE, null);
        point2 = createElement(GML.NAMESPACE, "myPoint", GML.POINTMEMBERTYPE, null);
        line1 = createElement(GML.NAMESPACE, "myLine", GML.LINESTRINGMEMBERTYPE, null);
        ring1 = createElement(GML.NAMESPACE, "myLine", GML.LINEARRINGMEMBERTYPE, null);
        poly1 = createElement(GML.NAMESPACE, "myPoly", GML.POLYGONMEMBERTYPE, null);
        gcol = createElement(GML.NAMESPACE, "myColl", GML.GEOMETRYCOLLECTIONTYPE, null);
        gf = new GeometryFactory();

        container = new DefaultPicoContainer();
        container.registerComponentImplementation(GeometryFactory.class);
        container.registerComponentImplementation(GMLGeometryCollectionTypeBinding.class);
    }

    public void testHomogeneous() throws Exception {
        Node node = createNode(gcol, new ElementInstance[] { point1, point2 },
                new Object[] {
                    gf.createPoint(new Coordinate(0, 0)), gf.createPoint(new Coordinate(1, 1))
                }, null, null);

        GMLGeometryCollectionTypeBinding s = (GMLGeometryCollectionTypeBinding) container
            .getComponentInstanceOfType(GMLGeometryCollectionTypeBinding.class);

        GeometryCollection gc = (GeometryCollection) s.parse(gcol, node, null);
        assertNotNull(gc);
        assertEquals(gc.getNumGeometries(), 2);
        assertTrue(gc.getGeometryN(0) instanceof Point);
        assertTrue(gc.getGeometryN(1) instanceof Point);
        assertEquals(((Point) gc.getGeometryN(0)).getX(), 0d, 0d);
        assertEquals(((Point) gc.getGeometryN(0)).getY(), 0d, 0d);
        assertEquals(((Point) gc.getGeometryN(1)).getX(), 1d, 0d);
        assertEquals(((Point) gc.getGeometryN(1)).getY(), 1d, 0d);
    }

    public void testHeterogeneous() throws Exception {
        Node node = createNode(gcol, new ElementInstance[] { point1, point2, line1, ring1, poly1 },
                new Object[] {
                    gf.createPoint(new Coordinate(0, 0)), gf.createPoint(new Coordinate(1, 1)),
                    gf.createLineString(
                        new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 1) }),
                    gf.createLinearRing(
                        new Coordinate[] {
                            new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2),
                            new Coordinate(0, 0)
                        }),
                    gf.createPolygon(gf.createLinearRing(
                            new Coordinate[] {
                                new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2),
                                new Coordinate(0, 0)
                            }), null)
                }, null, null);

        GMLGeometryCollectionTypeBinding s = (GMLGeometryCollectionTypeBinding) container
            .getComponentInstanceOfType(GMLGeometryCollectionTypeBinding.class);

        GeometryCollection gc = (GeometryCollection) s.parse(gcol, node, null);
        assertNotNull(gc);
        assertEquals(gc.getNumGeometries(), 5);
        assertTrue(gc.getGeometryN(0) instanceof Point);
        assertTrue(gc.getGeometryN(1) instanceof Point);
        assertTrue(gc.getGeometryN(2) instanceof LineString);
        assertTrue(gc.getGeometryN(3) instanceof LinearRing);
        assertTrue(gc.getGeometryN(4) instanceof Polygon);
    }
}
