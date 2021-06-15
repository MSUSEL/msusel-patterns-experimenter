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
package org.hibernate.ejb.criteria.path;

import java.io.Serializable;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;

import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.PathSource;

/**
 * Models a path for a {@link SingularAttribute} generally obtained from a
 * {@link javax.persistence.criteria.Path#get(SingularAttribute)} call
 *
 * @author Steve Ebersole
 */
public class SingularAttributePath<X> extends AbstractPathImpl<X> implements Serializable {
	private final SingularAttribute<?,X> attribute;
	private final ManagedType<X> managedType;

	@SuppressWarnings({ "unchecked" })
	public SingularAttributePath(
			CriteriaBuilderImpl criteriaBuilder,
			Class<X> javaType,
			PathSource pathSource,
			SingularAttribute<?, X> attribute) {
		super( criteriaBuilder, javaType, pathSource );
		this.attribute = attribute;
		this.managedType = resolveManagedType( attribute );
	}

	private ManagedType<X> resolveManagedType(SingularAttribute<?, X> attribute) {
		if ( Attribute.PersistentAttributeType.BASIC == attribute.getPersistentAttributeType() ) {
			return null;
		}
		else if ( Attribute.PersistentAttributeType.EMBEDDED == attribute.getPersistentAttributeType() ) {
			return (EmbeddableType<X>) attribute.getType();
		}
		else {
			return (IdentifiableType<X>) attribute.getType();
//			return criteriaBuilder.getEntityManagerFactory()
//					.getMetamodel()
//					.managedType( javaType );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SingularAttribute<?, X> getAttribute() {
		return attribute;
	}

	/**
	 * {@inheritDoc}
	 */
	public Bindable<X> getModel() {
		return getAttribute();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean canBeDereferenced() {
		return managedType != null;
	}

	@Override
	protected Attribute locateAttributeInternal(String attributeName) {
		final Attribute attribute = managedType.getAttribute( attributeName );
		// ManagedType.locateAttribute should throw exception rather than return
		// null, but just to be safe...
		if ( attribute == null ) {
			throw new IllegalArgumentException( "Could not resolve attribute named " + attributeName );
		}
		return attribute;
	}
}
