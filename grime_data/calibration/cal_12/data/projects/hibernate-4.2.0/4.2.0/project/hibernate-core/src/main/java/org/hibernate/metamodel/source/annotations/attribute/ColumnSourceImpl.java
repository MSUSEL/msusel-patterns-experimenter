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
package org.hibernate.metamodel.source.annotations.attribute;

import org.hibernate.internal.util.StringHelper;

/**
 * @author Hardy Ferentschik
 */
public class ColumnSourceImpl extends ColumnValuesSourceImpl {
	private final MappedAttribute attribute;
	private final String name;

	ColumnSourceImpl(MappedAttribute attribute, AttributeOverride attributeOverride) {
		super( attribute.getColumnValues() );
		if ( attributeOverride != null ) {
			setOverrideColumnValues( attributeOverride.getColumnValues() );
		}
		this.attribute = attribute;
		this.name = resolveColumnName();
	}

	protected String resolveColumnName() {
		if ( StringHelper.isEmpty( super.getName() ) ) {
			//no @Column defined.
			return attribute.getContext().getNamingStrategy().propertyToColumnName( attribute.getName() );
		}
		else {
			return super.getName();
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getReadFragment() {
		if ( attribute instanceof BasicAttribute ) {
			return ( (BasicAttribute) attribute ).getCustomReadFragment();
		}
		else {
			return null;
		}
	}

	@Override
	public String getWriteFragment() {
		if ( attribute instanceof BasicAttribute ) {
			return ( (BasicAttribute) attribute ).getCustomWriteFragment();
		}
		else {
			return null;
		}
	}

	@Override
	public String getCheckCondition() {
		if ( attribute instanceof BasicAttribute ) {
			return ( (BasicAttribute) attribute ).getCheckCondition();
		}
		else {
			return null;
		}
	}
}


