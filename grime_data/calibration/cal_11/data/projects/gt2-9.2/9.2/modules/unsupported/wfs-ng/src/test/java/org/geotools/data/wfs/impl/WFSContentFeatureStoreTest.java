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
package org.geotools.data.wfs.impl;

import static org.geotools.data.DataUtilities.createType;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.wfs.internal.WFSClient;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.identity.FeatureId;

public class WFSContentFeatureStoreTest {

    private static final QName TYPE1 = new QName("http://example.com/1", "points", "prefix1");

    private static final QName TYPE2 = new QName("http://example.com/2", "points", "prefix2");

    private static SimpleFeatureType featureType1;

    private static SimpleFeatureType featureType2;

    private static String simpleTypeName1;

    private static String simpleTypeName2;

    private WFSContentDataStore dataStore;

    private WFSClient wfs;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        simpleTypeName1 = TYPE1.getPrefix() + "_" + TYPE1.getLocalPart();
        simpleTypeName2 = TYPE2.getPrefix() + "_" + TYPE2.getLocalPart();

        featureType1 = createType("http://example.com/1", simpleTypeName1,
                "name:String,geom:Point:srid=4326");
        featureType2 = createType("http://example.com/2", "prefix2_roads",
                "name:String,geom:Point:srid=3857");

    }

    @Before
    public void setUp() throws Exception {
        wfs = mock(WFSClient.class);
        when(wfs.getRemoteTypeNames()).thenReturn(new HashSet<QName>(Arrays.asList(TYPE1, TYPE2)));
        when(wfs.supportsTransaction(TYPE1)).thenReturn(Boolean.TRUE);
        when(wfs.supportsTransaction(TYPE2)).thenReturn(Boolean.FALSE);

        dataStore = spy(new WFSContentDataStore(wfs));
        doReturn(featureType1).when(dataStore).getRemoteFeatureType(TYPE1);
        doReturn(featureType2).when(dataStore).getRemoteFeatureType(TYPE2);
        doReturn(featureType1).when(dataStore).getRemoteSimpleFeatureType(TYPE1);
        doReturn(featureType2).when(dataStore).getRemoteSimpleFeatureType(TYPE2);
    }

    @After
    public void tearDown() throws Exception {
    }

    //TODO: re-enable when transactions are worked out
    @Test
    @Ignore
    public void testAddFeaturesAutoCommit() throws Exception {
        ContentFeatureSource source;

        source = dataStore.getFeatureSource(simpleTypeName1);
        assertNotNull(source);
        assertTrue(source instanceof WFSContentFeatureStore);

        WFSContentFeatureStore store = (WFSContentFeatureStore) source;
        SimpleFeatureCollection collection = null;
        List<FeatureId> fids = store.addFeatures(collection);
    }
}
