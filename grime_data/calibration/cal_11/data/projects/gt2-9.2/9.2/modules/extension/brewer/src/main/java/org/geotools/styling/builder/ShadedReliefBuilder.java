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

import org.geotools.styling.ShadedRelief;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class ShadedReliefBuilder extends AbstractStyleBuilder<ShadedRelief> {
    private Expression factor;

    private boolean brightnessOnly;

    public ShadedReliefBuilder() {
        this(null);
    }

    public ShadedReliefBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public ShadedReliefBuilder factor(Expression factor) {
        this.factor = factor;
        unset = false;
        return this;
    }

    public ShadedReliefBuilder factor(double factor) {
        return factor(literal(factor));
    }

    public ShadedReliefBuilder factor(String cqlExpression) {
        return factor(cqlExpression(cqlExpression));
    }

    public ShadedReliefBuilder brightnessOnly(boolean brightnessOnly) {
        this.brightnessOnly = brightnessOnly;
        unset = false;
        return this;
    }

    public ShadedRelief build() {
        if (unset) {
            return null;
        }
        ShadedRelief relief = sf.shadedRelief(factor, brightnessOnly);
        return relief;
    }

    public ShadedReliefBuilder reset() {
        factor = literal(0);
        brightnessOnly = false;
        unset = false;
        return this;
    }

    public ShadedReliefBuilder reset(ShadedRelief relief) {
        if (relief == null) {
            return reset();
        }
        brightnessOnly = relief.isBrightnessOnly();
        factor = relief.getReliefFactor();
        unset = false;
        return this;
    }

    public ShadedReliefBuilder unset() {
        return (ShadedReliefBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().raster().shadedRelief().init(this);
    }

}
