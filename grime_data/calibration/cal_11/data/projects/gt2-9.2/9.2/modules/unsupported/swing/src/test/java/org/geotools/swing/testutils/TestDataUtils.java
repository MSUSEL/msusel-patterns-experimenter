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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.testutils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.Geometries;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;

import org.geotools.styling.SLD;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Provides static methods to load and access test data, thus reducing duplicated
 * code in test classes.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class TestDataUtils {
    
    /**
     * Creates a new {@linkplain FeatureLayer} containing polygon feature data.
     * 
     * @return the new layer
     * @throws Exception on error accessing the test data
     */
    public static Layer getPolygonLayer() throws Exception {
        URL url = TestData.url("shapes/statepop.shp");
        return createLayer(url);
    }
    
    /**
     * Creates a new {@linkplain FeatureLayer} containing line feature data.
     * 
     * @return the new layer
     * @throws Exception on error accessing the test data
     */
    public static Layer getLineLayer() throws Exception {
        URL url = TestData.url("shapes/roads.shp");
        return createLayer(url);
    }
    
    /**
     * Creates a new {@linkplain FeatureLayer} containing point feature data.
     * 
     * @return the new layer
     * @throws Exception on error accessing the test data
     */
    public static Layer getPointLayer() throws Exception {
        URL url = TestData.url("shapes/archsites.shp");
        return createLayer(url);
    }
    
    /**
     * Gets a world position which lies in or on the given feature.
     * 
     * @param feature the feature
     * @return a position in or on the feature
     */
    public static DirectPosition2D getPosInFeature(SimpleFeature feature) {
        if (feature == null) {
            throw new IllegalArgumentException("feature argument must not be null");
        }
        
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        Coordinate c = null;
        
        switch (Geometries.get(geom)) {
            case MULTIPOLYGON:
            case POLYGON:
                c = geom.getCentroid().getCoordinate();
                break;
                
            case MULTILINESTRING:
            case LINESTRING:
                Coordinate[] coords = geom.getCoordinates();
                c = coords[coords.length / 2];
                break;
                
            case MULTIPOINT:
            case POINT:
                c = geom.getCoordinate();
                break;
                
            default:
                throw new IllegalArgumentException("Unsupported geometry type");
        }
        
        CoordinateReferenceSystem crs = feature.getFeatureType().getCoordinateReferenceSystem();
        return new DirectPosition2D(crs, c.x, c.y);
    }

    /**
     * Creates a new {@linkplain FeatureLayer}.
     * 
     * @param url location of the feature data
     * @return the new layer
     * @throws Exception on error accessing the feature data
     */
    private static Layer createLayer(URL url) throws Exception {
        Map params = new HashMap();
        params.put("url", url);
        DataStore dataStore = DataStoreFinder.getDataStore(params);
        String typeName = dataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);

        return new FeatureLayer(featureSource, SLD.createSimpleStyle(featureSource.getSchema()));
    }

}
