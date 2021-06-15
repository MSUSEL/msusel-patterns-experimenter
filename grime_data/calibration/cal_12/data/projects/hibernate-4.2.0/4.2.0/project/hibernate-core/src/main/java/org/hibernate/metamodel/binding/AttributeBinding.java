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

import java.util.Set;

import org.hibernate.metamodel.domain.Attribute;
import org.hibernate.metamodel.source.MetaAttributeContext;

/**
 * The basic contract for binding a {@link #getAttribute() attribute} from the domain model to the relational model.
 *
 * @author Steve Ebersole
 */
public interface AttributeBinding {
	/**
	 * Obtain the entity binding to which this attribute binding exists.
	 *
	 * @return The entity binding.
	 */
	public AttributeBindingContainer getContainer();

	/**
	 * Obtain the attribute bound.
	 *
	 * @return The attribute.
	 */
	public Attribute getAttribute();

	/**
	 * Obtain the descriptor for the Hibernate {@link org.hibernate.type.Type} for this binding.
	 * <p/>
	 * For information about the Java type, query the {@link Attribute} obtained from {@link #getAttribute()}
	 * instead.
	 *
	 * @return The type descriptor
	 */
	public HibernateTypeDescriptor getHibernateTypeDescriptor();

	public boolean isAssociation();

	public boolean isBasicPropertyAccessor();

	public String getPropertyAccessorName();

	public void setPropertyAccessorName(String propertyAccessorName);

	public boolean isIncludedInOptimisticLocking();

	public void setIncludedInOptimisticLocking(boolean includedInOptimisticLocking);

	/**
	 * Obtain the meta attributes associated with this binding
	 *
	 * @return The meta attributes
	 */
	public MetaAttributeContext getMetaAttributeContext();

	public boolean isAlternateUniqueKey();

	public boolean isLazy();

	public void addEntityReferencingAttributeBinding(SingularAssociationAttributeBinding attributeBinding);

	public Set<SingularAssociationAttributeBinding> getEntityReferencingAttributeBindings();

	public void validate();
}
