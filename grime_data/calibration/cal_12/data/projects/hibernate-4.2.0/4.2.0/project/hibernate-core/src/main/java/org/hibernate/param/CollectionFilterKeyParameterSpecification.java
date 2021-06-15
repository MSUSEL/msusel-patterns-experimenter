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

import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.Type;

/**
 * A specialized ParameterSpecification impl for dealing with a collection-key as part of a collection filter
 * compilation.
 *
 * @author Steve Ebersole
 */
public class CollectionFilterKeyParameterSpecification implements ParameterSpecification {
	private final String collectionRole;
	private final Type keyType;
	private final int queryParameterPosition;

	/**
	 * Creates a specialized collection-filter collection-key parameter spec.
	 *
	 * @param collectionRole The collection role being filtered.
	 * @param keyType The mapped collection-key type.
	 * @param queryParameterPosition The position within {@link org.hibernate.engine.spi.QueryParameters} where
	 * we can find the appropriate param value to bind.
	 */
	public CollectionFilterKeyParameterSpecification(String collectionRole, Type keyType, int queryParameterPosition) {
		this.collectionRole = collectionRole;
		this.keyType = keyType;
		this.queryParameterPosition = queryParameterPosition;
	}

	/**
	 * {@inheritDoc}
	 */
	public int bind(
			PreparedStatement statement,
			QueryParameters qp,
			SessionImplementor session,
			int position) throws SQLException {
		Object value = qp.getPositionalParameterValues()[queryParameterPosition];
		keyType.nullSafeSet( statement, value, position, session );
		return keyType.getColumnSpan( session.getFactory() );
	}

	/**
	 * {@inheritDoc}
	 */
	public Type getExpectedType() {
		return keyType;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setExpectedType(Type expectedType) {
		// todo : throw exception?
	}

	/**
	 * {@inheritDoc}
	 */
	public String renderDisplayInfo() {
		return "collection-filter-key=" + collectionRole;
	}
}
