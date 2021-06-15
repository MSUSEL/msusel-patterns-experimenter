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
package org.hibernate.criterion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;

/**
 * A count
 * @author Gavin King
 */
public class CountProjection extends AggregateProjection {
	private boolean distinct;

	protected CountProjection(String prop) {
		super("count", prop);
	}

	public String toString() {
		if ( distinct ) {
			return "distinct " + super.toString();
		}
		else {
			return super.toString();
		}
	}

	protected List buildFunctionParameterList(Criteria criteria, CriteriaQuery criteriaQuery) {
		String cols[] = criteriaQuery.getColumns( propertyName, criteria );
		return ( distinct ? buildCountDistinctParameterList( cols ) : Arrays.asList( cols ) );
	}

	private List buildCountDistinctParameterList(String[] cols) {
		List params = new ArrayList( cols.length + 1 );
		params.add( "distinct" );
		params.addAll( Arrays.asList( cols ) );
		return params;
	}

	public CountProjection setDistinct() {
		distinct = true;
		return this;
	}
}
