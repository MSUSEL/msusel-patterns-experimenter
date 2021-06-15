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
package org.hibernate.loader.custom;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Extension point allowing any SQL query with named and positional parameters
 * to be executed by Hibernate, returning managed entities, collections and
 * simple scalar values.
 * 
 * @author Gavin King
 * @author Steve Ebersole
 */
public interface CustomQuery {
	/**
	 * The SQL query string to be performed.
	 *
	 * @return The SQL statement string.
	 */
	public String getSQL();

	/**
	 * Any query spaces to apply to the query execution.  Query spaces are
	 * used in Hibernate's auto-flushing mechanism to determine which
	 * entities need to be checked for pending changes.
	 *
	 * @return The query spaces
	 */
	public Set getQuerySpaces();

	/**
	 * A map representing positions within the supplied {@link #getSQL query} to
	 * which we need to bind named parameters.
	 * <p/>
	 * Optional, may return null if no named parameters.
	 * <p/>
	 * The structure of the returned map (if one) as follows:<ol>
	 * <li>The keys into the map are the named parameter names</li>
	 * <li>The corresponding value is either an {@link Integer} if the
	 * parameter occurs only once in the query; or a List of Integers if the
	 * parameter occurs more than once</li>
	 * </ol>
	 */
	public Map getNamedParameterBindPoints();

	/**
	 * A collection of {@link Return descriptors} describing the
	 * JDBC result set to be expected and how to map this result set.
	 *
	 * @return List of return descriptors.
	 */
	public List getCustomQueryReturns();
}
