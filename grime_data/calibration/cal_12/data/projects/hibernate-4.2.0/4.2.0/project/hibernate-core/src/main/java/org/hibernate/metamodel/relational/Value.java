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
package org.hibernate.metamodel.relational;

/**
 * Models a value within a {@link ValueContainer}.  This will generally be either a {@link Column column} or a
 * {@link DerivedValue derived value}, but we also allow the notion of {@link Tuple} at this level
 *
 * @author Steve Ebersole
 */
public interface Value {
	/**
	 * Retrieve the table that owns this value.
	 *
	 * @return The owning table.
	 */
	public TableSpecification getTable();

	/**
	 * Obtain the string representation of this value usable in log statements.
	 *
	 * @return The loggable representation
	 */
	public String toLoggableString();

	/**
	 * Used to track JDBC type usage throughout a series of potential recursive calls to component
	 * values since we do not know ahead of time which values correspond to which indexes of the
	 * jdbc type array.
	 */
	public static class JdbcCodes {
		private final int[] typeCodes;
		private int index = 0;

		public JdbcCodes(int[] typeCodes) {
			this.typeCodes = typeCodes;
		}

		public int nextJdbcCde() {
			return typeCodes[index++];
		}

		public int getIndex() {
			return index;
		}
	}

	/**
	 * Validate the value against the incoming JDBC type code array, both in terms of number of types
	 * and compatibility of types.
	 *
	 * @param typeCodes The type codes.
	 *
	 * @throws org.hibernate.metamodel.ValidationException if validaton fails.
	 */
	public void validateJdbcTypes(JdbcCodes typeCodes);

}
