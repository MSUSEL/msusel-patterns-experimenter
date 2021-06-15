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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0.gml;

import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.xml.gml.ChoiceGeometryType;
import org.geotools.xml.gml.ChoiceGeometryTypeImpl;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Transform a provided SimpleFeatureType to a different CoordinteReferenceSystem.
 * 
 * @see ChoiceAttributeType
 * @author Jesse
 * @since 2.4
 * 
 *
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/wfs/src/main/java/org/geotools
 *         /xml/gml/WFSFeatureTypeTransformer.java $
 */
public class WFSFeatureTypeTransformer {

    public static SimpleFeatureType transform(SimpleFeatureType schema,
            CoordinateReferenceSystem crs) throws SchemaException {
        SimpleFeatureTypeBuilder build = new SimpleFeatureTypeBuilder();
        build.setName(schema.getName());

        GeometryDescriptor defaultGeometryType = null;
        for (int i = 0; i < schema.getAttributeCount(); i++) {
            AttributeDescriptor attributeType = schema.getDescriptor(i);
            if (attributeType instanceof ChoiceGeometryType) {
                defaultGeometryType = handleChoiceGeometryAttribute(schema, crs, build,
                        defaultGeometryType, attributeType);
            } else if (attributeType instanceof GeometryDescriptor) {
                defaultGeometryType = handleGeometryAttribute(schema, crs, build,
                        defaultGeometryType, attributeType);
            } else {
                build.add(attributeType);
            }
        }
        if (defaultGeometryType != null) {
            // Only try to set default geometry when there actually is a geometry type
            build.setDefaultGeometry(defaultGeometryType.getLocalName());
        }
        return build.buildFeatureType();
    }

    private static GeometryDescriptor handleGeometryAttribute(SimpleFeatureType schema,
            CoordinateReferenceSystem crs, SimpleFeatureTypeBuilder factory,
            GeometryDescriptor defaultGeometryType, AttributeDescriptor attributeType) {
        GeometryDescriptor geometryType = (GeometryDescriptor) attributeType;
        GeometryDescriptor geometry;

        AttributeTypeBuilder builder = new AttributeTypeBuilder();
        builder.setName(geometryType.getLocalName());
        builder.setBinding(geometryType.getType().getBinding());
        builder.setNillable(geometryType.isNillable());

        // builder.setDefaultValue(defaultValue);
        builder.setCRS(crs);

        geometry = builder
                .buildDescriptor(geometryType.getLocalName(), builder.buildGeometryType());

        if (defaultGeometryType == null || geometryType == schema.getGeometryDescriptor()) {
            defaultGeometryType = geometry;
        }
        factory.add(geometry);
        return defaultGeometryType;
    }

    private static GeometryDescriptor handleChoiceGeometryAttribute(SimpleFeatureType schema,
            CoordinateReferenceSystem crs, SimpleFeatureTypeBuilder factory,
            GeometryDescriptor defaultGeometryType, AttributeDescriptor attributeType) {
        ChoiceGeometryTypeImpl geometryType = (ChoiceGeometryTypeImpl) attributeType;
        ChoiceGeometryTypeImpl geometry;

        geometry = new ChoiceGeometryTypeImpl(geometryType.getName(), geometryType.getChoices(),
                geometryType.getBinding(), geometryType.isNillable(), geometryType.getMinOccurs(),
                geometryType.getMaxOccurs(), geometryType.createDefaultValue(), crs, geometryType
                        .getRestrictions());

        if (defaultGeometryType == null || geometryType == schema.getGeometryDescriptor()) {
            defaultGeometryType = geometry;
        }
        factory.add(geometry);
        return defaultGeometryType;
    }

}
