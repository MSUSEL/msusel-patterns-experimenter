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
 * Models the ANSI SQL <tt>SUBSTRING</tt> function.
 *
 * @author Steve Ebersole
 */
public class SubstringFunction
		extends BasicFunctionExpression<String>
		implements Serializable {
	public static final String NAME = "substring";

	private final Expression<String> value;
	private final Expression<Integer> start;
	private final Expression<Integer> length;

	public SubstringFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> value,
			Expression<Integer> start,
			Expression<Integer> length) {
		super( criteriaBuilder, String.class, NAME );
		this.value = value;
		this.start = start;
		this.length = length;
	}

	@SuppressWarnings({ "RedundantCast" })
	public SubstringFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> value, 
			Expression<Integer> start) {
		this( criteriaBuilder, value, start, (Expression<Integer>)null );
	}

	public SubstringFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> value,
			int start) {
		this(
				criteriaBuilder,
				value,
				new LiteralExpression<Integer>( criteriaBuilder, start )
		);
	}

	public SubstringFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> value,
			int start,
			int length) {
		this(
				criteriaBuilder,
				value,
				new LiteralExpression<Integer>( criteriaBuilder, start ),
				new LiteralExpression<Integer>( criteriaBuilder, length )
		);
	}

	public Expression<Integer> getLength() {
		return length;
	}

	public Expression<Integer> getStart() {
		return start;
	}

	public Expression<String> getValue() {
		return value;
	}

	@Override
	public void registerParameters(ParameterRegistry registry) {
		Helper.possibleParameter( getLength(), registry );
		Helper.possibleParameter( getStart(), registry );
		Helper.possibleParameter( getValue(), registry );
	}

	public String render(CriteriaQueryCompiler.RenderingContext renderingContext) {
		StringBuilder buffer = new StringBuilder();
		buffer.append( "substring(" )
				.append( ( (Renderable) getValue() ).render( renderingContext ) )
				.append( ',' )
				.append( ( (Renderable) getStart() ).render( renderingContext ) );
		if ( getLength() != null ) {
			buffer.append( ',' )
					.append( ( (Renderable) getLength() ).render( renderingContext ) );
		}
		buffer.append( ')' );
		return buffer.toString();
	}
}
