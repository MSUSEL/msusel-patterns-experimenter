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
package org.hibernate.metamodel.domain;

import org.hibernate.internal.util.ValueHolder;

/**
 * Basic information about a Java type, in regards to its role in particular set of mappings.
 *
 * @author Steve Ebersole
 */
public interface Type {
	/**
	 * Obtain the name of the type.
	 *
	 * @return The name
	 */
	public String getName();

	/**
	 * Obtain the java class name for this type.
	 *
	 * @return The class name
	 */
	public String getClassName();

	/**
	 * Obtain the java {@link Class} reference for this type
	 *
	 * @return The {@link Class} reference
	 *
	 * @throws org.hibernate.service.classloading.spi.ClassLoadingException Indicates the class reference
	 * could not be determined.  Generally this is the case in reverse-engineering scenarios where the specified
	 * domain model classes do not yet exist.
	 */
	public Class<?> getClassReference();

	public ValueHolder<Class<?>> getClassReferenceUnresolved();

	public boolean isAssociation();

	public boolean isComponent();
}
