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
import antlr.collections.AST;
import org.jboss.logging.Logger;

import org.hibernate.internal.CoreMessageLogger;

/**
 * Represents a reference to a FROM element, for example a class alias in a WHERE clause.
 *
 * @author josh
 */
public abstract class FromReferenceNode extends AbstractSelectExpression
        implements ResolvableNode, DisplayableNode, InitializeableNode, PathNode {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, FromReferenceNode.class.getName() );

	private FromElement fromElement;
	private boolean resolved = false;
	public static final int ROOT_LEVEL = 0;

	@Override
    public FromElement getFromElement() {
		return fromElement;
	}

	public void setFromElement(FromElement fromElement) {
		this.fromElement = fromElement;
	}

	/**
	 * Resolves the left hand side of the DOT.
	 *
	 * @throws SemanticException
	 */
	public void resolveFirstChild() throws SemanticException {
	}

	public String getPath() {
		return getOriginalText();
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved() {
		this.resolved = true;
		if ( LOG.isDebugEnabled() ) {
			LOG.debugf( "Resolved : %s -> %s", this.getPath(), this.getText() );
		}
	}

	public String getDisplayText() {
		StringBuilder buf = new StringBuilder();
		buf.append( "{" ).append( ( fromElement == null ) ? "no fromElement" : fromElement.getDisplayText() );
		buf.append( "}" );
		return buf.toString();
	}

	public void recursiveResolve(int level, boolean impliedAtRoot, String classAlias) throws SemanticException {
		recursiveResolve( level, impliedAtRoot, classAlias, this );
	}

	public void recursiveResolve(int level, boolean impliedAtRoot, String classAlias, AST parent) throws SemanticException {
		AST lhs = getFirstChild();
		int nextLevel = level + 1;
		if ( lhs != null ) {
			FromReferenceNode n = ( FromReferenceNode ) lhs;
			n.recursiveResolve( nextLevel, impliedAtRoot, null, this );
		}
		resolveFirstChild();
		boolean impliedJoin = true;
		if ( level == ROOT_LEVEL && !impliedAtRoot ) {
			impliedJoin = false;
		}
		resolve( true, impliedJoin, classAlias, parent );
	}

	@Override
    public boolean isReturnableEntity() throws SemanticException {
		return !isScalar() && fromElement.isEntity();
	}

	public void resolveInFunctionCall(boolean generateJoin, boolean implicitJoin) throws SemanticException {
		resolve( generateJoin, implicitJoin );
	}

	public void resolve(boolean generateJoin, boolean implicitJoin) throws SemanticException {
		resolve( generateJoin, implicitJoin, null );
	}

	public void resolve(boolean generateJoin, boolean implicitJoin, String classAlias) throws SemanticException {
		resolve( generateJoin, implicitJoin, classAlias, null );
	}

	public void prepareForDot(String propertyName) throws SemanticException {
	}

	/**
	 * Sub-classes can override this method if they produce implied joins (e.g. DotNode).
	 *
	 * @return an implied join created by this from reference.
	 */
	public FromElement getImpliedJoin() {
		return null;
	}

}
