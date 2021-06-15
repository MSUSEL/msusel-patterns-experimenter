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
package org.hibernate.hql.internal.ast.tree;
import antlr.SemanticException;

import org.hibernate.type.Type;

/**
 * Partial implementation of SelectExpression for all the nodes that aren't constructors.
 *
 * @author Joshua Davis
 */
public abstract class AbstractSelectExpression extends HqlSqlWalkerNode implements SelectExpression {
	
	private String alias;
	private int scalarColumnIndex = -1;
	
	public final void setAlias(String alias) {
		this.alias = alias;
	}
	
	public final String getAlias() {
		return alias;
	}

	public boolean isConstructor() {
		return false;
	}

	public boolean isReturnableEntity() throws SemanticException {
		return false;
	}

	public FromElement getFromElement() {
		return null;
	}

	public boolean isScalar() throws SemanticException {
		// Default implementation:
		// If this node has a data type, and that data type is not an association, then this is scalar.
		Type type = getDataType();
		return type != null && !type.isAssociationType();	// Moved here from SelectClause [jsd]
	}

	public void setScalarColumn(int i) throws SemanticException {
		this.scalarColumnIndex = i;
		setScalarColumnText( i );
	}

	public int getScalarColumnIndex() {
		return scalarColumnIndex;
	}
}
