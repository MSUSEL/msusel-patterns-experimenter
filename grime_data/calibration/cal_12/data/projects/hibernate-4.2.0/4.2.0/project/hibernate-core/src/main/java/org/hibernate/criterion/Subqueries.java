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

/**
 * Factory class for criterion instances that represent expressions
 * involving subqueries.
 * 
 * @see Restrictions
 * @see Projection
 * @see org.hibernate.Criteria
 *
 * @author Gavin King
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@SuppressWarnings( {"UnusedDeclaration"})
public class Subqueries {
		
	public static Criterion exists(DetachedCriteria dc) {
		return new ExistsSubqueryExpression("exists", dc);
	}
	
	public static Criterion notExists(DetachedCriteria dc) {
		return new ExistsSubqueryExpression("not exists", dc);
	}
	
	public static Criterion propertyEqAll(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, "=", "all", dc);
	}
	
	public static Criterion propertyIn(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, "in", null, dc);
	}
	
	public static Criterion propertyNotIn(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, "not in", null, dc);
	}
	
	public static Criterion propertyEq(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, "=", null, dc);
	}

	public static Criterion propertiesEq(String[] propertyNames, DetachedCriteria dc) {
		return new PropertiesSubqueryExpression(propertyNames, "=", dc);
	}

	public static Criterion propertiesNotEq(String[] propertyNames, DetachedCriteria dc) {
		return new PropertiesSubqueryExpression(propertyNames, "<>", dc);
	}

	public static Criterion propertiesIn(String[] propertyNames, DetachedCriteria dc) {
		return new PropertiesSubqueryExpression(propertyNames, "in", dc);
	}

	public static Criterion propertiesNotIn(String[] propertyNames, DetachedCriteria dc) {
		return new PropertiesSubqueryExpression(propertyNames, "not in", dc);
	}
	
	public static Criterion propertyNe(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, "<>", null, dc);
	}
	
	public static Criterion propertyGt(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, ">", null, dc);
	}
	
	public static Criterion propertyLt(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, "<", null, dc);
	}
	
	public static Criterion propertyGe(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, ">=", null, dc);
	}
	
	public static Criterion propertyLe(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, "<=", null, dc);
	}
	
	public static Criterion propertyGtAll(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, ">", "all", dc);
	}
	
	public static Criterion propertyLtAll(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, "<", "all", dc);
	}
	
	public static Criterion propertyGeAll(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, ">=", "all", dc);
	}
	
	public static Criterion propertyLeAll(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, "<=", "all", dc);
	}
	
	public static Criterion propertyGtSome(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, ">", "some", dc);
	}
	
	public static Criterion propertyLtSome(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, "<", "some", dc);
	}
	
	public static Criterion propertyGeSome(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, ">=", "some", dc);
	}
	
	public static Criterion propertyLeSome(String propertyName, DetachedCriteria dc) {
		return new PropertySubqueryExpression(propertyName, "<=", "some", dc);
	}
	
	public static Criterion eqAll(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, "=", "all", dc);
	}
	
	public static Criterion in(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, "in", null, dc);
	}
	
	public static Criterion notIn(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, "not in", null, dc);
	}
	
	public static Criterion eq(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, "=", null, dc);
	}
	
	public static Criterion gt(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, ">", null, dc);
	}
	
	public static Criterion lt(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, "<", null, dc);
	}
	
	public static Criterion ge(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, ">=", null, dc);
	}
	
	public static Criterion le(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, "<=", null, dc);
	}
	
	public static Criterion ne(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, "<>", null, dc);
	}
	
	public static Criterion gtAll(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, ">", "all", dc);
	}
	
	public static Criterion ltAll(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, "<", "all", dc);
	}
	
	public static Criterion geAll(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, ">=", "all", dc);
	}
	
	public static Criterion leAll(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, "<=", "all", dc);
	}
	
	public static Criterion gtSome(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, ">", "some", dc);
	}
	
	public static Criterion ltSome(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, "<", "some", dc);
	}
	
	public static Criterion geSome(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, ">=", "some", dc);
	}
	
	public static Criterion leSome(Object value, DetachedCriteria dc) {
		return new SimpleSubqueryExpression(value, "<=", "some", dc);
	}
	

}
