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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.io.IOException;
import java.net.URL;

import org.geotools.TestData;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.referencing.CRS;

/**
 * 
 *
 * @source $URL$
 */
public abstract class OGRPeformanceTest extends TestCaseSupport {

    final static String STATE_POP = "shapes/statepop.shp";

    protected OGRPeformanceTest(Class<? extends OGRDataStoreFactory> dataStoreFactoryClass) {
        super(dataStoreFactoryClass);
    }

    protected void setUp() throws Exception {
        super.setUp();
        CRS.decode("EPSG:4326");
    }

    public void testOGRShapePerformance() throws Exception {
        OGRDataStore ods = new OGRDataStore(getAbsolutePath(STATE_POP), null, null, ogr);
        long start = System.currentTimeMillis();
        ods.getSchema(ods.getTypeNames()[0]);
        long end = System.currentTimeMillis();
        System.out.println("OGR schema: " + (end - start) / 1000.0);
        DefaultQuery query = new DefaultQuery(ods.getTypeNames()[0]);
        start = System.currentTimeMillis();
        FeatureReader ofr = ods.getFeatureReader(query, Transaction.AUTO_COMMIT);
        while (ofr.hasNext())
            ofr.next();
        ofr.close();
        end = System.currentTimeMillis();
        System.out.println("OGR: " + (end - start) / 1000.0);
    }
    
    public void testShapefilePerformance() throws Exception {
        URL url = TestData.url(STATE_POP);
        ShapefileDataStore sds = new ShapefileDataStore(url, false);
        long start = System.currentTimeMillis();
        sds.getSchema();
        long end = System.currentTimeMillis();
        System.out.println("SDS schema: " + (end - start) / 1000.0);

        DefaultQuery query = new DefaultQuery(sds.getTypeNames()[0]);
        start = System.currentTimeMillis();
        FeatureReader sfr = sds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        while (sfr.hasNext())
            sfr.next();
        sfr.close();
        end = System.currentTimeMillis();
        System.out.println("SDS: " + (end - start) / 1000.0);

        System.out.println("Attribute count: " + sds.getSchema().getAttributeCount());
        System.out.println("Feature count: "
                + sds.getFeatureSource(sds.getSchema().getTypeName()).getCount(Query.ALL));
    }

}
