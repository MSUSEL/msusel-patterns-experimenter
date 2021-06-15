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
package org.hibernate.metamodel.source.binder;

import org.hibernate.metamodel.relational.Datatype;
import org.hibernate.metamodel.relational.Size;

/**
 * Contract for source information pertaining to a column definition.
 *
 * @author Steve Ebersole
 */
public interface ColumnSource extends RelationalValueSource {
	/**
	 * Obtain the name of the column.
	 *
	 * @return The name of the column.  Can be {@code null}, in which case a naming strategy is applied.
	 */
	public String getName();

	/**
	 * A SQL fragment to apply to the column value on read.
	 *
	 * @return The SQL read fragment
	 */
	public String getReadFragment();

	/**
	 * A SQL fragment to apply to the column value on write.
	 *
	 * @return The SQL write fragment
	 */
	public String getWriteFragment();

	/**
	 * Is this column nullable?
	 *
	 * @return {@code true} indicates it is nullable; {@code false} non-nullable.
	 */
	public boolean isNullable();

	/**
	 * Obtain a specified default value for the column
	 *
	 * @return THe column default
	 */
	public String getDefaultValue();

	/**
	 * Obtain the free-hand definition of the column's type.
	 *
	 * @return The free-hand column type
	 */
	public String getSqlType();

	/**
	 * The deduced (and dialect convertible) type for this column
	 *
	 * @return The column's SQL data type.
	 */
	public Datatype getDatatype();

	/**
	 * Obtain the specified column size.
	 *
	 * @return The column size.
	 */
	public Size getSize();

	/**
	 * Is this column unique?
	 *
	 * @return {@code true} indicates it is unique; {@code false} non-unique.
	 */
	public boolean isUnique();

	/**
	 * Obtain the specified check constraint condition
	 *
	 * @return Check constraint condition
	 */
	public String getCheckCondition();

	/**
	 * Obtain the specified SQL comment
	 *
	 * @return SQL comment
	 */
	public String getComment();

	public boolean isIncludedInInsert();

	public boolean isIncludedInUpdate();
}
