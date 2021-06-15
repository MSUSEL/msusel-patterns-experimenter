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
package org.geotools.api.validation;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.validation.DefaultFeatureResults;
import org.geotools.validation.ValidationProcessor;
import org.geotools.validation.spatial.IsValidGeometryValidation;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

@SuppressWarnings("unused")
public class ValidationExamples {

private void validationExample() throws Exception{
    SimpleFeatureCollection roadFeatures = null;
    SimpleFeatureCollection riverFeatures = null;

    // validationExample start
    MemoryDataStore store;
    store = new MemoryDataStore();
    store.addFeatures( roadFeatures );
    store.addFeatures( riverFeatures );
    
    //
    // SETUP
    //
    ValidationProcessor processor = new ValidationProcessor(); 

    // normally you load definition from file
    // Here we are doing it by hand    
    IsValidGeometryValidation geom = new IsValidGeometryValidation();
    geom.setName( "IsValidGeometry");
    geom.setDescription("IsValid geomtry check");
    geom.setTypeRef("*"); // works on any feature type
    processor.addValidation( geom );

    //
    // TESTING
    //
    
    // Create a ValidationResults callback object to receive 
    // any warnings or errors
    //
    // Normally you implement this as a callback to your application;
    // here we will use a default implementation here that adds results to a list
    DefaultFeatureResults results = new DefaultFeatureResults();

    // test a featureCollection 
    processor.runFeatureTests( "dataStoreId", roadFeatures, results);

    // and check the results
    System.out.println("Found "+ results.error.size() + " failires" );
    // validationExample end
}

private void validationExample2() throws Exception {
    File pluginDirectory = null;
    File testSuiteDirectory = null;
    SimpleFeatureSource lakesFeatureSource = null;
    SimpleFeatureSource streamsFeatureSource = null;

    // validationExample2 start

    // set up a validation processor using two directories of
    // configuraiton files
    ValidationProcessor processor = new ValidationProcessor(); 
    processor.load( pluginDirectory, testSuiteDirectory );

    // normally you load definition from file
    // it will load all the files in the provided directories
    processor.load( pluginDirectory, testSuiteDirectory );

    DefaultFeatureResults results = new DefaultFeatureResults();

        // To run integrity tests (that compare several featureSources we need
    // to make a Map of FeatureSources.
    // the *key* is called the "typeRef" and will be used by test suites
    // to refer to look up a featureSource as needed
    Map<String,SimpleFeatureSource> map = new HashMap<String,SimpleFeatureSource>();
    
    // register in map so validation processor can find it
    map.put( "LAKES:lakes",  lakesFeatureSource );
    map.put( "STREAMS:streams", streamsFeatureSource );
    // optional shortlist of layers to check (these are usually the layers your
    // user modified)
    Set<Name> check = new HashSet<Name>();
    check.add( new NameImpl("LAKES:lakes"));
    
    processor.runIntegrityTests( check, map, null, results);

    // and check the results
    System.out.println("Found "+ results.error.size() + " failires" );
    // validationExample2 end
}
}
