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
package org.geotools.feature;

import org.opengis.feature.simple.SimpleFeature;

/**
 * An interface for the construction of Features.
 * <p>
 * Geotools 2.0: As Features always require a FeatureType the best place
 * to implement this is in the FeatureType itself,
 * thus the FeatureType interface extends this interface.  Other
 * FeatureFactories may be implemented, but they  should probably be
 * constructed with a FeatureType.
 * </p>
 * <p>
 * Geotools 2.1: This class is under revision, pleae help out with feedback.
 * Please see experimental FeatureFactory2 (a subclass) for the current best
 * idea of what is needed.
 * </p>
 *
 *
 * @source $URL$
 * @version $Id$
 *
 * @task REVISIT: consider a static create(Object[] attributes,  String
 *       FeatureID, FeatureType type) method.
 * @task REVISIT: move these methods directly to FeatureType?  This would not
 *       allow independent FeatureFactories, but I'm not sure if those are
 *       useful at all.
 */
public interface FeatureFactory {
    /**
     * Creates a new feature, with a generated unique featureID.  This is less
     * than ideal, as a FeatureID should be persistant over time, generally
     * created by a datasource.  This method is more for testing that doesn't
     * need featureID.
     * 
     * @deprecated Schema information is required
     * @param attributes the array of attribute values
     * @return The created feature
     * @throws IllegalAttributeException if the FeatureType does not validate
     *         the attributes.
     */
    SimpleFeature create(Object[] attributes) throws IllegalAttributeException;

    /**
     * Creates a new feature, with the proper featureID.
     * 
     * @deprecated Schema information is required
     * @param attributes the array of attribute values.
     * @param featureID the feature ID.
     *
     * @return the created feature.
     *
     * @throws IllegalAttributeException if the FeatureType does not validate
     *         the attributes.
     */
    SimpleFeature create(Object[] attributes, String featureID)
        throws IllegalAttributeException;
}
