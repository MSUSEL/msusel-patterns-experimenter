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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.schema;

import java.util.Map;

import org.xml.sax.Attributes;


/**
 * <p>
 * This interface is intended to represent an XML Schema complexType. This
 * interface extends the generic XML schema type interface to represent datum
 * within nested elements.
 * </p>
 *
 * @author dzwiers www.refractions.net
 *
 *
 * @source $URL$
 */
public interface ComplexType extends Type {
    /**
     * <p>
     * This is used to represent the heirarchy represented within an xml schema
     * document(s). This is particularily useful, as the parent will have the
     * first attempt to create a real (non Object[]) value of the element. For
     * more information see getValue.
     * </p>
     *
     *
     * @see Type#getValue(Element, ElementValue[], Attributes)
     */
    public Type getParent();

    /**
     * Returns true when the complexType should be considered abstract, as
     * defined by the XML schema of which this complex type definition is a
     * part.
     *
     */
    public boolean isAbstract();

    /**
     * This methos represents the potential 'anyAttribute' declaration's
     * namespace attribute which may occur within a complex type definition.
     *
     */
    public String getAnyAttributeNameSpace();

    /**
     * The set of attributes required by this complex type declaration. As  per
     * the xml schema definition, there is not an implied order to the
     * attributes. For performance reasons an implementor may wich to order
     * the attributes from most common to least commonly used attributes.
     *
     */
    public Attribute[] getAttributes();

    /**
     * Specifies a mask which denotes which substitution mechanisms may be used
     * for this complex type definition.
     *
     *
     * @see Schema#EXTENSION
     * @see Schema#RESTRICTION
     * @see Schema#ALL
     */
    public int getBlock();

    /**
     * Returns the child element representing the structure of nested  child
     * nodes (if any are allowed).
     *
     *
     * @see ElementGrouping
     */
    public ElementGrouping getChild();

    public Element[] getChildElements();

    /**
     * Specifies a mask which denotes which substitution mechanisms prohibited
     * for use by child definitions of this complex type.
     *
     *
     * @see Schema#EXTENSION
     * @see Schema#RESTRICTION
     * @see Schema#ALL
     */
    public int getFinal();

    /**
     * Returns the xml schema id of this complexType if one  exists, null
     * otherwise.
     *
     */
    public String getId();

    /**
     * Returns true if this complexType allows mixed content (Child elements
     * and a String value).
     *
     */
    public boolean isMixed();

    /**
     * This method is used to publish whether this complexType is at the root
     * of an inheritance tree, or a leaf within an inheritance tree. This
     * method should return true when the complexType is not a root of an
     * inheritance  tree.
     *
     */
    public boolean isDerived();

    /**
     * This method is a directive to the parser whether to keep the data around
     * in  memory for post processing. Generally this should return True,
     * except when  streaming.
     *
     * @param element DOCUMENT ME!
     * @param hints DOCUMENT ME!
     *
     * @return True, except when streaming the element.
     */
    public boolean cache(Element element, Map hints);

}
