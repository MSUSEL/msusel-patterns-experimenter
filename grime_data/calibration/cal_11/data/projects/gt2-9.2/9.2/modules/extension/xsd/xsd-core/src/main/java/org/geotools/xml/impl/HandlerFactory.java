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
package org.geotools.xml.impl;

import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import javax.xml.namespace.QName;


/**
 * Factory used to create element handler objects during the processing of an
 * instance document.
 *
 * @author Justin Deoliveira,Refractions Reserach Inc.,jdeolive@refractions.net
 *
 *
 *
 *
 * @source $URL$
 */
public interface HandlerFactory {
    /**
     * Creates a handler for the root element of a document.
     */
    DocumentHandler createDocumentHandler(ParserHandler parser);

    /**
     * Creates an element hander for a global or top level element in a document.
     *
     * @param qName The qualified name identifying the element.
     * @param parent The parent handler.
     * @param parser The content handler driving the parser.
     *
     * @return A new element handler, or null if one could not be created.
     */
    ElementHandler createElementHandler(QName qName, Handler parent, ParserHandler parser);

    /**
     * Creates a handler for a particular element in a document.
     *
     * @param element The schema component which represents the declaration
     * of the element.
     * @param parent The parent handler.
     * @param parser The content handler driving the parser.
     *
     * @return A new element handler, or null if one could not be created.
     */
    ElementHandler createElementHandler(XSDElementDeclaration element, Handler parent,
        ParserHandler parser);

    /**
     * Creates a handler for a particular element in a document.
     *
     * @param attribute The schema component which represents the declaration
     * of the attribute.
     * @param parent The parent handler.
     * @param parser The content handler driving the parser.
     *
     * @return A new attribute handler, or null if one could not be created.
     */

    //AttributeHandler createAttributeHandler(XSDAttributeDeclaration attribute, Handler parent, ParserHandler parser );
}
