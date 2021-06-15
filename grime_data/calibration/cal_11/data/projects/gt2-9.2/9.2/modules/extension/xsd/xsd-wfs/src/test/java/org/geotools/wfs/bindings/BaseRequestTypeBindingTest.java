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

import java.net.URL;

import net.opengis.wfs.BaseRequestType;

import org.geotools.test.TestData;
import org.geotools.wfs.WFS;
import org.geotools.wfs.WFSTestSupport;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Unit test suite for {@link BaseRequestTypeBinding}
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 *
 *
 * @source $URL$
 */

public class BaseRequestTypeBindingTest extends WFSTestSupport {
    public BaseRequestTypeBindingTest() {
        super(WFS.BaseRequestType, BaseRequestType.class, Binding.OVERRIDE);
    }

    public void testEncode() throws Exception {
        // BaseRequestType is abstract, use a concrete subclass instead
        BaseRequestType brq = factory.createDescribeFeatureTypeType();
        brq.setHandle("foo");
        brq.setService("NotAService");
        brq.setVersion("0.1.0");

        final Document dom = encode(brq, WFS.DescribeFeatureType);
        final Element root = dom.getDocumentElement();

        assertEquals("NotAService", root.getAttribute("service"));
        assertEquals("0.1.0", root.getAttribute("version"));
        assertEquals("foo", root.getAttribute("handle"));
    }

    public void testParse() throws Exception {
        final URL resource = TestData.getResource(this, "BaseRequestTypeBindingTest.xml");
        buildDocument(resource);

        Object parsed = parse(WFS.DescribeFeatureType);
        assertTrue(parsed instanceof BaseRequestType);

        BaseRequestType brq = (BaseRequestType) parsed;
        assertEquals("1.1.0", brq.getVersion());
        assertEquals("WFS", brq.getService());
        assertEquals("fooHandle", brq.getHandle());
    }
}
