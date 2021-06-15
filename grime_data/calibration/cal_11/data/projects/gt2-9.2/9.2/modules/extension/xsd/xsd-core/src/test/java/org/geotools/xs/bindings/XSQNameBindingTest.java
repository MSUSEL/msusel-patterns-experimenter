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
package org.geotools.xs.bindings;

import junit.framework.TestCase;
import org.xml.sax.helpers.NamespaceSupport;
import javax.xml.namespace.QName;
import org.geotools.xml.impl.NamespaceSupportWrapper;


/**
 * 
 *
 * @source $URL$
 */
public class XSQNameBindingTest extends TestCase {
    XSQNameBinding binding;

    protected void setUp() throws Exception {
        NamespaceSupport ns = new NamespaceSupport();
        ns.declarePrefix("foo", "http://foo");
        
        binding = new XSQNameBinding(new NamespaceSupportWrapper(ns));
    }

    public void testWithPrefix() throws Exception {
        QName qName = (QName) binding.parse(null, "foo:bar");
        assertNotNull(qName);

        assertEquals("foo", qName.getPrefix());
        assertEquals("http://foo", qName.getNamespaceURI());
        assertEquals("bar", qName.getLocalPart());
    }

    public void testWithNoPrefix() throws Exception {
        QName qName = (QName) binding.parse(null, "bar:foo");
        assertNotNull(qName);

        assertEquals("bar", qName.getPrefix());
        assertEquals("", qName.getNamespaceURI());
        assertEquals("foo", qName.getLocalPart());
    }
}
