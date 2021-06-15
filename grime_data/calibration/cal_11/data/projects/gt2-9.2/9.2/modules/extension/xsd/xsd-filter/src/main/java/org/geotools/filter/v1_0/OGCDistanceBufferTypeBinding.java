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
package org.geotools.filter.v1_0;

import org.picocontainer.MutablePicoContainer;
import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/ogc:DistanceBufferType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="DistanceBufferType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="ogc:SpatialOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="ogc:PropertyName"/&gt;
 *                  &lt;xsd:element ref="gml:_Geometry"/&gt;
 *                  &lt;xsd:element name="Distance" type="ogc:DistanceType"/&gt;
 *              &lt;/xsd:sequence&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
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
public class OGCDistanceBufferTypeBinding extends AbstractComplexBinding {
    private FilterFactory2 factory;

    public OGCDistanceBufferTypeBinding(FilterFactory2 factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.DistanceBufferType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return DistanceBufferOperator.class;
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
        //implemented by element bindings
        return null;

        //        //TODO: replace with element bindings
        //        Number distance = (Number) node.getChildValue(Number.class);
        //
        //        PropertyName propertyName = (PropertyName) node.getChildValue(PropertyName.class);
        //        Literal geometry = factory.literal(node.getChildValue(Geometry.class));
        //
        //        String name = instance.getName();
        //
        //        //<xsd:element name="DWithin" substitutionGroup="ogc:spatialOps" type="ogc:DistanceBufferType"/>
        //        if ("DWithin".equals(name)) {
        //            //TOOD: units
        //            return factory.dwithin(propertyName, geometry, distance.doubleValue(), null);
        //        }
        //        //<xsd:element name="Beyond" substitutionGroup="ogc:spatialOps" type="ogc:DistanceBufferType"/>
        //        else if ("Beyond".equals(name)) {
        //            //TODO: units
        //            return factory.beyond(propertyName, geometry, distance.doubleValue(), null);
        //        } else {
        //            throw new IllegalArgumentException("Unknown - " + name);
        //        }
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        DistanceBufferOperator operator = (DistanceBufferOperator) object;
        Object property = OGCUtils.property(operator.getExpression1(), operator.getExpression2(),
                name);

        if (property != null) {
            return property;
        }

        if ("Distance".equals(name.getLocalPart())) {
            return new Double(operator.getDistance());
        }

        return null;
    }
}
