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
package org.geotools.gml;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.TopologyException;


/**
 * Creates a simple OGC LinearRing (a closed LineString).
 *
 * @author Ian Turton, CCG
 * @author Rob Hranac, Vision for New York
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class SubHandlerLinearRing extends SubHandler {
    /** Internal coordinate list. */
    private ArrayList coordinateList = new ArrayList();

    /**
     * Creates a new instance of GMLLinearRingHandler.
     */
    public SubHandlerLinearRing() {
    }

    /**
     * Adds a coordinate to the LinearRing.
     *
     * @param coordinate The coordinate to add to the LinearRing.
     */
    public void addCoordinate(Coordinate coordinate) {
        coordinateList.add(coordinate);
    }

    /**
     * Determine whether or not this LinearRing is ready to be created.
     *
     * @param message The current geometry type in the GML stream.
     *
     * @return Ready for creation flag.
     */
    public boolean isComplete(String message) {
        // makes sure that this LinearRing has more than one coordinate and its first and last are identical
        if (coordinateList.size() > 1) {
            Coordinate firstCoordinate = (Coordinate) coordinateList.get(0);
            Coordinate lastCoordinate = (Coordinate) coordinateList.get(coordinateList
                    .size() - 1);

            if (lastCoordinate.equals2D(firstCoordinate)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Create the LinearRing.
     *
     * @param geometryFactory The geometry factory used for the build.
     *
     * @return LinearRing geometry created.
     */
    public Geometry create(GeometryFactory geometryFactory) {
        try {
            Coordinate[] coords = (Coordinate[]) coordinateList.toArray(new Coordinate[coordinateList.size()]);
            LinearRing ring = geometryFactory.createLinearRing(coords);
            ring.setUserData( getSRS() );
            ring.setSRID( getSRID() );
            return ring;
        } catch (TopologyException e) {
            System.err.println(
                "Caught Topology exception in GMLLinearRingHandler");

            return null;
        }
    }
}
