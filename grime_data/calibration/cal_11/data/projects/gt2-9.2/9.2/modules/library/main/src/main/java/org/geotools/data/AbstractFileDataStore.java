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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;

import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;


/**
 * <p>
 * This class assumes the DataStore represents a single source,  represented by
 * a URL. In many cases the default functionality  is chained off to the
 * parent class (AbstractDataStore).
 * </p>
 *
 * @author dzwiers
 *
 * @see AbstractDataStore
 *
 *
 * @source $URL$
 */
public abstract class AbstractFileDataStore extends AbstractDataStore implements FileDataStore {
    /**
     * Singular version, returns the FeatureType for the url being read.
     *
     * @see org.geotools.data.DataStore#getSchema(java.lang.String)
     */
    public SimpleFeatureType getSchema() throws IOException {
        return this.getSchema( getTypeNames()[0] );
    }

    /**
     * Singular version, which must be implemented to represent a Reader  for
     * the url being read.
     *
     * @see org.geotools.data.DataStore#getFeatureReader(java.lang.String)
     */
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader()
        throws IOException{
        return getFeatureReader( getTypeNames()[0] );
    }

    /**
     * Singular version, calls parent with getSchema().getTypeName()
     *
     * @see org.geotools.data.DataStore#updateSchema(java.lang.String,SimpleFeatureType)
     */
    public void updateSchema( SimpleFeatureType featureType) throws IOException {
        updateSchema(getSchema().getTypeName(), featureType);
    }

    /**
     * Singular version, calls parent with getSchema().getTypeName()
     *
     * @see org.geotools.data.DataStore#getFeatureSource(java.lang.String)
     */
    public SimpleFeatureSource getFeatureSource() throws IOException {
        return getFeatureSource(getSchema().getTypeName());
    }

    /**
     * Singular version, calls parent with getSchema().getTypeName()
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Filter filter, Transaction transaction)
        throws IOException {
        return getFeatureWriter(getSchema().getTypeName(), filter, transaction);
    }

    /**
     * @see org.geotools.data.DataStore#getFeatureWriter(java.lang.String,
     *      org.geotools.data.Transaction)
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Transaction transaction)
        throws IOException {
        return getFeatureWriter(getSchema().getTypeName(), transaction);
    }

    /**
     * @see org.geotools.data.DataStore#getFeatureWriterAppend(java.lang.String,
     *      org.geotools.data.Transaction)
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(Transaction transaction)
        throws IOException {
        return getFeatureWriterAppend(getSchema().getTypeName(), transaction);
    }
}
