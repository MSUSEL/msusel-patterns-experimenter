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
package org.geotools.filter;

import java.util.Iterator;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.FilteringFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollectionIteration;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.PropertyDescriptor;


/**
 * Run through the provided collection only returning features that pass the provided
 * filter.
 *
 * @author Ian Schneider
 *
 *
 * @source $URL$
 * @deprecated Please use {@link FilteringFeatureCollection}
 */
public class FilteringIteration extends FeatureCollectionIteration {
    /**
     * Creates a new instance of FilteringIteration
     *
     * @param filter DOCUMENT ME!
     * @param collection DOCUMENT ME!
     */
    public FilteringIteration(org.opengis.filter.Filter filter, FeatureCollection<?,?> collection) {
        super(new FilterHandler(filter),
              new FilteringFeatureCollection(collection,filter) );
    }

    public static void filter(FeatureCollection<?,?> features, Filter filter) {
        FilteringIteration i = new FilteringIteration(filter, features);
        i.iterate();
    }

    protected void iterate(FeatureIterator<?> iterator) {
        ((FilterHandler) handler).iterator = iterator;
        super.iterate(iterator);
    }
    
    static class FilterHandler implements Handler {
        FeatureIterator<?> iterator;
        final org.opengis.filter.Filter filter;

        public FilterHandler(org.opengis.filter.Filter filter) {
            this.filter = filter;
        }

        public void endFeature(Feature f) {
        }

        public void endFeatureCollection( FeatureCollection<?,?> fc) {
        }

        public void handleAttribute(PropertyDescriptor type,
            Object value) {
        }

        public void handleFeature(Feature f) {
            if (!filter.evaluate(f)) {
                // iterator.remove();
                // this shoudl not occur
            }
        }

        public void handleFeatureCollection(
            FeatureCollection<?,?> fc) {
        }
    }
}
