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

import java.io.ByteArrayInputStream;
import java.util.Enumeration;

import net.opengis.wfs20.QueryExpressionTextType;
import net.opengis.wfs20.Wfs20Factory;

import org.geotools.wfs.v2_0.WFS;
import org.geotools.xml.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.helpers.NamespaceSupport;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Binding object for the type http://www.opengis.net/wfs/2.0:QueryExpressionTextType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:complexType mixed="true" name="QueryExpressionTextType"&gt;
 *      &lt;xsd:choice&gt;
 *          &lt;xsd:any maxOccurs="unbounded" minOccurs="0" namespace="##other" processContents="skip"/&gt;
 *          &lt;xsd:any maxOccurs="unbounded" minOccurs="0"
 *              namespace="##targetNamespace" processContents="skip"/&gt;
 *      &lt;/xsd:choice&gt;
 *      &lt;xsd:attribute name="returnFeatureTypes"
 *          type="wfs:ReturnFeatureTypesListType" use="required"/&gt;
 *      &lt;xsd:attribute name="language" type="xsd:anyURI" use="required"/&gt;
 *      &lt;xsd:attribute default="false" name="isPrivate" type="xsd:boolean"/&gt;
 *  &lt;/xsd:complexType&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class QueryExpressionTextTypeBinding extends AbstractComplexEMFBinding {

    NamespaceSupport namespaceContext;
    
    public QueryExpressionTextTypeBinding(Wfs20Factory factory, NamespaceSupport namespaceContext) {
        super(factory, QueryExpressionTextType.class);
        this.namespaceContext = namespaceContext;
    }
    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.QueryExpressionTextType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        //parsing handled by QueryExpressionTextDelegate
        return null;
    }
    
    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        Element e = super.encode(object, document, value);
        
        QueryExpressionTextType qe = (QueryExpressionTextType) object;
        if (!qe.isIsPrivate()) {
            //include the query text
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            Document d = db.parse(new ByteArrayInputStream(qe.getValue().getBytes()));
            
            //register all the namespaces from this context with the root element of the parsed
            // content
            for (Enumeration en = namespaceContext.getPrefixes(); en.hasMoreElements(); ) {
                String prefix = (String) en.nextElement();
                if (prefix == null) {
                    continue;
                }
                String uri = namespaceContext.getURI(prefix);
                d.getDocumentElement().setAttribute("xmlns:" + prefix, uri);
            }
            e.appendChild(document.importNode(d.getDocumentElement(), true));
        }
        
        return e;
        
    }
}