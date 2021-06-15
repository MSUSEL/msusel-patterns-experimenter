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
import org.hibernate.type.Type;

/**
 * Describes a scalar return in a native SQL query.
 *
 * @author gloegl
 */
public class NativeSQLQueryScalarReturn implements NativeSQLQueryReturn {
	private final Type type;
	private final String columnAlias;
	private final int hashCode;

	public NativeSQLQueryScalarReturn(String alias, Type type) {
		this.type = type;
		this.columnAlias = alias;
		this.hashCode = determineHashCode();
	}

	public String getColumnAlias() {
		return columnAlias;
	}

	public Type getType() {
		return type;
	}

	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		NativeSQLQueryScalarReturn that = ( NativeSQLQueryScalarReturn ) o;

		if ( columnAlias != null ? !columnAlias.equals( that.columnAlias ) : that.columnAlias != null ) {
			return false;
		}
		if ( type != null ? !type.equals( that.type ) : that.type != null ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		return hashCode;
	}

	private int determineHashCode() {
		int result = type != null ? type.hashCode() : 0;
		result = 31 * result + ( getClass().getName().hashCode() );
		result = 31 * result + ( columnAlias != null ? columnAlias.hashCode() : 0 );
		return result;
	}
}
