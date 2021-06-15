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
package org.geotools.wfs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import net.opengis.wfs.WfsFactory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.xml.Binding;
import org.geotools.xml.Configuration;
import org.geotools.xml.test.XMLTestSupport;
import org.opengis.filter.FilterFactory2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 
 *
 * @source $URL$
 */
public abstract class WFSTestSupport extends XMLTestSupport {
    protected WfsFactory factory = WfsFactory.eINSTANCE;

    protected FilterFactory2 filterFac;

    private Class bindingTargetClass;

    private int executionMode;

    private QName qname;

    protected final Binding binding;

    protected WFSTestSupport(final QName qname, final Class<? extends EObject> bindingClass,
            final int executionMode) {
        super();
        this.qname = qname;
        this.bindingTargetClass = bindingClass;
        this.executionMode = executionMode;
        binding = binding(qname);
        filterFac = CommonFactoryFinder.getFilterFactory2(null);
    }

    protected Configuration createConfiguration() {
        return new org.geotools.wfs.v1_1.WFSConfiguration();
    }

    public void testTarget() {
        assertEquals(qname, binding.getTarget());
    }

    public void testType() throws Exception {
        assertEquals(bindingTargetClass, binding.getType());
    }

    public void testExecutionMode() throws Exception {
        assertEquals(toExModeString(executionMode), executionMode, binding.getExecutionMode());
    }

    public abstract void testParse() throws Exception;

    public abstract void testEncode() throws Exception;

    private String toExModeString(final int executionMode) {
        switch (executionMode) {
        case Binding.BEFORE:
            return "BEFORE";

        case Binding.AFTER:
            return "AFTER";

        case Binding.OVERRIDE:
            return "OVERRIDE";

        default:
            return "UNKNOWN";
        }
    }

    protected final void assertName(final QName expected, final Element element) {
        String localName = element.getLocalName();
        String ns = element.getNamespaceURI();

        assertEquals(expected.getNamespaceURI(), ns);
        assertEquals(expected.getLocalPart(), localName);
    }

    /**
     * Returns the value of the element named <code>propertyName</code> at
     * index <code>index</code>, where the index starts at 0 (zero).
     * 
     * @param dom
     * @param propertyName
     * @param index
     * @return
     */
    protected final String getElementValueByQName(final Document dom, final QName propertyName,
            final int index) {
        final NodeList elementsByQName = getElementsByQName(dom, propertyName);

        if (elementsByQName.getLength() == 0) {
            throw new NoSuchElementException("No element named " + propertyName + " in "
                    + dom.getDocumentElement().getLocalName());
        }

        if (index > elementsByQName.getLength()) {
            throw new NoSuchElementException("Expected element named " + propertyName
                    + " at index " + index + " but there are only " + elementsByQName.getLength()
                    + " elements in the node list");
        }

        final Node item = elementsByQName.item(index);
        final Node firstChild = item.getFirstChild();

        if (null == firstChild) {
            throw new NullPointerException(propertyName + "[" + index + "] has no content");
        }

        String nodeValue = firstChild.getNodeValue();

        return nodeValue;
    }

    protected final String getElementValueByQName(Document dom, QName propertyName) {
        return getElementValueByQName(dom, propertyName, 0);
    }

    /**
     * Convenience method which parses the content of the xml resource pointed
     * by the provided UEL into a dom and sets the built document which is to be
     * parsed.
     * 
     * @param xml
     *            An URL for the xml resource to build the document from
     */
    protected void buildDocument(URL resource) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);

        InputStream in = resource.openStream();
        InputStreamReader reader = new InputStreamReader(in, Charset.forName("UTF-8"));
        InputSource source = new InputSource(new BufferedReader(reader));
        document = docFactory.newDocumentBuilder().parse(source);
    }
}
