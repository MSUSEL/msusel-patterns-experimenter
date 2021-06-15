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

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.PluralJoin;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.Type;

import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.PathSource;

/**
 * Support for defining joins to plural attributes (JPA requires typing based on
 * the specific collection type so we cannot really implement all support in a
 * single class)
 *
 * @author Steve Ebersole
 */
public abstract class PluralAttributeJoinSupport<O,C,E>
		extends AbstractJoinImpl<O,E>
		implements PluralJoin<O,C,E> {

	public PluralAttributeJoinSupport(
			CriteriaBuilderImpl criteriaBuilder,
			Class<E> javaType,
			PathSource<O> pathSource,
			Attribute<? super O,?> joinAttribute,
			JoinType joinType) {
		super( criteriaBuilder, javaType, pathSource, joinAttribute, joinType );
	}

	@Override
	public PluralAttribute<? super O, C, E> getAttribute() {
		return (PluralAttribute<? super O, C, E>) super.getAttribute();
	}

	public PluralAttribute<? super O, C, E> getModel() {
		return getAttribute();
	}

	@Override
	protected ManagedType<E> locateManagedType() {
		return isBasicCollection()
				? null
				: (ManagedType<E>) getAttribute().getElementType();
	}

	public boolean isBasicCollection() {
		return Type.PersistenceType.BASIC.equals( getAttribute().getElementType().getPersistenceType() );
	}

	@Override
	protected boolean canBeDereferenced() {
		return !isBasicCollection();
	}

	@Override
	protected boolean canBeJoinSource() {
		return !isBasicCollection();
	}
}
