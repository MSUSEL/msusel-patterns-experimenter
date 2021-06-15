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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.metamodel.domain.Attribute;
import org.hibernate.metamodel.source.MetaAttributeContext;

/**
 * Basic support for {@link AttributeBinding} implementors
 *
 * @author Steve Ebersole
 */
public abstract class AbstractAttributeBinding implements AttributeBinding {
	private final AttributeBindingContainer container;
	private final Attribute attribute;

	private final HibernateTypeDescriptor hibernateTypeDescriptor = new HibernateTypeDescriptor();
	private final Set<SingularAssociationAttributeBinding> entityReferencingAttributeBindings = new HashSet<SingularAssociationAttributeBinding>();

	private boolean includedInOptimisticLocking;

	private boolean isLazy;
	private String propertyAccessorName;
	private boolean isAlternateUniqueKey;

	private MetaAttributeContext metaAttributeContext;

	protected AbstractAttributeBinding(AttributeBindingContainer container, Attribute attribute) {
		this.container = container;
		this.attribute = attribute;
	}

	@Override
	public AttributeBindingContainer getContainer() {
		return container;
	}

	@Override
	public Attribute getAttribute() {
		return attribute;
	}

	@Override
	public HibernateTypeDescriptor getHibernateTypeDescriptor() {
		return hibernateTypeDescriptor;
	}

	@Override
	public boolean isBasicPropertyAccessor() {
		return propertyAccessorName == null || "property".equals( propertyAccessorName );
	}

	@Override
	public String getPropertyAccessorName() {
		return propertyAccessorName;
	}

	public void setPropertyAccessorName(String propertyAccessorName) {
		this.propertyAccessorName = propertyAccessorName;
	}

	@Override
	public boolean isIncludedInOptimisticLocking() {
		return includedInOptimisticLocking;
	}

	public void setIncludedInOptimisticLocking(boolean includedInOptimisticLocking) {
		this.includedInOptimisticLocking = includedInOptimisticLocking;
	}

	@Override
	public MetaAttributeContext getMetaAttributeContext() {
		return metaAttributeContext;
	}

	public void setMetaAttributeContext(MetaAttributeContext metaAttributeContext) {
		this.metaAttributeContext = metaAttributeContext;
	}

	@Override
	public boolean isAlternateUniqueKey() {
		return isAlternateUniqueKey;
	}

	public void setAlternateUniqueKey(boolean alternateUniqueKey) {
		this.isAlternateUniqueKey = alternateUniqueKey;
	}

	@Override
	public boolean isLazy() {
		return isLazy;
	}

	public void setLazy(boolean isLazy) {
		this.isLazy = isLazy;
	}

	public void addEntityReferencingAttributeBinding(SingularAssociationAttributeBinding referencingAttributeBinding) {
		entityReferencingAttributeBindings.add( referencingAttributeBinding );
	}

	public Set<SingularAssociationAttributeBinding> getEntityReferencingAttributeBindings() {
		return Collections.unmodifiableSet( entityReferencingAttributeBindings );
	}

	public void validate() {
		if ( !entityReferencingAttributeBindings.isEmpty() ) {
			// TODO; validate that this AttributeBinding can be a target of an entity reference
			// (e.g., this attribute is the primary key or there is a unique-key)
			// can a unique attribute be used as a target? if so, does it need to be non-null?
		}
	}
}
