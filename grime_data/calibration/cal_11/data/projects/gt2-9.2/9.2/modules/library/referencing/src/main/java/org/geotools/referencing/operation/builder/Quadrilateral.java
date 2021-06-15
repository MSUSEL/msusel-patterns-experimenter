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
package org.geotools.referencing.operation.builder;

import org.opengis.geometry.DirectPosition;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple four-sided polygon.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Jan Jezek
 */
class Quadrilateral extends Polygon {
    /** The first vertex. */
    public DirectPosition p0;

    /** The second vertex. */
    public DirectPosition p1;

    /** The third vertex */
    public DirectPosition p2;

    /** the fourth vetrex */
    public DirectPosition p3;

    /**
     * Creates a Quadrilateral.
     * @param p0 one vertex
     * @param p1 another vertex
     * @param p2 another vertex
     * @param p3 another vertex
     */
    public Quadrilateral(DirectPosition p0, DirectPosition p1,
        DirectPosition p2, DirectPosition p3) {
        super(new DirectPosition[] { p0, p1, p2, p3, p0 });
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    /**
     * Test the Quadrilateral if it is a convex polygon.
     *
     * @return whether the diagonals intersects
     */
    public boolean isConvex() {
        return Line2D.linesIntersect(p0.getCoordinate()[0],
            p0.getCoordinate()[1], p2.getCoordinate()[0],
            p2.getCoordinate()[1], p1.getCoordinate()[0],
            p1.getCoordinate()[1], p3.getCoordinate()[0],
            p3.getCoordinate()[1]);
    }

    /**
     * Splits the Quadrilateral into two triangles.
     *
     * @return two Triangles: p0-p1-p2 and p0-p3-p2
     */
    public List <TINTriangle> getTriangles() {
        //Assert.isTrue(this.isValid());
        ArrayList <TINTriangle> triangles = new ArrayList<TINTriangle>();
        TINTriangle trigA = new TINTriangle(p0, p1, p2);
        TINTriangle trigB = new TINTriangle(p0, p3, p2);

        try {
            trigA.addAdjacentTriangle(trigB);
            trigB.addAdjacentTriangle(trigA);
        } catch (TriangulationException e) {
            //should never reach here
            e.printStackTrace();
        }

        triangles.add(trigA);
        triangles.add(trigB);

        return triangles;
    }
}
