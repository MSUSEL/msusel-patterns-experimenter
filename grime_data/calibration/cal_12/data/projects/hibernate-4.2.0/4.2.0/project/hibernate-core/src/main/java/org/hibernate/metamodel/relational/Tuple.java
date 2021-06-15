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

import java.util.LinkedHashSet;

/**
 * Models a compound value (a tuple or row-value-constructor is SQL terms).  It is both a {@link Value} and
 * a {@link ValueContainer} simultaneously.
 * <p/>
 * IMPL NOTE : in terms of the tables themselves, SQL has no notion of a tuple/compound-value.  We simply model
 * it this way because:
 * <ul>
 * <li>it is a cleaner mapping to the logical model</li>
 * <li>it allows more meaningful traversals from simple values back up to table through any intermediate tuples
 * because it gives us a better understanding of the model.</li>
 * <li>it better conveys intent</li>
 * <li>it adds richness to the model</li>
 * </ul>
 *
 * @author Steve Ebersole
 */
public class Tuple implements Value, ValueContainer, Loggable {
	private final TableSpecification table;
	private final String name;
	private final LinkedHashSet<SimpleValue> values = new LinkedHashSet<SimpleValue>();

	public Tuple(TableSpecification table, String name) {
		this.table = table;
		this.name = name;
	}

	@Override
	public TableSpecification getTable() {
		return table;
	}

	public int valuesSpan() {
		return values.size();
	}

	@Override
	public Iterable<SimpleValue> values() {
		return values;
	}

	public void addValue(SimpleValue value) {
		if ( ! value.getTable().equals( getTable() ) ) {
			throw new IllegalArgumentException( "Tuple can only group values from same table" );
		}
		values.add( value );
	}

	@Override
	public String getLoggableValueQualifier() {
		return getTable().getLoggableValueQualifier() + '.' + name + "{tuple}";
	}

	@Override
	public String toLoggableString() {
		return getLoggableValueQualifier();
	}

	@Override
	public void validateJdbcTypes(JdbcCodes typeCodes) {
		for ( Value value : values() ) {
			value.validateJdbcTypes( typeCodes );
		}
	}
}
