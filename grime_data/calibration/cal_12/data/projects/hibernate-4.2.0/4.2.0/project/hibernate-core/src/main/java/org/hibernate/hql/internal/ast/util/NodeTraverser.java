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
import java.util.Stack;

import antlr.collections.AST;

/**
 * A visitor for traversing an AST tree.
 * 
 * @author Steve Ebersole
 * @author Philip R. "Pib" Burns.
 * @author Strong Liu
 * 
 */

public class NodeTraverser {
	public static interface VisitationStrategy {
		public void visit( AST node );
	}

	private final VisitationStrategy strategy;

	public NodeTraverser( VisitationStrategy strategy ) {
		this.strategy = strategy;
	}

	/**
	 * Traverse the AST tree depth first.
	 * 
	 * @param ast
	 *            Root node of subtree to traverse.
	 * 
	 *            <p>
	 *            Note that the AST passed in is not visited itself. Visitation
	 *            starts with its children.
	 *            </p>
	 */
	public void traverseDepthFirst( AST ast ) {
		if ( ast == null ) {
			throw new IllegalArgumentException(
					"node to traverse cannot be null!" );
		}
		visitDepthFirst( ast.getFirstChild() );
	}
	
	private void visitDepthFirst(AST ast){
		if(ast==null){
			return;
		}
		Stack stack = new Stack();
		if ( ast != null ) {
			stack.push( ast );
			while (!stack.empty()) {
				ast = (AST) stack.pop();
				strategy.visit( ast );
				if ( ast.getNextSibling() != null ) 
					stack.push( ast.getNextSibling() );
				if ( ast.getFirstChild() != null ) 
					stack.push( ast.getFirstChild() );
			}
		}
	}

	
}
