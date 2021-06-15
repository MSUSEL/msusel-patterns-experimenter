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
package org.geotools.data.georest;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.georest.GeoRestDataStoreFactory;
import org.geotools.data.georest.GeoRestFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;

/**
 * 
 *
 * @source $URL$
 */
public class DataStoreTest extends TestCase {

    private static final String URL = "http://localhost:5000/";

    private static final String FEATURESOURCE = "countries";

    private static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2(null);

    public void testFactoryCanProcess() {
        GeoRestDataStoreFactory factory = new GeoRestDataStoreFactory();
        Assert.assertTrue(factory.canProcess(createParams()));
    }

    public void testCreateDataStore() throws IOException {
        GeoRestDataStoreFactory factory = new GeoRestDataStoreFactory();
        DataStore ds = factory.createDataStore(createParams());
        Assert.assertNotNull(ds);
    }

    public void testGetFeatureSource() throws IOException {
        GeoRestDataStoreFactory factory = new GeoRestDataStoreFactory();
        DataStore ds = factory.createDataStore(createParams());
        GeoRestFeatureSource source = (GeoRestFeatureSource) ds.getFeatureSource(FEATURESOURCE);
        Assert.assertNotNull(source);
    }

    public void testGetSchema() throws IOException {
        GeoRestDataStoreFactory factory = new GeoRestDataStoreFactory();
        DataStore ds = factory.createDataStore(createParams());
        GeoRestFeatureSource source = (GeoRestFeatureSource) ds.getFeatureSource(FEATURESOURCE);
        SimpleFeatureType type = source.getSchema();
        Assert.assertNotNull(type);
        Assert.assertTrue(type.getAttributeCount() > 0);
    }

    public void testCountFeatures() throws IOException {
        GeoRestDataStoreFactory factory = new GeoRestDataStoreFactory();
        DataStore ds = factory.createDataStore(createParams());
        GeoRestFeatureSource source = (GeoRestFeatureSource) ds.getFeatureSource(FEATURESOURCE);
        int count = source.getCount(new Query(FEATURESOURCE, Filter.INCLUDE));
        Assert.assertTrue(count > 0);
        BBOX bbox = FF.bbox("geometry", 0, 0, 10, 10, "EPSG:4326");
        count = source.getCount(new Query(FEATURESOURCE, bbox, 5, new String[] {}, ""));
        Assert.assertTrue(count == 5);
    }

    public void testGetFeatureReader() throws Exception {
        GeoRestDataStoreFactory factory = new GeoRestDataStoreFactory();
        DataStore ds = factory.createDataStore(createParams());
        FeatureReader<SimpleFeatureType, SimpleFeature> r = ds.getFeatureReader(new Query(
                FEATURESOURCE), Transaction.AUTO_COMMIT);
        assertNotNull(r);
        assertTrue(r.hasNext());
    }

    // Private methods:

    private Map<String, Serializable> createParams() {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        // URL url = DataStoreTest.class.getResource("/");
        params.put(GeoRestDataStoreFactory.PARAM_URL, URL);
        params.put(GeoRestDataStoreFactory.PARAM_LAYERS, "countries");
        return params;
    }
}
