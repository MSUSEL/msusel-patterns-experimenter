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
import java.util.Map;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.MapAttribute;

import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.CriteriaSubqueryImpl;
import org.hibernate.ejb.criteria.FromImplementor;
import org.hibernate.ejb.criteria.MapJoinImplementor;
import org.hibernate.ejb.criteria.PathImplementor;
import org.hibernate.ejb.criteria.PathSource;
import org.hibernate.ejb.criteria.expression.MapEntryExpression;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class MapAttributeJoin<O,K,V>
		extends PluralAttributeJoinSupport<O, Map<K,V>, V>
		implements MapJoinImplementor<O,K,V>, Serializable {

	public MapAttributeJoin(
			CriteriaBuilderImpl criteriaBuilder,
			Class<V> javaType,
			PathSource<O> pathSource,
			MapAttribute<? super O, K, V> joinAttribute,
			JoinType joinType) {
		super( criteriaBuilder, javaType, pathSource, joinAttribute, joinType );
	}

	@Override
	public MapAttribute<? super O, K, V> getAttribute() {
		return (MapAttribute<? super O, K, V>) super.getAttribute();
	}

	@Override
	public MapAttribute<? super O, K, V> getModel() {
		return getAttribute();
	}

	@Override
	public final MapAttributeJoin<O,K,V> correlateTo(CriteriaSubqueryImpl subquery) {
		return (MapAttributeJoin<O,K,V>) super.correlateTo( subquery );
	}

	@Override
	protected FromImplementor<O, V> createCorrelationDelegate() {
		return new MapAttributeJoin<O,K,V>(
				criteriaBuilder(),
				getJavaType(),
				(PathImplementor<O>) getParentPath(),
				getAttribute(),
				getJoinType()
		);
	}


	/**
	 * {@inheritDoc}
	 */
	public Path<V> value() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public Expression<Map.Entry<K, V>> entry() {
		return new MapEntryExpression( criteriaBuilder(), Map.Entry.class, this, getAttribute() );
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public Path<K> key() {
		final MapKeyHelpers.MapKeySource<K,V> mapKeySource = new MapKeyHelpers.MapKeySource<K,V>(
				criteriaBuilder(),
				getAttribute().getJavaType(),
				this,
				getAttribute()
		);
		final MapKeyHelpers.MapKeyAttribute mapKeyAttribute = new MapKeyHelpers.MapKeyAttribute( criteriaBuilder(), getAttribute() );
		return new MapKeyHelpers.MapKeyPath( criteriaBuilder(), mapKeySource, mapKeyAttribute );
	}

}
