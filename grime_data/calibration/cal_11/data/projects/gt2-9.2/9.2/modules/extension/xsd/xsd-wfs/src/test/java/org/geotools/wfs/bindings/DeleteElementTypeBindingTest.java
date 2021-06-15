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
import java.util.Collections;

import javax.xml.namespace.QName;

import net.opengis.wfs.DeleteElementType;

import org.geotools.test.TestData;
import org.geotools.wfs.WFS;
import org.geotools.wfs.WFSTestSupport;
import org.geotools.xml.Binding;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Unit test suite for {@link DeleteElementTypeBinding}
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 *
 *
 * @source $URL$
 */
public class DeleteElementTypeBindingTest extends WFSTestSupport {

    public DeleteElementTypeBindingTest() {
        super(WFS.DeleteElementType, DeleteElementType.class, Binding.OVERRIDE);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        //additional namespace
        registerNamespaceMapping("topp", "http://www.openplans.org/topp");
    }
    
    @Override
    public void testEncode() throws Exception {
        
        final DeleteElementType delete = factory.createDeleteElementType();
        final QName typeName = new QName("http://www.openplans.org/topp", "TestType", "topp");
        {
            delete.setHandle("testHandle");
            delete.setTypeName(typeName);
            Id id = filterFac.id(Collections.singleton(filterFac.featureId("fid1")));
            delete.setFilter(id);
        }
        final Document dom = encode(delete, WFS.Delete);
        final Element root = dom.getDocumentElement();
        assertName(WFS.Delete, root);
        
        assertEquals(typeName.getPrefix() + ":" + typeName.getLocalPart(), root.getAttribute("typeName"));
    }

    @Override
    public void testParse() throws Exception {
        final URL resource = TestData.getResource(this, "DeleteElementTypeBindingTest.xml");
        buildDocument(resource);

        Object parsed = parse(WFS.Delete);
        assertTrue(parsed instanceof DeleteElementType);
        DeleteElementType delete = (DeleteElementType) parsed;
        assertEquals("deleteHandle", delete.getHandle());
        assertEquals(new QName("http://www.openplans.org/topp", "TestType"), delete.getTypeName());
        Filter filter = delete.getFilter();
        assertTrue(filter instanceof Id);
        // done, checking the filter is not this module's responsibility
    }

}
