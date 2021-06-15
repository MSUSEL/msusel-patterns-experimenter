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
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.geotools.factory.AbstractFactory;
import org.geotools.factory.Hints;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.ConcatenatedOperation;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Makes sure the {@link testCoordinateOperationFactoryUsingWKT} is not ignored when
 * referencing is setup in "Longitude first" mode.
 * 
 * @author Oscar Fonts
 * @author Andrea Aime
 */
public class LongitudeFirstFactoryOverrideTest {
    
    private static final String DEFINITIONS_FILE_NAME = "epsg_operations.properties";
    private static Properties properties;
    
    private static final String SOURCE_CRS = "EPSG:TEST1";
    private static final String TARGET_CRS = "EPSG:TEST2";
    
    private static final double[] SRC_TEST_POINT = {39.592654167, 3.084896111};
    private static final double[] DST_TEST_POINT = {39.594235744481225, 3.0844689951999427};
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // force longitude first mode
        System.setProperty("org.geotools.referencing.forceXY", "true");
        
        CRS.reset("all");
        
        ReferencingFactoryFinder.addAuthorityFactory(
                new FactoryUsingWKT(null, AbstractFactory.MAXIMUM_PRIORITY));
        
        ReferencingFactoryFinder.addAuthorityFactory(new CoordinateOperationFactoryUsingWKT(null, AbstractFactory.MAXIMUM_PRIORITY));
        
        // Read definitions
        properties = new Properties();
        properties.load(this.getClass().getResourceAsStream(DEFINITIONS_FILE_NAME));
    }
    
    @After
    public void tearDown() {
        // unset axis ordering hint
        System.clearProperty("org.geotools.referencing.forceXY");
        Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        
        CRS.reset("all");
    }
    
    
    /**
     * Test method for {@link CoordinateOperationFactoryUsingWKT#createCoordinateOperation}.
     * @throws TransformException 
     */
    @Test
    public void testCreateOperationFromCustomCodes() throws Exception {
        // Test CRSs
        CoordinateReferenceSystem source = CRS.decode(SOURCE_CRS);
        CoordinateReferenceSystem target = CRS.decode(TARGET_CRS);
        MathTransform mt = CRS.findMathTransform(source, target, true);
        
        // Test MathTransform
        double[] p = new double[2];
        mt.transform(SRC_TEST_POINT, 0, p, 0, 1);
        assertEquals(p[0], DST_TEST_POINT[0], 1e-8);
        assertEquals(p[1], DST_TEST_POINT[1], 1e-8);
    }
    
    /**
     * Test method for {@link CoordinateOperationFactoryUsingWKT#createCoordinateOperation}.
     * @throws TransformException 
     */
    @Test
    public void testOverrideEPSGOperation() throws Exception {
        // Test CRSs
        CoordinateReferenceSystem source = CRS.decode("EPSG:4269");
        CoordinateReferenceSystem target = CRS.decode("EPSG:4326");
        MathTransform mt = CRS.findMathTransform(source, target, true);
        
        // Test MathTransform
        double[] p = new double[2];
        mt.transform(SRC_TEST_POINT, 0, p, 0, 1);
        assertEquals(p[0], DST_TEST_POINT[0], 1e-8);
        assertEquals(p[1], DST_TEST_POINT[1], 1e-8);
    }
    
    /**
     * Check we are actually using the EPSG database for anything not in override
     * 
     * @throws TransformException 
     */
    @Test
    public void testFallbackOnEPSGDatabase() throws Exception {
        // Test CRSs
        CoordinateReferenceSystem source = CRS.decode("EPSG:3003");
        CoordinateReferenceSystem target = CRS.decode("EPSG:4326");
        CoordinateOperation co = CRS.getCoordinateOperationFactory(true).createOperation(source, target);
        ConcatenatedOperation cco = (ConcatenatedOperation) co;
        // the EPSG one only has two steps, the non EPSG one 4
        assertEquals(2, cco.getOperations().size());
    }

    
}