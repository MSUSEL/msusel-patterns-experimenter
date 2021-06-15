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
package org.geotools.filter.expression;

import org.geotools.Builder;
import org.opengis.filter.expression.Expression;

/**
 * Child expression builder; suitable for use collecting function parameters and binary expression
 * arguments.
 * <p>
 * This builder is designed to be "chained" from a parent builder; you may return to the parent
 * builder at any time by calling end().
 * 
 * @param <P>
 *            parent builder
 *
 *
 *
 * @source $URL$
 */
public class ChildExpressionBuilder<P extends Builder<?>> extends ExpressionBuilder {

    private P parent;

    public ChildExpressionBuilder(P parent) {
        this.parent = parent;
    }
    public ChildExpressionBuilder(P parent, Expression expr1) {
        super(expr1);
        this.parent = parent;
    }
    @Override
    public ChildExpressionBuilder<P>  reset() {
        super.reset();
        return this;
    }
    @Override
    public ChildExpressionBuilder<P> reset(Expression original) {
        super.reset(original);
        return this;
    }
    @Override
    public ChildExpressionBuilder<P> unset() {
        super.unset();
        return this;
    }

    /**
     * Build the parameter; adding it to the parent.
     * <p>
     * When using this from another builder you may wish to override the this build() method as
     * shown below:
     * 
     * <pre>
     * final Expression array[] = ...
     * ChildExpressionBuilder first = new ChildExpressionBuilder&lt;?&gt;(this) {            
     *      public Expression build() {
     *          array[0] = super.build();
     *          return array[0];
     *      }
     *  };
     * }
     * </pre>
     * 
     * @return internal expression
     */
    public Expression build() {
        if (unset) {
            return null;
        }
        return delegate.build();
    }

    /**
     * Build the expression and return to the parent builder.
     * <p>
     * Example use:<code>b.add().expr1().literal(1).end().expr2().literal(2).end().build();</code>
     * 
     * @see _build()
     * @return
     */
    public P end() {
        build();
        return parent;
    }

    /**
     * Inline literal value.
     * <p>
     * Example:<code>b.literal( 1 );</code>
     * 
     * @param obj
     *            Object to use as the resulting literal
     */
    public P literal(Object obj) {
        literal().value(obj);
        return end();
    }

    /**
     * Inline property name value.
     * <p>
     * Example:<code>b.property("x");</code>
     */
    public P property(String xpath) {
        property().property(xpath);
        return end();
    }

}
