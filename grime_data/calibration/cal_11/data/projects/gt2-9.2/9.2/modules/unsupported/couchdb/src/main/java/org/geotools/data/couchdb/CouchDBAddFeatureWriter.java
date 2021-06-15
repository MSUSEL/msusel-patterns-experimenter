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
package org.geotools.data.couchdb;

import org.geotools.data.couchdb.client.CouchDBUtils;
import org.geotools.data.couchdb.client.CouchDBException;
import java.io.IOException;
import java.io.StringWriter;
import org.geotools.data.FeatureWriter;
import org.geotools.feature.LenientFeatureFactoryImpl;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

/**
 *
 * @author Ian Schneider (OpenGeo)
 */
class CouchDBAddFeatureWriter implements FeatureWriter {

    private final SimpleFeatureType featureType;
    private final CouchDBDataStore dataStore;
    private final GeometryJSON json;
    private final StringWriter buffer;
    private final FeatureFactory featureFactory;
    private SimpleFeature feature;
    private final String featureClass;
    private int wrote = 0;

    CouchDBAddFeatureWriter(SimpleFeatureType buildFeatureType, CouchDBDataStore dataStore) {
        this.featureType = buildFeatureType;
        this.dataStore = dataStore;
        this.json = new GeometryJSON();
        // @todo non scalable in memory buffer
        this.buffer = new StringWriter();
        featureFactory = dataStore.getFeatureFactory() == null
                ? new LenientFeatureFactoryImpl() : dataStore.getFeatureFactory();

        String localName = featureType.getTypeName(); // this will actuall be the full name
        int idx = localName.lastIndexOf(':');
        idx = localName.indexOf('.', idx);
        this.featureClass = localName.substring(idx + 1);
        buffer.write("{\"docs\":[");
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public Feature next() throws IOException {
        Object[] atts = new Object[featureType.getDescriptors().size()];
        return feature = featureFactory.createSimpleFeature(atts, featureType, "");
    }

    public void remove() throws IOException {
        // shouldn't get called on add?
    }

    public void write() throws IOException {
        if (wrote > 0) {
            buffer.write(',');
        }
        buffer.write(CouchDBUtils.writeJSON(feature, featureClass, json));
        wrote++;
    }

    public boolean hasNext() throws IOException {
        return true;
    }

    public void close() throws IOException {
        try {
            if (wrote > 0) {
                buffer.write("]}");
                String data = buffer.toString();
                dataStore.getConnection().postBulk(data);
            }
        } catch (CouchDBException ex) {
            ex.wrap();
        }
    }
}