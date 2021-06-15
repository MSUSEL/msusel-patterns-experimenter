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

import antlr.Token;
import antlr.collections.AST;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.util.StringHelper;

/**
 * Base node class for use by Hibernate within its AST trees.
 *
 * @author Joshua Davis
 * @author Steve Ebersole
 */
public class Node extends antlr.CommonAST {
	private String filename;
	private int line;
	private int column;
	private int textLength;

	public Node() {
		super();
	}

	public Node(Token tok) {
		super(tok);  // This will call initialize(tok)!
	}

	/**
	 * Retrieve the text to be used for rendering this particular node.
	 *
	 * @param sessionFactory The session factory
	 * @return The text to use for rendering
	 */
	public String getRenderText(SessionFactoryImplementor sessionFactory) {
		// The basic implementation is to simply use the node's text
		return getText();
	}

	@Override
    public void initialize(Token tok) {
		super.initialize(tok);
		filename = tok.getFilename();
		line = tok.getLine();
		column = tok.getColumn();
		String text = tok.getText();
		textLength = StringHelper.isEmpty(text) ? 0 : text.length();
	}

	@Override
    public void initialize(AST t) {
		super.initialize( t );
		if ( t instanceof Node ) {
			Node n = (Node)t;
			filename = n.filename;
			line = n.line;
			column = n.column;
			textLength = n.textLength;
		}
	}

	public String getFilename() {
		return filename;
	}

	@Override
    public int getLine() {
		return line;
	}

	@Override
    public int getColumn() {
		return column;
	}

	public int getTextLength() {
		return textLength;
	}
}
