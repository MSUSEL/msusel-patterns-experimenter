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
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.SingularAttribute;

import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.CriteriaSubqueryImpl;
import org.hibernate.ejb.criteria.FromImplementor;
import org.hibernate.ejb.criteria.PathSource;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class SingularAttributeJoin<Z,X> extends AbstractJoinImpl<Z,X> {
	private final Bindable<X> model;

	@SuppressWarnings({ "unchecked" })
	public SingularAttributeJoin(
			CriteriaBuilderImpl criteriaBuilder,
			Class<X> javaType,
			PathSource<Z> pathSource, 
			SingularAttribute<? super Z, ?> joinAttribute,
			JoinType joinType) {
		super( criteriaBuilder, javaType, pathSource, joinAttribute, joinType );
		this.model = (Bindable<X>) (
				Attribute.PersistentAttributeType.EMBEDDED == joinAttribute.getPersistentAttributeType()
						? joinAttribute
						: criteriaBuilder.getEntityManagerFactory().getMetamodel().managedType( javaType )
		);
	}

	@Override
	public SingularAttribute<? super Z, ?> getAttribute() {
		return (SingularAttribute<? super Z, ?>) super.getAttribute();
	}

	@Override
	public SingularAttributeJoin<Z, X> correlateTo(CriteriaSubqueryImpl subquery) {
		return (SingularAttributeJoin<Z, X>) super.correlateTo( subquery );
	}

	@Override
	protected FromImplementor<Z, X> createCorrelationDelegate() {
		return new SingularAttributeJoin<Z,X>(
				criteriaBuilder(),
				getJavaType(),
				getPathSource(),
				getAttribute(),
				getJoinType()
		);
	}

	@Override
	protected boolean canBeJoinSource() {
		return true;
	}

	public Bindable<X> getModel() {
		return model;
	}
}
