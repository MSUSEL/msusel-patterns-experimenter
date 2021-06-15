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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs20.ParameterType;
import net.opengis.wfs20.Wfs20Factory;

import org.geotools.wfs.v2_0.WFS;
import org.geotools.xml.ParserDelegate;
import org.geotools.xml.ParserDelegate2;
import org.geotools.xml.impl.Handler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ParameterParserDelegate extends CopyingHandler implements ParserDelegate, ParserDelegate2 {

    static QName Parameter = new QName(WFS.NAMESPACE, "Parameter");
    
    ParameterType result;
    
    public boolean canHandle(QName elementName) {
        return false;
    }
    
    public boolean canHandle(QName elementName, Attributes attributes, Handler handler,
            Handler parent) {
        if (Parameter.equals(elementName)) {
            return parent != null && "StoredQuery".equals(parent.getComponent().getName());
        }
        return false;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (Parameter.getLocalPart().equals(localName)) {
            result = Wfs20Factory.eINSTANCE.createParameterType();
            result.setName(attributes.getValue("name"));
        }
        else {
            super.startElement(uri, localName, qName, attributes);
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (Parameter.getLocalPart().equals(localName)) {
            result.setValue(buffer.toString());
        }
        else {
            super.endElement(uri, localName, qName);
        }
    }
    
    public Object getParsedObject() {
        return result;
    }

}
