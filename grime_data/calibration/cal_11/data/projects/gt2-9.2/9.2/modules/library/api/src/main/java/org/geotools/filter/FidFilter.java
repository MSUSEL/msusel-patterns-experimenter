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

import java.util.Collection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Id;
import org.opengis.filter.identity.FeatureId;


/**
 * Defines a feature ID filter, which holds a list of feature IDs. This filter
 * stores a series of feature IDs, which are used to distinguish features
 * uniquely.
 *
 * @author Rob Hranac, TOPP
 *
 *
 * @source $URL$
 * @version $Id$
 *
 * @deprecated use {@link org.opengis.filter.identity.FeatureId}
 */
public interface FidFilter extends Filter, Id {
    /**
     * Determines whether or not the given feature's ID matches this filter.
     *
     * @param feature Specified feature to examine.
     *
     * @return <tt>true</tt> if the feature's ID matches an fid held by this
     *         filter, <tt>false</tt> otherwise.
     *
     * @deprecated use {@link org.opengis.filter.Filter#evaluate(Feature)}
     */
    boolean contains(SimpleFeature feature);

    /**
     * Adds a feature ID to the filter.
     *
     * @param fid A single feature ID.
     */
    void addFid(String fid);

    /**
     * Returns all the fids in this filter.
     *
     * @return An array of all the fids in this filter.
     * @deprecated use {@link FeatureId#getIDs()}
     */
    String[] getFids();

    /**
     * Adds a collection of feature IDs to the filter.
     *
     * @param fidsToAdd A collection of feature IDs.
     */
    void addAllFids(Collection<?> fidsToAdd);

    /**
     * Removes a collection of feature IDs from the filter.
     *
     * @param fidsToRemove A collection of feature IDs.
     */
    void removeAllFids(Collection<?> fidsToRemove);

    /**
     * Removes a feature ID from the filter.
     *
     * @param fid A single feature ID.
     */
    void removeFid(String fid);
}
