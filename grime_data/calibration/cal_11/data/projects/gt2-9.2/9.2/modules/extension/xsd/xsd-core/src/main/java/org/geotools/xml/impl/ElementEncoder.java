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
import org.eclipse.xsd.XSDTypeDefinition;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.logging.Logger;


/**
 * Utility class to be used by bindings to encode an element or an attribute.
 *
 *
 * @author Justin Deoliveira, The Open Planning Project
 * TODO: rename this class, it is not just for element.s
 *
 *
 *
 *
 * @source $URL$
 */
public class ElementEncoder {
    /**
     * The walker used to traverse bindings
     */
    private BindingWalker bindingWalker;

    /**
     * The binding context
     */
    private MutablePicoContainer context;

    /**
     * Logger
     */
    private Logger logger;

    public ElementEncoder(BindingWalker bindingWalker, MutablePicoContainer context) {
        this.bindingWalker = bindingWalker;
        this.context = context;
    }

    /**
     * Sets the logger for the encoder to use.
     * @param logger
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Element encode(Object value,XSDElementDeclaration element, Document document) {
        return encode( value, element, document, null);
    }
    
    /**
     * Encodes a value corresponding to an element in a schema.
     *
     * @param value The value to encode.
     * @param element The declaration of the element corresponding to the value.
     * @param document The document used to create the encoded element.
     *
     * @return The encoded value as an element.
     */
    public Element encode(Object value, XSDElementDeclaration element,Document document, XSDTypeDefinition container) {
        ElementEncodeExecutor executor = new ElementEncodeExecutor(value, element, document, logger);
        BindingVisitorDispatch.walk(value, bindingWalker, element, executor, container, context);
        return executor.getEncodedElement();
    }

    public Attr encode(Object value, XSDAttributeDeclaration attribute, Document document,
            XSDTypeDefinition container) {
        AttributeEncodeExecutor executor = new AttributeEncodeExecutor(value, attribute, document,
                logger);
        BindingVisitorDispatch.walk(value, bindingWalker, attribute, executor, container, context);
        return executor.getEncodedAttribute();
    }

    public void setContext(MutablePicoContainer context) {
        this.context = context;
    }
}
