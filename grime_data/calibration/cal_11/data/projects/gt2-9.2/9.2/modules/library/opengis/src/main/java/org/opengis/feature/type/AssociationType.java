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

import org.opengis.feature.Association;

/**
 * The type of an association; used to describe kind of relationship between two entities.
 * <p>
 * The notion of an "association" is similar to that of an association in UML
 * and is used to model a relationship among two attributes. See the javadoc for
 * {@link Association} for more info on the semantics of associations.
 * </p>
 * <p>
 * An association is used to relate one attribute to another. The type of the
 * association specifies the type of the related attribute with the
 * {@link #getRelatedType()} method.
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public interface AssociationType extends PropertyType {

    /**
     * Override of {@link PropertyType#getSuper()} which type narrows to
     * {@link AssociationType}.
     *
     * @see PropertyType#getSuper()
     */
    AssociationType getSuper();

    /**
     * The attribute type of the related attribute in the association.
     *
     * @return The type of the related attribute.
     */
    AttributeType getRelatedType();

    /**
     * Override of {@link PropertyType#getBinding()} which specifies that this
     * method should return <code>getRelatedType().getBinding()</code>, that is
     * it returns the binding of the related type.
     */
    Class<?> getBinding();
}
