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
package org.geotools.ml.bindings;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.geotools.ml.Envelope;
import org.geotools.ml.Header;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Strategy object for the type http://mails/refractions/net:envelopeType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="envelopeType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="From" type="xsd:string"/&gt;
 *          &lt;xsd:element name="To" type="xsd:string"/&gt;
 *          &lt;xsd:element ref="ml:Date"/&gt;
 *          &lt;xsd:element name="Subject" type="xsd:string"/&gt;
 *          &lt;xsd:element maxOccurs="unbounded" minOccurs="0" ref="ml:header"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name="From" type="xsd:string" use="required"/&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public class MLEnvelopeTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return ML.ENVELOPETYPE;
    }

    public Class getType() {
        return Envelope.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        String from = (String) node.getChildValue("From");
        String to = (String) node.getChildValue("To");
        Calendar date = (Calendar) node.getChildValue("Data");
        String subject = (String) node.getChildValue("Subject");

        List headerElements = node.getChildValues("header");
        Header[] headers = new Header[headerElements.size()];

        int i = 0;

        for (Iterator itr = headerElements.iterator(); itr.hasNext();) {
            Map headerObject = (Map) itr.next();
            headers[i++] = new Header((String) headerObject.get("name"),
                    (String) headerObject.get(null));
        }

        return new Envelope(from, to, date, subject, headers);
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        Envelope e = (Envelope) object;
        
        if ( "From".equals( name.getLocalPart() ) ) {
            return e.getFrom();
        }
        if ( "To".equals( name.getLocalPart() ) ) {
            return e.getTo();
        }

        return null;
    }
}
