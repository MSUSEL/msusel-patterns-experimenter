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
package org.hibernate.metamodel.binding;

import org.hibernate.metamodel.relational.Column;
import org.hibernate.metamodel.relational.DerivedValue;
import org.hibernate.metamodel.relational.SimpleValue;

/**
 * @author Steve Ebersole
 */
public class SimpleValueBinding {
	private SimpleValue simpleValue;
	private boolean includeInInsert;
	private boolean includeInUpdate;

	public SimpleValueBinding() {
		this( true, true );
	}

	public SimpleValueBinding(SimpleValue simpleValue) {
		this();
		setSimpleValue( simpleValue );
	}

	public SimpleValueBinding(SimpleValue simpleValue, boolean includeInInsert, boolean includeInUpdate) {
		this( includeInInsert, includeInUpdate );
		setSimpleValue( simpleValue );
	}

	public SimpleValueBinding(boolean includeInInsert, boolean includeInUpdate) {
		this.includeInInsert = includeInInsert;
		this.includeInUpdate = includeInUpdate;
	}

	public SimpleValue getSimpleValue() {
		return simpleValue;
	}

	public void setSimpleValue(SimpleValue simpleValue) {
		this.simpleValue = simpleValue;
		if ( DerivedValue.class.isInstance( simpleValue ) ) {
			includeInInsert = false;
			includeInUpdate = false;
		}
	}

	public boolean isDerived() {
		return DerivedValue.class.isInstance( simpleValue );
	}

	public boolean isNullable() {
		return isDerived() || Column.class.cast( simpleValue ).isNullable();
	}

	/**
	 * Is the value to be inserted as part of its binding here?
	 * <p/>
	 * <b>NOTE</b> that a column may be bound to multiple attributes.  The purpose of this value is to track this
	 * notion of "insertability" for this particular binding.
	 *
	 * @return {@code true} indicates the value should be included; {@code false} indicates it should not
	 */
	public boolean isIncludeInInsert() {
		return includeInInsert;
	}

	public void setIncludeInInsert(boolean includeInInsert) {
		this.includeInInsert = includeInInsert;
	}

	public boolean isIncludeInUpdate() {
		return includeInUpdate;
	}

	public void setIncludeInUpdate(boolean includeInUpdate) {
		this.includeInUpdate = includeInUpdate;
	}
}
