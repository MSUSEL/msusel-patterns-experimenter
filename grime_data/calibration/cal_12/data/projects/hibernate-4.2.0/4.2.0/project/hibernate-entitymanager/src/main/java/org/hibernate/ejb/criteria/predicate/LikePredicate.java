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
package org.hibernate.ejb.criteria.predicate;

import java.io.Serializable;
import javax.persistence.criteria.Expression;

import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.CriteriaQueryCompiler;
import org.hibernate.ejb.criteria.ParameterRegistry;
import org.hibernate.ejb.criteria.Renderable;
import org.hibernate.ejb.criteria.expression.LiteralExpression;

/**
 * Models a SQL <tt>LIKE</tt> expression.
 *
 * @author Steve Ebersole
 */
public class LikePredicate extends AbstractSimplePredicate implements Serializable {
	private final Expression<String> matchExpression;
	private final Expression<String> pattern;
	private final Expression<Character> escapeCharacter;

	public LikePredicate(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> matchExpression,
			Expression<String> pattern) {
		this( criteriaBuilder, matchExpression, pattern, null );
	}

	public LikePredicate(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> matchExpression,
			String pattern) {
		this( criteriaBuilder, matchExpression, new LiteralExpression<String>( criteriaBuilder, pattern) );
	}

	public LikePredicate(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> matchExpression,
			Expression<String> pattern,
			Expression<Character> escapeCharacter) {
		super( criteriaBuilder );
		this.matchExpression = matchExpression;
		this.pattern = pattern;
		this.escapeCharacter = escapeCharacter;
	}

	public LikePredicate(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> matchExpression,
			Expression<String> pattern,
			char escapeCharacter) {
		this(
				criteriaBuilder,
				matchExpression,
				pattern,
				new LiteralExpression<Character>( criteriaBuilder, escapeCharacter )
		);
	}

	public LikePredicate(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> matchExpression,
			String pattern,
			char escapeCharacter) {
		this(
				criteriaBuilder,
				matchExpression,
				new LiteralExpression<String>( criteriaBuilder, pattern ),
				new LiteralExpression<Character>( criteriaBuilder, escapeCharacter )
		);
	}

	public LikePredicate(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> matchExpression,
			String pattern,
			Expression<Character> escapeCharacter) {
		this(
				criteriaBuilder,
				matchExpression,
				new LiteralExpression<String>( criteriaBuilder, pattern ),
				escapeCharacter
		);
	}

	public Expression<Character> getEscapeCharacter() {
		return escapeCharacter;
	}

	public Expression<String> getMatchExpression() {
		return matchExpression;
	}

	public Expression<String> getPattern() {
		return pattern;
	}

	public void registerParameters(ParameterRegistry registry) {
		Helper.possibleParameter( getEscapeCharacter(), registry );
		Helper.possibleParameter( getMatchExpression(), registry );
		Helper.possibleParameter( getPattern(), registry );
	}

	public String render(CriteriaQueryCompiler.RenderingContext renderingContext) {
		final String operator = isNegated() ? " not like " : " like ";
		StringBuilder buffer = new StringBuilder();
		buffer.append( ( (Renderable) getMatchExpression() ).render( renderingContext ) )
				.append( operator )
				.append( ( (Renderable) getPattern() ).render( renderingContext ) );
		if ( escapeCharacter != null ) {
			buffer.append( " escape " )
					.append( ( (Renderable) getEscapeCharacter() ).render( renderingContext ) );
		}
		return buffer.toString();
	}

	public String renderProjection(CriteriaQueryCompiler.RenderingContext renderingContext) {
		return render( renderingContext );
	}
}
