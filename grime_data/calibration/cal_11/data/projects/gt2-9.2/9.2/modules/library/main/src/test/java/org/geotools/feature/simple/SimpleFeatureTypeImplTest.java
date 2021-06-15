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
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.feature.simple;

import java.util.ArrayList;
import java.util.Collections;

import junit.framework.TestCase;

import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Point;

/**
 * Tests for {@link SimpleFeatureImpl}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 *
 *
 *
 * @source $URL$
 */
public class SimpleFeatureTypeImplTest extends TestCase {

    /**
     * Test that properties returned by getAttributeDescriptors() and getDescriptors() are the same
     * and in the same order.
     */
    public void testConsistentIterationOrder() {
        SimpleFeatureType type = buildLocationCountType();
        assertEquals(
                "FeatureType and SimpleFeatureType APIs must return the same descriptors in the same order",
                new ArrayList<PropertyDescriptor>(type.getAttributeDescriptors()),
                new ArrayList<PropertyDescriptor>(type.getDescriptors()));
    }

    /**
     * Test that property order is significant to equals().
     */
    public void testOrderSignificantEquals() {
        SimpleFeatureType type1 = buildLocationCountType();
        SimpleFeatureType type2 = buildCountLocationType();
        assertFalse("Simple feature types with properties in a different order must not be equal",
                type1.equals(type2));
    }

    /**
     * Test that distinct instances identically constructed are equal (location/count version).
     */
    public void testLocationCountEquals() {
        assertEquals("Identical simple feature types must be equal", buildLocationCountType(),
                buildLocationCountType());
    }

    /**
     * Test that distinct instances identically constructed are equal (count/location version).
     */
    public void testCountLocationEquals() {
        assertEquals("Identical simple feature types must be equal", buildCountLocationType(),
                buildCountLocationType());
    }

    /**
     * @return a simple feature type with location before count
     */
    private SimpleFeatureType buildLocationCountType() {
        SimpleFeatureTypeBuilder builder = buildPartialBuilder();
        builder.add("location", Point.class, (CoordinateReferenceSystem) null );
        builder.add("count", Integer.class);
        return builder.buildFeatureType();
    }

    /**
     * @return a simple feature type with count before location
     */
    private SimpleFeatureType buildCountLocationType() {
        SimpleFeatureTypeBuilder builder = buildPartialBuilder();
        builder.add("count", Integer.class);
        builder.add("location", Point.class, (CoordinateReferenceSystem) null);
        return builder.buildFeatureType();
    }

    /**
     * @return a simple feature type builder that is ready for the addition of location and count
     *         properties
     */
    private SimpleFeatureTypeBuilder buildPartialBuilder() {
        String uri = "http://example.org/things";
        FeatureTypeFactoryImpl typeFactory = new FeatureTypeFactoryImpl();
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder(typeFactory);
        builder.addBinding(typeFactory.createGeometryType(new NameImpl(uri, "description"),
                String.class, null, false, false, Collections.EMPTY_LIST, null, null));
        builder.addBinding(typeFactory.createGeometryType(new NameImpl(uri, "location"),
                Point.class, null, false, false, Collections.EMPTY_LIST, null, null));
        builder.addBinding(typeFactory.createAttributeType(new NameImpl(uri, "count"),
                Integer.class, false, false, Collections.EMPTY_LIST, null, null));
        builder.setName("ThingsType");
        builder.setNamespaceURI(uri);
        builder.add("description", String.class);
        return builder;
    }

}
