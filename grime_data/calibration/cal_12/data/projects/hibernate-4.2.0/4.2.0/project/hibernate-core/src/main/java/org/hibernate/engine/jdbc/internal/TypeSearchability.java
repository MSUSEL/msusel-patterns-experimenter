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
package org.hibernate.engine.jdbc.internal;

import java.sql.DatabaseMetaData;

/**
 * Describes the searchability of a data type as reported by the JDBC driver.
 *
 * @author Steve Ebersole
 */
public enum TypeSearchability {
	/**
	 * Type is not searchable.
	 * @see java.sql.DatabaseMetaData#typePredNone
	 */
	NONE,
	/**
	 * Type is fully searchable
	 * @see java.sql.DatabaseMetaData#typeSearchable
	 */
	FULL,
	/**
	 * Type is valid only in {@code WHERE ... LIKE}
	 * @see java.sql.DatabaseMetaData#typePredChar
	 */
	CHAR,
	/**
	 * Type is supported only in {@code WHERE ... LIKE}
	 * @see java.sql.DatabaseMetaData#typePredBasic
	 */
	BASIC;

	/**
	 * Based on the code retrieved from {@link java.sql.DatabaseMetaData#getTypeInfo()} for the {@code SEARCHABLE}
	 * column, return the appropriate enum.
	 *
	 * @param code The retrieved code value.
	 *
	 * @return The corresponding enum.
	 */
	public static TypeSearchability interpret(short code) {
		switch ( code ) {
			case DatabaseMetaData.typeSearchable: {
				return FULL;
			}
			case DatabaseMetaData.typePredNone: {
				return NONE;
			}
			case DatabaseMetaData.typePredBasic: {
				return BASIC;
			}
			case DatabaseMetaData.typePredChar: {
				return CHAR;
			}
			default: {
				throw new IllegalArgumentException( "Unknown type searchability code [" + code + "] enountered" );
			}
		}
	}
}
