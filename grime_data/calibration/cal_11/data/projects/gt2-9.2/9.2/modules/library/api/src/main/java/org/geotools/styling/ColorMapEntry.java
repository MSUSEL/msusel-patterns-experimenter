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
package org.geotools.styling;

import org.opengis.filter.expression.Expression;


/**
 * A basic interface for objects which can hold color map entries.
 * <pre>
 *  &lt;xs:element name="ColorMapEntry"&gt;
 *  &lt;xs:complexType&gt;
 *  &lt;xs:attribute name="color" type="xs:string" use="required"/&gt;
 *  &lt;xs:attribute name="opacity" type="xs:double"/&gt;
 *  &lt;xs:attribute name="quantity" type="xs:double"/&gt;
 *  &lt;xs:attribute name="label" type="xs:string"/&gt;
 *  &lt;/xs:complexType&gt;
 *  &lt;/xs:element&gt;
 *  </pre>
 *
 *
 * @source $URL$
 */
public interface ColorMapEntry {
    /** Label for this Color Map Entry */
    String getLabel();
    
    /**
     * @param label
     */
    void setLabel(String label);

    /**
     * Expression resulting in a color
     * @param color
     */
    void setColor(Expression color);

    /**
     * @return Expression evaualted into a color
     */
    Expression getColor();

    /**
     * @param opacity Expressed as a value between 0 and 1
     */
    void setOpacity(Expression opacity);
    /**
     * 
     * @return Opacity expressed as a value between 0 and 1
     */
    Expression getOpacity();

    /**
     * Quantity marking the start of this color map entry.
     * 
     * @param quantity
     */
    void setQuantity(Expression quantity);
    
    /**
     * @return Quanity marking the start of this color map entry
     */
    Expression getQuantity();

    void accept(org.geotools.styling.StyleVisitor visitor);
}
