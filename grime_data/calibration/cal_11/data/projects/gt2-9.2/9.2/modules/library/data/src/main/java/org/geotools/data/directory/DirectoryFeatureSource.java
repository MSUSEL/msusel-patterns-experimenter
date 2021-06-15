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
package org.geotools.data.directory;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.Set;

import org.geotools.data.DataAccess;
import org.geotools.data.FeatureListener;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * 
 *
 * @source $URL$
 */
public class DirectoryFeatureSource implements SimpleFeatureSource {
    SimpleFeatureSource fsource;
    
    public DirectoryFeatureSource(
            SimpleFeatureSource delegate) {
        this.fsource = delegate;
    }

    public void addFeatureListener(FeatureListener listener) {
        fsource.addFeatureListener(listener);
    }

    public ReferencedEnvelope getBounds() throws IOException {
        return fsource.getBounds();
    }

    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return fsource.getBounds(query);
    }

    public int getCount(Query query) throws IOException {
        return fsource.getCount(query);
    }

    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        // this is done on purpose to avoid crippling the shapefile renderer optimizations
        return fsource.getDataStore();
    }

    public SimpleFeatureCollection getFeatures()
            throws IOException {
        return fsource.getFeatures();
    }

    public SimpleFeatureCollection getFeatures(
            Filter filter) throws IOException {
        return fsource.getFeatures(filter);
    }

    public SimpleFeatureCollection getFeatures(
            Query query) throws IOException {
        return fsource.getFeatures(query);
    }

    public ResourceInfo getInfo() {
        return fsource.getInfo();
    }

    public Name getName() {
        return fsource.getName();
    }

    public QueryCapabilities getQueryCapabilities() {
        return fsource.getQueryCapabilities();
    }

    public SimpleFeatureType getSchema() {
        return fsource.getSchema();
    }

    public Set<Key> getSupportedHints() {
        return fsource.getSupportedHints();
    }

    public void removeFeatureListener(FeatureListener listener) {
        fsource.removeFeatureListener(listener);
    }

    public SimpleFeatureSource unwrap() {
        return fsource;
    }
    
}
