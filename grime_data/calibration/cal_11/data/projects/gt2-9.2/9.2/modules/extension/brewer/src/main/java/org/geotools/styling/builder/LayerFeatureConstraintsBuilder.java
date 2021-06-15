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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.LayerFeatureConstraints;
import org.geotools.styling.StyleFactory;

/**
 * 
 *
 * @source $URL$
 */
public class LayerFeatureConstraintsBuilder<P> implements Builder<LayerFeatureConstraints> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    private ChildExpressionBuilder<LayerFeatureConstraintsBuilder<P>> x = new ChildExpressionBuilder<LayerFeatureConstraintsBuilder<P>>(
            this);

    private ChildExpressionBuilder<LayerFeatureConstraintsBuilder<P>> y = new ChildExpressionBuilder<LayerFeatureConstraintsBuilder<P>>(
            this);

    boolean unset = true; // current value is null

    public LayerFeatureConstraintsBuilder() {
        this(null);
    }

    public LayerFeatureConstraintsBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public LayerFeatureConstraints build() {
        if (unset) {
            return null;
        }
        FeatureTypeConstraint[] featureTypeConstraints = null;
        LayerFeatureConstraints constraints = sf
                .createLayerFeatureConstraints(featureTypeConstraints);
        return constraints;
    }

    public P end() {
        return parent;
    }

    public LayerFeatureConstraintsBuilder<P> reset() {
        x.reset().literal(0);
        y.reset().literal(0);
        unset = false;
        return this;
    }

    public LayerFeatureConstraintsBuilder<P> reset(LayerFeatureConstraints constraints) {
        if (constraints == null) {
            return reset();
        }

        unset = false;
        return this;
    }

    public LayerFeatureConstraintsBuilder<P> unset() {
        x.unset();
        y.unset();
        unset = true;
        return this;
    }

}
