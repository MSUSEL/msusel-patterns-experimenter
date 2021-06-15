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
package org.hibernate;

import java.sql.ResultSet;

/**
 * Specifies the type of JDBC scrollable result set to use
 * underneath a <tt>ScrollableResults</tt>
 *
 * @author Gavin King
 * @see Query#scroll(ScrollMode)
 * @see ScrollableResults
 */
public enum ScrollMode {
	/**
	 * @see java.sql.ResultSet#TYPE_FORWARD_ONLY
	 */
	FORWARD_ONLY( ResultSet.TYPE_FORWARD_ONLY ),

	/**
	 * @see java.sql.ResultSet#TYPE_SCROLL_SENSITIVE
	 */
	SCROLL_SENSITIVE(
			ResultSet.TYPE_SCROLL_SENSITIVE
	),
	/**
	 * Note that since the Hibernate session acts as a cache, you
	 * might need to expicitly evict objects, if you need to see
	 * changes made by other transactions.
	 *
	 * @see java.sql.ResultSet#TYPE_SCROLL_INSENSITIVE
	 */
	SCROLL_INSENSITIVE(
			ResultSet.TYPE_SCROLL_INSENSITIVE
	);
	private final int resultSetType;

	private ScrollMode(int level) {
		this.resultSetType = level;
	}


	/**
	 * @return the JDBC result set type code
	 */
	public int toResultSetType() {
		return resultSetType;
	}


	public boolean lessThan(ScrollMode other) {
		return this.resultSetType < other.resultSetType;
	}

}






