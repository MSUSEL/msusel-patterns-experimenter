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
package org.geotools.data.shapefile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

/**
 * 
 *
 * @source $URL$
 */
public class ShapefileAttributeReaderTest extends TestCaseSupport {

	public final String STATEPOP = "shapes/statepop.shp";

	public ShapefileAttributeReaderTest(String name) throws IOException {
		super(name);
	}

	public void testAttributeReader() throws IOException {
	        File shpFile =  copyShapefiles(STATEPOP);
	        URL url = shpFile.toURI().toURL();
		ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
		
		//creates both indexed and regular shapefile data store
		IndexedShapefileDataStore indexedstore = new IndexedShapefileDataStore(url);
		ShapefileDataStore store = new ShapefileDataStore(url);

		//get a random feature id from one of the stores
		SimpleFeatureIterator it = indexedstore.getFeatureSource().getFeatures().features();
                FeatureId fid = it.next().getIdentifier();
                it.close();
		
		//query the datastore
		FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
		Filter idFilter = ff.id(Collections.singleton(fid));
		final Query query = new DefaultQuery(indexedstore.getSchema().getName().getLocalPart(), idFilter, new String[] { "STATE_NAME"});
		final SimpleFeatureCollection indexedfeatures = indexedstore.getFeatureSource().getFeatures(query);
		final SimpleFeatureCollection features = store.getFeatureSource().getFeatures(query);
		
		// compare the results
		SimpleFeatureIterator indexIterator = indexedfeatures.features();
		SimpleFeature indexedFeature = indexIterator.next();
		indexIterator.close();
		
		SimpleFeatureIterator iterator = features.features();
		SimpleFeature feature = iterator.next();
		iterator.close();
		
		String indexedStateName = (String) indexedFeature.getAttribute("STATE_NAME");
		String stateName = (String) feature.getAttribute("STATE_NAME");

		// System.out.println(indexedStateName);
		// System.out.println(stateName);

		assertEquals(indexedStateName, stateName);
		store.dispose();
		indexedstore.dispose();
	}

}
