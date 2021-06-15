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
 * Represents an element of a projection list, i.e. a select expression.
 *
 * @author josh
 */
public interface SelectExpression {
	/**
	 * Returns the data type of the select expression.
	 *
	 * @return The data type of the select expression.
	 */
	Type getDataType();

	/**
	 * Appends AST nodes that represent the columns after the current AST node.
	 * (e.g. 'as col0_O_')
	 *
	 * @param i The index of the select expression in the projection list.
	 *
	 * @throws antlr.SemanticException if a semantic error occurs
	 */
	void setScalarColumnText(int i) throws SemanticException;

	/**
	 * Sets the index and text for select expression in the projection list.
	 *  
	 * @param i The index of the select expression in the projection list.
	 *
	 * @throws SemanticException if a semantic error occurs
	 */
	void setScalarColumn(int i) throws SemanticException;

	/**
	 * Gets index of the select expression in the projection list.
	 *
	 * @return The index of the select expression in the projection list.
	 */
	int getScalarColumnIndex();
	
	/**
	 * Returns the FROM element that this expression refers to.
	 *
	 * @return The FROM element.
	 */
	FromElement getFromElement();

	/**
	 * Returns true if the element is a constructor (e.g. new Foo).
	 *
	 * @return true if the element is a constructor (e.g. new Foo).
	 */
	@SuppressWarnings( {"UnusedDeclaration"})
	boolean isConstructor();

	/**
	 * Returns true if this select expression represents an entity that can be returned.
	 *
	 * @return true if this select expression represents an entity that can be returned.
	 *
	 * @throws SemanticException if a semantic error occurs
	 */
	boolean isReturnableEntity() throws SemanticException;

	/**
	 * Sets the text of the node.
	 *
	 * @param text the new node text.
	 */
	void setText(String text);

	boolean isScalar() throws SemanticException;
	
	void setAlias(String alias);
	String getAlias();
}
