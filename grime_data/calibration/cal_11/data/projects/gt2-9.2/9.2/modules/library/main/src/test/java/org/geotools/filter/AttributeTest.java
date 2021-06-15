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
 *    
 *    Created on 20 June 2002, 18:53
 */
package org.geotools.filter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;


/**
 * Tests for Attribute Expressions
 *
 * @author James Macgill
 *
 *
 * @source $URL$
 */
public class AttributeTest extends TestCase {
    SimpleFeatureType schema = null;

    public AttributeTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(BetweenTest.class);

        return suite;
    }

    public SimpleFeature[] sampleFeatures() throws Exception {
    	SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
    	ftb.add("value", Integer.class);
    	ftb.add("geometry", Geometry.class);
    	ftb.add("name", String.class);
    	ftb.setName("test");
        schema = ftb.buildFeatureType();

        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        SimpleFeature[] f = new SimpleFeature[3];
        f[0] = SimpleFeatureBuilder.build(schema, new Object[] {
                    new Integer(12), gf.createGeometryCollection(null),
                    "first"
                }, null);
        f[1] = SimpleFeatureBuilder.build(schema, new Object[] {
                    new Integer(3), gf.createGeometryCollection(null),
                    "second"
                }, null);
        f[2] = SimpleFeatureBuilder.build(schema, new Object[] {
                    new Integer(15), gf.createGeometryCollection(null),
                    "third"
                }, null);

        return f;
    }

    public void testTypeMissmatch() throws Exception {
        SimpleFeature[] f = sampleFeatures();

        //the following are intentionaly backwards
        AttributeExpressionImpl e1 = new AttributeExpressionImpl(schema, "value");
        AttributeExpressionImpl e2 = new AttributeExpressionImpl(schema, "name");
        boolean pass = false;
        Object value = null;
        value = e1.getValue(f[0]);

        if (value instanceof Integer) {
            pass = true;
        }

        assertTrue("String expresion returned an Integer", pass);
        pass = false;

        value = e2.getValue(f[0]);

        if (value instanceof String) {
            pass = true;
        }

        assertTrue("Integer expresion returned a String", pass);
    }

    public void testSetupAndExtraction() throws Exception {
        //this should move out to a more configurable system run from scripts
        //but we can start with a set of hard coded tests
        SimpleFeature[] f = sampleFeatures();

        AttributeExpressionImpl e1 = new AttributeExpressionImpl(schema, "value");
        AttributeExpressionImpl e2 = new AttributeExpressionImpl(schema, "name");

        assertEquals(12d, ((Integer) e1.getValue(f[0])).doubleValue(), 0);
        assertEquals(3d, ((Integer) e1.getValue(f[1])).doubleValue(), 0);
        assertEquals(15d, ((Integer) e1.getValue(f[2])).doubleValue(), 0);
        assertEquals("first", (String) e2.getValue(f[0]));
        assertEquals("second", (String) e2.getValue(f[1]));
    }
}
