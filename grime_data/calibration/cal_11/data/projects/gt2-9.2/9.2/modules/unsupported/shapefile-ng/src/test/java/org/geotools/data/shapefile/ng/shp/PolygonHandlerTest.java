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
package org.geotools.data.shapefile.ng.shp;

import java.io.IOException;
import java.util.ArrayList;

import org.geotools.data.shapefile.ng.TestCaseSupport;
import org.geotools.data.shapefile.ng.shp.PolygonHandler;
import org.geotools.data.shapefile.ng.shp.ShapeType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * 
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Ian Schneider
 */
public class PolygonHandlerTest extends TestCaseSupport {

    public PolygonHandlerTest(String testName) throws IOException {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite(PolygonHandlerTest.class));
    }

    public void testPolygonHandler() {
        Coordinate[] c = new Coordinate[3];
        c[0] = new Coordinate(0, 0, 0);
        c[1] = new Coordinate(1, 1, Double.NaN);
        c[2] = new Coordinate(1, 2, 3);
        PolygonHandler handler = new PolygonHandler(new GeometryFactory());
        assertTrue(handler.getShapeType() == ShapeType.POLYGON);
        for (int i = 0, ii = c.length; i < ii; i++) {
            assertTrue(handler.pointInList(c[i], c));
        }
    }

    public void testHoleAssignment() {
        java.awt.Dimension ps = new java.awt.Dimension(500, 500);
        PrecisionModel precision = new PrecisionModel();

        ArrayList shells = new ArrayList();
        ArrayList holes = new ArrayList();

        int x = 10;
        int y = 10;

        shells.add(copyTo(x, y, ps.width - 2 * x, ps.height - 2 * y, rectangle(
                precision, 0)));

        int w = 11;
        int h = 11;
        int s = 10;

        int nx = (ps.width - 2 * x) / (w + s);
        int ny = (ps.height - 2 * y) / (h + s);

        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                holes.add(copyTo(x + s + i * (w + s), y + s + j * (h + s), w,
                        h, rectangle(precision, 0)));
            }
        }

        PolygonHandler ph = new PolygonHandler(new GeometryFactory());
        ArrayList assigned = ph.assignHolesToShells(shells, holes);
        assertEquals(((ArrayList) assigned.get(0)).size(), holes.size());

    }

    public static Geometry rectangle(PrecisionModel pm, int SRID) {
        Coordinate[] coords = new Coordinate[5];
        for (int i = 0; i < coords.length; i++) {
            coords[i] = new Coordinate();
        }
        return new GeometryFactory().createLinearRing(coords);
    }

    public static Geometry copyTo(double x, double y, double w, double h,
            Geometry g) {
        if (g.getNumPoints() != 5)
            throw new IllegalArgumentException("Geometry must have 5 points");
        if (!LinearRing.class.isAssignableFrom(g.getClass()))
            throw new IllegalArgumentException("Geometry must be linear ring");
        Coordinate[] coords = g.getCoordinates();
        coords[0].x = x;
        coords[0].y = y;
        coords[1].x = x + w;
        coords[1].y = y;
        coords[2].x = x + w;
        coords[2].y = y + h;
        coords[3].x = x;
        coords[3].y = y + h;
        coords[4].x = x;
        coords[4].y = y;
        return g;
    }
}
