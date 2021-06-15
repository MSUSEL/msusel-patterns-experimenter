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
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.build.line;

import java.util.ArrayList;
import java.util.Collection;

import com.vividsolutions.jts.geom.Point;
import junit.framework.TestCase;

import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.basic.BasicNode;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * @author Anders Bakkevold, Bouvet AS, bakkedev@gmail.com
 */
public class LineStringGraphGeneratorTest extends TestCase {

    private Coordinate c1, c2, c3, c4, c6, c7;

    private GeometryFactory gf;

    private LineStringGraphGenerator gen;

    public void setUp() throws Exception {
        c1 = new Coordinate(1, 1);
        c2 = new Coordinate(2, 2);
        c3 = new Coordinate(3, 3);
        c4 = new Coordinate(4, 4);
        c6 = new Coordinate(2.01, 2.0007); // within tolerance (0.02) of c2
        c7 = new Coordinate(2.0, 1.75); // outsite tolerance (0.02) of c2

        gf = new GeometryFactory();
        gen = new LineStringGraphGenerator(0.2);
    }

    public void testThatCoordinatesNearbySnapToSameNode() {
        LineString lineString = gf.createLineString(new Coordinate[] { c1, c2 });
        LineString lineString2 = gf.createLineString(new Coordinate[] { c6, c3 });
        LineString lineString3 = gf.createLineString(new Coordinate[] { c7, c4 });
        gen.add(lineString);
        gen.add(lineString2);
        gen.add(lineString3);

        Graph graph = gen.getGraph();
        Collection graphNodes = graph.getNodes();
        assertEquals(5, graphNodes.size());
        Collection<Coordinate> graphNodeCoordinates = getCoordinates(graphNodes);
        assertTrue(graphNodeCoordinates.contains(c2));
        assertFalse("c6 should be snapped to c2", graphNodeCoordinates.contains(c6));
        assertTrue("c7 should not have been snapped to c2 - distance bigger than tolerance", graphNodeCoordinates.contains(c7)); //
        assertEquals(3, graph.getEdges().size());
    }

    private Collection<Coordinate> getCoordinates(Collection<BasicNode> graphNodes) {
        Collection<Coordinate> coordinates = new ArrayList<Coordinate>();
        for (BasicNode node : graphNodes) {
            coordinates.add(((Point) node.getObject()).getCoordinate());
        }
        return coordinates;
    }
}
