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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid.ortholine;

import java.util.Map;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.grid.GridElement;
import org.geotools.grid.GridFeatureBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;

/**
 * A basic implementation of {@code LineFeatureBuilder} which will create a
 * {@code SimpleFeatureType} having two properties:
 * <ul>
 * <li>element - TYPE LineString
 * <li>id - TYPE Integer
 * </ul>
 * The attribute names can also be referred to using
 * {@linkplain LineFeatureBuilder#DEFAULT_GEOMETRY_ATTRIBUTE_NAME} and
 * {@linkplain #ID_ATTRIBUTE_NAME}
 * <p>
 * Line elements will be assigned sequential id values starting with 1.
 *
 * @author mbedward
 * @since 2.7
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class OrthoLineFeatureBuilder extends GridFeatureBuilder {
    /** Default feature TYPE name: "linegrid" */
    public static final String DEFAULT_TYPE_NAME = "linegrid";

    /** Name used for the integer id attribute: "id" */
    public static final String ID_ATTRIBUTE_NAME = "id";
    
    /**
     * Name of the Integer level attribute ("level")
     */
    public static final String LEVEL_ATTRIBUTE_NAME = "level";

    /**
     * Name of the Object value attribute ("value")
     */
    public static final String VALUE_ATTRIBUTE_NAME = "value";

    protected int id;

    /**
     * Creates the feature TYPE
     *
     * @param typeName name for the feature TYPE; if {@code null} or empty,
     *        {@linkplain #DEFAULT_TYPE_NAME} will be used
     *
     * @param crs coordinate reference system (may be {@code null})
     *
     * @return the feature TYPE
     */
    protected static SimpleFeatureType createType(String typeName, CoordinateReferenceSystem crs) {
        final String finalName;
        if (typeName != null && typeName.trim().length() > 0) {
            finalName = typeName;
        } else {
            finalName = DEFAULT_TYPE_NAME;
        }

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(finalName);
        tb.add(DEFAULT_GEOMETRY_ATTRIBUTE_NAME, LineString.class, crs);
        tb.add(ID_ATTRIBUTE_NAME, Integer.class);
        tb.add(VALUE_ATTRIBUTE_NAME, Object.class);
        tb.add(LEVEL_ATTRIBUTE_NAME, Integer.class);
        
        return tb.buildFeatureType();
    }

    /**
     * Creates a new instance with a feature TYPE having the default name
     * and a null coordinate reference system.
     *
     * @see #DEFAULT_TYPE_NAME
     */
    public OrthoLineFeatureBuilder() {
        this(DEFAULT_TYPE_NAME, null);
    }

    /**
     * Creates a new instance with a null coordinate reference system.
     *
     * @param typeName name for the feature TYPE; if {@code null} or empty,
     *        {@linkplain #DEFAULT_TYPE_NAME} will be used
     */
    OrthoLineFeatureBuilder(String typeName) {
        this(typeName, null);
    }

    /**
     * Creates a new instance with a feature TYPE having the default name
     * and the supplied coordinate reference system.
     *
     * @param crs coordinate reference system (may be {@code null})
     *
     * @see #DEFAULT_TYPE_NAME
     */
    public OrthoLineFeatureBuilder(CoordinateReferenceSystem crs) {
        this(DEFAULT_TYPE_NAME, crs);
    }

    /**
     * Creates a new instance.
     *
     * @param typeName name for the feature TYPE; if {@code null} or empty,
     *        {@linkplain #DEFAULT_TYPE_NAME} will be used
     *
     * @param crs coordinate reference system (may be {@code null})
     */
    public OrthoLineFeatureBuilder(String typeName, CoordinateReferenceSystem crs) {
        super(createType(typeName, crs));
        id = 0;
    }

    /**
     * Sets the following attributes in the provided {@code Map}:
     * <ul>
     * <li>id: sequential integer</li>
     * <li>level: integer level of associated with the element</li>
     * <li>value: X-ordinate for a vertical line; Y-ordinate for a horizontal line</li>
     * </ul>
     * 
     * @param el the element from which the new feature is being constructed
     * @param attributes a {@code Map} into which the attributes will be put
     */
    @Override
    public void setAttributes(GridElement el, Map<String, Object> attributes) {
        if (el instanceof OrthoLine) {
            OrthoLine orthoLine = (OrthoLine) el;
            attributes.put(ID_ATTRIBUTE_NAME, ++id);
            attributes.put(LEVEL_ATTRIBUTE_NAME, orthoLine.getLevel());
            
            Coordinate v0 = orthoLine.getVertices()[0];
            Double value = null;
            if (orthoLine.getOrientation() == LineOrientation.HORIZONTAL) {
                value = v0.y;
            } else {
                value = v0.x;
            }
            attributes.put(VALUE_ATTRIBUTE_NAME, value);
            
        } else {
            throw new IllegalArgumentException("Expected an instance of OrthoLine");
        }
    }

}
