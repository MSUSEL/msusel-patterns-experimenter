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

import java.util.Collections;

import org.hibernate.dialect.Dialect;

/**
 * A <tt>data container</tt> defined by a <tt>SELECT</tt> statement.  This translates into an inline view in the
 * SQL statements: <code>select ... from (select ... from logical_table_table ...) ...</code>
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class InLineView extends AbstractTableSpecification {
	private final Schema schema;
	private final String logicalName;
	private final String select;

	public InLineView(Schema schema, String logicalName, String select) {
		this.schema = schema;
		this.logicalName = logicalName;
		this.select = select;
	}

	public Schema getSchema() {
		return schema;
	}

	public String getSelect() {
		return select;
	}

	@Override
	public String getLoggableValueQualifier() {
		return logicalName;
	}

	@Override
	public Iterable<Index> getIndexes() {
		return Collections.emptyList();
	}

	@Override
	public Index getOrCreateIndex(String name) {
		throw new UnsupportedOperationException( "Cannot create index on inline view" );
	}

	@Override
	public Iterable<UniqueKey> getUniqueKeys() {
		return Collections.emptyList();
	}

	@Override
	public UniqueKey getOrCreateUniqueKey(String name) {
		throw new UnsupportedOperationException( "Cannot create unique-key on inline view" );
	}

	@Override
	public Iterable<CheckConstraint> getCheckConstraints() {
		return Collections.emptyList();
	}

	@Override
	public void addCheckConstraint(String checkCondition) {
		throw new UnsupportedOperationException( "Cannot create check constraint on inline view" );
	}

	@Override
	public Iterable<String> getComments() {
		return Collections.emptyList();
	}

	@Override
	public void addComment(String comment) {
		throw new UnsupportedOperationException( "Cannot comment on inline view" );
	}

	@Override
	public String getQualifiedName(Dialect dialect) {
		return new StringBuilder( select.length() + 4 )
				.append( "( " )
				.append( select )
				.append( " )" )
				.toString();
	}

	@Override
	public String toLoggableString() {
		return "{inline-view}";
	}
}
