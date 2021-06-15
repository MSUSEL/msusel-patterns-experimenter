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
package org.hibernate.dialect;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Strategy for extracting the unique column alias out of a {@link ResultSetMetaData}.  This is used during the
 * "auto discovery" phase of native SQL queries.
 * <p/>
 * Generally this should be done via {@link ResultSetMetaData#getColumnLabel}, but not all drivers do this correctly.
 *
 * @author Steve Ebersole
 */
public interface ColumnAliasExtractor {
	/**
	 * Extract the unique column alias.
	 *
	 * @param metaData The result set metadata
	 * @param position The column position
	 *
	 * @return The alias
	 */
	public String extractColumnAlias(ResultSetMetaData metaData, int position) throws SQLException;

	/**
	 * An extractor which uses {@link ResultSetMetaData#getColumnLabel}
	 */
	public static final ColumnAliasExtractor COLUMN_LABEL_EXTRACTOR = new ColumnAliasExtractor() {
		@Override
		public String extractColumnAlias(ResultSetMetaData metaData, int position) throws SQLException {
			return metaData.getColumnLabel( position );
		}
	};

	/**
	 * An extractor which uses {@link ResultSetMetaData#getColumnName}
	 */
	public static final ColumnAliasExtractor COLUMN_NAME_EXTRACTOR = new ColumnAliasExtractor() {
		@Override
		public String extractColumnAlias(ResultSetMetaData metaData, int position) throws SQLException {
			return metaData.getColumnName( position );
		}
	};
}
