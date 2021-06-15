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

import javax.persistence.criteria.CriteriaBuilder.Trimspec;
import javax.persistence.criteria.Expression;

import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.CriteriaQueryCompiler;
import org.hibernate.ejb.criteria.ParameterRegistry;
import org.hibernate.ejb.criteria.Renderable;
import org.hibernate.ejb.criteria.expression.LiteralExpression;

/**
 * Models the ANSI SQL <tt>TRIM</tt> function.
 *
 * @author Steve Ebersole
 * @author Brett Meyer
 */
public class TrimFunction
		extends BasicFunctionExpression<String>
		implements Serializable {
	public static final String NAME = "trim";
	public static final Trimspec DEFAULT_TRIMSPEC = Trimspec.BOTH;
	public static final char DEFAULT_TRIM_CHAR = ' ';

	private final Trimspec trimspec;
	private final Expression<Character> trimCharacter;
	private final Expression<String> trimSource;

	public TrimFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Trimspec trimspec,
			Expression<Character> trimCharacter,
			Expression<String> trimSource) {
		super( criteriaBuilder, String.class, NAME );
		this.trimspec = trimspec;
		this.trimCharacter = trimCharacter;
		this.trimSource = trimSource;
	}

	public TrimFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Trimspec trimspec,
			char trimCharacter,
			Expression<String> trimSource) {
		super( criteriaBuilder, String.class, NAME );
		this.trimspec = trimspec;
		this.trimCharacter = new LiteralExpression<Character>( criteriaBuilder, trimCharacter );
		this.trimSource = trimSource;
	}

	public TrimFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> trimSource) {
		this( criteriaBuilder, DEFAULT_TRIMSPEC, DEFAULT_TRIM_CHAR, trimSource );
	}

	public TrimFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<Character> trimCharacter,
			Expression<String> trimSource) {
		this( criteriaBuilder, DEFAULT_TRIMSPEC, trimCharacter, trimSource );
	}

	public TrimFunction(
			CriteriaBuilderImpl criteriaBuilder,
			char trimCharacter,
			Expression<String> trimSource) {
		this( criteriaBuilder, DEFAULT_TRIMSPEC, trimCharacter, trimSource );
	}

	public TrimFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Trimspec trimspec,
			Expression<String> trimSource) {
		this( criteriaBuilder, trimspec, DEFAULT_TRIM_CHAR, trimSource );
	}

	public Expression<Character> getTrimCharacter() {
		return trimCharacter;
	}

	public Expression<String> getTrimSource() {
		return trimSource;
	}

	public Trimspec getTrimspec() {
		return trimspec;
	}

	@Override
	public void registerParameters(ParameterRegistry registry) {
		Helper.possibleParameter( getTrimCharacter(), registry );
		Helper.possibleParameter( getTrimSource(), registry );
	}

	@Override
	public String render(CriteriaQueryCompiler.RenderingContext renderingContext) {
		String renderedTrimChar;
		if ( trimCharacter.getClass().isAssignableFrom( 
				LiteralExpression.class ) ) {
			// If the character is a literal, treat it as one.  A few dialects
			// do not support parameters as trim() arguments.
			renderedTrimChar = ( ( LiteralExpression<Character> ) 
					trimCharacter ).getLiteral().toString();
		} else {
			renderedTrimChar = ( (Renderable) trimCharacter ).render( 
					renderingContext );
		}
		return new StringBuilder()
				.append( "trim(" )
				.append( trimspec.name() )
				.append( ' ' )
				.append( renderedTrimChar )
				.append( " from " )
				.append( ( (Renderable) trimSource ).render( renderingContext ) )
				.append( ')' )
				.toString();
	}
}
