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
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid;

import static org.junit.Assert.assertEquals;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Base class for vector grid unit tests.
 *
 * @author mbedward
 * @since 2.7
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class TestBase {

    protected static final double TOL = 1.0E-8d;

    protected void assertEnvelope(Envelope expected, Envelope actual) {
        assertEquals((expected == null), (actual == null));
        if (expected != null) {
            assertEquals(expected.getMinX(), actual.getMinX(), TOL);
            assertEquals(expected.getMinY(), actual.getMinY(), TOL);
            assertEquals(expected.getMaxX(), actual.getMaxX(), TOL);
            assertEquals(expected.getMaxY(), actual.getMaxY(), TOL);
        }
    }

    protected void assertCoordinate(Coordinate expected, Coordinate actual) {
        assertEquals((expected == null), (actual == null));
        if (expected != null) {
            assertEquals(expected.x, actual.x, TOL);
            assertEquals(expected.y, actual.y, TOL);
        }
    }

    protected String getSydneyWKT() {
        return "PROJCS[\"GDA94 / MGA zone 56\", "
                + "GEOGCS[\"GDA94\", "
                + "DATUM[\"Geocentric Datum of Australia 1994\", "
                + "SPHEROID[\"GRS 1980\", 6378137.0, 298.257222101, AUTHORITY[\"EPSG\",\"7019\"]], "
                + "TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], "
                + "AUTHORITY[\"EPSG\",\"6283\"]], "
                + "PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], "
                + "UNIT[\"degree\", 0.017453292519943295], "
                + "AXIS[\"Geodetic latitude\", NORTH], "
                + "AXIS[\"Geodetic longitude\", EAST], "
                + "AUTHORITY[\"EPSG\",\"4283\"]], "
                + "PROJECTION[\"Transverse Mercator\", AUTHORITY[\"EPSG\",\"9807\"]], "
                + "PARAMETER[\"central_meridian\", 153.0], "
                + "PARAMETER[\"latitude_of_origin\", 0.0], "
                + "PARAMETER[\"scale_factor\", 0.9996], "
                + "PARAMETER[\"false_easting\", 500000.0], "
                + "PARAMETER[\"false_northing\", 10000000.0], "
                + "UNIT[\"m\", 1.0], "
                + "AXIS[\"Easting\", EAST], "
                + "AXIS[\"Northing\", NORTH], "
                + "AUTHORITY[\"EPSG\",\"28356\"]]";
    }

    protected SimpleFeatureType createFeatureType(CoordinateReferenceSystem crs) {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("type");
        tb.add(GridFeatureBuilder.DEFAULT_GEOMETRY_ATTRIBUTE_NAME, Polygon.class, crs);
        tb.add("name", String.class);
        return tb.buildFeatureType();
    }

}
