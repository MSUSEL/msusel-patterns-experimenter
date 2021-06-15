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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.jdbc;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.DelegatingFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.jdbc.JoinInfo.JoinPart;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Feature reader that wraps multiple feature readers in a joining / post filtered query.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class JDBCJoiningFilteringFeatureReader implements DelegatingFeatureReader<SimpleFeatureType, SimpleFeature> {

    FeatureReader<SimpleFeatureType,SimpleFeature> delegate;
    JoinInfo join;
    SimpleFeature next;
    
    public JDBCJoiningFilteringFeatureReader(FeatureReader delegate, JoinInfo join) {
        this.delegate = delegate;
        this.join = join;
    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> getDelegate() {
        return delegate;
    }

    public SimpleFeatureType getFeatureType() {
        return delegate.getFeatureType();
    }

    public boolean hasNext() throws IOException {
        if (next != null) {
            return true;
        }

        //scroll through the delegate reader until we get a feature whose joined features match
        // all the post features
        while(delegate.hasNext()) {
            SimpleFeature peek = delegate.next();

            for (JoinPart part : join.getParts()) {
                if (part.getPostFilter() != null) {
                    SimpleFeature f = (SimpleFeature) peek.getAttribute(part.getAttributeName());
                    if (!part.getPostFilter().evaluate(f)) {
                        peek = null;
                        break;
                    }
                }
            }
            
            if (peek != null) {
                next = peek;
                break;
            }
        }
        
        return next != null;
    }

    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException("No more features");
        }

        SimpleFeature f = next;
        next = null;
        return f;
    }

    public void close() throws IOException {
        delegate.close();
    }
}
