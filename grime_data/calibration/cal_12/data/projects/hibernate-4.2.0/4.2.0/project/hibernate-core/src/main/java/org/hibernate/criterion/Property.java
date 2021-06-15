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
package org.hibernate.criterion;
import java.util.Collection;

/**
 * A factory for property-specific criterion and projection instances
 * @author Gavin King
 */
public class Property extends PropertyProjection {
	//private String propertyName;
	protected Property(String propertyName) {
		super(propertyName);
	}

	public Criterion between(Object min, Object max) {
		return Restrictions.between(getPropertyName(), min, max);
	}

	public Criterion in(Collection values) {
		return Restrictions.in(getPropertyName(), values);
	}

	public Criterion in(Object[] values) {
		return Restrictions.in(getPropertyName(), values);
	}

	public SimpleExpression like(Object value) {
		return Restrictions.like(getPropertyName(), value);
	}

	public SimpleExpression like(String value, MatchMode matchMode) {
		return Restrictions.like(getPropertyName(), value, matchMode);
	}

	public SimpleExpression eq(Object value) {
		return Restrictions.eq(getPropertyName(), value);
	}

	public Criterion eqOrIsNull(Object value) {
		return Restrictions.eqOrIsNull(getPropertyName(), value);
	}

	public SimpleExpression ne(Object value) {
		return Restrictions.ne(getPropertyName(), value);
	}

	public Criterion neOrIsNotNull(Object value) {
		return Restrictions.neOrIsNotNull(getPropertyName(), value);
	}

	public SimpleExpression gt(Object value) {
		return Restrictions.gt(getPropertyName(), value);
	}

	public SimpleExpression lt(Object value) {
		return Restrictions.lt(getPropertyName(), value);
	}

	public SimpleExpression le(Object value) {
		return Restrictions.le(getPropertyName(), value);
	}

	public SimpleExpression ge(Object value) {
		return Restrictions.ge(getPropertyName(), value);
	}

	public PropertyExpression eqProperty(Property other) {
		return Restrictions.eqProperty( getPropertyName(), other.getPropertyName() );
	}

	public PropertyExpression neProperty(Property other) {
		return Restrictions.neProperty( getPropertyName(), other.getPropertyName() );
	}
	
	public PropertyExpression leProperty(Property other) {
		return Restrictions.leProperty( getPropertyName(), other.getPropertyName() );
	}

	public PropertyExpression geProperty(Property other) {
		return Restrictions.geProperty( getPropertyName(), other.getPropertyName() );
	}
	
	public PropertyExpression ltProperty(Property other) {
		return Restrictions.ltProperty( getPropertyName(), other.getPropertyName() );
	}

	public PropertyExpression gtProperty(Property other) {
		return Restrictions.gtProperty( getPropertyName(), other.getPropertyName() );
	}
	
	public PropertyExpression eqProperty(String other) {
		return Restrictions.eqProperty( getPropertyName(), other );
	}

	public PropertyExpression neProperty(String other) {
		return Restrictions.neProperty( getPropertyName(), other );
	}
	
	public PropertyExpression leProperty(String other) {
		return Restrictions.leProperty( getPropertyName(), other );
	}

	public PropertyExpression geProperty(String other) {
		return Restrictions.geProperty( getPropertyName(), other );
	}
	
	public PropertyExpression ltProperty(String other) {
		return Restrictions.ltProperty( getPropertyName(), other );
	}

	public PropertyExpression gtProperty(String other) {
		return Restrictions.gtProperty( getPropertyName(), other );
	}
	
	public Criterion isNull() {
		return Restrictions.isNull(getPropertyName());
	}

	public Criterion isNotNull() {
		return Restrictions.isNotNull(getPropertyName());
	}

	public Criterion isEmpty() {
		return Restrictions.isEmpty(getPropertyName());
	}

	public Criterion isNotEmpty() {
		return Restrictions.isNotEmpty(getPropertyName());
	}
	
	public CountProjection count() {
		return Projections.count(getPropertyName());
	}
	
	public AggregateProjection max() {
		return Projections.max(getPropertyName());
	}

	public AggregateProjection min() {
		return Projections.min(getPropertyName());
	}

	public AggregateProjection avg() {
		return Projections.avg(getPropertyName());
	}
	
	/*public PropertyProjection project() {
		return Projections.property(getPropertyName());
	}*/

	public PropertyProjection group() {
		return Projections.groupProperty(getPropertyName());
	}
	
	public Order asc() {
		return Order.asc(getPropertyName());
	}

	public Order desc() {
		return Order.desc(getPropertyName());
	}

	public static Property forName(String propertyName) {
		return new Property(propertyName);
	}
	
	/**
	 * Get a component attribute of this property
	 */
	public Property getProperty(String propertyName) {
		return forName( getPropertyName() + '.' + propertyName );
	}
	
	public Criterion eq(DetachedCriteria subselect) {
		return Subqueries.propertyEq( getPropertyName(), subselect );
	}

	public Criterion ne(DetachedCriteria subselect) {
		return Subqueries.propertyNe( getPropertyName(), subselect );
	}

	public Criterion lt(DetachedCriteria subselect) {
		return Subqueries.propertyLt( getPropertyName(), subselect );
	}

	public Criterion le(DetachedCriteria subselect) {
		return Subqueries.propertyLe( getPropertyName(), subselect );
	}

	public Criterion gt(DetachedCriteria subselect) {
		return Subqueries.propertyGt( getPropertyName(), subselect );
	}

	public Criterion ge(DetachedCriteria subselect) {
		return Subqueries.propertyGe( getPropertyName(), subselect );
	}

	public Criterion notIn(DetachedCriteria subselect) {
		return Subqueries.propertyNotIn( getPropertyName(), subselect );
	}

	public Criterion in(DetachedCriteria subselect) {
		return Subqueries.propertyIn( getPropertyName(), subselect );
	}

	public Criterion eqAll(DetachedCriteria subselect) {
		return Subqueries.propertyEqAll( getPropertyName(), subselect );
	}

	public Criterion gtAll(DetachedCriteria subselect) {
		return Subqueries.propertyGtAll( getPropertyName(), subselect );
	}

	public Criterion ltAll(DetachedCriteria subselect) {
		return Subqueries.propertyLtAll( getPropertyName(), subselect );
	}

	public Criterion leAll(DetachedCriteria subselect) {
		return Subqueries.propertyLeAll( getPropertyName(), subselect );
	}

	public Criterion geAll(DetachedCriteria subselect) {
		return Subqueries.propertyGeAll( getPropertyName(), subselect );
	}

	public Criterion gtSome(DetachedCriteria subselect) {
		return Subqueries.propertyGtSome( getPropertyName(), subselect );
	}

	public Criterion ltSome(DetachedCriteria subselect) {
		return Subqueries.propertyLtSome( getPropertyName(), subselect );
	}

	public Criterion leSome(DetachedCriteria subselect) {
		return Subqueries.propertyLeSome( getPropertyName(), subselect );
	}

	public Criterion geSome(DetachedCriteria subselect) {
		return Subqueries.propertyGeSome( getPropertyName(), subselect );
	}

}
