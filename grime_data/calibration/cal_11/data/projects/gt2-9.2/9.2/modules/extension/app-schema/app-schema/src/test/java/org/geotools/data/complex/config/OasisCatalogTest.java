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
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.config;

import java.net.URL;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.tools.ResolvingXMLReader;
import org.geotools.test.AppSchemaTestSupport;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * 
 *
 * @source $URL$
 */
public class OasisCatalogTest extends AppSchemaTestSupport {
    org.apache.xml.resolver.Catalog catalog;
    
    @Before
    public void setUp() throws Exception {
        catalog = new Catalog();
        catalog.setupReaders();
    }

    @Test
    public void testParseCatalog() throws Exception{
        URL file = TestData.url(this, "commonSchemas_new.oasis.xml");

        ResolvingXMLReader reader = new ResolvingXMLReader();
        Catalog catalog = reader.getCatalog();
        catalog.getCatalogManager().setVerbosity(9);

        catalog.parseCatalog(file);
        
        final URL baseUri = new URL("http://schemas.opengis.net/gml/");
        //the system override defined in the catalog
        final URL override = new URL("file:///schemas/gml/trunk/gml/");
        
        final String extraPath = "3.1.1/basicTypes.xsd";
        final String uri = new URL(baseUri, extraPath).toExternalForm();
        final String expected = new URL(override, extraPath).toExternalForm();

        String resolved = catalog.resolveSystem(uri);
        assertNotNull(resolved);

        final String actual = new URL(resolved).toExternalForm();        
        assertEquals(expected, actual);
    }
}
