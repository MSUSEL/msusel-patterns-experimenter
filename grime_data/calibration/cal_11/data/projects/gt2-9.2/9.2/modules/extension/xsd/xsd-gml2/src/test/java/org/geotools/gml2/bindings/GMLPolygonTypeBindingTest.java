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
import com.vividsolutions.jts.geom.Polygon;


/**
 * 
 *
 * @source $URL$
 */
public class GMLPolygonTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance poly;
    ElementInstance oring;
    ElementInstance iring;
    MutablePicoContainer container;

    protected void setUp() throws Exception {
        super.setUp();

        poly = createElement(GML.NAMESPACE, "myPolygon", GML.POLYGONTYPE, null);
        oring = createElement(GML.NAMESPACE, "outerBoundaryIs", GML.LINEARRINGTYPE, null);
        iring = createElement(GML.NAMESPACE, "innerBoundaryIs", GML.LINEARRINGTYPE, null);

        container = new DefaultPicoContainer();
        container.registerComponentImplementation(GeometryFactory.class);
        container.registerComponentImplementation(GMLPolygonTypeBinding.class);
    }

    public void testNoInnerRing() throws Exception {
        Node node = createNode(poly, new ElementInstance[] { oring },
                new Object[] {
                    new GeometryFactory().createLinearRing(
                        new Coordinate[] {
                            new Coordinate(1, 2), new Coordinate(3, 4), new Coordinate(5, 6),
                            new Coordinate(1, 2)
                        })
                }, null, null);

        GMLPolygonTypeBinding s = (GMLPolygonTypeBinding) container.getComponentInstanceOfType(GMLPolygonTypeBinding.class);
        Polygon p = (Polygon) s.parse(poly, node, null);
        assertNotNull(p);
        assertEquals(p.getExteriorRing().getPointN(0).getX(), 1d, 0d);
        assertEquals(p.getExteriorRing().getPointN(0).getY(), 2d, 0d);
        assertEquals(p.getExteriorRing().getPointN(1).getX(), 3d, 0d);
        assertEquals(p.getExteriorRing().getPointN(1).getY(), 4d, 0d);
        assertEquals(p.getExteriorRing().getPointN(2).getX(), 5d, 0d);
        assertEquals(p.getExteriorRing().getPointN(2).getY(), 6d, 0d);
        assertEquals(p.getExteriorRing().getPointN(3).getX(), 1d, 0d);
        assertEquals(p.getExteriorRing().getPointN(3).getY(), 2d, 0d);
    }

    public void testInnerRing() throws Exception {
        Node node = createNode(poly, new ElementInstance[] { oring, iring },
                new Object[] {
                    new GeometryFactory().createLinearRing(
                        new Coordinate[] {
                            new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10),
                            new Coordinate(0, 10), new Coordinate(0, 0)
                        }),
                    new GeometryFactory().createLinearRing(
                        new Coordinate[] {
                            new Coordinate(1, 1), new Coordinate(9, 1), new Coordinate(9, 9),
                            new Coordinate(1, 9), new Coordinate(1, 1)
                        })
                }, null, null);

        GMLPolygonTypeBinding s = (GMLPolygonTypeBinding) container.getComponentInstanceOfType(GMLPolygonTypeBinding.class);
        Polygon p = (Polygon) s.parse(poly, node, null);
        assertNotNull(p);
        assertEquals(p.getExteriorRing().getPointN(0).getX(), 0d, 0d);
        assertEquals(p.getExteriorRing().getPointN(0).getY(), 0d, 0d);
        assertEquals(p.getExteriorRing().getPointN(1).getX(), 10d, 0d);
        assertEquals(p.getExteriorRing().getPointN(1).getY(), 0d, 0d);
        assertEquals(p.getExteriorRing().getPointN(2).getX(), 10d, 0d);
        assertEquals(p.getExteriorRing().getPointN(2).getY(), 10d, 0d);
        assertEquals(p.getExteriorRing().getPointN(3).getX(), 0d, 0d);
        assertEquals(p.getExteriorRing().getPointN(3).getY(), 10d, 0d);
        assertEquals(p.getExteriorRing().getPointN(4).getX(), 0d, 0d);
        assertEquals(p.getExteriorRing().getPointN(4).getY(), 0d, 0d);

        assertEquals(p.getInteriorRingN(0).getPointN(0).getX(), 1d, 0d);
        assertEquals(p.getInteriorRingN(0).getPointN(0).getY(), 1d, 0d);
        assertEquals(p.getInteriorRingN(0).getPointN(1).getX(), 9d, 0d);
        assertEquals(p.getInteriorRingN(0).getPointN(1).getY(), 1d, 0d);
        assertEquals(p.getInteriorRingN(0).getPointN(2).getX(), 9d, 0d);
        assertEquals(p.getInteriorRingN(0).getPointN(2).getY(), 9d, 0d);
        assertEquals(p.getInteriorRingN(0).getPointN(3).getX(), 1d, 0d);
        assertEquals(p.getInteriorRingN(0).getPointN(3).getY(), 9d, 0d);
        assertEquals(p.getInteriorRingN(0).getPointN(4).getX(), 1d, 0d);
        assertEquals(p.getInteriorRingN(0).getPointN(4).getY(), 1d, 0d);
    }
}
