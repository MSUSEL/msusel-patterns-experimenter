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
package org.geotools.gml3.bindings;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.geotools.xml.Configuration;
import org.opengis.feature.simple.SimpleFeature;
import org.w3c.dom.Element;


/**
 * 
 *
 * @source $URL$
 */
public class AbstractFeatureCollectionTypeBindingTest extends GML3TestSupport {
    protected Configuration createConfiguration() {
        return new TestConfiguration();
    }

    protected void registerNamespaces(Element root) {
        super.registerNamespaces(root);
        root.setAttribute("xmlns:test", TEST.NAMESPACE);
    }

    public void testFeatureMember() throws Exception {
        Element featureCollection = GML3MockData.element(TEST.TestFeatureCollection, document,
                document);

        Element featureMember = GML3MockData.element(GML.featureMember, document, featureCollection);

        Element feature = GML3MockData.feature(document, featureMember);
        feature.setAttributeNS(GML.NAMESPACE, "id", "fid.1");

        featureMember = GML3MockData.element(GML.featureMember, document, featureCollection);

        feature = GML3MockData.feature(document, featureMember);
        feature.setAttributeNS(GML.NAMESPACE, "id", "fid.2");

        SimpleFeatureCollection fc = (SimpleFeatureCollection) parse();
        assertNotNull(fc);

        assertEquals(2, fc.size());

        SimpleFeatureIterator i = fc.features();
        try {
            SimpleFeature f = (SimpleFeature) i.next();
            assertEquals("fid.1", f.getID());
    
            f = (SimpleFeature) i.next();
            assertEquals("fid.2", f.getID());
        } finally {
            i.close();
        }
    }

    public void testFeatureMembers() throws Exception {
        Element featureCollection = GML3MockData.element(TEST.TestFeatureCollection, document,
                document);

        Element featureMember = GML3MockData.element(GML.featureMembers, document, featureCollection);

        Element feature = GML3MockData.feature(document, featureMember);
        feature.setAttributeNS(GML.NAMESPACE, "id", "fid.1");

        feature = GML3MockData.feature(document, featureMember);
        feature.setAttributeNS(GML.NAMESPACE, "id", "fid.2");

        SimpleFeatureCollection fc = (SimpleFeatureCollection) parse();
        assertNotNull(fc);

        assertEquals(2, fc.size());

        SimpleFeatureIterator i = fc.features();
        try {
            SimpleFeature f = (SimpleFeature) i.next();
            assertEquals("fid.1", f.getID());
    
            f = (SimpleFeature) i.next();
            assertEquals("fid.2", f.getID());
        } finally {
            i.close();
        }
    }
}
