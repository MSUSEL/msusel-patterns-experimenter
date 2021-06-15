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

import javax.xml.namespace.QName;

import net.opengis.wfs.DescribeFeatureTypeType;

import org.geotools.test.TestData;
import org.geotools.wfs.WFS;
import org.geotools.wfs.WFSTestSupport;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Unit test suite for {@link DescribeFeatureTypeTypeBinding}
 * 
 * @author Justin Deoliveira
 * @author Gabriel Roldan
 * @version $Id: DescribeFeatureTypeTypeBindingTest.java 27759 2007-11-05
 *          19:46:45Z groldan $
 * @since 2.5.x
 *
 *
 * @source $URL$
 */
public class DescribeFeatureTypeTypeBindingTest extends WFSTestSupport {
    public DescribeFeatureTypeTypeBindingTest() {
        super(WFS.DescribeFeatureTypeType, DescribeFeatureTypeType.class, Binding.BEFORE);
    }

    public void testEncode() throws Exception {
        final DescribeFeatureTypeType dft = factory.createDescribeFeatureTypeType();
        // set BaseRequestType propertyes (DescribeFeatureType extends
        // BaseRequestType)
        dft.setService("WFS");
        dft.setVersion("1.1.0");
        dft.setHandle("bar");

        // set DescribeFeatureType properties
        dft.setOutputFormat("foo");

        final QName name1 = new QName("http://www.geotools.org/test", "Type1");
        final QName name2 = new QName("http://www.geotools.org/test", "Type2");
        dft.getTypeName().add(name1);
        dft.getTypeName().add(name2);

        final Document dom = encode(dft, WFS.DescribeFeatureType);
        final Element root = dom.getDocumentElement();

        assertEquals("WFS", root.getAttribute("service"));
        assertEquals("1.1.0", root.getAttribute("version"));
        assertEquals("bar", root.getAttribute("handle"));

        assertEquals("foo", root.getAttribute("outputFormat"));

        String typeName = getElementValueByQName(dom, new QName(WFS.NAMESPACE, "TypeName"), 0);
        assertEquals(name1.getLocalPart(), typeName);
        typeName = getElementValueByQName(dom, new QName(WFS.NAMESPACE, "TypeName"), 1);
        assertEquals(name2.getLocalPart(), typeName);
    }

    public void testParse() throws Exception {
        final URL resource = TestData.getResource(this, "DescribeFeatureTypeTypeBindingTest.xml");
        buildDocument(resource);

        final Object parsed = parse(WFS.DescribeFeatureType);
        assertNotNull(parsed);
        assertTrue(parsed instanceof DescribeFeatureTypeType);

        DescribeFeatureTypeType dft = (DescribeFeatureTypeType) parsed;
        assertEquals("1.1.0", dft.getVersion());
        assertEquals("fooHandle", dft.getHandle());
        assertEquals("WFS", dft.getService());

        assertEquals("fooFormat", dft.getOutputFormat());
        assertEquals(2, dft.getTypeName().size());

        QName name = new QName("http://www.openplans.org/topp", "states");
        assertEquals(name, dft.getTypeName().get(0));
        name = new QName("http://www.geotools.org/test", "sampleType");
        assertEquals(name, dft.getTypeName().get(1));
    }
}
