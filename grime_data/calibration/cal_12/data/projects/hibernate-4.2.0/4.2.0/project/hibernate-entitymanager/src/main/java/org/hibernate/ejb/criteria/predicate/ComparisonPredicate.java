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
import org.hibernate.ejb.criteria.ValueHandlerFactory;
import org.hibernate.ejb.criteria.expression.BinaryOperatorExpression;
import org.hibernate.ejb.criteria.expression.LiteralExpression;

/**
 * Models a basic relational comparison predicate.
 *
 * @author Steve Ebersole
 */
public class ComparisonPredicate
		extends AbstractSimplePredicate
		implements BinaryOperatorExpression<Boolean>, Serializable {
	private final ComparisonOperator comparisonOperator;
	private final Expression<?> leftHandSide;
	private final Expression<?> rightHandSide;

	public ComparisonPredicate(
			CriteriaBuilderImpl criteriaBuilder,
			ComparisonOperator comparisonOperator,
			Expression<?> leftHandSide,
			Expression<?> rightHandSide) {
		super( criteriaBuilder );
		this.comparisonOperator = comparisonOperator;
		this.leftHandSide = leftHandSide;
		this.rightHandSide = rightHandSide;
	}

	@SuppressWarnings({ "unchecked" })
	public ComparisonPredicate(
			CriteriaBuilderImpl criteriaBuilder,
			ComparisonOperator comparisonOperator,
			Expression<?> leftHandSide,
			Object rightHandSide) {
		super( criteriaBuilder );
		this.comparisonOperator = comparisonOperator;
		this.leftHandSide = leftHandSide;
		if ( ValueHandlerFactory.isNumeric( leftHandSide.getJavaType() ) ) {
			this.rightHandSide = new LiteralExpression(
					criteriaBuilder,
					ValueHandlerFactory.convert( rightHandSide, (Class<Number>) leftHandSide.getJavaType() )
			);
		}
		else {
			this.rightHandSide = new LiteralExpression( criteriaBuilder, rightHandSide );
		}
	}

	@SuppressWarnings( {"unchecked"})
	public <N extends Number> ComparisonPredicate(
			CriteriaBuilderImpl criteriaBuilder,
			ComparisonOperator comparisonOperator,
			Expression<N> leftHandSide,
			Number rightHandSide) {
		super( criteriaBuilder );
		this.comparisonOperator = comparisonOperator;
		this.leftHandSide = leftHandSide;
		Class type = leftHandSide.getJavaType();
		if ( Number.class.equals( type ) ) {
			this.rightHandSide = new LiteralExpression( criteriaBuilder, rightHandSide );
		}
		else {
			N converted = (N) ValueHandlerFactory.convert( rightHandSide, type );
			this.rightHandSide = new LiteralExpression<N>( criteriaBuilder, converted );
		}
	}

	public ComparisonOperator getComparisonOperator() {
		return isNegated()
				? comparisonOperator.negated()
				: comparisonOperator;
	}

	public Expression getLeftHandOperand() {
		return leftHandSide;
	}

	public Expression getRightHandOperand() {
		return rightHandSide;
	}

	public void registerParameters(ParameterRegistry registry) {
		Helper.possibleParameter( getLeftHandOperand(), registry );
		Helper.possibleParameter( getRightHandOperand(), registry );
	}

	/**
	 * Defines the comparison operators.  We could also get away with
	 * only 3 and use negation...
	 */
	public static enum ComparisonOperator {
		EQUAL {
			public ComparisonOperator negated() {
				return NOT_EQUAL;
			}
			public String rendered() {
				return "=";
			}
		},
		NOT_EQUAL {
			public ComparisonOperator negated() {
				return EQUAL;
			}
			public String rendered() {
				return "<>";
			}
		},
		LESS_THAN {
			public ComparisonOperator negated() {
				return GREATER_THAN_OR_EQUAL;
			}
			public String rendered() {
				return "<";
			}
		},
		LESS_THAN_OR_EQUAL {
			public ComparisonOperator negated() {
				return GREATER_THAN;
			}
			public String rendered() {
				return "<=";
			}
		},
		GREATER_THAN {
			public ComparisonOperator negated() {
				return LESS_THAN_OR_EQUAL;
			}
			public String rendered() {
				return ">";
			}
		},
		GREATER_THAN_OR_EQUAL {
			public ComparisonOperator negated() {
				return LESS_THAN;
			}
			public String rendered() {
				return ">=";
			}
		};

		public abstract ComparisonOperator negated();

		public abstract String rendered();
	}

	public String render(CriteriaQueryCompiler.RenderingContext renderingContext) {
		return ( (Renderable) getLeftHandOperand() ).render( renderingContext )
				+ getComparisonOperator().rendered()
				+ ( (Renderable) getRightHandOperand() ).render( renderingContext );
	}

	public String renderProjection(CriteriaQueryCompiler.RenderingContext renderingContext) {
		return render( renderingContext );
	}
}
