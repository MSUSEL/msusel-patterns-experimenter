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
package org.geotools.validation.spatial;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.validation.DefaultFeatureValidation;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Tests to see if a geometry is valid by calling Geometry.isValid().
 * 
 * <p>
 * The geometry is first tested to see if it is null, and if it is null,  then
 * it is tested to see if it is allowed to be null by calling isNillable().
 * </p>
 *
 * @author bowens, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class IsValidGeometryValidation extends DefaultFeatureValidation {
    /** The logger for the validation module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.validation");

    /**
     * IsValidGeometryFeatureValidation constructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public IsValidGeometryValidation() {
    }

    /**
     * Override getPriority.
     * 
     * <p>
     * Sets the priority level of this validation.
     * </p>
     *
     * @return A made up priority for this validation.
     *
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return PRIORITY_TRIVIAL;
    }

    /**
     * Override getTypeNames.
     * 
     * <p>
     * Returns the TypeNames of the FeatureTypes used in this particular
     * validation.
     * </p>
     *
     * @return An array of TypeNames
     *
     * @see org.geotools.validation.Validation#getTypeRefs()
     */
    public String[] getTypeNames() {
        if (getTypeRef() == null) {
            return null; // disabled
        } else if (getTypeRef().equals("*")) {
            return new String[0]; // apply to all
        } else {
            return new String[] { getTypeRef(), };
        }
    }

    /**
     * Tests to see if a geometry is valid by calling Geometry.isValid().
     * 
     * <p>
     * The geometry is first tested to see if it is null, and if it is null,
     * then it is tested to see if it is allowed to be null by calling
     * isNillable().
     * </p>
     *
     * @param feature The Feature to be validated
     * @param type The FeatureTypeInfo of the feature
     * @param results The storage for error messages.
     *
     * @return True if the feature is a valid geometry.
     *
     * @see org.geotools.validation.FeatureValidation#validate(
     *      org.geotools.feature.Feature,
     *      org.geotools.feature.FeatureType,
     *      org.geotools.validation.ValidationResults)
     */
    public boolean validate(SimpleFeature feature, SimpleFeatureType type,
        ValidationResults results) {
        Geometry geom = (Geometry) feature.getDefaultGeometry();

        if (geom == null) {
            if (type.getGeometryDescriptor().isNillable()) {
                LOGGER.log(Level.FINEST,
                    getName() + "(" + feature.getID() + ") passed");

                return true;
            } else {
                String message = "Geometry was null but is not nillable.";
                results.error(feature, message);
                LOGGER.log(Level.FINEST,
                    getName() + "(" + feature.getID() + "):" + message);

                return false;
            }
        }

        if (!geom.isValid()) {
            String message = "Not a valid geometry. isValid() failed";
            LOGGER.log(Level.FINEST,
                getName() + "(" + feature.getID() + "):" + message);
            results.error(feature, message);

            return false;
        }

        LOGGER.log(Level.FINEST, getName() + "(" + feature.getID() + ") passed");

        return true;
    }
}

