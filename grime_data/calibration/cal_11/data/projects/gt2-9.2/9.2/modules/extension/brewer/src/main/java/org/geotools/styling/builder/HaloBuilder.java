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

import org.geotools.styling.Halo;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class HaloBuilder extends AbstractStyleBuilder<org.opengis.style.Halo> {
    Expression radius;

    FillBuilder fill = new FillBuilder(this);

    public HaloBuilder() {
        this(null);
    }

    public HaloBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    /**
     * Set the HaloBuilder to produce <code>node</code>
     * 
     * @return current HaloBuilder for chaining operations
     */
    public HaloBuilder unset() {
        return (HaloBuilder) super.unset();
    }

    /**
     * Set the HaloBuilder
     * <P>
     * to produce a default Halo.
     * 
     * @return current HaloBuilder
     *         <P>
     *         for chaining operations
     */
    public HaloBuilder reset() {
        unset = false; //
        radius = literal(0);
        fill.reset();

        return this;
    }

    /**
     * Set the HaloBuilder to produce the provided Halo.
     * 
     * @param halo Halo under construction; if null HaloBuilder will be unset()
     * @return current HaloBuilder for chaining operations
     */
    public HaloBuilder reset(org.opengis.style.Halo halo) {
        if (halo == null) {
            return unset();
        }
        fill = new FillBuilder(this).reset(halo.getFill());
        radius = halo.getRadius();

        return this;
    }

    public HaloBuilder radius(Expression radius) {
        unset = false;
        this.radius = radius;
        return this;
    }

    public HaloBuilder radius(double radius) {
        return radius(literal(radius));
    }

    public HaloBuilder radius(String cqlExpression) {
        return radius(cqlExpression(cqlExpression));
    }

    public FillBuilder fill() {
        unset = false;
        return fill;
    }

    public Halo build() {
        if (unset)
            return null;

        Halo halo = sf.createHalo(fill.build(), radius);
        if (parent == null)
            reset();

        return halo;
    }

    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().halo().init(this);
    }

}
