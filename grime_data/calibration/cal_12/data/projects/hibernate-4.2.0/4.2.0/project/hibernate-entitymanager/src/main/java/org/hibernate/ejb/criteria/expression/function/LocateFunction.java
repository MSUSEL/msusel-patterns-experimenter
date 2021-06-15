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
package org.hibernate.ejb.criteria.expression.function;

import java.io.Serializable;
import javax.persistence.criteria.Expression;

import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.CriteriaQueryCompiler;
import org.hibernate.ejb.criteria.ParameterRegistry;
import org.hibernate.ejb.criteria.Renderable;
import org.hibernate.ejb.criteria.expression.LiteralExpression;

/**
 * Models the ANSI SQL <tt>LOCATE</tt> function.
 *
 * @author Steve Ebersole
 */
public class LocateFunction
		extends BasicFunctionExpression<Integer>
		implements Serializable {
	public static final String NAME = "locate";

	private final Expression<String> pattern;
	private final Expression<String> string;
	private final Expression<Integer> start;

	public LocateFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> pattern,
			Expression<String> string,
			Expression<Integer> start) {
		super( criteriaBuilder, Integer.class, NAME );
		this.pattern = pattern;
		this.string = string;
		this.start = start;
	}

	public LocateFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> pattern,
			Expression<String> string) {
		this( criteriaBuilder, pattern, string, null );
	}

	public LocateFunction(CriteriaBuilderImpl criteriaBuilder, String pattern, Expression<String> string) {
		this(
				criteriaBuilder,
				new LiteralExpression<String>( criteriaBuilder, pattern ),
				string,
				null
		);
	}

	public LocateFunction(CriteriaBuilderImpl criteriaBuilder, String pattern, Expression<String> string, int start) {
		this(
				criteriaBuilder,
				new LiteralExpression<String>( criteriaBuilder, pattern ),
				string,
				new LiteralExpression<Integer>( criteriaBuilder, start )
		);
	}

	public Expression<String> getPattern() {
		return pattern;
	}

	public Expression<Integer> getStart() {
		return start;
	}

	public Expression<String> getString() {
		return string;
	}

	@Override
	public void registerParameters(ParameterRegistry registry) {
		Helper.possibleParameter( getPattern(), registry );
		Helper.possibleParameter( getStart(), registry );
		Helper.possibleParameter( getString(), registry );
	}

	@Override
	public String render(CriteriaQueryCompiler.RenderingContext renderingContext) {
		StringBuilder buffer = new StringBuilder();
		buffer.append( "locate(" )
				.append( ( (Renderable) getPattern() ).render( renderingContext ) )
				.append( ',' )
				.append( ( (Renderable) getString() ).render( renderingContext ) );
		if ( getStart() != null ) {
			buffer.append( ',' )
					.append( ( (Renderable) getStart() ).render( renderingContext ) );
		}
		buffer.append( ')' );
		return buffer.toString();
	}
}
