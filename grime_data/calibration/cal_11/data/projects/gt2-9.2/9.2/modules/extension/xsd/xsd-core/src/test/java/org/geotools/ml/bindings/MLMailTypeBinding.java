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

import java.math.BigInteger;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.ml.Attachment;
import org.geotools.ml.Envelope;
import org.geotools.ml.Mail;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Strategy object for the type http://mails/refractions/net:mailType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="mailType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="envelope" type="ml:envelopeType"/&gt;
 *          &lt;xsd:element name="body" type="ml:bodyType"/&gt;
 *          &lt;xsd:element maxOccurs="unbounded" minOccurs="0"
 *              name="attachment" type="ml:attachmentType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name="id" type="xsd:integer" use="required"/&gt;
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
public class MLMailTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return ML.MAILTYPE;
    }

    public Class getType() {
        return Mail.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        Envelope envelope = (Envelope) node.getChildValue("envelope");
        String body = (String) node.getChildValue("body");
        BigInteger id = (BigInteger) node.getAttributeValue("id");

        List atts = node.getChildValues("attachment");
        Attachment[] attachments = (Attachment[]) atts.toArray(new Attachment[atts.size()]);

        return new Mail(id, body, envelope, attachments);
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        Mail m = (Mail) object;
        if ( "envelope".equals( name.getLocalPart() ) ) {
            return m.getEnvelope();
        }
        
        return null;
    }
}
