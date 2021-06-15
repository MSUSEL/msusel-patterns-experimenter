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

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.spatial.DistanceBufferOperator;


/**
 * Defines geometry filters with a distance element.
 *
 * <p>
 * These filters are defined in the filter spec by the DistanceBufferType,
 * which contains an additioinal field for a distance.  The two filters that
 * use the distance buffer type are Beyond and DWithin.
 * </p>
 *
 * <p>
 * From the spec: The spatial operators DWithin and Beyond test whether the
 * value of a geometric property is within or beyond a specified distance of
 * the specified literal geometric value.  Distance values are expressed using
 * the Distance element.
 * </p>
 *
 * <p>
 * For now this code does not take into account the units of distance,  we will
 * assume that the filter units are the same as the geometry being filtered.
 * </p>
 *
 * <p></p>
 *
 * @author Chris Holmes, TOPP
 *
 *
 * @source $URL$
 * @version $Id$
 *
 * @task REVISIT: add units for distance.  Should it just be a string?  Or
 *       should it actually resolve the definition?
 *
 * @deprecated use {@link org.opengis.filter.spatial.DistanceBufferOperator}
 *
 */
public interface GeometryDistanceFilter extends GeometryFilter, DistanceBufferOperator {
    /**
     * Returns true if the passed in object is the same as this filter.  Checks
     * to make sure the filter types are the same as well as all three of the
     * values.
     *
     * @param obj The filter to test equality against.
     *
     * @return True if the objects are equal.
     */
    boolean equals(Object obj);

    /**
     * Sets the distance allowed by this filter.
     *
     * @param distance the length beyond which this filter is valid or not.
     *
     * @throws IllegalFilterException for problems setting the distance.
     */
    void setDistance(double distance) throws IllegalFilterException;

    /**
     * Determines whether or not a given feature is 'inside' this filter.
     *
     * @param feature Specified feature to examine.
     *
     * @return Flag confirming whether or not this feature is inside the
     *         filter.
     *
     * @deprecated use {@link org.opengis.filter.Filter#evaluate(Feature)}
     *
     */
    boolean contains(SimpleFeature feature);

    /**
     * Gets the distance allowed by this filter.
     *
     * @return distance the length beyond which this filter is valid or not.
     *
     * @deprecated use {@link DistanceBufferOperator#getDistance()}
     */
    double getDistance();
}
