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

package org.geotools.data.complex;

import java.util.NoSuchElementException;

import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.filter.Filter;

/**
 * An extension to {@linkplain org.geotools.data.complex.DataAccessMappingFeatureIterator} where
 * filter is present. Unlike with FilteringMappingFeatureIterator The filter is applied on
 * the complex feature
 * 
 * @author Niels Charlier (Curtin University of Technology)
 *
 * @source $URL$
 */
public class PostFilteringMappingFeatureIterator implements IMappingFeatureIterator {

    protected FeatureIterator<Feature> delegate;
    protected Feature next;
    protected Filter filter;
    protected int maxFeatures;
    protected int count = 0;
    
    public PostFilteringMappingFeatureIterator(FeatureIterator<Feature> iterator, Filter filter, int maxFeatures) {
        this.delegate = iterator;
        this.filter = filter;
        this.maxFeatures = maxFeatures;
        next = getFilteredNext();
    }

    public void close() {
        delegate.close();        
    } 
  
    protected Feature getFilteredNext() {
        while (delegate.hasNext() && count < maxFeatures) {
            Feature feature = delegate.next();
            try {
                if (filter.evaluate(feature)) {
                    return feature;
                }
            } catch (NullPointerException e) {
                // ignore this exception
                // this is to cater the case if the attribute has no value and 
                // has been skipped in the delegate DataAccessMappingFeatureIterator
            }
        }
        return null;
    }

    public boolean hasNext() {    
        return next != null;
    }
        
    public Feature next() {
        if(next == null){
            throw new NoSuchElementException();
        }
        
        count++;
        Feature current = next;
        next = getFilteredNext();
        return current;
    }

    public void remove() {
        throw new UnsupportedOperationException();
        
    }

}
