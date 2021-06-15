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

import org.opengis.feature.Attribute;

/**
 * The type of an attribute.
 * <p>
 * An attribute is similar to the notion of a UML attribute, or a field of a java
 * object. See the javadoc of {@link Attribute} for more info on the semantics
 * of attributes.
 * </p>
 * <p>
 * <h3>Identifiablily</h3>
 * An attribute may be "identifiable". When this is the case the attribute has a
 * unique identifier associated with it. See {@link Attribute#getID()}. The type
 * of the attribute specifies wether it is identifiable or not ({@link #isIdentified()}.
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public interface AttributeType extends PropertyType {

    /**
     * Indicates if the type is identified or not.
     * <p>
     * If this method returns <code>true</code>, then the corresponding
     * attribute must have a unique identifier, ie, {@link Attribute#getID()}
     * must return a value, and cannot be <code>null</code>.
     * </p>
     *
     * @return <code>true</code> if the attribute is identified, otherwise <code>false</code>.
     *
     * @see Attribute#getID()
     */
    boolean isIdentified();

    /**
     * Override of {@link PropertyType#getSuper()} which type narrows to
     * {@link AttributeType}.
     *
     * @see PropertyType#getSuper()
     */
    AttributeType getSuper();
}
