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
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import java.io.IOException;
import java.net.URL;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertSame;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.test.AppSchemaTestSupport;

import org.junit.Test;

/**
 * To test client properties of app-schema mapping file
 * 
 * @author Jacqui Githaiga (Curtin University of Technology)
 *
 *
 *
 * @source $URL$
 */
public class XlinkMissingNamespaceTest extends AppSchemaTestSupport {
    /**
     * Illustrates that if xlink namespace has not been declared in the app-schema mapping file,the
     * client property is set as if for href (in the no-name namespace).
     * 
     * This test shows correct behaviour by throwing an exception reporting the undeclared
     * namespace.
     * 
     * @throws IllegalArgumentException
     */
    @Test
    public void testGetClientProperties() throws IOException {
        try {
            XMLConfigDigester reader = new XMLConfigDigester();

            URL url = getClass().getResource("/test-data/MappedFeatureMissingNamespaceXlink.xml");
            AppSchemaDataAccessDTO config = reader.parse(url);
            AppSchemaDataAccessConfigurator.buildMappings(config);
            fail("No exception caught");

        } catch (Exception ex) {
            assertSame(java.io.IOException.class, ex.getClass());
        }
    }
}
