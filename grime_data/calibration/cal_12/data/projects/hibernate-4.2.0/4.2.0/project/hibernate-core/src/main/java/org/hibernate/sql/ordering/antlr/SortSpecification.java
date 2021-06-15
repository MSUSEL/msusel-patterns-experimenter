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
package org.hibernate.sql.ordering.antlr;

import antlr.collections.AST;

/**
 * Models each sorting expression.
 *
 * @author Steve Ebersole
 */
public class SortSpecification extends NodeSupport {
	/**
	 * Locate the specified {@link SortKey}.
	 *
	 * @return The sort key.
	 */
	public SortKey getSortKey() {
		return ( SortKey ) getFirstChild();
	}

	/**
	 * Locate the specified <tt>collation specification</tt>, if one.
	 *
	 * @return The <tt>collation specification</tt>, or null if none was specified.
	 */
	public CollationSpecification getCollation() {
		AST possible = getSortKey().getNextSibling();
		return  possible != null && OrderByTemplateTokenTypes.COLLATE == possible.getType()
				? ( CollationSpecification ) possible
				: null;
	}

	/**
	 * Locate the specified <tt>ordering specification</tt>, if one.
	 *
	 * @return The <tt>ordering specification</tt>, or null if none was specified.
	 */
	public OrderingSpecification getOrdering() {
		// IMPL NOTE : the ordering-spec would be either the 2nd or 3rd child (of the overall sort-spec), if it existed,
		// 		depending on whether a collation-spec was specified.

		AST possible = getSortKey().getNextSibling();
		if ( possible == null ) {
			// There was no sort-spec parts specified other then the sort-key so there can be no ordering-spec...
			return null;
		}

		if ( OrderByTemplateTokenTypes.COLLATE == possible.getType() ) {
			// the 2nd child was a collation-spec, so we need to check the 3rd child instead.
			possible = possible.getNextSibling();
		}

		return possible != null && OrderByTemplateTokenTypes.ORDER_SPEC == possible.getType()
				?  ( OrderingSpecification ) possible
				:  null;
	}
}
