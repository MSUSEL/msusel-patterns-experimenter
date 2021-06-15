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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This class shows how to "join" two feature sources.
 * 
 * @author Jody
 * 
 * 
 * @source $URL$
 */
public class JoinExample {

/**
 * 
 * @param args
 *            shapefile to use, if not provided the user will be prompted
 */
public static void main(String[] args) throws Exception {
    System.out.println("Welcome to GeoTools:" + GeoTools.getVersion());
    
    File file, file2;
    if (args.length == 0) {
        file = JFileDataStoreChooser.showOpenFile("shp", null);
    } else {
        file = new File(args[0]);
    }
    if (args.length <= 1) {
        file2 = JFileDataStoreChooser.showOpenFile("shp", null);
    } else {
        file2 = new File(args[1]);
    }
    if (file == null || !file.exists() || file2 == null || !file2.exists()) {
        System.exit(1);
    }
    Map<String, Object> connect = new HashMap<String, Object>();
    connect.put("url", file.toURI().toURL());
    DataStore shapefile = DataStoreFinder.getDataStore(connect);
    String typeName = shapefile.getTypeNames()[0];
    SimpleFeatureSource shapes = shapefile.getFeatureSource(typeName);
    
    Map<String, Object> connect2 = new HashMap<String, Object>();
    connect.put("url", file2.toURI().toURL());
    DataStore shapefile2 = DataStoreFinder.getDataStore(connect);
    String typeName2 = shapefile2.getTypeNames()[0];
    SimpleFeatureSource shapes2 = shapefile2.getFeatureSource(typeName2);
    
    joinExample(shapes, shapes2);
    System.exit(0);
}

// joinExample start
private static void joinExample(SimpleFeatureSource shapes, SimpleFeatureSource shapes2)
        throws Exception {
    SimpleFeatureType schema = shapes.getSchema();
    String typeName = schema.getTypeName();
    String geomName = schema.getGeometryDescriptor().getLocalName();
    
    SimpleFeatureType schema2 = shapes2.getSchema();
    String typeName2 = schema2.getTypeName();
    String geomName2 = schema2.getGeometryDescriptor().getLocalName();
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    
    Query outerGeometry = new Query(typeName, Filter.INCLUDE, new String[] { geomName });
    SimpleFeatureCollection outerFeatures = shapes.getFeatures(outerGeometry);
    SimpleFeatureIterator iterator = outerFeatures.features();
    int max = 0;
    try {
        while (iterator.hasNext()) {
            SimpleFeature feature = iterator.next();
            try {
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                if (!geometry.isValid()) {
                    // skip bad data
                    continue;
                }
                Filter innerFilter = ff.intersects(ff.property(geomName2), ff.literal(geometry));
                Query innerQuery = new Query(typeName2, innerFilter, Query.NO_NAMES);
                SimpleFeatureCollection join = shapes2.getFeatures(innerQuery);
                int size = join.size();
                max = Math.max(max, size);
            } catch (Exception skipBadData) {
            }
        }
    } finally {
        iterator.close();
    }
    System.out.println("At most " + max + " " + typeName2 + " features in a single " + typeName
            + " feature");
}
// joinExample end

// Filter innerFilter = ff.dwithin(ff.property(geomName2), ff.literal( geometry
// ),1.0,"km");
// Filter innerFilter = ff.beyond(ff.property(geomName2), ff.literal( geometry
// ),1.0,"km");
// Filter innerFilter = ff.not( ff.disjoint(ff.property(geomName2), ff.literal(
// geometry )) );


}
