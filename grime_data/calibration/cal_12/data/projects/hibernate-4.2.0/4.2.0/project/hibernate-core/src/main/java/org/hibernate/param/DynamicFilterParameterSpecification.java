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
package org.hibernate.param;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.Type;

/**
 * A specialized ParameterSpecification impl for dealing with a dynamic filter parameters.
 * 
 * @see org.hibernate.Session#enableFilter(String)
 *
 * @author Steve Ebersole
 */
public class DynamicFilterParameterSpecification implements ParameterSpecification {
	private final String filterName;
	private final String parameterName;
	private final Type definedParameterType;

	/**
	 * Constructs a parameter specification for a particular filter parameter.
	 *
	 * @param filterName The name of the filter
	 * @param parameterName The name of the parameter
	 * @param definedParameterType The paremeter type specified on the filter metadata
	 */
	public DynamicFilterParameterSpecification(
			String filterName,
			String parameterName,
			Type definedParameterType) {
		this.filterName = filterName;
		this.parameterName = parameterName;
		this.definedParameterType = definedParameterType;
	}

	/**
	 * {@inheritDoc}
	 */
	public int bind(
			PreparedStatement statement,
			QueryParameters qp,
			SessionImplementor session,
			int start) throws SQLException {
		final int columnSpan = definedParameterType.getColumnSpan( session.getFactory() );
		final Object value = session.getLoadQueryInfluencers().getFilterParameterValue( filterName + '.' + parameterName );
		if ( Collection.class.isInstance( value ) ) {
			int positions = 0;
			Iterator itr = ( ( Collection ) value ).iterator();
			while ( itr.hasNext() ) {
				definedParameterType.nullSafeSet( statement, itr.next(), start + positions, session );
				positions += columnSpan;
			}
			return positions;
		}
		else {
			definedParameterType.nullSafeSet( statement, value, start, session );
			return columnSpan;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Type getExpectedType() {
		return definedParameterType;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setExpectedType(Type expectedType) {
		// todo : throw exception?  maybe warn if not the same?
	}

	/**
	 * {@inheritDoc}
	 */
	public String renderDisplayInfo() {
		return "dynamic-filter={filterName=" + filterName + ",paramName=" + parameterName + "}";
	}
}
