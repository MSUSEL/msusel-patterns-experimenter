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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 
 *
 * @source $URL$
 */
public class TeradataPrimaryKeyFinderTest extends JDBCPrimaryKeyFinderTest {

    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new TeradataPrimaryKeyFinderTestSetup(new TeradataTestSetup());
    }


    @Override
    public void testSequencedPrimaryKey() throws Exception {
      // sequences not a distinct type in teradata (at least as far as I can tell)
    }
    
    @Override
    public void testAssignedMultiPKeyView() throws Exception {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("assignedmultipk"));
        
        assertEquals( 2, fs.getPrimaryKey().getColumns().size() );
        assertTrue( fs.getPrimaryKey().getColumns().get(0) instanceof NonIncrementingPrimaryKeyColumn );
        assertTrue( fs.getPrimaryKey().getColumns().get(1) instanceof NonIncrementingPrimaryKeyColumn );
        
        FeatureIterator<?> i = fs.getFeatures().features();

        // On Teradata it's undered !!
        SimpleFeature f = (SimpleFeature) i.next();
        assertEquals( tname("assignedmultipk") + ".3.4" , f.getID() );
        f = (SimpleFeature) i.next();
        assertEquals( tname("assignedmultipk") + ".1.2" , f.getID() );
        f = (SimpleFeature) i.next();
        assertEquals( tname("assignedmultipk") + ".2.3" , f.getID() );
        
        i.close();
    }
}
