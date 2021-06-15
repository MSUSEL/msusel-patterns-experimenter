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

import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.type.Type;

/**
 * @deprecated Use <tt>Restrictions</tt>.
 * @see Restrictions
 * @author Gavin King
 */
@Deprecated
public final class Expression extends Restrictions {

	private Expression() {
		//cannot be instantiated
	}

	/**
	 * Apply a constraint expressed in SQL, with the given JDBC
	 * parameters. Any occurrences of <tt>{alias}</tt> will be
	 * replaced by the table alias.
	 *
	 * @deprecated use {@link org.hibernate.criterion.Restrictions#sqlRestriction(String, Object[], Type[])}
	 * @param sql
	 * @param values
	 * @param types
	 * @return Criterion
	 */
	@Deprecated
    public static Criterion sql(String sql, Object[] values, Type[] types) {
		return new SQLCriterion(sql, values, types);
	}
	/**
	 * Apply a constraint expressed in SQL, with the given JDBC
	 * parameter. Any occurrences of <tt>{alias}</tt> will be replaced
	 * by the table alias.
	 *
	 * @deprecated use {@link org.hibernate.criterion.Restrictions#sqlRestriction(String, Object, Type)}
	 * @param sql
	 * @param value
	 * @param type
	 * @return Criterion
	 */
	@Deprecated
    public static Criterion sql(String sql, Object value, Type type) {
		return new SQLCriterion(sql, new Object[] { value }, new Type[] { type } );
	}
	/**
	 * Apply a constraint expressed in SQL. Any occurrences of <tt>{alias}</tt>
	 * will be replaced by the table alias.
	 *
	 * @deprecated use {@link org.hibernate.criterion.Restrictions#sqlRestriction(String)}
	 * @param sql
	 * @return Criterion
	 */
	@Deprecated
    public static Criterion sql(String sql) {
		return new SQLCriterion(sql, ArrayHelper.EMPTY_OBJECT_ARRAY, ArrayHelper.EMPTY_TYPE_ARRAY);
	}

}
