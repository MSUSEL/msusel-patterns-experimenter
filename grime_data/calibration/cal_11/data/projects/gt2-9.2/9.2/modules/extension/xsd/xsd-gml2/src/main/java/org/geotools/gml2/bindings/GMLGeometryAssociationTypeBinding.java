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
package org.geotools.gml2.bindings;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.gml2.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Binding object for the type http://www.opengis.net/gml:GeometryAssociationType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="GeometryAssociationType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         An instance of this type (e.g. a
 *              geometryMember) can either          enclose or point to a
 *              primitive geometry element. When serving          as a
 *              simple link that references a remote geometry instance,
 *              the value of the gml:remoteSchema attribute can be used to
 *              locate a schema fragment that constrains the target
 *              instance.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence minOccurs="0"&gt;
 *          &lt;element ref="gml:_Geometry"/&gt;
 *      &lt;/sequence&gt;optional
 *      &lt;!-- &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt; --&gt;
 *      &lt;attributeGroup ref="xlink:simpleLink"/&gt;
 *      &lt;attribute ref="gml:remoteSchema" use="optional"/&gt;
 *  &lt;/complexType&gt;
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
public class GMLGeometryAssociationTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.GeometryAssociationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Geometry.class;
    }

    /**
     * <!-- begin-user-doc -->
     * Returns an object of type @link Geometry.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //TODO: xlink and remoteSchema attributes, hard to do because of streaming
        return node.getChildValue(Geometry.class);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        return GML2EncodingUtils.GeometryPropertyType_getProperty((Geometry)object, name);
    }
    
    @Override
    public List getProperties(Object object, XSDElementDeclaration element)
            throws Exception {
        return GML2EncodingUtils.GeometryPropertyType_getProperties((Geometry)object);
    }
}
