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

/**
 * An "enhanced" Projection for a {@link Criteria} query.
 *
 * @author Gail Badner
 * @see Projection
 * @see Criteria
 */
public interface EnhancedProjection extends Projection {

	/**
	 * Get the SQL column aliases used by this projection for the columns it writes for inclusion into the
	 * <tt>SELECT</tt> clause ({@link #toSqlString}.  Hibernate always uses column aliases to extract data from the
	 * JDBC {@link java.sql.ResultSet}, so it is important that these be implemented correctly in order for
	 * Hibernate to be able to extract these val;ues correctly.
	 *
	 * @param position Just as in {@link #toSqlString}, represents the number of <b>columns</b> rendered
	 * prior to this projection.
	 * @param criteria The local criteria to which this project is attached (for resolution).
	 * @param criteriaQuery The overall criteria query instance.
	 * @return The columns aliases.
	 */
	public String[] getColumnAliases(int position, Criteria criteria, CriteriaQuery criteriaQuery);

	/**
	 * Get the SQL column aliases used by this projection for the columns it writes for inclusion into the
	 * <tt>SELECT</tt> clause ({@link #toSqlString} <i>for a particular criteria-level alias</i>.
	 *
	 * @param alias The criteria-level alias
	 * @param position Just as in {@link #toSqlString}, represents the number of <b>columns</b> rendered
	 * prior to this projection.
	 * @param criteria The local criteria to which this project is attached (for resolution).
	 * @param criteriaQuery The overall criteria query instance.
	 * @return The columns aliases pertaining to a particular criteria-level alias; expected to return null if
	 * this projection does not understand this alias.
	 */
	public String[] getColumnAliases(String alias, int position, Criteria criteria, CriteriaQuery criteriaQuery);
}
