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
package org.geotools.data.shapefile.indexed;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.geotools.TestData;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileReader;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Ian Schneider
 * @author James Macgill
 */
public class ShapefileTest extends org.geotools.data.shapefile.ShapefileTest {

    public ShapefileTest(String testName) throws IOException {
        super(testName);
    }

    public void testShapefileReaderRecord() throws Exception {
        File file = copyShapefiles(STATEPOP);
        ShpFiles shpFiles = new ShpFiles(file.toURI().toURL());
        ShapefileReader reader = new ShapefileReader(shpFiles, false, false, new GeometryFactory());
        ArrayList offsets = new ArrayList();

        while (reader.hasNext()) {
            ShapefileReader.Record record = reader.nextRecord();
            offsets.add(new Integer(record.offset()));

            Geometry geom = (Geometry) record.shape();
            assertEquals(new Envelope(record.minX, record.maxX, record.minY,
                    record.maxY), geom.getEnvelopeInternal());
            record.toString();
        }
        reader.close();
        copyShapefiles(STATEPOP);
        reader = new ShapefileReader(shpFiles, false, false, new GeometryFactory());

        for (int i = 0, ii = offsets.size(); i < ii; i++) {
            reader.shapeAt(((Integer) offsets.get(i)).intValue());
        }
        reader.close();

    }

    protected void loadShapes(String resource, int expected) throws Exception {
        ShpFiles shpFiles = new ShpFiles(TestData.url(resource));
        ShapefileReader reader = new ShapefileReader(shpFiles, false, false, new GeometryFactory());
        int cnt = 0;
        try {
            while (reader.hasNext()) {
                reader.nextRecord().shape();
                cnt++;
            }
        } finally {
            reader.close();
        }
        assertEquals("Number of Geometries loaded incorect for : " + resource,
                expected, cnt);
    }

    protected void loadMemoryMapped(String resource, int expected)
            throws Exception {
        ShpFiles shpFiles = new ShpFiles(TestData.url(resource));
        ShapefileReader reader = new ShapefileReader(shpFiles, false, false, new GeometryFactory());
        int cnt = 0;
        try {
            while (reader.hasNext()) {
                reader.nextRecord().shape();
                cnt++;
            }
        } finally {
            reader.close();
        }
        assertEquals("Number of Geometries loaded incorect for : " + resource,
                expected, cnt);
    }
}
