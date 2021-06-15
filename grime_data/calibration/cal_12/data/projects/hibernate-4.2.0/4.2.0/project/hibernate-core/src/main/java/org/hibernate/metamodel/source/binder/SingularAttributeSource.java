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
package org.hibernate.metamodel.source.binder;

import org.hibernate.mapping.PropertyGeneration;

/**
 * Source-agnostic description of information needed to bind a singular attribute.
 *
 * @author Steve Ebersole
 */
public interface SingularAttributeSource extends AttributeSource, RelationalValueSourceContainer {
	/**
	 * Determine whether this is a virtual attribute or whether it physically exists on the users domain model.
	 *
	 * @return {@code true} indicates the attribute is virtual, meaning it does NOT exist on the domain model;
	 *         {@code false} indicates the attribute physically exists.
	 */
	public boolean isVirtualAttribute();

	/**
	 * Obtain the nature of this attribute type.
	 *
	 * @return The attribute type nature
	 */
	public SingularAttributeNature getNature();

	/**
	 * Determine whether this attribute is insertable.
	 *
	 * @return {@code true} indicates the attribute value should be used in the {@code SQL INSERT}; {@code false}
	 *         indicates it should not.
	 */
	public boolean isInsertable();

	/**
	 * Determine whether this attribute is updateable.
	 *
	 * @return {@code true} indicates the attribute value should be used in the {@code SQL UPDATE}; {@code false}
	 *         indicates it should not.
	 */
	public boolean isUpdatable();

	/**
	 * Obtain a description of if/when the attribute value is generated by the database.
	 *
	 * @return The attribute value generation information
	 */
	public PropertyGeneration getGeneration();

	/**
	 * Should the attribute be (bytecode enhancement) lazily loaded?
	 *
	 * @return {@code true} to indicate the attribute should be lazily loaded.
	 */
	public boolean isLazy();
}
