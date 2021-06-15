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
import org.opengis.filter.expression.Divide;

/**
 * 
 *
 * @source $URL$
 */
public class DivideBuilder implements Builder<Divide> {

    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    boolean unset = false;

    ChildExpressionBuilder<DivideBuilder> expr1;

    ChildExpressionBuilder<DivideBuilder> expr2;

    public DivideBuilder() {
        reset();
    }

    public DivideBuilder(Divide expression) {
        reset(expression);
    }

    public DivideBuilder reset() {
        unset = false;
        expr1 = new ChildExpressionBuilder<DivideBuilder>(this);
        expr2 = new ChildExpressionBuilder<DivideBuilder>(this);
        return this;
    }

    public DivideBuilder reset(Divide original) {
        unset = false;
        expr1 = new ChildExpressionBuilder<DivideBuilder>(this, original.getExpression1());
        expr2 = new ChildExpressionBuilder<DivideBuilder>(this, original.getExpression2());
        return this;
    }

    public DivideBuilder unset() {
        unset = true;
        expr1 = new ChildExpressionBuilder<DivideBuilder>(this).unset();
        expr2 = null;
        return this;
    }

    public Divide build() {
        if (unset) {
            return null;
        }
        return ff.divide(expr1.build(), expr2.build());
    }

    public ChildExpressionBuilder<DivideBuilder> expr1() {
        return expr1();
    }

    public DivideBuilder expr1(Object literal) {
        expr1.literal(literal);
        return this;
    }

    public ChildExpressionBuilder<DivideBuilder> expr2() {
        return expr2;
    }

    public DivideBuilder expr2(Object literal) {
        expr2.literal(literal);
        return this;
    }
}
