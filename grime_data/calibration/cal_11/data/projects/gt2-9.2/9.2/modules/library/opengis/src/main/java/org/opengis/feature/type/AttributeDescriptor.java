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
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature.type;

/**
 * Describes an instance of an Attribute.
 * <p>
 * An AttributeDescriptor is an extension of {@link PropertyDescriptor} which
 * defines some additional information:
 * <ul>
 *   <li>A default value for an attribute
 * </ul>
 * </p>
 * <p>
 *
 * @author Jody Garnett, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public interface AttributeDescriptor extends PropertyDescriptor {

    /**
     * Override of {@link PropertyDescriptor#getType()} which type narrows to
     * {@link AttributeType}.
     *
     *  @see PropertyDescriptor#getType()
     */
    AttributeType getType();

    /**
     * The local name for this AttributeDescriptor.
     * Specifically this returns <code>getName().getLocalPart</code>().
     * @return The local name for this attribute descriptor.
     */
    String getLocalName();

    /**
     * The default value for the attribute.
     * <p>
     * This value is used when an attribute is created and no value for it is
     * specified.
     * </p>
     * <p>
     * This value may be <code>null</code>. If it is non-null it should be an
     * instance of of the class specified by <code>getType().getBinding()</code>.
     * </p>
     */
    Object getDefaultValue();
}
