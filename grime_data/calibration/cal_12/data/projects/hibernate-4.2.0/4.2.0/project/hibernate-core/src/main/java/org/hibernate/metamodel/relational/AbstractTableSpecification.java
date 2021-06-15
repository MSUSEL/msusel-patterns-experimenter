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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Convenience base class for implementing the {@link ValueContainer} contract centralizing commonality
 * between modeling tables, views and inline views.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractTableSpecification implements TableSpecification {
	private final static AtomicInteger tableCounter = new AtomicInteger( 0 );
	private final int tableNumber;

	private final LinkedHashMap<String, SimpleValue> values = new LinkedHashMap<String, SimpleValue>();

	private final PrimaryKey primaryKey = new PrimaryKey( this );
	private final List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();

	public AbstractTableSpecification() {
		this.tableNumber = tableCounter.getAndIncrement();
	}

	@Override
	public int getTableNumber() {
		return tableNumber;
	}

	@Override
	public Iterable<SimpleValue> values() {
		return values.values();
	}

	@Override
	public Column locateOrCreateColumn(String name) {
		if(values.containsKey( name )){
			return (Column) values.get( name );
		}
		final Column column = new Column( this, values.size(), name );
		values.put( name, column );
		return column;
	}

	@Override
	public DerivedValue locateOrCreateDerivedValue(String fragment) {
		if(values.containsKey( fragment )){
			return (DerivedValue) values.get( fragment );
		}
		final DerivedValue value = new DerivedValue( this, values.size(), fragment );
		values.put( fragment, value );
		return value;
	}

	@Override
	public Tuple createTuple(String name) {
		return new Tuple( this, name );
	}

	@Override
	public Iterable<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}

	@Override
	public ForeignKey createForeignKey(TableSpecification targetTable, String name) {
		ForeignKey fk = new ForeignKey( this, targetTable, name );
		foreignKeys.add( fk );
		return fk;
	}

	@Override
	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}
}
