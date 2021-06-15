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
package org.geotools.sld.bindings;

import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.namespace.QName;

import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;


/**
 * Binding object for the element http://www.opengis.net/sld:PolygonSymbolizer.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="PolygonSymbolizer" substitutionGroup="sld:Symbolizer"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A &quot;PolygonSymbolizer&quot;
 *              specifies the rendering of a polygon or         area
 *              geometry, including its interior fill and border stroke.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:complexContent&gt;
 *              &lt;xsd:extension base="sld:SymbolizerType"&gt;
 *                  &lt;xsd:sequence&gt;
 *                      &lt;xsd:element ref="sld:Geometry" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:Fill" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:Stroke" minOccurs="0"/&gt;
 *                  &lt;/xsd:sequence&gt;
 *              &lt;/xsd:extension&gt;
 *          &lt;/xsd:complexContent&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
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
public class SLDPolygonSymbolizerBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDPolygonSymbolizerBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.POLYGONSYMBOLIZER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return PolygonSymbolizer.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        PolygonSymbolizer ps = styleFactory.createPolygonSymbolizer();

        //&lt;xsd:element ref="sld:Geometry" minOccurs="0"/&gt;
        if(node.hasChild("Geometry")) {
            Expression geometry = (Expression) node.getChildValue("Geometry");
            if(geometry instanceof PropertyName) {
                PropertyName propertyName = (PropertyName) geometry;
                ps.setGeometryPropertyName(propertyName.getPropertyName());
            } else {
                ps.setGeometry(geometry);
            }
        }

        //&lt;xsd:element ref="sld:Fill" minOccurs="0"/&gt;
        if (node.hasChild(Fill.class)) {
            ps.setFill((Fill) node.getChildValue(Fill.class));
        }

        //&lt;xsd:element ref="sld:Stroke" minOccurs="0"/&gt;
        if (node.hasChild(Stroke.class)) {
            ps.setStroke((Stroke) node.getChildValue(Stroke.class));
        }

        return ps;
    }
}
