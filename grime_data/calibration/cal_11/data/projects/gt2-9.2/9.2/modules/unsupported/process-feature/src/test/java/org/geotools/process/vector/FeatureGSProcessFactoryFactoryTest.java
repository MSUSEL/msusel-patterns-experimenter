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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.process.Process;
import org.geotools.process.Processors;
import org.geotools.process.vector.AggregateProcess.AggregationFunction;
import org.geotools.process.vector.AggregateProcess.Results;
import org.geotools.test.TestData;
import org.geotools.util.KVP;
import org.geotools.util.NullProgressListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 *
 * @source $URL$
 */
public class FeatureGSProcessFactoryFactoryTest {

    DataStore bugs;
    
    @Before
    public void setup() throws IOException {
        File file = TestData.file(this, null );
        bugs = new PropertyDataStore( file );
    }
    @After
    public void tearDown(){
        bugs.dispose();
    }
    
    @Test
    public void testSum() throws Exception {
        SimpleFeatureSource source = bugs.getFeatureSource("bugsites");
        
        Map<String,Object> input = new KVP(
                "features", source.getFeatures(),
                "aggregationAttribute","cat",
                "function",EnumSet.of(AggregationFunction.Sum),
                "singlePass", true);
        
        NameImpl name = new NameImpl("vec","Aggregate");
        Process process = Processors.createProcess( name );
        assertNotNull("aggregateProcess not found", process);
        NullProgressListener monitor = new NullProgressListener();
        Map<String, Object> output = process.execute(input, monitor );
        
        Results result = (Results) output.get("result");
        assertTrue( result.sum > 0 );
    }
    
}
