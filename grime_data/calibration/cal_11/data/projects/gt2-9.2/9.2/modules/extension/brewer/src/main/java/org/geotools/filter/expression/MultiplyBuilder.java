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
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Multiply;


/**
 * 
 *
 * @source $URL$
 */
public class MultiplyBuilder implements Builder<Multiply> {

    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    boolean unset = false;

    ChildExpressionBuilder<MultiplyBuilder> expr1;

    ChildExpressionBuilder<MultiplyBuilder> expr2;

    public MultiplyBuilder() {
        reset();
    }

    public MultiplyBuilder(Multiply expression) {
        reset(expression);
    }

    public MultiplyBuilder reset() {
        unset = false;
        expr1 = new ChildExpressionBuilder<MultiplyBuilder>(this);
        expr2 = new ChildExpressionBuilder<MultiplyBuilder>(this);
        return this;
    }

    public MultiplyBuilder reset(Multiply original) {
        unset = false;
        expr1 = new ChildExpressionBuilder<MultiplyBuilder>(this, original.getExpression1());
        expr2 = new ChildExpressionBuilder<MultiplyBuilder>(this, original.getExpression2());
        return this;
    }

    public MultiplyBuilder unset() {
        unset = true;
        expr1 = new ChildExpressionBuilder<MultiplyBuilder>(this).unset();
        expr2 = null;
        return this;
    }

    public Multiply build() {
        if (unset) {
            return null;
        }
        return ff.multiply(expr1.build(), expr2.build());
    }

    public ChildExpressionBuilder<MultiplyBuilder> expr1() {
        return expr1();
    }

    public MultiplyBuilder expr1(Object literal) {
        expr1.literal(literal);
        return this;
    }

    public ChildExpressionBuilder<MultiplyBuilder> expr2() {
        return expr2;
    }

    public MultiplyBuilder expr2(Object literal) {
        expr2.literal(literal);
        return this;
    }
}
