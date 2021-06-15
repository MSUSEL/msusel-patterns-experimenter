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
import org.jboss.logging.Logger;

import org.hibernate.hql.internal.antlr.HqlSqlTokenTypes;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;

/**
 * Provides utility methods for paths.
 *
 * @author josh
 */
public final class PathHelper {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, PathHelper.class.getName() );

	private PathHelper() {
	}

	/**
	 * Turns a path into an AST.
	 *
	 * @param path    The path.
	 * @param factory The AST factory to use.
	 * @return An HQL AST representing the path.
	 */
	public static AST parsePath(String path, ASTFactory factory) {
		String[] identifiers = StringHelper.split( ".", path );
		AST lhs = null;
		for ( int i = 0; i < identifiers.length; i++ ) {
			String identifier = identifiers[i];
			AST child = ASTUtil.create( factory, HqlSqlTokenTypes.IDENT, identifier );
			if ( i == 0 ) {
				lhs = child;
			}
			else {
				lhs = ASTUtil.createBinarySubtree( factory, HqlSqlTokenTypes.DOT, ".", lhs, child );
			}
		}
		if ( LOG.isDebugEnabled() ) {
			LOG.debugf( "parsePath() : %s -> %s", path, ASTUtil.getDebugString( lhs ) );
		}
		return lhs;
	}

	public static String getAlias(String path) {
		return StringHelper.root( path );
	}
}
