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

package org.geotools.feature.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.geotools.data.complex.ComplexFeatureConstants;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.util.InternationalString;

/**
 * This is a specialization of complex feature type that can be nested inside another feature type.
 * A system attribute descriptor called "FEATURE_LINK" is added in the descriptors so it can be used
 * to link the type to its parent type, without being encoded, since it doesn't exist in the real
 * schema.
 * 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 *
 *
 *
 *
 * @source $URL$
 */
public class ComplexFeatureTypeImpl extends UniqueNameFeatureTypeImpl {
    /**
     * Type specific descriptors, excluding FEATURE_LINK
     */
    private Collection<PropertyDescriptor> schema;

    /**
     * Constructor for complex feature type with fake feature type descriptor provided.
     * 
     * @param name
     *            Name of feature type
     * @param schema
     *            Schema property descriptors
     * @param defaultGeometry
     *            Default geometry
     * @param isAbstract
     *            True if this type is abstract
     * @param restrictions
     *            List of restrictions
     * @param superType
     *            Super type
     * @param description
     *            Feature description
     * @param featureLink
     *            System attribute used to link between features
     */
    public ComplexFeatureTypeImpl(Name name, Collection<PropertyDescriptor> schema,
            GeometryDescriptor defaultGeometry, boolean isAbstract, List<Filter> restrictions,
            AttributeType superType, InternationalString description) {
        super(name, new ArrayList<PropertyDescriptor>(schema) {
            private static final long serialVersionUID = 1L;

            {
                add(ComplexFeatureConstants.FEATURE_CHAINING_LINK);
            };
        }, defaultGeometry, isAbstract, restrictions, superType, description);

        this.schema = schema;
    }

    /**
     * Create a clone of an existing ComplexFeatureTypeImpl with new schema.
     * 
     * @param type
     *            Type to copy
     * @param schema
     *            Set of descriptors
     */
    public ComplexFeatureTypeImpl(ComplexFeatureTypeImpl type, Collection<PropertyDescriptor> schema) {
        super(type.name, schema, null, type.isAbstract, type.restrictions,
                (AttributeType) type.superType, type.description);
        this.schema = schema;
        this.userData.putAll(type.userData);
    }

    /**
     * Return all the descriptors that come from the schema, excluding the system descriptors, such
     * as 'FEATURE_LINK', used for linking features.
     * 
     * @return schema descriptors
     */
    public Collection<PropertyDescriptor> getTypeDescriptors() {
        return schema;
    }
}
