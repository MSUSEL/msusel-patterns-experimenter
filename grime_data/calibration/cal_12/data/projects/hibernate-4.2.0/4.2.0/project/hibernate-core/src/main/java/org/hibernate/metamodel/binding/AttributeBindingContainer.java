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

import org.hibernate.metamodel.domain.AttributeContainer;
import org.hibernate.metamodel.domain.PluralAttribute;
import org.hibernate.metamodel.domain.SingularAttribute;
import org.hibernate.metamodel.source.MetaAttributeContext;

/**
 * Common contract for {@link EntityBinding} and {@link ComponentAttributeBinding} in so far as they are both
 * containers for {@link AttributeBinding} descriptors
 *
 * @author Steve Ebersole
 */
public interface AttributeBindingContainer {
	/**
	 * Obtain the path base of this container.  Intended to help uniquely identify each attribute binding.
	 *
	 * @return The path base for this container.
	 */
	public String getPathBase();

	/**
	 * Obtain the underlying domain attribute container.
	 *
	 * @return The attribute container
	 */
	public AttributeContainer getAttributeContainer();

	/**
	 * Obtain all attribute bindings
	 *
	 * @return All attribute bindings
	 */
	public Iterable<AttributeBinding> attributeBindings();

	/**
	 * Locate a specific attribute binding, by its local name.
	 *
	 * @param name The name of the attribute, local to this container.
	 *
	 * @return The attribute binding.
	 */
	public AttributeBinding locateAttributeBinding(String name);

	/**
	 * Factory method for basic attribute bindings.
	 *
	 * @param attribute The attribute for which to make a binding.
	 *
	 * @return The attribute binding instance.
	 */
	public BasicAttributeBinding makeBasicAttributeBinding(SingularAttribute attribute);

	/**
	 * Factory method for component attribute bindings.
	 *
	 * @param attribute The attribute for which to make a binding.
	 *
	 * @return The attribute binding instance.
	 */
	public ComponentAttributeBinding makeComponentAttributeBinding(SingularAttribute attribute);

	/**
	 * Factory method for many-to-one attribute bindings.
	 *
	 * @param attribute The attribute for which to make a binding.
	 *
	 * @return The attribute binding instance.
	 */
	public ManyToOneAttributeBinding makeManyToOneAttributeBinding(SingularAttribute attribute);

	/**
	 * Factory method for bag attribute bindings.
	 *
	 * @param attribute The attribute for which to make a binding.
	 * @param nature The nature of the collection elements.
	 *
	 * @return The attribute binding instance.
	 */
	public BagBinding makeBagAttributeBinding(PluralAttribute attribute, CollectionElementNature nature);

	/**
	 * Factory method for bag attribute bindings.
	 *
	 * @param attribute The attribute for which to make a binding.
	 * @param nature The nature of the collection elements.
	 *
	 * @return The attribute binding instance.
	 */
	public SetBinding makeSetAttributeBinding(PluralAttribute attribute, CollectionElementNature nature);

	/**
	 * Seeks out the entity binding that is the root of this component path.
	 *
	 * @return The entity binding
	 */
	public EntityBinding seekEntityBinding();

	/**
	 * Obtain the {@link Class} reference for this attribute container.  Generally this is used to perform reflection
	 * on the attributes.
	 *
	 * @return The {@link Class} reference
	 */
	public Class<?> getClassReference();

	/**
	 * Obtain the meta-attribute context for this container.
	 *
	 * @return The meta-attribute context.
	 */
	public MetaAttributeContext getMetaAttributeContext();
}
