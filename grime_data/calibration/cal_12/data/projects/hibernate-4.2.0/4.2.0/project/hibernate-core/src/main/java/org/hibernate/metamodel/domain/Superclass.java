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
 * Models the concept of a (intermediate) superclass
 *
 * @author Steve Ebersole
 */
public class Superclass extends AbstractAttributeContainer {
	/**
	 * Constructor for the entity
	 *
	 * @param entityName The name of the entity
	 * @param className The name of this entity's java class
	 * @param classReference The reference to this entity's {@link Class}
	 * @param superType The super type for this entity. If there is not super type {@code null} needs to be passed.
	 */
	public Superclass(String entityName, String className, ValueHolder<Class<?>> classReference, Hierarchical superType) {
		super( entityName, className, classReference, superType );
	}

	@Override
	public boolean isAssociation() {
		return true;
	}

	@Override
	public boolean isComponent() {
		return false;
	}
}
