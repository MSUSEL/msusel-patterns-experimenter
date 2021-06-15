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

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.Builder;
import org.geotools.styling.PointSymbolizer;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class PointSymbolizerBuilder extends AbstractStyleBuilder<PointSymbolizer> {
    Expression geometry;

    GraphicBuilder graphic = new GraphicBuilder(this).unset();

    Unit<Length> uom = null;

    public PointSymbolizerBuilder() {
        this(null);
    }

    public PointSymbolizerBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public PointSymbolizerBuilder geometry(Expression geometry) {
        this.geometry = geometry;
        return this;
    }

    public PointSymbolizerBuilder geometry(String cqlExpression) {
        return geometry(cqlExpression(cqlExpression));
    }

    public GraphicBuilder graphic() {
        unset = false;
        return graphic;
    }

    public PointSymbolizerBuilder uom(Unit<Length> uom) {
        this.uom = uom;
        return this;
    }

    public PointSymbolizer build() {
        if (unset) {
            return null;
        }
        PointSymbolizer ps = sf.createPointSymbolizer();
        ps.setGeometry(geometry);
        ps.setGraphic(graphic.build());
        if (uom != null) {
            ps.setUnitOfMeasure(uom);
        }
        if (parent == null) {
            reset();
        }
        return ps;
    }

    public PointSymbolizerBuilder reset() {
        this.geometry = null;
        this.graphic.reset(); // TODO: See what the actual default is
        this.uom = null;
        unset = false;

        return this;
    }

    public Builder<PointSymbolizer> reset(PointSymbolizer original) {
        if (original == null) {
            return unset();
        }
        this.geometry = original.getGeometry();
        this.graphic.reset(original.getGraphic());
        this.uom = original.getUnitOfMeasure();
        unset = false;

        return this;
    }

    public Builder<PointSymbolizer> reset(org.opengis.style.PointSymbolizer original) {
        if (original == null) {
            return unset();
        } else if (original instanceof PointSymbolizer) {
            return reset((PointSymbolizer) original);
        }
        this.geometry = property(original.getGeometryPropertyName());
        this.graphic.reset(original.getGraphic());
        this.uom = original.getUnitOfMeasure();
        unset = false;

        return this;
    }

    public PointSymbolizerBuilder unset() {
        return (PointSymbolizerBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().point().reset(this.build());
    }
}
