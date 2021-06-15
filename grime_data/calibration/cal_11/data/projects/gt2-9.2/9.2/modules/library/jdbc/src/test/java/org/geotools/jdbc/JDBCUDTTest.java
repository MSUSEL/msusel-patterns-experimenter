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
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 
 *
 * @source $URL$
 */
public abstract class JDBCUDTTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCUDTTestSetup createTestSetup();

    public void testSchema() throws Exception {
        SimpleFeatureType type = dataStore.getSchema(tname("udt"));
        assertNotNull(type);
        assertNotNull(type.getDescriptor(aname("ut")));
            
        assertEquals(String.class, type.getDescriptor(aname("ut")).getType().getBinding());
    }
    
    public void testRead() throws Exception {
        SimpleFeatureType type = dataStore.getSchema(tname("udt"));
            
        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("udt")).getFeatures();
        SimpleFeatureIterator fi = null;
        try {
            fi = features.features();
            assertTrue(fi.hasNext());
            assertEquals("12ab", fi.next().getAttribute(aname("ut")));
            assertFalse(fi.hasNext());
        } finally { 
            fi.close();
        }
        
    }

    public void testWrite() throws Exception {
        int count = dataStore.getFeatureSource(tname("udt")).getCount(Query.ALL);
        
        FeatureWriter w = dataStore.getFeatureWriterAppend(tname("udt"), Transaction.AUTO_COMMIT);
        w.hasNext();
        
        SimpleFeature f = (SimpleFeature) w.next();
        f.setAttribute(aname("ut"), "abcd");
        try {
            w.write();
            fail("Write should have failed with UDT constraint failure");
        }
        catch(Exception e) {
        }
        
        f.setAttribute(aname("ut"), "34cd");
        w.write();
        w.close();
        
        assertEquals(count+1, dataStore.getFeatureSource(tname("udt")).getCount(Query.ALL));
    }
}
