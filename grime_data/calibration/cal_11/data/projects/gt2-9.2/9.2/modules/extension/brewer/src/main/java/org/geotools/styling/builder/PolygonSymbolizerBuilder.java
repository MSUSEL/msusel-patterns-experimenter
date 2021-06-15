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

import org.geotools.styling.PolygonSymbolizer;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class PolygonSymbolizerBuilder extends AbstractStyleBuilder<PolygonSymbolizer> {
    StrokeBuilder stroke = new StrokeBuilder(this).unset();

    FillBuilder fill = new FillBuilder(this).unset();

    Expression geometry = null;

    Unit<Length> uom;

    public PolygonSymbolizerBuilder() {
        this(null);
    }

    PolygonSymbolizerBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public PolygonSymbolizerBuilder geometry(Expression geometry) {
        this.geometry = geometry;
        return this;
    }

    public PolygonSymbolizerBuilder geometry(String cqlExpression) {
        return geometry(cqlExpression(cqlExpression));
    }

    public StrokeBuilder stroke() {
        unset = false;
        stroke.reset();
        return stroke;
    }

    public FillBuilder fill() {
        unset = false;
        fill.reset();
        return fill;
    }

    public PolygonSymbolizerBuilder uom(Unit<Length> uom) {
        unset = false;
        this.uom = uom;
        return this;
    }

    public PolygonSymbolizer build() {
        if (unset) {
            return null;
        }
        PolygonSymbolizer ps = sf.createPolygonSymbolizer(stroke.build(), fill.build(), null);
        if (geometry != null) {
            ps.setGeometry(geometry);
        }
        if (uom != null) {
            ps.setUnitOfMeasure(uom);
        }
        if (parent == null) {
            reset();
        }
        return ps;
    }

    public PolygonSymbolizerBuilder reset() {
        stroke.unset();
        fill.unset();
        unset = false;
        return this;
    }

    public PolygonSymbolizerBuilder reset(org.opengis.style.PolygonSymbolizer symbolizer) {
        if (symbolizer == null) {
            return unset();
        }
        if (symbolizer instanceof PolygonSymbolizer) {
            return reset((PolygonSymbolizer) symbolizer);
        }
        stroke.reset(symbolizer.getStroke());
        fill.reset(symbolizer.getFill());
        uom = symbolizer.getUnitOfMeasure();
        geometry = property(symbolizer.getGeometryPropertyName());

        unset = false;
        return this;
    }

    public PolygonSymbolizerBuilder reset(PolygonSymbolizer symbolizer) {
        if (symbolizer == null) {
            return unset();
        }
        stroke.reset(symbolizer.getStroke());
        fill.reset(symbolizer.getFill());
        uom = symbolizer.getUnitOfMeasure();
        geometry = symbolizer.getGeometry();

        unset = false;
        return this;
    }

    public PolygonSymbolizerBuilder unset() {
        return (PolygonSymbolizerBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().polygon().init(this);
    }
}
