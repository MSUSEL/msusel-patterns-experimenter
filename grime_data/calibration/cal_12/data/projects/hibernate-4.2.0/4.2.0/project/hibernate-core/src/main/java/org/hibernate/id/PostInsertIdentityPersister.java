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
package org.hibernate.id;
import org.hibernate.persister.entity.EntityPersister;

/**
 * A persister that may have an identity assigned by execution of 
 * a SQL <tt>INSERT</tt>.
 *
 * @author Gavin King
 */
public interface PostInsertIdentityPersister extends EntityPersister {
	/**
	 * Get a SQL select string that performs a select based on a unique
	 * key determined by the given property name).
	 *
	 * @param propertyName The name of the property which maps to the
	 * column(s) to use in the select statement restriction.
	 * @return The SQL select string
	 */
	public String getSelectByUniqueKeyString(String propertyName);

	/**
	 * Get the database-specific SQL command to retrieve the last
	 * generated IDENTITY value.
	 *
	 * @return The SQL command string
	 */
	public String getIdentitySelectString();

	public String[] getIdentifierColumnNames();

	/**
	 * The names of the primary key columns in the root table.
	 *
	 * @return The primary key column names.
	 */
	public String[] getRootTableKeyColumnNames();
}
