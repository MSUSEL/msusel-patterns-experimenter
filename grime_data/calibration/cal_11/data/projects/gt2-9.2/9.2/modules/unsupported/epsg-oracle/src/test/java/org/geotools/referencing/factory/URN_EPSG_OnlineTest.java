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
package org.geotools.referencing.factory;

// OpenGIS dependencies
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

// Geotools dependencies
import org.geotools.util.Version;
import org.geotools.referencing.CRS;
import org.geotools.referencing.factory.epsg.oracle.OracleOnlineTestCase;


/**
 * Tests the {@link org.geotools.referencing.factory.URN_AuthorityFactory} with EPSG codes.
 *
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Justin Deoliveira
 * @author Martin Desruisseaux
 */
public class URN_EPSG_OnlineTest extends OracleOnlineTestCase {

    /**
     * Tests {@link AuthorityFactoryAdapter#isCodeMethodOverriden}.
     */
    public void testMethodOverriden() {
        final Versioned test = new Versioned();
        assertTrue(test.isCodeMethodOverriden());
    }

    /**
     * Tests the 4326 code.
     */
    public void test4326() throws FactoryException {
        CoordinateReferenceSystem expected = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem actual   = CRS.decode("urn:ogc:def:crs:EPSG:6.8:4326");
        assertSame(expected, actual);
        actual = CRS.decode("urn:x-ogc:def:crs:EPSG:6.8:4326");
        assertSame(expected, actual);
        actual = CRS.decode("urn:ogc:def:crs:EPSG:6.11:4326");
        assertSame(expected, actual);
    }

    /**
     * Tests versioning.
     */
    public void testVersion() throws FactoryException {
        CoordinateReferenceSystem expected = CRS.decode("EPSG:4326");
        final String version = String.valueOf(CRS.getVersion("EPSG"));
        final String urn = "urn:ogc:def:crs:EPSG:" + version + ":4326";
        final Versioned test = new Versioned();
        final int failureCount = FallbackAuthorityFactory.getFailureCount();
        assertNull(test.lastVersion);
        assertSame(expected, test.createCoordinateReferenceSystem(urn));
        assertEquals(version, test.lastVersion.toString());
        assertEquals("Primary factory should not fail.",
                failureCount, FallbackAuthorityFactory.getFailureCount());

        test.lastVersion = null;
        assertSame(expected, test.createCoordinateReferenceSystem(urn));
        assertNull("Should not create a new factory.", test.lastVersion);
        assertEquals("Primary factory should not fail.",
                failureCount, FallbackAuthorityFactory.getFailureCount());

        assertSame(expected, test.createCoordinateReferenceSystem("urn:ogc:def:crs:EPSG:6.11:4326"));
        assertEquals("6.11", test.lastVersion.toString());
        assertEquals("Should use the fallback factory.",
                failureCount + 1, FallbackAuthorityFactory.getFailureCount());
    }

    /**
     * A custom class for testing versioning.
     */
    private static final class Versioned extends URN_AuthorityFactory {
        public Version lastVersion;

        protected AuthorityFactory createVersionedFactory(final Version version)
                throws FactoryException
        {
            lastVersion = version;
            return super.createVersionedFactory(version);
        }
    }

}
