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
package org.geotools.wfs.bindings;

import java.math.BigInteger;
import java.net.URL;
import java.util.List;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;
import net.opengis.wfs.ResultTypeType;

import org.geotools.test.TestData;
import org.geotools.wfs.WFS;
import org.geotools.wfs.WFSTestSupport;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;

/**
 * Unit test suite for {@link GetFeatureTypeBinding}
 * 
 * @author Justin Deoliveira
 * @version $Id: GetFeatureTypeBindingTest.java 27749 2007-11-05 09:51:33Z
 *          groldan $
 * @since 2.5.x
 *
 *
 * @source $URL$
 */
public class GetFeatureTypeBindingTest extends WFSTestSupport {
    public GetFeatureTypeBindingTest() {
        super(WFS.GetFeatureType, GetFeatureType.class, Binding.OVERRIDE);
    }

    @SuppressWarnings("unchecked")
    public void testEncode() throws Exception {
        GetFeatureType getFeature = factory.createGetFeatureType();
        getFeature.setHandle("handle");
        getFeature.setMaxFeatures(BigInteger.valueOf(10));
        getFeature.getQuery().add(factory.createQueryType());
        getFeature.getQuery().add(factory.createQueryType());

        Document dom = encode(getFeature, WFS.GetFeature);
        assertEquals("handle", dom.getDocumentElement().getAttribute("handle"));
        assertEquals("10", dom.getDocumentElement().getAttribute("maxFeatures"));
        assertEquals(2, getElementsByQName(dom, WFS.Query).getLength());
    }

    public void testParse() throws Exception {
        final URL resource = TestData.getResource(this, "GetFeatureTypeBindingTest.xml");
        buildDocument(resource);

        Object parsed = parse(WFS.GetFeature);
        assertTrue(parsed instanceof GetFeatureType);
        GetFeatureType req = (GetFeatureType) parsed;
        assertEquals("WFS", req.getService());
        assertEquals("1.1.0", req.getVersion());
        assertEquals("fooHandle", req.getHandle());
        assertEquals("fooFormat", req.getOutputFormat());
        assertEquals(ResultTypeType.HITS_LITERAL, req.getResultType());
        assertEquals(1000, req.getMaxFeatures().intValue());
        assertEquals("*", req.getTraverseXlinkDepth());
        assertEquals(2, req.getTraverseXlinkExpiry().intValue());

        List queries = req.getQuery();
        assertEquals(2, queries.size());
        assertTrue(queries.get(0) instanceof QueryType);
        assertTrue(queries.get(1) instanceof QueryType);
    }
}
