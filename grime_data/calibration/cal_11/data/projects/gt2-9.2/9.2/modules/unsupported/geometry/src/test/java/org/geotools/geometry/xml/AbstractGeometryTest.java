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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public abstract class AbstractGeometryTest extends TestCase {

    private org.opengis.geometry.coordinate.GeometryFactory gFact;

    private org.opengis.geometry.primitive.PrimitiveFactory pFact;

    protected CoordinateReferenceSystem crs;

    protected static String WGS84_WKT =
            "GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]]," +
                    "PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], " +
                    "AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]]";


    /**
     * setUp
     * Called before each test.
     *
     * @throws FactoryException
     */
    public void setUp() throws FactoryException {
    	GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);       
        gFact = builder.getGeometryFactory();
        pFact = builder.getPrimitiveFactory();
    }


    protected GeometryFactory getGeometryFactory() {
        return gFact;
    }

    protected PrimitiveFactory getPrimitiveFactory() {
        return pFact;
    }

    protected DirectPosition createDirectPosition(double x, double y) {
        double[] coords = new double[2];
        coords[0] = x;
        coords[1] = y;
        return gFact.createDirectPosition(coords);
    }

    /**
     * A helper method for creating a Curve from an array of DirectPositions
     *
     * @param points
     * @return a <tt>Curve</tt>
     */
    protected Curve createCurve(final DirectPosition[] points) {
        final List curveSegmentList = Collections.singletonList(createLineString(points));
        final Curve curve = pFact.createCurve(curveSegmentList);
        return curve;
    }

    /**
     * A helper method for creating a lineString from an array of DirectPositions
     *
     * @param points
     * @return <tt>LineString</tt>
     */
    protected LineString createLineString(final DirectPosition[] points) {
        final LineString lineString = gFact.createLineString(new ArrayList(Arrays.asList(points)));
        return lineString;
    }

    /**
     * A helper method for creating a Ring from an array of DirectPositions
     *
     * @param curve
     * @return a <tt>Ring</tt>
     */
    protected Ring createRing(final Curve curve) {
        final List curveList = Collections.singletonList(curve);
        final Ring ring = pFact.createRing(curveList);
        return ring;
    }

    /**
     * creates a SurfaceBoundary using a curve as the exterior
     *
     * @param exterior
     * @return <tt>SurfaceBoundary</tt>
     */
    protected SurfaceBoundary createSurfaceBoundary(Curve exterior) {
        final Ring exteriorRing = createRing(exterior);
        List interiorRingList = Collections.EMPTY_LIST;
        SurfaceBoundary surfaceBoundary = null;
        surfaceBoundary = pFact.createSurfaceBoundary(exteriorRing, interiorRingList);
        return surfaceBoundary;
    }

    /**
     * Creates a simple polygon with no holes
     * @param points points defining the polygon (surface)
     * @return the surface created out of the points
     */
    protected Surface createSurface(final DirectPosition[] points) {
        Curve curve = createCurve(points);
        SurfaceBoundary surfaceBoundary = createSurfaceBoundary(curve);
        Surface surface = getPrimitiveFactory().createSurface(surfaceBoundary);
        return surface;
    }

}
