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
package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.styling.Style;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

abstract class AbstractStyleBuilder<T> extends AbstractSLDBuilder<T> {

    public AbstractStyleBuilder(AbstractSLDBuilder<?> parent) {
        super(parent);
    }

    protected Expression literal(Object literal) {
        return FF.literal(literal);
    }

    protected Expression property(String name) {
        return FF.property(name);
    }

    protected Expression cqlExpression(String cql) {
        try {
            return ECQL.toExpression(cql);
        } catch (CQLException e) {
            // failed to parse as ecql, attempt to fall back on to CQL
            try {
                return CQL.toExpression(cql);
            } catch (CQLException e1) {
                // throw back original exception
            }
            throw new RuntimeException("Failed to build an expression out of CQL", e);
        }
    }

    protected Filter cqlFilter(String cql) {
        try {
            return ECQL.toFilter(cql);
        } catch (CQLException e) {
            // failed to parse as ecql, attempt to fall back on to CQL
            try {
                return CQL.toFilter(cql);
            } catch (CQLException e1) {
                // throw back original exception
            }
            throw new RuntimeException("Failed to build a filter out of CQL", e);
        }
    }

    @Override
    protected void buildSLDInternal(StyledLayerDescriptorBuilder sb) {
        // sb -> user layer -> user style -> init this
        throw new UnsupportedOperationException("Implementation missing");
    }

    public Style buildStyle() {
        if (parent != null && parent instanceof AbstractStyleBuilder) {
            return ((AbstractStyleBuilder) parent).buildStyle();
        } else {
            StyleBuilder sb = new StyleBuilder();
            buildStyleInternal(sb);
            return sb.buildStyle();
        }
    }

    public Object buildRoot() {
        if (parent != null) {
            return parent.build();
        } else {
            return build();
        }
    }

    protected abstract void buildStyleInternal(StyleBuilder sb);

    protected void init(Builder<T> other) {
        reset(other.build());
    }

    public AbstractStyleBuilder<T> unset() {
        reset();
        unset = true;
        return this;
    }

    boolean isUnset() {
        return unset;
    }
}
