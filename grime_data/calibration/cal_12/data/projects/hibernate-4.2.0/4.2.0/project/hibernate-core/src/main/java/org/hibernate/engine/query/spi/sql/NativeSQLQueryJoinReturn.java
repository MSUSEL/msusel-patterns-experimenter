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
package org.hibernate.engine.query.spi.sql;
import java.util.Map;

import org.hibernate.LockMode;

/**
 * Represents a return defined as part of a native sql query which
 * names a fetched role.
 *
 * @author Steve Ebersole
 */
public class NativeSQLQueryJoinReturn extends NativeSQLQueryNonScalarReturn {
	private final String ownerAlias;
	private final String ownerProperty;
	private final int hashCode;
	/**
	 * Construct a return descriptor representing some form of fetch.
	 *
	 * @param alias The result alias
	 * @param ownerAlias The owner's result alias
	 * @param ownerProperty The owner's property representing the thing to be fetched
	 * @param propertyResults Any user-supplied column->property mappings
	 * @param lockMode The lock mode to apply
	 */
	public NativeSQLQueryJoinReturn(
			String alias,
			String ownerAlias,
			String ownerProperty,
			Map propertyResults,
			LockMode lockMode) {
		super( alias, propertyResults, lockMode );
		this.ownerAlias = ownerAlias;
		this.ownerProperty = ownerProperty;
		this.hashCode = determineHashCode();
	}

	/**
	 * Retrieve the alias of the owner of this fetched association.
	 *
	 * @return The owner's alias.
	 */
	public String getOwnerAlias() {
		return ownerAlias;
	}

	/**
	 * Retrieve the property name (relative to the owner) which maps to
	 * the association to be fetched.
	 *
	 * @return The property name.
	 */
	public String getOwnerProperty() {
		return ownerProperty;
	}

	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		if ( !super.equals( o ) ) {
			return false;
		}

		NativeSQLQueryJoinReturn that = ( NativeSQLQueryJoinReturn ) o;

		if ( ownerAlias != null ? !ownerAlias.equals( that.ownerAlias ) : that.ownerAlias != null ) {
			return false;
		}
		if ( ownerProperty != null ? !ownerProperty.equals( that.ownerProperty ) : that.ownerProperty != null ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		return hashCode;
	}

	private int determineHashCode() {
		int result = super.hashCode();
		result = 31 * result + ( ownerAlias != null ? ownerAlias.hashCode() : 0 );
		result = 31 * result + ( ownerProperty != null ? ownerProperty.hashCode() : 0 );
		return result;
	}
}
