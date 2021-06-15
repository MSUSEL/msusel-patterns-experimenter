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
package org.geotools.validation;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

import org.geotools.data.DefaultRepository;
import org.geotools.data.Repository;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.test.TestData;
import org.geotools.validation.xml.XMLReader;


/**
 * A proper test fixture for the ValidationProcessor (and friends to hit).
 * 
 * <p>
 * For geoserver developers this environment is similar to confDemo. Where
 * possible names have been forced to agree with geoserver.
 * </p>
 * <p>
 * In the interests of saving space the contents of confDemo have been reduced to:
 * <ul>
 * <li>lakes - MultiPolygon
 * <li>streams - MultiLinestring
 * <li>swamps - MultiPolygon
 * <li>rivers - MultiPolygon
 * </ul>
 * The complete confDemo also includes a large road and building shapefile.
 * </p>
 * @author Jody Garnett
 *
 *
 * @source $URL$
 */
public class TestFixture {
    Repository data = new DefaultRepository();
    public Map pluginDTOs;
    public Map testSuiteDTOs;
    public ValidationProcessor processor;
    public DefaultRepository repository;

    /** Starting point for your own TestFixture */
    public TestFixture( File pluginDir, File testDir, File propertiesFile ) throws Exception {
        repository = new DefaultRepository();
        repository.load( propertiesFile );

        pluginDTOs = XMLReader.loadPlugIns( pluginDir);
        testSuiteDTOs = XMLReader.loadValidations( testDir, pluginDTOs);
        processor = new ValidationProcessor();
        processor.load(pluginDTOs, testSuiteDTOs);
    }
    /**
     * Sets up a test fixture based on GeoServer confDemo.
     * <p>
     * The shapefiles are copied into MemoryDataStores - allowing tests to be more
     * abusive.
     * </p>
     */
    public TestFixture() throws Exception {
        repository = new DefaultRepository();        
        File directory = TestData.file( this, "shapefiles" );
        String shapefiles[] = directory.list( new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return name.toUpperCase().endsWith(".SHP"); 
			}        
        });
        for( int i=0; i<shapefiles.length;i++){
        	File shapefile = new File( directory, shapefiles[i] );
        	ShapefileDataStore dataStore = new ShapefileDataStore( shapefile.toURI().toURL() );
        	String dataStoreId = dataStore.getTypeNames()[0].toUpperCase();
        	String typeName = dataStore.getTypeNames()[0];
        	SimpleFeatureIterator features = dataStore.getFeatureSource( typeName ).getFeatures().features();
        	MemoryDataStore cache = new MemoryDataStore( features );
        	
        	repository.register( dataStoreId, cache );
        }
        File pluginDir = TestData.file(this, "plugins");
        File validationDir = TestData.file(this, "validation" );
        
        pluginDTOs = XMLReader.loadPlugIns( pluginDir );
        testSuiteDTOs = XMLReader.loadValidations( validationDir, pluginDTOs);
        
        processor = new ValidationProcessor();
        processor.load(pluginDTOs, testSuiteDTOs);
    }
}
