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

import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

/**
 * Contract for nodes representing logical BETWEEN (ternary) operators.
 *
 * @author Steve Ebersole
 */
public class BetweenOperatorNode extends SqlNode implements OperatorNode {

	public void initialize() throws SemanticException {
		Node fixture = getFixtureOperand();
		if ( fixture == null ) {
			throw new SemanticException( "fixture operand of a between operator was null" );
		}
		Node low = getLowOperand();
		if ( low == null ) {
			throw new SemanticException( "low operand of a between operator was null" );
		}
		Node high = getHighOperand();
		if ( high == null ) {
			throw new SemanticException( "high operand of a between operator was null" );
		}
		check( fixture, low, high );
		check( low, high, fixture );
		check( high, fixture, low );
	}

	public Type getDataType() {
		// logic operators by definition resolve to boolean.
		return StandardBasicTypes.BOOLEAN;
	}

	public Node getFixtureOperand() {
		return ( Node ) getFirstChild();
	}

	public Node getLowOperand() {
		return ( Node ) getFirstChild().getNextSibling();
	}

	public Node getHighOperand() {
		return ( Node ) getFirstChild().getNextSibling().getNextSibling();
	}

	private void check(Node check, Node first, Node second) {
		if ( ExpectedTypeAwareNode.class.isAssignableFrom( check.getClass() ) ) {
			Type expectedType = null;
			if ( SqlNode.class.isAssignableFrom( first.getClass() ) ) {
				expectedType = ( ( SqlNode ) first ).getDataType();
			}
			if ( expectedType == null && SqlNode.class.isAssignableFrom( second.getClass() ) ) {
				expectedType = ( ( SqlNode ) second ).getDataType();
			}
			( ( ExpectedTypeAwareNode ) check ).setExpectedType( expectedType );
		}
	}
}
