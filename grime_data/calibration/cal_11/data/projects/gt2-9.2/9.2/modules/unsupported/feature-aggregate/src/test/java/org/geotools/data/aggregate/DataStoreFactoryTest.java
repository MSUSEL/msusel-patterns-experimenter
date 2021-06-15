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
package org.geotools.data.aggregate;

import static org.geotools.data.aggregate.AggregatingDataStoreFactory.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.geotools.data.DataStoreFinder;
import org.geotools.feature.NameImpl;
import org.junit.Test;

/**
 * 
 *
 * @source $URL$
 */
public class DataStoreFactoryTest extends AbstractAggregatingStoreTest {

    @Test
    public void testConnectMinimalParams() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(REPOSITORY_PARAM.key, repository);

        AggregatingDataStore store = (AggregatingDataStore) DataStoreFinder.getDataStore(params);
        assertNotNull(store);
        assertEquals(0, store.getTypeNames().length);
        assertTrue(store.isTolerant());
    }

    @Test
    public void testOneStore() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(REPOSITORY_PARAM.key, repository);
        params.put(STORES_PARAM.key, new String[] { "gt:store3" });

        AggregatingDataStore store = (AggregatingDataStore) DataStoreFinder.getDataStore(params);
        assertNotNull(store);
        assertEquals(1, store.getTypeNames().length);
        assertTrue(store.isTolerant());
    }

    @Test
    public void testConfigurationUrl() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(REPOSITORY_PARAM.key, repository);
        params.put(CONFIGURATION.key, DataStoreFactoryTest.class.getResource("configuration.xml"));

        checkManualConfiguration(params);
    }

    @Test
    public void testConfigurationXml() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(REPOSITORY_PARAM.key, repository);
        InputStream is = DataStoreFactoryTest.class.getResourceAsStream("configuration.xml");
        params.put(CONFIGURATION_XML.key, IOUtils.toString(is));

        checkManualConfiguration(params);
    }

    private void checkManualConfiguration(Map<String, Object> params) throws IOException {
        AggregatingDataStore store = (AggregatingDataStore) DataStoreFinder.getDataStore(params);
        assertNotNull(store);
        assertEquals(2, store.getTypeNames().length);
        assertTrue(store.isTolerant());

        AggregateTypeConfiguration bp = store.getConfiguration("BasicPolygons");
        assertNotNull(bp);
        assertEquals("BasicPolygons", bp.getName());
        assertEquals(2, bp.getSourceTypes().size());
        assertSingleSourceType("BasicPolygons", new NameImpl("store1"), bp);
        assertSingleSourceType("BasicPolygons2", new NameImpl("store2"), bp);

        AggregateTypeConfiguration rs = store.getConfiguration("RoadSegments");
        assertNotNull(rs);
        assertEquals("RoadSegments", rs.getName());
        assertEquals(2, rs.getSourceTypes().size());
        assertSingleSourceType("RoadSegments", new NameImpl("store1"), rs);
        assertSingleSourceType("RoadSegments2", new NameImpl("store3"), rs);
    }

    @Test
    public void testEncodeDecode() throws Exception {
        // build the store so that we have a ready to use configuration
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(REPOSITORY_PARAM.key, repository);
        params.put(STORES_PARAM.key, new String[] { "store1", "gt:store3" });

        AggregatingDataStore store = (AggregatingDataStore) DataStoreFinder.getDataStore(params);
        List<AggregateTypeConfiguration> configs = new ArrayList<AggregateTypeConfiguration>(store
                .getConfigurations().values());
        
        AggregatingDataStoreFactory factory = new AggregatingDataStoreFactory();
        String xml = factory.encodeConfiguration(configs);
        
        List<AggregateTypeConfiguration> configs2 = factory.parseConfiguration(xml);
        assertEquals(configs, configs2);
    }

}
