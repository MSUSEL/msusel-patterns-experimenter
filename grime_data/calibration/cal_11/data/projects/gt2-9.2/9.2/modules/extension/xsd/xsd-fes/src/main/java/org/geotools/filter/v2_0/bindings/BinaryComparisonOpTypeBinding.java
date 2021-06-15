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
package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexBinding;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.expression.Expression;

/**
 * <pre>
 * &lt;xsd:complexType name="BinaryComparisonOpType">
 *     &lt;xsd:complexContent>
 *        &lt;xsd:extension base="fes:ComparisonOpsType">
 *           &lt;xsd:sequence>
 *              &lt;xsd:element ref="fes:expression" minOccurs="2" maxOccurs="2"/>
 *           &lt;/xsd:sequence>
 *           &lt;xsd:attribute name="matchCase" type="xsd:boolean"
 *                          use="optional" default="true"/>
 *           &lt;xsd:attribute name="matchAction" type="fes:MatchActionType"
 *                          use="optional" default="Any"/>
 *        &lt;/xsd:extension>
 *     &lt;/xsd:complexContent>
 *  &lt;/xsd:complexType>
 *  <pre>
 *  
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class BinaryComparisonOpTypeBinding extends AbstractComplexBinding {

    @Override
    public QName getTarget() {
        return FES.BinaryComparisonOpType;
    }

    @Override
    public Class getType() {
        return BinaryComparisonOperator.class;
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        BinaryComparisonOperator op = (BinaryComparisonOperator) object;
        if ("matchAction".equals(name.getLocalPart())) {
            return op.getMatchAction().name();
        }
        if ("matchCase".equals(name.getLocalPart())) {
            return op.isMatchingCase();
        }
        if (FES.expression.equals(name)) {
            return new Expression[]{op.getExpression1(), op.getExpression2()};
        }
        return null;
    }
}
