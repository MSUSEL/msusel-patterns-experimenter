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
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.aggregate;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A blocking queue that can also carry an exception marker
 * 
 * @author Andrea Aime - GeoSolutions
 */
class FeatureQueue extends ArrayBlockingQueue<SimpleFeature> {
    private static final long serialVersionUID = -8717436655125657625L;

    /**
     * The marker is put on the queue when a source is done. This we need to have in order to avoid
     * blocking indefinitely in case a queue does not have any data to offer (as the reader blocks
     * on the queue read)
     */
    static final SimpleFeature END_MARKER;

    static {
        try {
            SimpleFeatureType endMarkerType = DataUtilities.createType("END_MARKER", "id:String");
            END_MARKER = new SimpleFeatureImpl(new Object[] { "end" }, endMarkerType,
                    CommonFactoryFinder.getFilterFactory(null).featureId("END_ID"), false);
        } catch (SchemaException e) {
            throw new RuntimeException("Unexpected error occurred creating the end marker", e);
        }
    }

    Exception exception;

    ConcurrentHashMap<FeatureCallable, FeatureCallable> sources = new ConcurrentHashMap<FeatureCallable, FeatureCallable>();

    public FeatureQueue(int sourceCount) {
        super(100);
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        if (exception != null) {
            shutDown();
            this.exception = exception;
        }
    }

    public boolean isDone() {
        return sources.size() == 0 && size() == 0;
    }

    public synchronized void addSource(FeatureCallable source) {
        sources.put(source, source);
    }

    public void sourceComplete(FeatureCallable source) {
        sources.remove(source);
    }

    public void shutDown() {
        for (FeatureCallable fc : sources.keySet()) {
            fc.shutdown();
        }
    }

}
