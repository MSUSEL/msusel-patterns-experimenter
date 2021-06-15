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

package org.geotools.data.complex;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.gml3.v3_2.GML;
import org.geotools.gml3.v3_2.gco.GCO;
import org.geotools.gml3.v3_2.gmd.GMD;
import org.geotools.test.AppSchemaTestSupport;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * Test app-schema with GeoSciML 3.0rc1, a GML 3.2 application schema.
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 *
 * @source $URL$
 */
public class Gsml30MappedFeatureTest extends AppSchemaTestSupport {

    private static final String TEST_DATA = "/test-data/gsml30/";

    /**
     * GeoSciML 3.0 Core namespace.
     */
    private static final String GSML = "urn:cgi:xmlns:CGI:GeoSciML-Core:3.0.0";

    /**
     * gsml:MappedFeature, the type under test.
     */
    private static final Name MAPPED_FEATURE = new NameImpl(GSML, "MappedFeature");

    @Test
    public void features() throws Exception {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("dbtype", "app-schema");
        URL url = getClass().getResource(TEST_DATA + "Gsml30MappedFeature.xml");
        Assert.assertNotNull(url);
        params.put("url", url.toExternalForm());
        DataAccess<FeatureType, Feature> dataAccess = null;
        try {
            dataAccess = DataAccessFinder.getDataStore(params);
            Assert.assertNotNull(dataAccess);
            FeatureType mappedFeatureType = dataAccess.getSchema(MAPPED_FEATURE);
            Assert.assertNotNull(mappedFeatureType);
            FeatureSource<FeatureType, Feature> source = dataAccess
                    .getFeatureSource(MAPPED_FEATURE);
            FeatureCollection<FeatureType, Feature> features = source.getFeatures();
            FeatureIterator<Feature> iterator = features.features();
            Map<String, Feature> featureMap = new LinkedHashMap<String, Feature>();
            try {
                while (iterator.hasNext()) {
                    Feature f = iterator.next();
                    featureMap.put(f.getIdentifier().getID(), f);
                }
            } finally {
                iterator.close();
            }
            Assert.assertEquals(2, featureMap.size());
            // test gml:name
            Assert.assertEquals(
                    "First",
                    ((ComplexAttribute) featureMap.get("mf.1").getProperty(
                            new NameImpl(GML.NAMESPACE, "name"))).getProperty(
                            new NameImpl("simpleContent")).getValue());
            Assert.assertEquals(
                    "Second",
                    ((ComplexAttribute) featureMap.get("mf.2").getProperty(
                            new NameImpl(GML.NAMESPACE, "name"))).getProperty(
                            new NameImpl("simpleContent")).getValue());
            // test gsml:resolutionScale/gmd:MD_RepresentativeFraction/gmd:denominator/gco:Integer
            for (int i = 1; i <= 2; i++) {
                Assert.assertEquals(
                        BigInteger.valueOf(250000),
                        ((Attribute) ((ComplexAttribute) ((ComplexAttribute) ((ComplexAttribute) featureMap
                                .get("mf." + i).getProperty(new NameImpl(GSML, "resolutionScale")))
                                .getProperty(new NameImpl(GMD.NAMESPACE,
                                        "MD_RepresentativeFraction"))).getProperty(new NameImpl(
                                GMD.NAMESPACE, "denominator"))).getProperty(new NameImpl(
                                GCO.NAMESPACE, "Integer"))).getValue());
            }
        } finally {
            if (dataAccess != null) {
                dataAccess.dispose();
            }
        }
    }

}
