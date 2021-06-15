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
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.Attribute;

import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.CriteriaQueryCompiler;
import org.hibernate.ejb.criteria.CriteriaSubqueryImpl;
import org.hibernate.ejb.criteria.FromImplementor;
import org.hibernate.ejb.criteria.JoinImplementor;
import org.hibernate.ejb.criteria.PathSource;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public abstract class AbstractJoinImpl<Z, X>
		extends AbstractFromImpl<Z, X>
		implements JoinImplementor<Z,X>, Serializable {

	private final Attribute<? super Z, ?> joinAttribute;
	private final JoinType joinType;

	public AbstractJoinImpl(
			CriteriaBuilderImpl criteriaBuilder,
			PathSource<Z> pathSource,
			Attribute<? super Z, X> joinAttribute,
			JoinType joinType) {
		this( criteriaBuilder, joinAttribute.getJavaType(), pathSource, joinAttribute, joinType );
	}

	public AbstractJoinImpl(
			CriteriaBuilderImpl criteriaBuilder,
			Class<X> javaType,
			PathSource<Z> pathSource,
			Attribute<? super Z, ?> joinAttribute,
			JoinType joinType) {
		super( criteriaBuilder, javaType, pathSource );
		this.joinAttribute = joinAttribute;
		this.joinType = joinType;
	}

	/**
	 * {@inheritDoc}
	 */
	public Attribute<? super Z, ?> getAttribute() {
		return joinAttribute;
	}

	/**
	 * {@inheritDoc}
	 */
	public JoinType getJoinType() {
		return joinType;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public From<?, Z> getParent() {
		// this cast should be ok by virtue of our constructors...
		return (From<?, Z>) getPathSource();
	}

	public String renderTableExpression(CriteriaQueryCompiler.RenderingContext renderingContext) {
		prepareAlias( renderingContext );
		( (FromImplementor) getParent() ).prepareAlias( renderingContext );
		return getParent().getAlias() + '.' + getAttribute().getName() + " as " + getAlias();
	}


	public JoinImplementor<Z, X> correlateTo(CriteriaSubqueryImpl subquery) {
		return (JoinImplementor<Z, X>) super.correlateTo( subquery );
	}
}
