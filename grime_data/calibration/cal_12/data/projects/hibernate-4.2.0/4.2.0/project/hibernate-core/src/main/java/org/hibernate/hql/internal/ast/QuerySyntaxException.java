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
package org.hibernate.hql.internal.ast;
import antlr.RecognitionException;

import org.hibernate.QueryException;

/**
 * Exception thrown when there is a syntax error in the HQL.
 *
 * @author josh
 */
public class QuerySyntaxException extends QueryException {

	public QuerySyntaxException(String message) {
		super( message );
	}

	public QuerySyntaxException(String message, String hql) {
		this( message );
		setQueryString( hql );
	}

	public static QuerySyntaxException convert(RecognitionException e) {
		return convert( e, null );
	}

	public static QuerySyntaxException convert(RecognitionException e, String hql) {
		String positionInfo = e.getLine() > 0 && e.getColumn() > 0
				? " near line " + e.getLine() + ", column " + e.getColumn()
				: "";
		return new QuerySyntaxException( e.getMessage() + positionInfo, hql );
	}

}
