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
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.type.Type;

/**
 * @author Gavin King
 */
public class Distinct implements EnhancedProjection {

	private final Projection projection;
	
	public Distinct(Projection proj) {
		this.projection = proj;
	}

	public String toSqlString(Criteria criteria, int position, CriteriaQuery criteriaQuery)
			throws HibernateException {
		return "distinct " + projection.toSqlString(criteria, position, criteriaQuery);
	}

	public String toGroupSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException {
		return projection.toGroupSqlString(criteria, criteriaQuery);
	}

	public Type[] getTypes(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException {
		return projection.getTypes(criteria, criteriaQuery);
	}

	public Type[] getTypes(String alias, Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException {
		return projection.getTypes(alias, criteria, criteriaQuery);
	}

	public String[] getColumnAliases(int loc) {
		return projection.getColumnAliases(loc);
	}

	public String[] getColumnAliases(int loc, Criteria criteria, CriteriaQuery criteriaQuery) {
		return projection instanceof EnhancedProjection ?
				( ( EnhancedProjection ) projection ).getColumnAliases( loc, criteria, criteriaQuery ) :
				getColumnAliases( loc );
	}

	public String[] getColumnAliases(String alias, int loc) {
		return projection.getColumnAliases(alias, loc);
	}

	public String[] getColumnAliases(String alias, int loc, Criteria criteria, CriteriaQuery criteriaQuery) {
		return projection instanceof EnhancedProjection ?
				( ( EnhancedProjection ) projection ).getColumnAliases( alias, loc, criteria, criteriaQuery ) :
				getColumnAliases( alias, loc );
	}

	public String[] getAliases() {
		return projection.getAliases();
	}

	public boolean isGrouped() {
		return projection.isGrouped();
	}

	public String toString() {
		return "distinct " + projection.toString();
	}
}
