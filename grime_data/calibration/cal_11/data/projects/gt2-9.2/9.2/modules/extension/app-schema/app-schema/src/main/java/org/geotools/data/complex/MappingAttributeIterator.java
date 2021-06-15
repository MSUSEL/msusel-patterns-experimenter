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
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Iterator;

import org.geotools.data.Query;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.filter.XPathUtil.StepList;
import org.opengis.feature.type.Name;
/**
 * A Feature iterator that operates over the FeatureSource of a
 * {@linkplain org.geotools.data.complex.FeatureTypeMapping} that is of a simple content type, e.g.
 * representing a gml:name element. This is required for feature chaining for such types to reduce
 * the need of creating an additional database view when simple element values come from another
 * table. Therefore this iterator should have a method that return attributes that are to be chained
 * directly in another feature type.
 * 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/extension/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/MappingAttributeIterator.java $
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/MappingAttributeIterator.java $
 * @since 2.7
 */
public class MappingAttributeIterator extends DataAccessMappingFeatureIterator {
    /**
     * Name of the chained element, e.g. gml:name. 
     */
    private Name elementName;

    public MappingAttributeIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping,
            Query query, Query unrolledQuery) throws IOException {
        super(store, mapping, query, unrolledQuery);
        elementName = mapping.getTargetFeature().getName();
        checkAttributeMappings();
    }

    private void checkAttributeMappings() {
        // there should only be 1 attribute mapping for this element since it's a simple type
        // exception for FEATURE_LINK attributes
        Iterator<AttributeMapping> mappings = mapping.getAttributeMappings().iterator();
        AttributeMapping rootMapping = null;
        while (mappings.hasNext()) {
            AttributeMapping att = mappings.next();
            StepList xpath = att.getTargetXPath();
            if (XPath.equals(elementName, xpath)) {
                if (rootMapping != null) {
                    throw new RuntimeException("Duplicate AttributeMapping for: '" + elementName
                            + "' is found in FeatureTypeMapping '" + elementName + "'!");
                }
                rootMapping = att;
            } else if (!XPath.equals(ComplexFeatureConstants.FEATURE_CHAINING_LINK_NAME, xpath)) {
                // log warning
                String msg = "AttributeMapping for: '" + xpath + "' found in FeatureTypeMapping '"
                        + elementName
                        + "' ! This will be ignored as it doesn't belong to the type.";
                LOGGER.warning(msg);
            }
        }
        if (rootMapping == null) {
            // not found, throw exception
            throw new RuntimeException("AttributeMapping for: '" + elementName
                    + "' is missing in FeatureTypeMapping '" + elementName + "'!");
        }
    }
}
