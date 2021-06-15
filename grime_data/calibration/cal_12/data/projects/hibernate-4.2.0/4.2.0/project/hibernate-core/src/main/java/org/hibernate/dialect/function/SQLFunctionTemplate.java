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
package org.hibernate.dialect.function;
import java.util.List;

import org.hibernate.QueryException;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 * Represents HQL functions that can have different representations in different SQL dialects.
 * E.g. in HQL we can define function <code>concat(?1, ?2)</code> to concatenate two strings 
 * p1 and p2. Target SQL function will be dialect-specific, e.g. <code>(?1 || ?2)</code> for 
 * Oracle, <code>concat(?1, ?2)</code> for MySql, <code>(?1 + ?2)</code> for MS SQL.
 * Each dialect will define a template as a string (exactly like above) marking function 
 * parameters with '?' followed by parameter's index (first index is 1).
 *
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 */
public class SQLFunctionTemplate implements SQLFunction {
	private final Type type;
	private final TemplateRenderer renderer;
	private final boolean hasParenthesesIfNoArgs;

	public SQLFunctionTemplate(Type type, String template) {
		this( type, template, true );
	}

	public SQLFunctionTemplate(Type type, String template, boolean hasParenthesesIfNoArgs) {
		this.type = type;
		this.renderer = new TemplateRenderer( template );
		this.hasParenthesesIfNoArgs = hasParenthesesIfNoArgs;
	}

	/**
	 * {@inheritDoc}
	 */
	public String render(Type argumentType, List args, SessionFactoryImplementor factory) {
		return renderer.render( args, factory );
	}

	/**
	 * {@inheritDoc}
	 */
	public Type getReturnType(Type argumentType, Mapping mapping) throws QueryException {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasArguments() {
		return renderer.getAnticipatedNumberOfArguments() > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasParenthesesIfNoArguments() {
		return hasParenthesesIfNoArgs;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return renderer.getTemplate();
	}
}
