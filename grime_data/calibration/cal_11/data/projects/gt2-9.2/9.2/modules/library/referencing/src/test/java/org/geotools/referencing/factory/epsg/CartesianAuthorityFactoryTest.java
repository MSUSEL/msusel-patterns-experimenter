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
package org.geotools.referencing.factory.epsg;

import static org.junit.Assert.*;

import java.util.Set;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.wkt.Formattable;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * 
 *
 * @source $URL$
 */
public class CartesianAuthorityFactoryTest {
    
    static final String CODE = CartesianAuthorityFactory.GENERIC_2D_CODE;
    
    @Test 
    public void testCodeInList() {
        Set<String> supportedCodes = CRS.getSupportedCodes("EPSG");
        assertTrue(supportedCodes.contains(CODE));
    }

    @Test
    public void testDecode() throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:" + CODE);
        assertTrue(CRS.equalsIgnoreMetadata(DefaultEngineeringCRS.GENERIC_2D, crs));
    }
    
    @Test
    public void testIdentifier() throws NoSuchAuthorityCodeException, FactoryException {
        Set<ReferenceIdentifier> identifiers = CartesianAuthorityFactory.GENERIC_2D.getIdentifiers();
        assertEquals(1, identifiers.size());
        final ReferenceIdentifier id = identifiers.iterator().next();
        assertEquals(Citations.EPSG, id.getAuthority());
        assertEquals(CODE, id.getCode());
    }
    
    @Test
    public void testLookup() throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:" + CODE);
        assertEquals(new Integer(CODE), CRS.lookupEpsgCode(crs, true));
        assertEquals(new Integer(CODE), CRS.lookupEpsgCode(crs, false));
    }
    
    @Test
    public void testTransform() throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateReferenceSystem epsg0 = CRS.decode("EPSG:" + CODE);
        CoordinateReferenceSystem epsg42101 = CRS.decode("EPSG:42101");
        
        assertTrue(CRS.findMathTransform(DefaultEngineeringCRS.GENERIC_2D, epsg42101).isIdentity());
        assertTrue(CRS.findMathTransform(epsg42101, DefaultEngineeringCRS.GENERIC_2D).isIdentity());
        
        assertTrue(CRS.findMathTransform(epsg0, epsg42101).isIdentity());
        assertTrue(CRS.findMathTransform(epsg42101, epsg0).isIdentity());
    }
    
    @Test
    public void testWKT() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:" + CODE);
        Formattable formattable = (Formattable) crs;
        String wkt = formattable.toWKT(Citations.EPSG, 2);
        
        final String lineSep=System.getProperty("line.separator", "\n");
        String expected = "LOCAL_CS[\"Wildcard 2D cartesian plane in metric unit\", " +lineSep + 
        		"  LOCAL_DATUM[\"Unknown\", 0], " +lineSep + 
        		"  UNIT[\"m\", 1.0], " +lineSep + 
        		"  AXIS[\"x\", EAST], " +lineSep + 
        		"  AXIS[\"y\", NORTH], " +lineSep + 
        		"  AUTHORITY[\"EPSG\",\"" + CODE + "\"]]";
        
        System.out.println(wkt);
        assertEquals(expected, wkt);
    }
}
