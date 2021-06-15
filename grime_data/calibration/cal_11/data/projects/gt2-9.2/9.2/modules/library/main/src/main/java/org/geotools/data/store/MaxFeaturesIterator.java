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
package org.geotools.data.store;

import java.util.Iterator;

import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.DelegateFeatureIterator;
import org.opengis.feature.Feature;

/**
 * Iterator wrapper which caps the number of returned features.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/library/main/src/main/java/org/geotools/
 *         data/store/MaxFeaturesIterator.java $
 */
public class MaxFeaturesIterator<F extends Feature> implements FeatureIterator<F> {

    FeatureIterator<F> delegate;

    long start;
    long end;
    long counter;

    public MaxFeaturesIterator(Iterator<F> iterator, long max) {
        this( new DelegateFeatureIterator<F>(iterator), 0, max);
    }

    public MaxFeaturesIterator(Iterator<F> iterator, long start, long max) {
        this( new DelegateFeatureIterator<F>(iterator), start, max);
    }
    
    public MaxFeaturesIterator(FeatureIterator<F> delegate, long max) {
        this(delegate, 0, max);
    }

    public MaxFeaturesIterator(FeatureIterator<F> delegate, long start, long max) {
        this.delegate = delegate;
        this.start = start;
        this.end = start + max;
        counter = 0;
    }

    public FeatureIterator<F> getDelegate() {
        return delegate;
    }

    public boolean hasNext() {
        if (counter < start) {
            // skip to just before start if needed
            skip();
        }
        return delegate.hasNext() && counter < end;
    }

    public F next() {
        if (counter < start) {
            // skip to just before start if needed
            skip();
        }
        if (counter <= end) {
            counter++;
            F next = delegate.next();
            return next;
        }
        return null;
    }

    private void skip() {
        if (counter < start) {
            while (delegate.hasNext() && counter < start) {
                counter++;
                F skip = delegate.next(); // skip!
            }
        }
    }

    @Override
    public void close() {
        delegate.close();
    }
}
