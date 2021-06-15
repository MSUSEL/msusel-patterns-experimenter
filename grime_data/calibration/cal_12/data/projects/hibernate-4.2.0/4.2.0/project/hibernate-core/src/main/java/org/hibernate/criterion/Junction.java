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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.internal.util.StringHelper;

/**
 * A sequence of a logical expressions combined by some
 * associative logical operator
 *
 * @author Gavin King
 */
public class Junction implements Criterion {
	private final Nature nature;
	private final List<Criterion> conditions = new ArrayList<Criterion>();

	protected Junction(Nature nature) {
		this.nature = nature;
	}
	
	public Junction add(Criterion criterion) {
		conditions.add( criterion );
		return this;
	}

	public Nature getNature() {
		return nature;
	}

	public Iterable<Criterion> conditions() {
		return conditions;
	}

	@Override
	public TypedValue[] getTypedValues(Criteria crit, CriteriaQuery criteriaQuery) throws HibernateException {
		ArrayList<TypedValue> typedValues = new ArrayList<TypedValue>();
		for ( Criterion condition : conditions ) {
			TypedValue[] subValues = condition.getTypedValues( crit, criteriaQuery );
			Collections.addAll( typedValues, subValues );
		}
		return typedValues.toArray( new TypedValue[ typedValues.size() ] );
	}

	@Override
	public String toSqlString(Criteria crit, CriteriaQuery criteriaQuery) throws HibernateException {
		if ( conditions.size()==0 ) {
			return "1=1";
		}

		StringBuilder buffer = new StringBuilder().append( '(' );
		Iterator itr = conditions.iterator();
		while ( itr.hasNext() ) {
			buffer.append( ( (Criterion) itr.next() ).toSqlString( crit, criteriaQuery ) );
			if ( itr.hasNext() ) {
				buffer.append(' ').append( nature.getOperator() ).append(' ');
			}
		}
		return buffer.append(')').toString();
	}

	@Override
	public String toString() {
		return '(' + StringHelper.join( ' ' + nature.getOperator() + ' ', conditions.iterator() ) + ')';
	}

	public static enum Nature {
		AND,
		OR
		;

		public String getOperator() {
			return name().toLowerCase();
		}
	}
}
