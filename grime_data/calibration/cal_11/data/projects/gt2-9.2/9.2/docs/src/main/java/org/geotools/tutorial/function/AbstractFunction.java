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
package org.geotools.tutorial.function;

import java.util.Collections;
import java.util.List;

import org.geotools.util.Converters;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

public abstract class AbstractFunction implements Function {
    protected final FunctionName name;
    protected final List<Expression> params;
    protected final Literal fallback;
    
    protected AbstractFunction(FunctionName name, List<Expression> args, Literal fallback) {
        this.name = name;
        this.params = args;
        this.fallback = fallback;
    }
    public abstract Object evaluate(Object object);
    public <T> T evaluate(Object object, Class<T> context) {
        Object value = evaluate(object);
        return Converters.convert(value, context);
    }
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }
    public String getName() {
        return name.getName();
    }
    public FunctionName getFunctionName() {
        return name;
    }
    public List<Expression> getParameters() {
        return Collections.unmodifiableList(params);
    }
    public Literal getFallbackValue() {
        return fallback;
    }
    // helper methods
    <T> T eval( Object feature, int index, Class<T> type ){
        Expression expr = params.get(index);
        Object value = expr.evaluate( feature, type );        
        return type.cast(value);
    }
}
