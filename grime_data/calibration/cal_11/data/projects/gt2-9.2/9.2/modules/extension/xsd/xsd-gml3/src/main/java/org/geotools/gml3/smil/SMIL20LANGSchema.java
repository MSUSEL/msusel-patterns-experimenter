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
package org.geotools.gml3.smil;

import java.util.Collections;

import org.geotools.feature.NameImpl;
import org.geotools.feature.type.ComplexTypeImpl;
import org.geotools.feature.type.SchemaImpl;
import org.opengis.feature.type.ComplexType;


/**
 * 
 *
 * @source $URL$
 */
public class SMIL20LANGSchema extends SchemaImpl {
    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="animateType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="smil20:animatePrototype"&gt;
     *              &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
     *                  &lt;any namespace="##other" processContents="lax"/&gt;
     *              &lt;/choice&gt;
     *              &lt;attributeGroup ref="smil20lang:CoreAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20lang:TimingAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20:animTargetAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20:animModeAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20:skipContentAttrs"/&gt;
     *              &lt;anyAttribute namespace="##any" processContents="strict"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ANIMATETYPE_TYPE = new ComplexTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/Language", "animateType"), Collections.EMPTY_LIST,
            false, false, Collections.EMPTY_LIST, SMIL20Schema.ANIMATEPROTOTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="animateMotionType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="smil20:animateMotionPrototype"&gt;
     *              &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
     *                  &lt;any namespace="##other" processContents="lax"/&gt;
     *              &lt;/choice&gt;
     *              &lt;attributeGroup ref="smil20lang:CoreAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20lang:TimingAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20:animTargetAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20:animModeAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20:skipContentAttrs"/&gt;
     *              &lt;anyAttribute namespace="##any" processContents="strict"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ANIMATEMOTIONTYPE_TYPE = new ComplexTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/Language", "animateMotionType"),
            Collections.EMPTY_LIST, false, false, Collections.EMPTY_LIST,
            SMIL20Schema.ANIMATEMOTIONPROTOTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="setType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="smil20:setPrototype"&gt;
     *              &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
     *                  &lt;any namespace="##other" processContents="lax"/&gt;
     *              &lt;/choice&gt;
     *              &lt;attributeGroup ref="smil20lang:CoreAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20lang:TimingAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20:animTargetAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20:skipContentAttrs"/&gt;
     *              &lt;anyAttribute namespace="##any" processContents="strict"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType SETTYPE_TYPE = new ComplexTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/Language", "setType"), Collections.EMPTY_LIST,
            false, false, Collections.EMPTY_LIST, SMIL20Schema.SETPROTOTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="animateColorType"&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="smil20:animateColorPrototype"&gt;
     *              &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
     *                  &lt;any namespace="##other" processContents="lax"/&gt;
     *              &lt;/choice&gt;
     *              &lt;attributeGroup ref="smil20lang:CoreAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20lang:TimingAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20:animTargetAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20:animModeAttrs"/&gt;
     *              &lt;attributeGroup ref="smil20:skipContentAttrs"/&gt;
     *              &lt;anyAttribute namespace="##any" processContents="strict"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ANIMATECOLORTYPE_TYPE = new ComplexTypeImpl(new NameImpl(
                "http://www.w3.org/2001/SMIL20/Language", "animateColorType"),
            Collections.EMPTY_LIST, false, false, Collections.EMPTY_LIST,
            SMIL20Schema.ANIMATECOLORPROTOTYPE_TYPE, null);

    public SMIL20LANGSchema() {
        super("http://www.w3.org/2001/SMIL20/Language");

        put(new NameImpl("http://www.w3.org/2001/SMIL20/Language", "animateType"), ANIMATETYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/SMIL20/Language", "animateMotionType"),
            ANIMATEMOTIONTYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/SMIL20/Language", "setType"), SETTYPE_TYPE);
        put(new NameImpl("http://www.w3.org/2001/SMIL20/Language", "animateColorType"),
            ANIMATECOLORTYPE_TYPE);
    }
}
