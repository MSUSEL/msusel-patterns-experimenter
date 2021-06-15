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
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1.bindings;

import java.util.ArrayList;
import java.util.List;

import org.geotools.se.v1_1.SE;
import org.geotools.xml.*;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:FormatNumber.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="FormatNumber" substitutionGroup="se:Function" type="se:FormatNumberType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *  Function for formatting numbers to make them human readable.
 *               &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="FormatNumberType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="se:FunctionType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="se:NumericValue"/&gt;
 *                  &lt;xsd:element ref="se:Pattern"/&gt;
 *                  &lt;xsd:element minOccurs="0" ref="se:NegativePattern"/&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:attribute default="." name="decimalPoint"
 *                  type="xsd:string" use="optional"/&gt;
 *              &lt;xsd:attribute default="," name="groupingSeparator"
 *                  type="xsd:string" use="optional"/&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt; 
 *              
 *        </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 *
 *
 * @source $URL$
 */
public class FormatNumberBinding extends AbstractComplexBinding {

    FilterFactory filterFactory;
    
    public FormatNumberBinding(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }
    
    /**
     * @generated
     */
    public QName getTarget() {
        return SE.FormatNumber;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return Function.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Expression[] expressions = new Expression[5];
        
        //&lt;xsd:element ref="se:NumericValue"/&gt;
        expressions[1] = (Expression) node.getChildValue("NumericValue");
        
        //&lt;xsd:element ref="se:Pattern"/&gt;
        expressions[0] = filterFactory.literal(node.getChildValue("Pattern"));
        
        //&lt;xsd:element minOccurs="0" ref="se:NegativePattern"/&gt;
        if (node.hasChild("NegativePattern")) {
            expressions[2] = filterFactory.literal(node.getChildValue("NegativePattern"));
        }
        else {
            expressions[2] = filterFactory.literal("-");
        }
        
        //&lt;xsd:attribute default="." name="decimalPoint" type="xsd:string" use="optional"/&gt;
        if (node.hasAttribute("decimalPoint")) {
            expressions[3] = filterFactory.literal(node.getAttributeValue("decimalPoint"));
        }
        else {
            expressions[3] = filterFactory.literal(".");
        }
        
        //&lt;xsd:attribute default="," name="groupingSeparator" type="xsd:string" use="optional"/&gt;
        if (node.hasAttribute("groupingSeparator")) {
            expressions[4] = filterFactory.literal(node.getAttributeValue("groupingSeparator"));
        }
        else {
            expressions[4] = filterFactory.literal(",");
        }
        
        return filterFactory.function("numberFormat2", expressions);
    }

}
