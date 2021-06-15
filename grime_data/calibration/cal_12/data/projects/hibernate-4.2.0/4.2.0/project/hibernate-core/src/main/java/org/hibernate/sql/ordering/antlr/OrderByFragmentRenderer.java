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
import org.jboss.logging.Logger;

import org.hibernate.NullPrecedence;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.util.ASTPrinter;
import org.hibernate.internal.util.StringHelper;

/**
 * Extension of the Antlr-generated tree walker for rendering the parsed order-by tree back to String form.
 * {@link #out(antlr.collections.AST)} is the sole semantic action here and it is used to utilize our
 * split between text (tree debugging text) and "renderable text" (text to use during rendering).
 *
 * @author Steve Ebersole
 */
public class OrderByFragmentRenderer extends GeneratedOrderByFragmentRenderer {

	private static final Logger LOG = Logger.getLogger( OrderByFragmentRenderer.class.getName() );
	private static final ASTPrinter printer = new ASTPrinter( GeneratedOrderByFragmentRendererTokenTypes.class );

	private final SessionFactoryImplementor sessionFactory;

	public OrderByFragmentRenderer(SessionFactoryImplementor sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
    protected void out(AST ast) {
		out( ( ( Node ) ast ).getRenderableText() );
	}


	// handle trace logging ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private int traceDepth = 0;

	@Override
    public void traceIn(String ruleName, AST tree) {
		if ( inputState.guessing > 0 ) {
			return;
		}
		String prefix = StringHelper.repeat( '-', (traceDepth++ * 2) ) + "-> ";
		String traceText = ruleName + " (" + buildTraceNodeName(tree) + ")";
		LOG.trace( prefix + traceText );
	}

	private String buildTraceNodeName(AST tree) {
		return tree == null
				? "???"
				: tree.getText() + " [" + printer.getTokenTypeName( tree.getType() ) + "]";
	}

	@Override
    public void traceOut(String ruleName, AST tree) {
		if ( inputState.guessing > 0 ) {
			return;
		}
		String prefix = "<-" + StringHelper.repeat( '-', (--traceDepth * 2) ) + " ";
		LOG.trace( prefix + ruleName );
	}

	@Override
	protected String renderOrderByElement(String expression, String collation, String order, String nulls) {
		final NullPrecedence nullPrecedence = NullPrecedence.parse( nulls, sessionFactory.getSettings().getDefaultNullPrecedence() );
		return sessionFactory.getDialect().renderOrderByElement( expression, collation, order, nullPrecedence );
	}
}
