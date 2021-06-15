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
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.util;


/**
 * Indicates that it is legal to make a field-for-field copy of instances of implementing classes.
 * A cloneable class implements the J2SE's {@link java.lang.Cloneable} standard interface and
 * additionnaly overrides the {@link Object#clone()} method with public access.
 * <p>
 * Because the {@link Object#clone()} method has protected access, containers wanting to clone
 * theirs elements need to 1) use Java reflection (which is less efficient than standard method
 * calls), or 2) cast every elements to a specific type like {@link java.util.Date} (which may
 * require a large amount of "{@code if (x instanceof y)}" checks if arbitrary classes are
 * allowed). This {@code Cloneable} interface had a third alternative: checks only for this
 * interface instead of a list of particular cases.
 * <p>
 * Implementors of cloneable classes may consider implementing this interface, but this is not
 * mandatory. A large amount of independant classes like {@link java.util.Date} will continue to
 * ignore this interface, so no rule can be enforced anyway. However this interface may help the
 * work of containers in some case. For example a container may checks for this interface first,
 * and uses Java reflection as a fallback.
 *
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @see java.lang.Cloneable
 * @see <A HREF="http://developer.java.sun.com/developer/bugParade/bugs/4098033.html">&quot;<cite>Cloneable
 *      doesn't define <code>clone()</code></cite>&quot; on Sun's bug parade</A>
 *
 *
 * @source $URL$
 */
public interface Cloneable extends java.lang.Cloneable {
    /**
     * Creates and returns a copy of this object.
     * The precise meaning of "copy" may depend on the class of the object.
     *
     * @return A copy of this object.
     *
     * @see Object#clone
     */
    Object clone();
}
