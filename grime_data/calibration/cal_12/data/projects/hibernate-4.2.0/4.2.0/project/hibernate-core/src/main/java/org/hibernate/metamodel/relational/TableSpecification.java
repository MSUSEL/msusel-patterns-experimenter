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
package org.hibernate.metamodel.relational;

import org.hibernate.dialect.Dialect;

/**
 * Models what ANSI SQL terms a table specification which is a table or a view or an inline view.
 *
 * @author Steve Ebersole
 */
public interface TableSpecification extends ValueContainer, Loggable {
	/**
	 * Obtain a reference to the schema to which this table specification belongs.
	 *
	 * @return The schema to which this table specification belongs.
	 */
	public Schema getSchema();

	/**
	 * Get the table number.
	 *
	 * @return the table number.
	 */
	public int getTableNumber();

	/**
	 * Get the primary key definition for this table spec.
	 *
	 * @return The PK definition.
	 */
	public PrimaryKey getPrimaryKey();

	/**
	 * Factory method for creating a {@link Column} associated with this container.
	 *
	 * @param name The column name
	 *
	 * @return The generated column
	 */
	public Column locateOrCreateColumn(String name);

	/**
	 * Factory method for creating a {@link Column} associated with this container.
	 *
	 * @param name The column name
	 *
	 * @return The generated column
	 */
	public Tuple createTuple(String name);

	/**
	 * Factory method for creating a {@link DerivedValue} associated with this container.
	 *
	 * @param fragment The value expression
	 *
	 * @return The generated value.
	 */
	public DerivedValue locateOrCreateDerivedValue(String fragment);

	public Iterable<ForeignKey> getForeignKeys();

	public ForeignKey createForeignKey(TableSpecification targetTable, String name);

	public Iterable<Index> getIndexes();

	public Index getOrCreateIndex(String name);

	public Iterable<UniqueKey> getUniqueKeys();

	public UniqueKey getOrCreateUniqueKey(String name);

	public Iterable<CheckConstraint> getCheckConstraints();

	public void addCheckConstraint(String checkCondition);

	public Iterable<String> getComments();

	public void addComment(String comment);

	public String getQualifiedName(Dialect dialect);
}
