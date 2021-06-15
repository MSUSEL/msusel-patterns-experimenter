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
package org.geotools.jdbc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;

/**
 * 
 *
 * @source $URL$
 */
public abstract class JDBCLobTest extends JDBCTestSupport {

    protected static final String TESTLOB = "testlob";
    protected static final String ID = "id";
    protected static final String BLOB_FIELD = "blob_field";
    protected static final String CLOB_FIELD = "clob_field";
    protected static final String RAW_FIELD = "raw_field";
    
    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory(null); 
    protected SimpleFeatureType lobSchema;

    @Override
    protected abstract JDBCLobTestSetup createTestSetup();
    
    
    @Override
    protected void connect() throws Exception {
        super.connect();
        
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(TESTLOB);
        tb.add(aname(BLOB_FIELD), byte[].class);
        tb.add(aname(CLOB_FIELD), String.class);
        tb.add(aname(RAW_FIELD), byte[].class);
        lobSchema = tb.buildFeatureType();
    }
    
    public void testSchema() throws Exception {
        SimpleFeatureType ft =  dataStore.getSchema(tname(TESTLOB));
        assertFeatureTypesEqual(lobSchema, ft);
    }
    
    public void testRead() throws Exception {
        FeatureCollection<SimpleFeatureType, SimpleFeature> fc = 
            dataStore.getFeatureSource(tname(TESTLOB)).getFeatures();
        
        FeatureIterator<SimpleFeature> fi = fc.features();
        assertTrue(fi.hasNext());
        SimpleFeature f = fi.next();
        fi.close();
        assertTrue(Arrays.equals(new byte[] {1,2,3,4,5}, (byte[]) f.getAttribute(aname(BLOB_FIELD))));
        assertTrue(Arrays.equals(new byte[] {6,7,8,9,10}, (byte[]) f.getAttribute(aname(RAW_FIELD))));
        assertEquals("small clob", f.getAttribute(aname(CLOB_FIELD)));
    }
    
    public void testWrite() throws Exception {
        FeatureStore<SimpleFeatureType, SimpleFeature> fs =  (FeatureStore<SimpleFeatureType, SimpleFeature>) 
            dataStore.getFeatureSource(tname(TESTLOB));
        
        SimpleFeature sf = SimpleFeatureBuilder.build(lobSchema, new Object[] {
                new byte[] {6,7,8}, "newclob", new byte[] {11,12,13}}, null);
        List<FeatureId> fids = fs.addFeatures(DataUtilities.collection(sf));
        
        Filter filter = ff.id(new HashSet<Identifier>(fids));
        FeatureIterator<SimpleFeature> fi = fs.getFeatures(filter).features();
        assertTrue(fi.hasNext());
        SimpleFeature f = fi.next();
        fi.close();
        assertTrue(Arrays.equals(new byte[] {6,7,8}, (byte[]) f.getAttribute(aname(BLOB_FIELD))));
        assertTrue(Arrays.equals(new byte[] {11,12,13}, (byte[]) f.getAttribute(aname(RAW_FIELD))));
        assertEquals("newclob", f.getAttribute(aname(CLOB_FIELD)));
    }

}
