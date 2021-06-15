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
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.ng;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;

import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.util.KVP;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

import static org.geotools.data.shapefile.ng.ShapefileDataStoreFactory.*;

/**
 * Test the functionality of ShapefileDataStoreFactory; specifically the handling of 
 * connection parameters.
 *
 * @author Jody Garnett
 */
public class ShapefileDataStoreFactoryTest extends TestCaseSupport {

    private ShapefileDataStore store = null;
    private ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
    
    public ShapefileDataStoreFactoryTest(String testName) throws IOException {
        super(testName);
    }
    @Override
    protected void tearDown() throws Exception {
        if(store != null) {
                store.dispose();
        }
        super.tearDown();
    }
    
    @Test
    public void testFSTypeParameter() throws Exception {
        URL url = TestData.url(STATE_POP);
        
        KVP params = new KVP( URLP.key,url );
        
        assertTrue( "Sorting is optional", factory.canProcess(params) );
        
        params.put( FSTYPE.key, "shape-ng" );
        assertTrue( "Shape NG supported", factory.canProcess(params) );
        
        params.put(FSTYPE.key, "shape" );
        assertFalse( "Plain shape not supported", factory.canProcess(params) );
        
        params.put(FSTYPE.key, "index" );
        assertFalse( "Plain index not supported", factory.canProcess(params) );
        
        params.put( FSTYPE.key, "smurf" );
        assertFalse( "Feeling blue; don't try a smruf", factory.canProcess(params) );
    }
    @Test
    public void testQueryCapabilities() throws Exception {
        URL url = TestData.url(STATE_POP);
        
        Map params = new KVP( URLP.key,url );
        DataStore dataStore = factory.createDataStore( params );
        Name typeName = dataStore.getNames().get(0);
        SimpleFeatureSource featureSource = dataStore.getFeatureSource( typeName);
        
        QueryCapabilities caps = featureSource.getQueryCapabilities();
        
        SortBy[] sortBy = new SortBy[]{SortBy.NATURAL_ORDER,};
        assertTrue( "Natural", caps.supportsSorting( sortBy ));
        
        SimpleFeatureType schema = featureSource.getSchema();
        String attr = schema.getDescriptor(1).getLocalName();
        
        sortBy[0] = ff.sort( attr, SortOrder.ASCENDING );
        assertTrue( "Sort "+attr, caps.supportsSorting( sortBy ));
        
        sortBy[0] = ff.sort( "the_geom", SortOrder.ASCENDING );
        assertFalse( "Cannot sort the_geom", caps.supportsSorting( sortBy ));
    }
}
