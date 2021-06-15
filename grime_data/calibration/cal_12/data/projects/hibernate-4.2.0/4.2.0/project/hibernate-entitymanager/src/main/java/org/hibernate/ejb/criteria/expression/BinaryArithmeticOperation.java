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
package org.hibernate.ejb.criteria.expression;

import java.io.Serializable;
import javax.persistence.criteria.Expression;

import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.CriteriaQueryCompiler;
import org.hibernate.ejb.criteria.ParameterRegistry;
import org.hibernate.ejb.criteria.Renderable;
import org.hibernate.ejb.criteria.predicate.ImplicitNumericExpressionTypeDeterminer;

/**
 * Models standard arithmetc operations with two operands.
 *
 * @author Steve Ebersole
 */
public class BinaryArithmeticOperation<N extends Number>
		extends ExpressionImpl<N>
		implements BinaryOperatorExpression<N>, Serializable {

	public static enum Operation {
		ADD {
			String apply(String lhs, String rhs) {
				return applyPrimitive( lhs, '+', rhs );
			}
		},
		SUBTRACT {
			String apply(String lhs, String rhs) {
				return applyPrimitive( lhs, '-', rhs );
			}
		},
		MULTIPLY {
			String apply(String lhs, String rhs) {
				return applyPrimitive( lhs, '*', rhs );
			}
		},
		DIVIDE {
			String apply(String lhs, String rhs) {
				return applyPrimitive( lhs, '/', rhs );
			}
		},
		QUOT {
			String apply(String lhs, String rhs) {
				return applyPrimitive( lhs, '/', rhs );
			}
		},
		MOD {
			String apply(String lhs, String rhs) {
//				return lhs + " % " + rhs;
				return "mod(" + lhs + "," + rhs + ")";
			}
		};
		abstract String apply(String lhs, String rhs);

		private static final char LEFT_PAREN = '(';
		private static final char RIGHT_PAREN = ')';
		private static String applyPrimitive(String lhs, char operator, String rhs) {
			return new StringBuffer( lhs.length() + rhs.length() + 3 )
					.append( LEFT_PAREN )
					.append( lhs )
					.append( operator )
					.append( rhs )
					.append( RIGHT_PAREN )
					.toString();
		}
	}

	private final Operation operator;
	private final Expression<? extends N> rhs;
	private final Expression<? extends N> lhs;

	public static Class<? extends Number> determineResultType(
			Class<? extends Number> argument1Type,
			Class<? extends Number> argument2Type
	) {
		return determineResultType( argument1Type, argument2Type, false );
	}

	public static Class<? extends Number> determineResultType(
			Class<? extends Number> argument1Type,
			Class<? extends Number> argument2Type,
			boolean isQuotientOperation
	) {
		if ( isQuotientOperation ) {
			return Number.class;
		}
		return ImplicitNumericExpressionTypeDeterminer.determineResultType( argument1Type, argument2Type );
	}

	/**
	 * Helper for determining the appropriate operation return type based on one of the operands as an expression.
	 *
	 * @param defaultType The default return type to use if we cannot determine the java type of 'expression' operand.
	 * @param expression The operand.
	 *
	 * @return The appropriate return type.
	 */
	public static Class<? extends Number> determineReturnType(
			Class<? extends Number> defaultType,
			Expression<? extends Number> expression) {
		return expression == null || expression.getJavaType() == null 
				? defaultType
				: expression.getJavaType();
	}

	/**
	 * Helper for determining the appropriate operation return type based on one of the operands as a literal.
	 *
	 * @param defaultType The default return type to use if we cannot determine the java type of 'numberLiteral' operand.
	 * @param numberLiteral The operand.
	 *
	 * @return The appropriate return type.
	 */
	public static Class<? extends Number> determineReturnType(
			Class<? extends Number> defaultType,
			Number numberLiteral) {
		return numberLiteral == null ? defaultType : numberLiteral.getClass();
	}

	/**
	 * Creates an arithmethic operation based on 2 expressions.
	 *
	 * @param criteriaBuilder The builder for query components.
	 * @param resultType The operation result type
	 * @param operator The operator (type of operation).
	 * @param lhs The left-hand operand.
	 * @param rhs The right-hand operand
	 */
	public BinaryArithmeticOperation(
			CriteriaBuilderImpl criteriaBuilder,
			Class<N> resultType,
			Operation operator,
			Expression<? extends N> lhs,
			Expression<? extends N> rhs) {
		super( criteriaBuilder, resultType );
		this.operator = operator;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	/**
	 * Creates an arithmethic operation based on an expression and a literal.
	 *
	 * @param criteriaBuilder The builder for query components.
	 * @param javaType The operation result type
	 * @param operator The operator (type of operation).
	 * @param lhs The left-hand operand
	 * @param rhs The right-hand operand (the literal)
	 */
	public BinaryArithmeticOperation(
			CriteriaBuilderImpl criteriaBuilder,
			Class<N> javaType,
			Operation operator,
			Expression<? extends N> lhs,
			N rhs) {
		super( criteriaBuilder, javaType );
		this.operator = operator;
		this.lhs = lhs;
		this.rhs = new LiteralExpression<N>( criteriaBuilder, rhs );
	}

	/**
	 * Creates an arithmetic operation based on an expression and a literal.
	 *
	 * @param criteriaBuilder The builder for query components.
	 * @param javaType The operation result type
	 * @param operator The operator (type of operation).
	 * @param lhs The left-hand operand (the literal)
	 * @param rhs The right-hand operand
	 */
	public BinaryArithmeticOperation(
			CriteriaBuilderImpl criteriaBuilder,
			Class<N> javaType,
			Operation operator,
			N lhs,
			Expression<? extends N> rhs) {
		super( criteriaBuilder, javaType );
		this.operator = operator;
		this.lhs = new LiteralExpression<N>( criteriaBuilder, lhs );
		this.rhs = rhs;
	}
	public Operation getOperator() {
		return operator;
	}

	/**
	 * {@inheritDoc}
	 */
	public Expression<? extends N> getRightHandOperand() {
		return rhs;
	}

	/**
	 * {@inheritDoc}
	 */
	public Expression<? extends N> getLeftHandOperand() {
		return lhs;
	}

	/**
	 * {@inheritDoc}
	 */
	public void registerParameters(ParameterRegistry registry) {
		Helper.possibleParameter( getRightHandOperand(), registry );
		Helper.possibleParameter( getLeftHandOperand(), registry );
	}

	public String render(CriteriaQueryCompiler.RenderingContext renderingContext) {
		return getOperator().apply(
				( (Renderable) getLeftHandOperand() ).render( renderingContext ),
				( (Renderable) getRightHandOperand() ).render( renderingContext )
		);
	}

	public String renderProjection(CriteriaQueryCompiler.RenderingContext renderingContext) {
		return render( renderingContext );
	}
}
