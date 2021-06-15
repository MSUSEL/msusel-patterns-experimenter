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

import java.io.IOException;
import java.util.List;

import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;

/**
 * 
 *
 * @source $URL$
 */
public class DirectoryFeatureStore extends DirectoryFeatureSource implements
        SimpleFeatureStore {

    SimpleFeatureStore fstore;

    public DirectoryFeatureStore(SimpleFeatureStore store) {
        super(store);
        this.fstore = store;
    }

    public Transaction getTransaction() {
        return fstore.getTransaction();
    }

    public void modifyFeatures(Name attributeName, Object attributeValue, Filter filter)
            throws IOException {
        fstore.modifyFeatures(attributeName, attributeValue, filter);
    }
    
    public void modifyFeatures(AttributeDescriptor type, Object value,
            Filter filter) throws IOException {
        fstore.modifyFeatures(type, value, filter);
    }
    
    public void modifyFeatures(Name[] name, Object[] value,
            Filter filter) throws IOException {
        fstore.modifyFeatures(name, value, filter);
    }

    public void modifyFeatures(String name, Object value, Filter filter)
            throws IOException {
        fstore.modifyFeatures(name, value, filter);
    }

    public void modifyFeatures(String[] names, Object[] values, Filter filter) throws IOException {
        fstore.modifyFeatures(names, values, filter);
    }
    
    public void modifyFeatures(AttributeDescriptor[] type, Object[] value,
            Filter filter) throws IOException {
        fstore.modifyFeatures(type, value, filter);
    }

    public void removeFeatureListener(FeatureListener listener) {
        fstore.removeFeatureListener(listener);
    }

    public void removeFeatures(Filter filter) throws IOException {
        fstore.removeFeatures(filter);
    }

    public void setFeatures(
            FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws IOException {
        fstore.setFeatures(reader);
    }

    public void setTransaction(Transaction transaction) {
        fstore.setTransaction(transaction);
    }

    public List<FeatureId> addFeatures(
            FeatureCollection collection)
            throws IOException {
        return fstore.addFeatures(collection);
    }
    
    @Override
    public SimpleFeatureStore unwrap() {
        return fstore;
    }

}
