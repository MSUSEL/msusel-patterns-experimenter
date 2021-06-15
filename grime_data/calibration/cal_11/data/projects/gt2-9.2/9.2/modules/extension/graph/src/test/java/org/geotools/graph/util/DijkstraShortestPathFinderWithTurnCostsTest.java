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
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.util;

import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.TestCase;

import org.geotools.graph.build.line.BasicLineGraphGenerator;
import org.geotools.graph.path.DijkstraShortestPathFinder;
import org.geotools.graph.path.Path;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.standard.DijkstraIterator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

/**
 * 
 *
 * @source $URL$
 */
public class DijkstraShortestPathFinderWithTurnCostsTest extends TestCase {

    private Graph graph;

    public DijkstraShortestPathFinderWithTurnCostsTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();

        LineSegment ld1 = new LineSegment();
        ld1.setCoordinates(new Coordinate(0, 0), new Coordinate(1, 1));

        LineSegment ld2 = new LineSegment();
        ld2.setCoordinates(new Coordinate(1, 1), new Coordinate(0, 2));

        LineSegment ld3 = new LineSegment();
        ld3.setCoordinates(new Coordinate(1, 1), new Coordinate(1, 0));

        // we have some line segments
        LineSegment[] lines = { ld1, ld2, ld3 };

        // create the graph generator
        BasicLineGraphGenerator graphGen = new BasicLineGraphGenerator();

        // add the lines to the graph
        for (int i = 0; i < lines.length; i++) {
            graphGen.add(lines[i]);
        }

        this.graph = graphGen.getGraph();

        System.out.println(graph);

    }

    public void test1() {

        double[] expected = { 0.0, 1.0, 3.0, 3.0 };

        ArrayList gotArray = new ArrayList(4);

        Iterator it = graph.getNodes().iterator();

        Node source = (Node) it.next();

        // create the path finder
        DijkstraShortestPathFinder pf = new DijkstraShortestPathFinder(
                graph, (Graphable) source, costFunction(), tcostFunction());

        pf.calculate();

        Iterator it1 = graph.getNodes().iterator();

        while (it1.hasNext()) {
            Node d = (Node) it1.next();

            Path path = pf.getPath((Graphable) d);

            gotArray.add(pf.getCost(d));

        }

    }

    protected DijkstraIterator.EdgeWeighter costFunction() {
        return (new DijkstraIterator.EdgeWeighter() {
            public double getWeight(Edge e) {
                return 1;
            }
        });
    }

    protected DijkstraIterator.NodeWeighter tcostFunction() {
        return (new DijkstraIterator.NodeWeighter() {
            public double getWeight(Node n, Edge e1, Edge e2) {
                return 1.0;
            }
        });
    }

}
