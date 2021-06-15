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
 *    (C) 2005, David Zwiers
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
package org.geotools.data.wfs.online;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.wfs.impl.WFSContentDataStore;
import org.geotools.data.wfs.impl.WFSDataStoreFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

/**
 */
public class WFSOnlineTestSupport {

    public void testEmpty() {/**/
    }

    public static WFSContentDataStore getDataStore(URL server, Boolean post) throws IOException {

        Map<String, Serializable> m = new HashMap<String, Serializable>();
        m.put(WFSDataStoreFactory.URL.key, server);
        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(10000)); // not debug
        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(1000000)); // for debug

        if (post != null) {
            m.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.valueOf(post));
        }

        return new WFSDataStoreFactory().createDataStore(m);

    }

    public static void doFeatureType(DataStore wfs, String typeName) throws Exception {

        assertNotNull("No featureTypes", wfs.getTypeNames());

        // post
        SimpleFeatureType ft = wfs.getSchema(typeName);
        assertNotNull("DescribeFeatureType for " + typeName + " resulted in null", ft);

        GeometryDescriptor geometryDescriptor = ft.getGeometryDescriptor();
        List<AttributeDescriptor> attributeDescriptors = ft.getAttributeDescriptors();
        int attributeCount = ft.getAttributeCount();

        assertNotNull("CRS missing ", geometryDescriptor.getCoordinateReferenceSystem());

        assertTrue("POST " + typeName
                + " must have 1 geom and atleast 1 other attribute -- fair assumption",
                geometryDescriptor != null && attributeDescriptors != null && attributeCount > 0);

    }

    public static void doFeatureReader(DataStore wfs, String typeName) throws Exception {
        assertNotNull("No featureTypes", wfs.getTypeNames());

        Query query = new Query(typeName);
        query.setMaxFeatures(5);
        
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        reader = wfs.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            assertNotNull("FeatureType was null", reader);
            assertTrue("must have 1 feature -- fair assumption", reader.hasNext());

            SimpleFeature next = reader.next();
            assertNotNull(next);
        } finally {
            reader.close();
        }
    }

}
