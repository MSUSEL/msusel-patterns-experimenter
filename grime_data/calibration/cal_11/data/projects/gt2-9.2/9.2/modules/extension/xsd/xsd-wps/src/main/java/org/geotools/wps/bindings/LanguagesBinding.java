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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wps.bindings;

import javax.xml.namespace.QName;

import net.opengis.wps10.DefaultType2;
import net.opengis.wps10.LanguagesType;
import net.opengis.wps10.LanguagesType1;
import net.opengis.wps10.Wps10Factory;

import org.geotools.ows.v1_1.OWS;
import org.geotools.wps.WPS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Manual binding for 'wps:Languages' in wpsGetCapabilibies_response.xsd'. 
 *<p>
 *This binding is implemented because it is an anonymous complex type which contains another
 *anonymous complex type. On top of all that its name conflicts with another type in the schema.
 *</p>
 * <pre>
 * &lt;element name="Languages">
 *   &lt;annotation>
 *       &lt;documentation>Listing of the default and other languages supported by this service. &lt;/documentation>
 *   &lt;/annotation>
 *   &lt;complexType>
 *       &lt;sequence>
 *         &lt;element name="Default">
 *           &lt;annotation>
 *              &lt;documentation>Identifies the default language that will be used unless the operation request specifies another supported language. &lt;/documentation>
 *           &lt;/annotation>
 *           &lt;complexType>
 *              &lt;sequence>
 *                  &lt;element ref="ows:Language">
 *                  &lt;/element>
 *               &lt;/sequence>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Supported" type="wps:LanguagesType">
 *           &lt;annotation>
 *                    &lt;documentation>Unordered list of references to all of the languages supported by this service. The default language shall be included in this list.&lt;/documentation>
 *           &lt;/annotation>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/complexType>
 *   &lt;/element>
 *
 * </pre>
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 *
 * @source $URL$
 */
public class LanguagesBinding extends AbstractComplexBinding {

    Wps10Factory factory;
    public LanguagesBinding( Wps10Factory factory ) {
        this.factory = factory;
    }
    
    public QName getTarget() {
        return WPS._Languages;
    }

    public Class getType() {
        return LanguagesType1.class;
    }
    
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        LanguagesType1 langs = factory.createLanguagesType1();
        
        DefaultType2 def = factory.createDefaultType2();
        langs.setDefault( def );
        def.setLanguage( (String) node.getChildValue( "Default") );
        
        LanguagesType supported = (LanguagesType) node.getChildValue( "Supported" );
        langs.setSupported( supported );
        
        return langs;
    }
    
    @Override
    public Element encode(Object object, Document document, Element value)
            throws Exception {
        
        LanguagesType1 langs = (LanguagesType1) object;
        
        //add the default manually
        Element def = document.createElementNS( WPS.NAMESPACE, "Default");
        value.appendChild( def );
        
        Element lang = document.createElementNS( OWS.NAMESPACE, OWS.Language.getLocalPart() );
        def.appendChild( lang );
        
        lang.appendChild( document.createTextNode( langs.getDefault().getLanguage() ) );
        
        return value;
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        LanguagesType1 langs = (LanguagesType1) object;
        
        if ( "Supported".equals( name.getLocalPart() ) ) {
            return langs.getSupported();
        }
        
        return null;
    }

}
