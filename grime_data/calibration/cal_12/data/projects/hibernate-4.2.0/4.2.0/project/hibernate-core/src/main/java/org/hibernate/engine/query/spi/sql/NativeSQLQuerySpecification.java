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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.internal.util.collections.ArrayHelper;

/**
 * Defines the specification or blue-print for a native-sql query.
 * Essentially a simple struct containing the information needed to "translate"
 * a native-sql query and cache that translated representation.  Also used as
 * the key by which the native-sql query plans are cached.
 *
 * @author Steve Ebersole
 */
public class NativeSQLQuerySpecification {
	private final String queryString;
	private final NativeSQLQueryReturn[] queryReturns;
	private final Set querySpaces;
	private final int hashCode;

	public NativeSQLQuerySpecification(
			String queryString,
	        NativeSQLQueryReturn[] queryReturns,
	        Collection querySpaces) {
		this.queryString = queryString;
		this.queryReturns = queryReturns;
		if ( querySpaces == null ) {
			this.querySpaces = Collections.EMPTY_SET;
		}
		else {
			Set tmp = new HashSet();
			tmp.addAll( querySpaces );
			this.querySpaces = Collections.unmodifiableSet( tmp );
		}

		// pre-determine and cache the hashcode
		int hashCode = queryString.hashCode();
		hashCode = 29 * hashCode + this.querySpaces.hashCode();
		if ( this.queryReturns != null ) {
			hashCode = 29 * hashCode + ArrayHelper.toList( this.queryReturns ).hashCode();
		}
		this.hashCode = hashCode;
	}

	public String getQueryString() {
		return queryString;
	}

	public NativeSQLQueryReturn[] getQueryReturns() {
		return queryReturns;
	}

	public Set getQuerySpaces() {
		return querySpaces;
	}

	@Override
    public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		final NativeSQLQuerySpecification that = ( NativeSQLQuerySpecification ) o;

		return querySpaces.equals( that.querySpaces ) &&
		       queryString.equals( that.queryString ) &&
		       Arrays.equals( queryReturns, that.queryReturns );
	}


	@Override
    public int hashCode() {
		return hashCode;
	}
}
