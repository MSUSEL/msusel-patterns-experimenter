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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.gen.info;

import java.io.IOException;
import java.util.Collection;

import junit.framework.TestCase;

import org.junit.Assert;

/**
 * 
 *
 * @source $URL$
 */
public class GeneralizationInfosTest extends TestCase {

    public void testGeneralizationInfos() {
        GeneralizationInfosProvider provider = new GeneralizationInfosProviderImpl();
        GeneralizationInfos infos = null;
        try {
            infos = provider.getGeneralizationInfos("src/test/resources/geninfo1.xml");
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
            return;
        }

        assertTrue("DSInfos".equals(infos.getDataSourceName()));
        assertTrue("WSInfos".equals(infos.getDataSourceNameSpace()));

        Collection<String> coll = null;
        coll = infos.getBaseFeatureNames();
        assertTrue(coll.size() == 2);
        assertTrue(coll.contains("BaseFeature1"));
        assertTrue(coll.contains("BaseFeature2"));

        coll = infos.getFeatureNames();
        assertTrue(coll.size() == 2);
        assertTrue(coll.contains("GenFeature1"));
        assertTrue(coll.contains("BaseFeature2"));

        GeneralizationInfo info1 = infos.getGeneralizationInfoForBaseFeatureName("BaseFeature1");
        assertTrue("DSInfo".equals(info1.getDataSourceName()));
        assertTrue("WSInfo".equals(info1.getDataSourceNameSpace()));

        assertTrue(info1 == infos.getGeneralizationInfoForFeatureName("GenFeature1"));

        GeneralizationInfo info2 = infos.getGeneralizationInfoForBaseFeatureName("BaseFeature2");
        assertTrue("DSInfos".equals(info2.getDataSourceName()));
        assertTrue("WSInfos".equals(info2.getDataSourceNameSpace()));

        assertTrue(info2 == infos.getGeneralizationInfoForFeatureName("BaseFeature2"));

        assertTrue(info1.getFeatureName().equals("GenFeature1"));
        assertTrue(info1.getBaseFeatureName().equals("BaseFeature1"));
        assertTrue(info1.getGeomPropertyName().equals("the_geom"));

        assertTrue(info2.getFeatureName().equals("BaseFeature2"));
        assertTrue(info2.getBaseFeatureName().equals("BaseFeature2"));
        assertTrue(info2.getGeomPropertyName().equals("the_geom"));

        assertTrue(info1.getGeneralizations().size() == 2);
        assertTrue(info2.getGeneralizations().size() == 2);

        assertTrue(info1.getGeneralizationForDistance(99.0) == null);
        assertTrue(info1.getGeneralizationForDistance(100.0).getFeatureName().equals("GenFeature1"));
        assertTrue(info1.getGeneralizationForDistance(999.0).getFeatureName().equals("GenFeature1"));
        assertTrue("DSInfo".equals(info1.getGeneralizationForDistance(100.0).getDataSourceName()));
        assertTrue("WSInfo".equals(info1.getGeneralizationForDistance(100.0)
                .getDataSourceNameSpace()));

        assertTrue(info1.getGeneralizationForDistance(1000.0).getFeatureName()
                .equals("GenFeature2"));
        assertTrue(info1.getGeneralizationForDistance(10000.0).getFeatureName().equals(
                "GenFeature2"));
        assertTrue("DSDistance".equals(info1.getGeneralizationForDistance(1000.0)
                .getDataSourceName()));
        assertTrue("WSDistance".equals(info1.getGeneralizationForDistance(1000.0)
                .getDataSourceNameSpace()));

        assertTrue(info2.getGeneralizationForDistance(99.0) == null);
        assertTrue(info2.getGeneralizationForDistance(100.0).getGeomPropertyName().equals(
                "the_geom1"));
        assertTrue(info2.getGeneralizationForDistance(999.0).getGeomPropertyName().equals(
                "the_geom1"));
        assertTrue(info2.getGeneralizationForDistance(1000.0).getGeomPropertyName().equals(
                "the_geom2"));
        assertTrue(info2.getGeneralizationForDistance(10000.0).getGeomPropertyName().equals(
                "the_geom2"));

        assertTrue("DSInfos".equals(info2.getGeneralizationForDistance(100.0).getDataSourceName()));
        assertTrue("WSInfos".equals(info2.getGeneralizationForDistance(100.0)
                .getDataSourceNameSpace()));

        GeneralizationInfo gi = infos.getGeneralizationInfoForFeatureName("GenFeature1");
        assertNotNull(gi);
        infos.removeGeneralizationInfo(gi);
        assertNull(infos.getGeneralizationInfoForFeatureName("GenFeature1"));
        infos.addGeneralizationInfo(gi);
        assertNotNull(infos.getGeneralizationInfoForFeatureName("GenFeature1"));
    }
}
