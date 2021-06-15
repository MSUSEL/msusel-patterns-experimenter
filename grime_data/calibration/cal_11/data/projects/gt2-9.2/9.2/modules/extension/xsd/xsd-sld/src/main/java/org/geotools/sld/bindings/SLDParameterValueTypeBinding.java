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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xml.Text;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.picocontainer.MutablePicoContainer;


/**
 * Binding object for the type http://www.opengis.net/sld:ParameterValueType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="ParameterValueType" mixed="true"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         The &quot;ParameterValueType&quot;
 *              uses WFS-Filter expressions to give         values for SLD
 *              graphic parameters.  A &quot;mixed&quot; element-content
 *              model is used with textual substitution for values.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence minOccurs="0" maxOccurs="unbounded"&gt;
 *          &lt;xsd:element ref="ogc:expression"/&gt;
 *      &lt;/xsd:sequence&gt;
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
public class SLDParameterValueTypeBinding extends AbstractComplexBinding {
    FilterFactory filterFactory;

    public SLDParameterValueTypeBinding(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.PARAMETERVALUETYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Expression.class;
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
     * Even though the spec allows an instance of ParamterValueType to have
     * multiple expressions as children, it is more often that there is only
     * one. Therefore this binding returns the first expression it finds or
     * null. If a subtype needs multiple expressions they should ovveride.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {

        List<Expression> expressions = new ArrayList();
        for (Iterator itr = node.getChildren().iterator(); itr.hasNext();) {
            Node child = (Node) itr.next();

            if (child.getValue() instanceof Expression) {
                expressions.add((Expression) child.getValue());
            }
            else if (child.getValue() instanceof Text) {
                expressions.add(filterFactory.literal(((Text)child.getValue()).getValue()));
            }
        }
        
        if (expressions.isEmpty()) {
            return null;
        }
        if (expressions.size() == 1) return (Expression) expressions.get(0);
      
        Expression e = expressions.get(0);
        for (int i = 1; i < expressions.size(); i++) {
              e = filterFactory.function("strConcat", new Expression[]{e, expressions.get(i)});
        }

        return e;
    }
}
