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
package org.geotools.validation.attributes;

import org.geotools.validation.DefaultFeatureValidation;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * NullZeroFeatureValidation purpose.
 * 
 * <p>
 * Description of NullZeroFeatureValidation ...
 * </p>
 * 
 * <p>
 * Capabilities:
 * 
 * <ul>
 * <li>
 * Tests for null/0 atribute values.
 * </li>
 * </ul>
 * 
 * Example Use:
 * <pre><code>
 * NullZeroFeatureValidation x = new NullZeroFeatureValidation(...);
 * </code></pre>
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class NullZeroValidation extends DefaultFeatureValidation {
	/** XPATH expression for attribtue */
    private String attribute;

    public NullZeroValidation() {
        super();
    }

    /**
     * Implement validate.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param feature Provides the attributes to test.
     * @param type not used.
     * @param results a reference for returning error codes.
     *
     * @return false when null or 0 values are found in the attribute.
     *
     * @see org.geotools.validation.FeatureValidation#validate(org.geotools.feature.Feature,
     *      org.geotools.feature.FeatureType,
     *      org.geotools.validation.ValidationResults)
     */
    public boolean validate(SimpleFeature feature, SimpleFeatureType type,
        ValidationResults results) { // throws Exception {

    	//if attribute not set, just pass
    	if (attribute == null)
    		return true;
    	
        Object obj = feature.getAttribute(attribute);

        if (obj == null) {
            results.error(feature, attribute + " is Empty");

            return false;
        }

        if (obj instanceof Number) {
            Number number = (Number) obj;

            if (number.intValue() == 0) {
                results.error(feature, attribute + " is Zero");

                return false;
            }
        }

		if (obj instanceof String) {
			String string = (String) obj;

			if ("".equals(string.trim())) {
				results.error(feature, attribute + " is \"\"");

				return false;
			}
		}

        return true;
    }

    /**
     * Implement getPriority.
     *
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return 0;
    }

    /**
     * Implementation of getTypeNames.
     *
     * @return Array of typeNames, or empty array for all, null for disabled
     *
     * @see org.geotools.validation.Validation#getTypeRefs()
     */
    public String[] getTypeRefs() {
        if (getTypeRef() == null) {
            return null;
        }

        if (getTypeRef().equals("*")) {
            return ALL;
        }

        return new String[] { getTypeRef(), };
    }

    /**
     * Access attributeName property.
     *
     * @return the path being stored for validation
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * set AttributeName to xpath expression.
     *
     * @param xpath A String
     */
    public void setAttribute(String xpath) {
        attribute = xpath;
    }
}
