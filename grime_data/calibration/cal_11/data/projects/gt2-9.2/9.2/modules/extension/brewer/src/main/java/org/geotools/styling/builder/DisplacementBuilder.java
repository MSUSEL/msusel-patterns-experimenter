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

import org.geotools.styling.Displacement;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class DisplacementBuilder extends AbstractStyleBuilder<Displacement> {
    private Expression x = null;

    private Expression y = null;

    public DisplacementBuilder() {
        this(null);
    }

    public DisplacementBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public Displacement build() {
        if (unset) {
            return null;
        }
        Displacement displacement = sf.displacement(x, y);
        return displacement;
    }

    public DisplacementBuilder x(Expression x) {
        this.x = x;
        return this;
    }

    public DisplacementBuilder x(double x) {
        return x(literal(x));
    }

    public DisplacementBuilder x(String cqlExpression) {
        return x(cqlExpression(cqlExpression));
    }

    public DisplacementBuilder y(Expression y) {
        this.y = y;
        return this;
    }

    public DisplacementBuilder y(double y) {
        return y(literal(y));
    }

    public DisplacementBuilder y(String cqlExpression) {
        return y(cqlExpression(cqlExpression));
    }

    public DisplacementBuilder reset() {
        x = literal(0);
        y = literal(0);
        unset = false;
        return this;
    }

    public DisplacementBuilder reset(Displacement displacement) {
        if (displacement == null) {
            return reset();
        }
        x = literal(displacement.getDisplacementX());
        y = literal(displacement.getDisplacementY());
        unset = false;
        return this;
    }

    public DisplacementBuilder unset() {
        return (DisplacementBuilder) super.unset();
    }

    public DisplacementBuilder reset(org.opengis.style.Displacement displacement) {
        if (displacement == null) {
            return reset();
        }
        x = displacement.getDisplacementX();
        y = displacement.getDisplacementY();
        unset = false;
        return this;
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().labelText("label").pointPlacement()
                .displacement().init(this);
    }

}
