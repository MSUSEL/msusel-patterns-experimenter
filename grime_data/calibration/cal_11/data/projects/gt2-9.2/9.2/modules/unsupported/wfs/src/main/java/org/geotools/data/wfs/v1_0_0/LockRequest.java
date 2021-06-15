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
package org.geotools.data.wfs.v1_0_0;

import java.util.Map;

import org.geotools.data.FeatureLock;
import org.opengis.filter.Filter;


/**
 * Extension to FeatureLock that
 * records types and filters effected.
 *
 * @author dzwiers
 *
 *
 *
 * @source $URL$
 */
public class LockRequest extends FeatureLock {
    //private long duration = 0;
    private String[] types = null;
    private Filter[] filters = null;
    //private String lockId = null;

    private LockRequest() {
        super(null,-1);
        // should not be used
    }

    protected LockRequest(long duration, Map<String,Filter> dataSets) {
        super( null, duration );
        //this.duration = duration;
        types = (String[]) dataSets.keySet().toArray(new String[dataSets.size()]);
        filters = new Filter[types.length];

        for (int i = 0; i < types.length; i++)
            filters[i] = (Filter) dataSets.get(types[i]);
    }

    protected LockRequest(long duration, String[] types, Filter[] filters) {
        super( null, duration );
        this.types = types;
        this.filters = filters;
    }

    /**
     * Authorisation provided.
     * 
     * @see org.geotools.data.FeatureLock#getAuthorization()
     */
    public String getAuthorization() {
        return authorization;
    }

    protected void setAuthorization(String auth) {
        authorization = auth;
    }

    /**
     * 
     * @see org.geotools.data.FeatureLock#getDuration()
     */
    public long getDuration() {
        return duration;
    }

    /**
     * 
     * @return Type Names
     */
    public String[] getTypeNames() {
        return types;
    }

    /**
     * 
     * @return Filters
     */
    public Filter[] getFilters() {
        return filters;
    }
}
