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
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;

import org.geotools.data.FeatureSource;
import org.geotools.util.InterpolationProperties;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * A registry that stores all app schema data access instances per application. This allows
 * mappings from different data accesses to be accessed globally.
 * 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 *
 * @source $URL$
 */
public class AppSchemaDataAccessRegistry extends DataAccessRegistry {
    
    //NC - this class is only kept for backward compatibility, all the work is done in DataAccessRegistry

    private static final long serialVersionUID = -1517768637801603351L;
    
    //-------------------------------------------------------------------------------------
    // Static short-cut methods for convenience and backward compatibility
    //-----------------------------------------------------------------------------------
       
    /**
     * Return true if a type name is mapped in one of the registered app-schema data accesses. If
     * the type mapping has mappingName, then it will be the key that is matched in the search. If
     * it doesn't, then it will match the targetElementName.
     * 
     * @param featureTypeName
     *            Feature type name
     * @return
     * @throws IOException
     */
    public static boolean hasName(Name featureTypeName) throws IOException {
        return getInstance().hasAppSchemaAccessName(featureTypeName);
    }
    
    /**
     * Get a feature type mapping from a registered app-schema data access. Please note that this is
     * only possible for app-schema data access instances.
     * 
     * @param featureTypeName
     * @return feature type mapping
     * @throws IOException
     */
    public static FeatureTypeMapping getMappingByName(Name featureTypeName) throws IOException {
        return getInstance().mappingByName(featureTypeName);
    }

    public static FeatureTypeMapping getMappingByElement(Name featureTypeName) throws IOException {
        return getInstance().mappingByElement(featureTypeName);
    }

    /**
     * Get a feature source for simple features with supplied feature type name.
     * 
     * @param featureTypeName
     * @return feature source
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static FeatureSource<FeatureType, Feature> getSimpleFeatureSource(Name featureTypeName)
            throws IOException {
        return getMappingByElement(featureTypeName).getSource();
    }

    /**
     * Return true if a type name is mapped in one of the registered app-schema data accesses as
     * targetElementName, regardless whether or not mappingName exists.
     * 
     * @param featureTypeName
     * @return
     * @throws IOException
     */
    public static boolean hasTargetElement(Name featureTypeName) throws IOException {
        return getInstance().hasAppSchemaTargetElement(featureTypeName);
    }
    
    /**
     * Get App-schema properties
     * 
     * @return app-schema properties
     */
    public static InterpolationProperties getAppSchemaProperties() {
        return getInstance().getProperties();
    }
    
    /**
     * Clean-up properties, mainly used for cleaning up after tests
     */
    public static void clearAppSchemaProperties() {
        getInstance().clearProperties();
    }

}
