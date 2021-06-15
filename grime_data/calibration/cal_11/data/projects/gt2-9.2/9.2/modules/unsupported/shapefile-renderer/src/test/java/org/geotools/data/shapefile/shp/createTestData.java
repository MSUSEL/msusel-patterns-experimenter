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
package org.geotools.data.shapefile.shp;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;


/**
 *
 *
 *
 *
 * @source $URL$
 */
public class createTestData {
    public static URL createLineData(final Dimension d)
        throws Exception {
        File file = new File("test_lines.shp");

        if (file.exists()) {
            file.delete();
        }

        IndexedShapefileDataStoreFactory factory = new IndexedShapefileDataStoreFactory();
        DataStore datastore = factory.createDataStore(file.toURI().toURL());
        
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName( "test_lines" );
        builder.add("x", Integer.class);
        builder.add("y", Integer.class);
        builder.add("geom", LineString.class);
        
        final SimpleFeatureType featureType = builder.buildFeatureType();
        datastore.createSchema(featureType);

        SimpleFeatureStore store = (SimpleFeatureStore) datastore.getFeatureSource(
                "test_lines");
        SimpleFeatureCollection collection;
        collection = DataUtilities
                .collection(new FeatureReader<SimpleFeatureType, SimpleFeature>() {
            public SimpleFeatureType getFeatureType() {
                return featureType;
            }

            GeometryFactory factory = new GeometryFactory();
            int x = 0;
            int y = 0;

            public SimpleFeature next()
                throws IOException, IllegalAttributeException, 
                    NoSuchElementException {
                LineString geom = factory.createLineString(new Coordinate[] {
                            new Coordinate(x + 0.0, y + 0.0),
                            new Coordinate(x + .9, y + 0.9)
                        });
                SimpleFeature feature = 
                    SimpleFeatureBuilder.build( featureType,new Object[] {
                            geom, new Integer(x), new Integer(y)
                        }, null);

                if (x == (d.width - 1)) {
                    y++;
                    x = 0;
                } else {
                    x++;
                }

                return feature;
            }

            public boolean hasNext() throws IOException {
                return y < d.height;
            }

            public void close() throws IOException {
                // TODO Auto-generated method stub
            }
        });
        store.addFeatures(collection);

        return file.toURI().toURL();
    }

    /**
     * DOCUMENT ME!
     *
     * @param args
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println(createLineData(new Dimension(512, 512)));
    }
}
