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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.feature.xpath;

import java.util.Iterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.xml.Schemas;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.PropertyType;

/**
 * Special node pointer for an XML-attribute inside an attribute.
 * 
 * @author Niels Charlier (Curtin University of Technology)
 * 
 *
 *
 *
 *
 * @source $URL$
 */
public class DescriptorXmlAttributeNodePointer extends NodePointer {

    /**
     * 
     */
    private static final long serialVersionUID = 8096170689141331692L;

    /**
     * The name of the node.
     */
    Name name;

    /**
     * The underlying descriptor
     */
    PropertyDescriptor descriptor;

    protected DescriptorXmlAttributeNodePointer(NodePointer parent, PropertyDescriptor descriptor, Name name) {
        super(parent);
        this.name = name;
        this.descriptor = descriptor;
    }

    public boolean isLeaf() {
        return true;
    }

    public boolean isCollection() {
        return false;
    }
    
    public boolean isAttribute() {
        return true;
    }

    public QName getName() {
        return new QName( name.getURI(), name.getLocalPart() );
    }

    public Object getBaseValue() {
        return null;
    }

    public Object getImmediateNode() {

        //first try regular way
        PropertyType pt = descriptor.getType();
        if (pt instanceof ComplexType) {
            ComplexType ct = (ComplexType) pt;
            PropertyDescriptor ad = ct.getDescriptor("@" + name.getLocalPart());
            if (ad != null) {
                return ad;
            }
        }

        XSDElementDeclaration decl = (XSDElementDeclaration) descriptor.getUserData().get(
                XSDElementDeclaration.class);

        Iterator it = Schemas.getAttributeDeclarations(decl).iterator();
        while (it.hasNext()) {
            XSDAttributeDeclaration attDecl = ((XSDAttributeDeclaration) it.next());
            if (attDecl.getURI().equals(
                    (name.getNamespaceURI() == null ? "" : name.getNamespaceURI()) + "#"
                            + name.getLocalPart())) {
                return name;
            }
        }
        return null;
    }

    public void setValue(Object value) {
        throw new UnsupportedOperationException("Feature types are immutable");
    }

    @Override
    public int compareChildNodePointers(NodePointer arg0, NodePointer arg1) {
        return 0;
    }

    @Override
    public int getLength() {
        return 0;
    }


}
