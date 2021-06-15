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
package org.geotools.xs.bindings;

import org.picocontainer.MutablePicoContainer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xs.XS;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:anyType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:complexType name="anyType" mixed="true"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:documentation&gt;    Not the real urType, but as close an
 *              approximation as we can    get in the XML representation&lt;/xs:documentation&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:sequence&gt;
 *          &lt;xs:any minOccurs="0" maxOccurs="unbounded" processContents="lax"/&gt;
 *      &lt;/xs:sequence&gt;
 *      &lt;xs:anyAttribute processContents="lax"/&gt;
 *  &lt;/xs:complexType&gt;
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
public class XSAnyTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.ANYTYPE;
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
     * This binding returns objects of type Map
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Object.class;
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
     *
     * <!-- begin-user-doc -->
     * A quick approx of the available content:
     * <p>
     * This method returns a {@link Map} in which the names of children and
     * attributes are keys, and the parsed children and attributes are the
     * values. If the element being parsed contains child text, it is available
     * under the <code>nulll</code> key.
     *
     * @return Map,
     * </p>
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        String text = null;

        if ((value != null) && value instanceof String) {
            text = ((String) value).trim();

            if ("".equals(text)) {
                text = null;
            }
        }

        //if there is just some text, return it
        if (node.getChildren().isEmpty() && node.getAttributes().isEmpty() && (text != null)) {
            return text;
        }

        //if there is only a single child, return it
        if ((node.getChildren().size() == 1) && node.getAttributes().isEmpty() && (text == null)) {
            return node.getChildValue(0);
        }

        //if there is a single attribute, return it
        if ((node.getAttributes().size() == 1) && node.getChildren().isEmpty() && (text == null)) {
            return ((Node) node.getAttributes().get(0)).getValue();
        }

        //create a map of the elements and attributes
        Map map = new HashMap();
        List attributes = node.getAttributes();
        List children = node.getChildren();
        mapBinding(map, attributes);
        mapBinding(map, children);

        if ((text != null) && !"".equals(text.trim())) {
            map.put(null, text.trim());
        }

        return map;
    }

    private void mapBinding(Map map, List attributes) {
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            Node attribute = (Node) i.next();
            String name = attribute.getComponent().getName();
            Object value = attribute.getValue();

            if (map.containsKey(name)) {
                List values;
                Object obj = map.get(name);

                if (obj instanceof List) {
                    values = (List) obj;
                } else {
                    values = new ArrayList();
                    values.add(obj);
                    map.put(name, values);
                }

                values.add(value);
            } else {
                map.put(name, value);
            }
        }
    }
}
