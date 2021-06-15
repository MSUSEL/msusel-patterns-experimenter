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
package org.geotools.gml3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.feature.AttributeImpl;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.ComplexTypeImpl;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * Create ComplexAttributes to assist in testing bindings
 * 
 * @author Rob Atkinson, CSIRO Land and Water
 *
 *
 * @source $URL$
 */

public abstract class ComplexAttributeTestSupport extends GML3TestSupport {

    /*
     * can extend this later to generate more generate complex attributes - lets start with
     * something concrete
     */
    public ComplexAttribute gmlCodeType(QName typeName, String value, String codeSpace) {
        Name myType = new NameImpl(typeName.getNamespaceURI(), typeName.getLocalPart());

        List<Property> properties = new ArrayList<Property>();
        List<PropertyDescriptor> propertyDescriptors = new ArrayList<PropertyDescriptor>();

        Name attName = new NameImpl("codeSpace");
        // Name name, Class<?> binding, boolean isAbstract, List<Filter> restrictions,
        // PropertyType superType, InternationalString description
        AttributeType p = new AttributeTypeImpl(attName, String.class, false, false, null, null,
                null);
        AttributeDescriptor pd = new AttributeDescriptorImpl(p, attName, 0, 0, false, null);

        propertyDescriptors.add(pd);
        properties.add(new AttributeImpl(codeSpace, pd, null));

        p = new AttributeTypeImpl(new NameImpl("simpleContent"), String.class, false, false, null,
                null, null);
        AttributeDescriptor pd2 = new AttributeDescriptorImpl(p, new NameImpl("simpleContent"), 0,
                0, false, null);

        properties.add(new AttributeImpl(value, pd2, null));
        propertyDescriptors.add(pd2);

        ComplexTypeImpl at = new ComplexTypeImpl(myType, propertyDescriptors, false, false,
                Collections.EMPTY_LIST, null, null);

        AttributeDescriptorImpl ai = new AttributeDescriptorImpl(at, myType, 0, 0, false, null);

        return new ComplexAttributeImpl(properties, ai, null);
    }

    public ComplexAttribute gmlMeasureType(QName typeName, String value, String uom) {
        Name myType = new NameImpl(typeName.getNamespaceURI(), typeName.getLocalPart());

        List<Property> properties = new ArrayList<Property>();
        List<PropertyDescriptor> propertyDescriptors = new ArrayList<PropertyDescriptor>();

        // assume attributes from same namespace as typename

        Name attName = new NameImpl("uom");
        // Name name, Class<?> binding, boolean isAbstract, List<Filter> restrictions,
        // PropertyType superType, InternationalString description
        AttributeType p = new AttributeTypeImpl(attName, String.class, false, false, null, null,
                null);
        AttributeDescriptor pd = new AttributeDescriptorImpl(p, attName, 0, 0, false, null);

        propertyDescriptors.add(pd);
        properties.add(new AttributeImpl(uom, pd, null));

        p = new AttributeTypeImpl(new NameImpl("simpleContent"), String.class, false, false, null,
                null, null);
        AttributeDescriptor pd2 = new AttributeDescriptorImpl(p, new NameImpl("simpleContent"), 0,
                0, false, null);

        properties.add(new AttributeImpl(value, pd2, null));
        propertyDescriptors.add(pd2);

        ComplexTypeImpl at = new ComplexTypeImpl(myType, propertyDescriptors, false, false,
                Collections.EMPTY_LIST, null, null);

        AttributeDescriptorImpl ai = new AttributeDescriptorImpl(at, myType, 0, 0, false, null);

        return new ComplexAttributeImpl(properties, ai, null);
    }

}
