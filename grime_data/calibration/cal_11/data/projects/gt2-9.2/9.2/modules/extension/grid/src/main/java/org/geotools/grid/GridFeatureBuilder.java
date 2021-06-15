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
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid;

import java.util.Map;

import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Controls the creating of features representing grid elements during vector
 * grid construction.
 * <pre><code>
 * GridFeatureBuilder builder = new GridFeatureBuilder(myFeatureType) {
 *     private int id = 0;
 *
 *     public setAttributes(GridElement el, Map<String, Object> attributes) {
 *         // assumes "id" and "value" are valid property names for
 *         // the feature type
 *         attributes.put("id", ++id);
 *         attributes.put("value", myValueGettingFunction(el.toPolygon()));
 *     }
 * };
 * </code></pre>
 *
 * @author mbedward
 * @since 2.7
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public abstract class GridFeatureBuilder {

    /** Default name for the geometry attribute: "element" */
    public static final String DEFAULT_GEOMETRY_ATTRIBUTE_NAME = "element";

    private final SimpleFeatureType TYPE;

    /**
     * Creates a {@code GridFeatureBuilder} to work with the given feature type.
     *
     * @param type the feature type
     */
    public GridFeatureBuilder(SimpleFeatureType type) {
        this.TYPE = type;
    }

    /**
     * Gets the feature type. 
     *
     * @return the feature type
     */
    public SimpleFeatureType getType() {
        return TYPE;
    }

    /**
     * Sets the values of attributes for a new {@code SimpleFeature} being
     * constructed from the given {@code GridElement}.
     * <p>
     * This method must be overridden by the user. It is called by the grid
     * building classes as each new feature is constructed.
     *
     * @param el the element from which the new feature is being constructed
     *
     * @param attributes a {@code Map} with attribute names as keys and
     *        attribute values as values
     */
    public abstract void setAttributes(GridElement el, Map<String, Object> attributes);

    /**
     * Gets the {@code FeatureID} as a {@code String} for a new {@code SimpleFeature}
     * being constructed from the given {@code GridElement}.
     * <p>
     * It is optional to override this method. The base implementation returns
     * {@code null}.
     *
     * @param el the element from which the new feature is being constructed
     *
     * @return value to use as the feature ID
     */
    public String getFeatureID(GridElement el) {
        return null;
    }

    /**
     * Tests whether a feature will be constructed for the given {@code GridElement}.
     * This can be overriden to create vector grids with 'holes' where elements are not
     * required, for example, based on location or the relationship to other data layers.
     * <p>
     * The base implementation always returns {@code true}.
     *
     * @param el the element from which the new feature would be constructed
     *
     * @return {@code true} to create a feature for the element; {@code false}
     *         to skip the element
     */
    public boolean getCreateFeature(GridElement el) {
        return true;
    }

}
