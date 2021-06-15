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
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.NoSuchElementException;

import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;


/**
 * Basic support for a  FeatureReader<SimpleFeatureType, SimpleFeature> that limits itself to the number of
 * features passed in.
 *
 * @author Chris Holmes
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class MaxFeatureReader<T extends FeatureType, F extends Feature> implements DelegatingFeatureReader<T,F>{
    
    protected final  FeatureReader<T, F> featureReader;
    protected final int maxFeatures;
    protected int counter = 0;

    /**
     * Creates a new instance of MaxFeatureReader
     *
     * @param featureReader FeatureReader being maxed
     * @param maxFeatures DOCUMENT ME!
     */
    public MaxFeatureReader(FeatureReader<T, F> featureReader, int maxFeatures) {
        this.featureReader = featureReader;
        this.maxFeatures = maxFeatures;
    }

    public FeatureReader<T, F> getDelegate() {
        return featureReader;
    }
    
    public F next()
        throws IOException, IllegalAttributeException, NoSuchElementException {
        if (hasNext()) {
            counter++;

            return featureReader.next();
        } else {
            throw new NoSuchElementException("No such Feature exists");
        }
    }

    public void close() throws IOException {
        featureReader.close();
    }

    public T getFeatureType() {
        return featureReader.getFeatureType();
    }

    /**
     * <p></p>
     *
     * @return <code>true</code> if the featureReader has not passed the max
     *         and still has more features.
     *
     * @throws IOException If the reader we are filtering encounters a problem
     */
    public boolean hasNext() throws IOException {
        return (featureReader.hasNext() && (counter < maxFeatures));
    }
}
