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
import javax.persistence.metamodel.PluralAttribute;

import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.PathSource;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.persister.collection.CollectionPersister;

/**
 * Models a path for a {@link PluralAttribute} generally obtained from a
 * {@link javax.persistence.criteria.Path#get} call
 *
 * @author Steve Ebersole
 */
public class PluralAttributePath<X> extends AbstractPathImpl<X> implements Serializable {
	private final PluralAttribute<?,X,?> attribute;
	private final CollectionPersister persister;

	public PluralAttributePath(
			CriteriaBuilderImpl criteriaBuilder,
			PathSource source,
			PluralAttribute<?,X,?> attribute) {
		super( criteriaBuilder, attribute.getJavaType(), source );
		this.attribute = attribute;
		this.persister = resolvePersister( criteriaBuilder, attribute );
	}

	private static CollectionPersister resolvePersister(CriteriaBuilderImpl criteriaBuilder, PluralAttribute attribute) {
		SessionFactoryImplementor sfi = (SessionFactoryImplementor)
				criteriaBuilder.getEntityManagerFactory().getSessionFactory();
		return sfi.getCollectionPersister( resolveRole( attribute ) );
	}

	private static String resolveRole(PluralAttribute attribute) {
		return attribute.getDeclaringType().getJavaType().getName() +
				'.' + attribute.getName();
	}

	public PluralAttribute<?,X,?> getAttribute() {
		return attribute;
	}

	@SuppressWarnings({ "UnusedDeclaration" })
	public CollectionPersister getPersister() {
		return persister;
	}

	@Override
	protected boolean canBeDereferenced() {
		// cannot be dereferenced
		return false;
	}

	@Override
	protected Attribute locateAttributeInternal(String attributeName) {
		throw new IllegalArgumentException( "Plural attribute paths cannot be further dereferenced" );
	}

	public Bindable<X> getModel() {
		// the issue here is the parameterized type; X is the collection
		// type (Map, Set, etc) while the "bindable" for a collection is the
		// elements.
		//
		// TODO : throw exception instead?
		return null;
	}
}
