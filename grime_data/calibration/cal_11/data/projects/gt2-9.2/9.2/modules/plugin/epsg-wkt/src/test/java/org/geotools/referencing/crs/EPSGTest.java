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
package org.geotools.referencing.crs;

// J2SE dependencies
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.test.TestData;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * These EPSG support.
 *
 * @author Jody Garnett
 * @since 2.1.M3
 *
 *
 * @source $URL$
 * @version 2.1.M3
 */
public class EPSGTest extends TestCase {
    EPSGCRSAuthorityFactory factory;
    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        factory = new EPSGCRSAuthorityFactory();
    }
    public void testAuthority(){
        Citation authority = factory.getAuthority();

        assertNotNull( authority );
        assertEquals( "European Petroleum Survey Group", authority.getTitle().toString() );
        assertTrue( Citations.identifierMatches(authority, "EPSG" ) );
    }

    public void testVendor(){
        Citation vendor = factory.getVendor();
        assertNotNull( vendor );
        assertEquals( "Geotools", vendor.getTitle().toString() );
    }

    public void testCodes() throws Exception {
        Set codes = factory.getAuthorityCodes( CoordinateReferenceSystem.class );

        assertNotNull( codes );
        assertEquals(4891, codes.size() );
    }

    /**
     * A random CRS for fun.
     */
    public void test26910() throws Exception {
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) factory.createObject("EPSG:26910");
        assertNotNull( crs );
    }

    /** UDIG requires this to work */
    public void test4326() throws Exception {
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) factory.createObject("EPSG:4326");
        assertNotNull( crs );
    }
    /** UDIG requires this to work */
    public void test4269() throws Exception {
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) factory.createObject("EPSG:4269");
        assertNotNull( crs );
    }
    /** UDIG requires this to work */
    public void test42102() throws Exception {
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) factory.createObject("EPSG:42102");
        assertNotNull( crs );
        assertNotNull(crs.getIdentifiers());
        assertTrue(crs.getIdentifiers().size()>0);
        NamedIdentifier expected = new NamedIdentifier(Citations.EPSG, "42102");
        assertTrue( crs.getIdentifiers() .contains( expected ));
    }
    
    public void testDecodeAll() throws Exception {
        // this test is heavy, only enable with extensive tests
        if (!TestData.isExtensiveTest()) {
            return;
        }
        
        Set codes = factory.getAuthorityCodes( CoordinateReferenceSystem.class );
        int total = codes.size();
        int count = 0;

        for( Iterator i=codes.iterator(); i.hasNext(); ){
            CoordinateReferenceSystem crs;
            String code = (String) i.next();
            try {
                crs = (CoordinateReferenceSystem) factory.createObject( code );
            } catch(Exception e) {
                e.printStackTrace();
                fail("Failed to decode " + code + " with error: " + e.getMessage());
            }
        }
    }



    /**
     * A random CRS for fun.
     */
    public void test26910Lower() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("epsg:26910");
        assertNotNull( crs );
    }

    /**
     * A random CRS for fun.
     */
    public void test26986Lower() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("epsg:26986");
        assertNotNull( crs );
    }

    /** wfs requires this to work */
    public void test4326Lower() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("epsg:4326");
        assertNotNull( crs );
    }
    /** wfs requires this to work */
    public void test26742Lower() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("epsg:26742");
        assertNotNull( crs );
    }
    /** wfs requires this to work */
    public void test4269Lower() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("epsg:4269");
        assertNotNull( crs );
    }
    /** wfs requires this to work */
    public void test42304Lower() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("epsg:42304");
        assertNotNull( crs );
    }
    /** wfs requires this to work */
    public void test42102Lower() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("epsg:42102");
        assertNotNull( crs );
        assertNotNull(crs.getIdentifiers());
        assertTrue(crs.getIdentifiers().size()>0);
        NamedIdentifier expected = new NamedIdentifier(Citations.EPSG, "42102");
        assertTrue( crs.getIdentifiers() .contains( expected ));
    }
}
