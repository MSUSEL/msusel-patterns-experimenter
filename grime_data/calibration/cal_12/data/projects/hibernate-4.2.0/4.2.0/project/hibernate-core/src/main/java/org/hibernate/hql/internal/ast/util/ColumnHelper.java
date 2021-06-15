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
package org.hibernate.hql.internal.ast.util;
import antlr.ASTFactory;
import antlr.collections.AST;

import org.hibernate.hql.internal.NameGenerator;
import org.hibernate.hql.internal.antlr.SqlTokenTypes;
import org.hibernate.hql.internal.ast.tree.HqlSqlWalkerNode;

/**
 * Provides utility methods for dealing with arrays of SQL column names.
 *
 * @author josh
 */
public final class ColumnHelper {

	/**
	 * @deprecated (tell clover to filter this out)
	 */
	private ColumnHelper() {
	}

	public static void generateSingleScalarColumn(HqlSqlWalkerNode node, int i) {
		ASTFactory factory = node.getASTFactory();
		ASTUtil.createSibling( factory, SqlTokenTypes.SELECT_COLUMNS, " as " + NameGenerator.scalarName( i, 0 ), node );
	}

	/**
	 * Generates the scalar column AST nodes for a given array of SQL columns
	 */
	public static void generateScalarColumns(HqlSqlWalkerNode node, String sqlColumns[], int i) {
		if ( sqlColumns.length == 1 ) {
			generateSingleScalarColumn( node, i );
		}
		else {
			ASTFactory factory = node.getASTFactory();
			AST n = node;
			n.setText( sqlColumns[0] );	// Use the DOT node to emit the first column name.
			// Create the column names, folled by the column aliases.
			for ( int j = 0; j < sqlColumns.length; j++ ) {
				if ( j > 0 ) {
					n = ASTUtil.createSibling( factory, SqlTokenTypes.SQL_TOKEN, sqlColumns[j], n );
				}
				n = ASTUtil.createSibling( factory, SqlTokenTypes.SELECT_COLUMNS, " as " + NameGenerator.scalarName( i, j ), n );
			}
		}
	}
}
