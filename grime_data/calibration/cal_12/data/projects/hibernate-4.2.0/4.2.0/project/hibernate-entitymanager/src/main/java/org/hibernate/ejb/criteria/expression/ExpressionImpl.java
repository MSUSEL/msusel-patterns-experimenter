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
package org.hibernate.ejb.criteria.expression;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.ExpressionImplementor;
import org.hibernate.ejb.criteria.expression.function.CastFunction;

/**
 * Models an expression in the criteria query language.
 *
 * @author Steve Ebersole
 */
public abstract class ExpressionImpl<T>
		extends SelectionImpl<T>
		implements ExpressionImplementor<T>, Serializable {
	public ExpressionImpl(CriteriaBuilderImpl criteriaBuilder, Class<T> javaType) {
		super( criteriaBuilder, javaType );
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public <X> Expression<X> as(Class<X> type) {
		return type.equals( getJavaType() )
				? (Expression<X>) this
				: new CastFunction<X, T>( criteriaBuilder(), type, this );
	}

	/**
	 * {@inheritDoc}
	 */
	public Predicate isNull() {
		return criteriaBuilder().isNull( this );
	}

	/**
	 * {@inheritDoc}
	 */
	public Predicate isNotNull() {
		return criteriaBuilder().isNotNull( this );
	}

	/**
	 * {@inheritDoc}
	 */
    public Predicate in(Object... values) {
		return criteriaBuilder().in( this, values );
	}

	/**
	 * {@inheritDoc}
	 */
	public Predicate in(Expression<?>... values) {
		return criteriaBuilder().in( this, values );
	}

	/**
	 * {@inheritDoc}
	 */
	public Predicate in(Collection<?> values) {
		return criteriaBuilder().in( this, values.toArray() );
	}

	/**
	 * {@inheritDoc}
	 */
	public Predicate in(Expression<Collection<?>> values) {
		return criteriaBuilder().in( this, values );
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public ExpressionImplementor<Long> asLong() {
		resetJavaType( Long.class );
		return (ExpressionImplementor<Long>) this;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public ExpressionImplementor<Integer> asInteger() {
		resetJavaType( Integer.class );
		return (ExpressionImplementor<Integer>) this;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public ExpressionImplementor<Float> asFloat() {
		resetJavaType( Float.class );
		return (ExpressionImplementor<Float>) this;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public ExpressionImplementor<Double> asDouble() {
		resetJavaType( Double.class );
		return (ExpressionImplementor<Double>) this;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public ExpressionImplementor<BigDecimal> asBigDecimal() {
		resetJavaType( BigDecimal.class );
		return (ExpressionImplementor<BigDecimal>) this;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public ExpressionImplementor<BigInteger> asBigInteger() {
		resetJavaType( BigInteger.class );
		return (ExpressionImplementor<BigInteger>) this;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public ExpressionImplementor<String> asString() {
		resetJavaType( String.class );
		return (ExpressionImplementor<String>) this;
	}
}
