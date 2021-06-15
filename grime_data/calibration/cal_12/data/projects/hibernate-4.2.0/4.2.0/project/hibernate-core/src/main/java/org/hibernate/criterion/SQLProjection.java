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
import org.hibernate.internal.util.StringHelper;
import org.hibernate.type.Type;

/**
 * A SQL fragment. The string {alias} will be replaced by the
 * alias of the root entity.
 */
public class SQLProjection implements Projection {

	private final String sql;
	private final String groupBy;
	private final Type[] types;
	private String[] aliases;
	private String[] columnAliases;
	private boolean grouped;

	public String toSqlString(
			Criteria criteria, 
			int loc, 
			CriteriaQuery criteriaQuery)
	throws HibernateException {
		return StringHelper.replace( sql, "{alias}", criteriaQuery.getSQLAlias(criteria) );
	}

	public String toGroupSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
	throws HibernateException {
		return StringHelper.replace( groupBy, "{alias}", criteriaQuery.getSQLAlias( criteria ) );
	}

	public Type[] getTypes(Criteria crit, CriteriaQuery criteriaQuery)
	throws HibernateException {
		return types;
	}

	public String toString() {
		return sql;
	}

	protected SQLProjection(String sql, String[] columnAliases, Type[] types) {
		this(sql, null, columnAliases, types);
	}
	
	protected SQLProjection(String sql, String groupBy, String[] columnAliases, Type[] types) {
		this.sql = sql;
		this.types = types;
		this.aliases = columnAliases;
		this.columnAliases = columnAliases;
		this.grouped = groupBy!=null;
		this.groupBy = groupBy;
	}

	public String[] getAliases() {
		return aliases;
	}
	
	public String[] getColumnAliases(int loc) {
		return columnAliases;
	}
	
	public boolean isGrouped() {
		return grouped;
	}

	public Type[] getTypes(String alias, Criteria crit, CriteriaQuery criteriaQuery) {
		return null; //unsupported
	}

	public String[] getColumnAliases(String alias, int loc) {
		return null; //unsupported
	}
}
