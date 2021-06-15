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
package org.hibernate.metamodel.binding;

import org.hibernate.mapping.PropertyGeneration;
import org.hibernate.metamodel.relational.Value;

/**
 * Specialized binding contract for singular (non-collection) attributes
 *
 * @author Steve Ebersole
 */
public interface SingularAttributeBinding extends AttributeBinding {
	/**
	 * Obtain the value bound here.  This could potentially be a {@link org.hibernate.metamodel.relational.Tuple}
	 * indicating multiple database values are bound, in which case access to the individual values can be achieved by
	 * either casting this return to {@link org.hibernate.metamodel.relational.Tuple} and using its
	 * {@link org.hibernate.metamodel.relational.Tuple#values()} method or using the {@link #getSimpleValueBindings()}
	 * method here and accessing each bindings {@link SimpleValueBinding#getSimpleValue simple value}
	 *
	 * @return The bound value
	 */
	public Value getValue();

	/**
	 * Returns the number of {@link SimpleValueBinding} objects that will be returned by
	 * {@link #getSimpleValueBindings()}
	 *
	 * @return the number of {@link SimpleValueBinding simple value bindings}
	 *
	 * @see #getSimpleValueBindings()
	 */
	public int getSimpleValueSpan();

	public Iterable<SimpleValueBinding> getSimpleValueBindings();

	public void setSimpleValueBindings(Iterable<SimpleValueBinding> simpleValueBindings);

	/**
	 * Convenience method to determine if any {@link SimpleValueBinding simple value bindings} are derived values
	 * (formula mappings).
	 *
	 * @return {@code true} indicates that the binding contains a derived value; {@code false} indicates it does not.
	 */
	public boolean hasDerivedValue();

	/**
	 * Convenience method to determine if all {@link SimpleValueBinding simple value bindings} allow nulls.
	 *
	 * @return {@code true} indicates that all values allow {@code null}; {@code false} indicates one or more do not
	 */
	public boolean isNullable();

	/**
	 * Obtain the generation strategy for this attribute/value.
	 *
	 * @return The generation strategy
	 */
	public PropertyGeneration getGeneration();
}
