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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import java.io.IOException;
import java.net.URI;

import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Schema;
import org.xml.sax.Attributes;


/**
 * PrintHandler accepts SAXish events and generated output.
 *
 * @author dzwiers
 *
 *
 * @source $URL$
 */
public interface PrintHandler {
    /**
     * DOCUMENT ME!
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param attributes DOCUMENT ME!
     *
     * @throws IOException
     */
    public void startElement(URI namespaceURI, String localName,
        Attributes attributes) throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param attributes DOCUMENT ME!
     *
     * @throws IOException
     */
    public void element(URI namespaceURI, String localName,
        Attributes attributes) throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @throws IOException
     */
    public void endElement(URI namespaceURI, String localName)
        throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     * @param arg1 DOCUMENT ME!
     * @param arg2 DOCUMENT ME!
     *
     * @throws IOException
     */
    public void characters(char[] arg0, int arg1, int arg2)
        throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @throws IOException
     */
    public void characters(String s) throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     * @param arg1 DOCUMENT ME!
     * @param arg2 DOCUMENT ME!
     *
     * @throws IOException
     */
    public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
        throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @throws IOException
     */
    public void startDocument() throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @throws IOException
     */
    public void endDocument() throws IOException;

    /**
     * Returns the default Schema for the document being printed
     * 
     * @return Schema
     */
    public Schema getDocumentSchema();

    /**
     * Tries to find an appropriate Element so represent the value. 
     * 
     * @param value The Object being attempted to write
     * @return Element The element instance found, or null if not found.
     */
    public Element findElement(Object value);

    public Element findElement(String name);

    public Object getHint(Object key);
}
