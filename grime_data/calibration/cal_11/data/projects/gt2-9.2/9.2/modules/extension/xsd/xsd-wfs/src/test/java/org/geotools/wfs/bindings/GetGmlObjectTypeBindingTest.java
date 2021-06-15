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

import net.opengis.wfs.GetGmlObjectType;

import org.geotools.filter.v1_1.OGC;
import org.geotools.gml3.GML;
import org.geotools.test.TestData;
import org.geotools.wfs.WFS;
import org.geotools.wfs.WFSTestSupport;
import org.geotools.xml.Binding;
import org.opengis.filter.identity.GmlObjectId;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Unit test suite for {@link GetGmlObjectTypeBinding}
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 *
 *
 * @source $URL$
 */
public class GetGmlObjectTypeBindingTest extends WFSTestSupport {

    public GetGmlObjectTypeBindingTest() {
        super(WFS.GetGmlObjectType, GetGmlObjectType.class, Binding.OVERRIDE);
    }

    @Override
    public void testEncode() throws Exception {
        GetGmlObjectType req = factory.createGetGmlObjectType();
        {
            req.setService("WFS");
            req.setVersion("1.1.0");
            req.setHandle("testHandle");
            req.setOutputFormat("GML2");
            req.setTraverseXlinkDepth("3");
            req.setTraverseXlinkExpiry(BigInteger.valueOf(2));
            GmlObjectId gmlObjectId = filterFac.gmlObjectId("gmlid.1");
            req.setGmlObjectId(gmlObjectId);
        }
        final Document dom = encode(req, WFS.GetGmlObject);
        final Element root = dom.getDocumentElement();
        assertName(WFS.GetGmlObject, root);
        assertEquals("WFS", root.getAttribute("service"));
        assertEquals("1.1.0", root.getAttribute("version"));
        assertEquals("testHandle", root.getAttribute("handle"));
        assertEquals("GML2", root.getAttribute("outputFormat"));
        assertEquals("3", root.getAttribute("traverseXlinkDepth"));
        assertEquals("2", root.getAttribute("traverseXlinkExpiry"));
        
        Element gmlObjectId = getElementByQName(root, OGC.GmlObjectId);
        assertNotNull(gmlObjectId);
        assertEquals("gmlid.1", gmlObjectId.getAttributeNS(GML.NAMESPACE, GML.id.getLocalPart()));
    }

    @Override
    public void testParse() throws Exception {
        final URL resource = TestData.getResource(this, "GetGmlObjectTypeBindingTest.xml");
        buildDocument(resource);

        Object parsed = parse(WFS.GetGmlObjectType);
        assertTrue(parsed instanceof GetGmlObjectType);

        final GetGmlObjectType getGmlObj = (GetGmlObjectType) parsed;
        assertEquals("WFS", getGmlObj.getService());
        assertEquals("1.1.0", getGmlObj.getVersion());
        assertEquals("getGmlObjectHandle", getGmlObj.getHandle());
        assertEquals("GML3", getGmlObj.getOutputFormat());
        assertEquals("*", getGmlObj.getTraverseXlinkDepth());
        assertEquals(5, getGmlObj.getTraverseXlinkExpiry().intValue());

        // TODO: remove cast once the wfs-1.1.0 jar is deployed
        GmlObjectId gmlObjectId = (GmlObjectId) getGmlObj.getGmlObjectId();
        assertEquals("id1", gmlObjectId.getID());
    }

}
