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

import org.geotools.styling.PointPlacement;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class PointPlacementBuilder extends AbstractStyleBuilder<PointPlacement> {
    private Expression rotation;

    private AnchorPointBuilder anchor = new AnchorPointBuilder(this).unset();

    private DisplacementBuilder displacement = new DisplacementBuilder(this).unset();

    public PointPlacementBuilder() {
        this(null);
    }

    public PointPlacementBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public PointPlacement build() {
        if (unset) {
            return null;
        }
        PointPlacement placement = sf
                .pointPlacement(anchor.build(), displacement.build(), rotation);
        if (parent == null) {
            reset();
        }
        return placement;
    }

    public AnchorPointBuilder anchor() {
        unset = false;
        return anchor;
    }

    public DisplacementBuilder displacement() {
        unset = false;
        return displacement;
    }

    public PointPlacementBuilder rotation(Expression rotation) {
        this.rotation = rotation;
        return this;
    }

    public PointPlacementBuilder rotation(double rotation) {
        return rotation(literal(rotation));
    }

    public PointPlacementBuilder rotation(String cqlExpression) {
        return rotation(cqlExpression(cqlExpression));
    }

    public PointPlacementBuilder reset() {
        rotation = literal(0);
        anchor.reset();
        displacement.reset();
        unset = false;
        return this;
    }

    public PointPlacementBuilder reset(PointPlacement placement) {
        if (placement == null) {
            return reset();
        }
        rotation = placement.getRotation();
        anchor.reset(placement.getAnchorPoint());
        displacement.reset(placement.getDisplacement());
        unset = false;
        return this;
    }

    public PointPlacementBuilder unset() {
        return (PointPlacementBuilder) super.unset();
    }

    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().labelText("label").pointPlacement().init(this);
    }

}
