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
package org.hibernate.persister.entity;
import java.util.Map;

import org.hibernate.MappingException;

/**
 * Anything that can be loaded by outer join - namely
 * persisters for classes or collections.
 *
 * @author Gavin King
 */
public interface Joinable {
	//should this interface extend PropertyMapping?

	/**
	 * An identifying name; a class name or collection role name.
	 */
	public String getName();
	/**
	 * The table to join to.
	 */
	public String getTableName();

	/**
	 * All columns to select, when loading.
	 */
	public String selectFragment(Joinable rhs, String rhsAlias, String lhsAlias, String currentEntitySuffix, String currentCollectionSuffix, boolean includeCollectionColumns);

	/**
	 * Get the where clause part of any joins
	 * (optional operation)
	 */
	public String whereJoinFragment(String alias, boolean innerJoin, boolean includeSubclasses);
	/**
	 * Get the from clause part of any joins
	 * (optional operation)
	 */
	public String fromJoinFragment(String alias, boolean innerJoin, boolean includeSubclasses);
	/**
	 * The columns to join on
	 */
	public String[] getKeyColumnNames();
	/**
	 * Get the where clause filter, given a query alias and considering enabled session filters
	 */
	public String filterFragment(String alias, Map enabledFilters) throws MappingException;

	public String oneToManyFilterFragment(String alias) throws MappingException;
	/**
	 * Is this instance actually a CollectionPersister?
	 */
	public boolean isCollection();

	/**
	 * Very, very, very ugly...
	 *
	 * @return Does this persister "consume" entity column aliases in the result
	 * set?
	 */
	public boolean consumesEntityAlias();

	/**
	 * Very, very, very ugly...
	 *
	 * @return Does this persister "consume" collection column aliases in the result
	 * set?
	 */
	public boolean consumesCollectionAlias();
}
