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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.AssertionFailure;
import org.hibernate.metamodel.domain.SingularAttribute;
import org.hibernate.metamodel.relational.SimpleValue;
import org.hibernate.metamodel.relational.Tuple;
import org.hibernate.metamodel.relational.Value;

/**
 * @author Steve Ebersole
 */
public abstract class AbstractSingularAttributeBinding
		extends AbstractAttributeBinding
		implements SingularAttributeBinding {

	private Value value;
	private List<SimpleValueBinding> simpleValueBindings = new ArrayList<SimpleValueBinding>();

	private boolean hasDerivedValue;
	private boolean isNullable = true;

	protected AbstractSingularAttributeBinding(AttributeBindingContainer container, SingularAttribute attribute) {
		super( container, attribute );
	}

	@Override
	public SingularAttribute getAttribute() {
		return (SingularAttribute) super.getAttribute();
	}

	public Value getValue() {
		return value;
	}

	public void setSimpleValueBindings(Iterable<SimpleValueBinding> simpleValueBindings) {
		List<SimpleValue> values = new ArrayList<SimpleValue>();
		for ( SimpleValueBinding simpleValueBinding : simpleValueBindings ) {
			this.simpleValueBindings.add( simpleValueBinding );
			values.add( simpleValueBinding.getSimpleValue() );
			this.hasDerivedValue = this.hasDerivedValue || simpleValueBinding.isDerived();
			this.isNullable = this.isNullable && simpleValueBinding.isNullable();
		}
		if ( values.size() == 1 ) {
			this.value = values.get( 0 );
		}
		else {
			final Tuple tuple = values.get( 0 ).getTable().createTuple( getRole() );
			for ( SimpleValue value : values ) {
				tuple.addValue( value );
			}
			this.value = tuple;
		}
	}

	private String getRole() {
		return getContainer().getPathBase() + '.' + getAttribute().getName();
	}

	@Override
	public int getSimpleValueSpan() {
		checkValueBinding();
		return simpleValueBindings.size();
	}

	protected void checkValueBinding() {
		if ( value == null ) {
			throw new AssertionFailure( "No values yet bound!" );
		}
	}

	@Override
	public Iterable<SimpleValueBinding> getSimpleValueBindings() {
		return simpleValueBindings;
	}

	@Override
	public boolean hasDerivedValue() {
		checkValueBinding();
		return hasDerivedValue;
	}

	@Override
	public boolean isNullable() {
		checkValueBinding();
		return isNullable;
	}
}
