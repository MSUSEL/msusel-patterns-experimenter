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

import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Stroke;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class LineSymbolizerBuilder extends AbstractStyleBuilder<LineSymbolizer> {
    StrokeBuilder strokeBuilder = new StrokeBuilder(this);

    Expression geometry = null;

    Unit<Length> uom = null;

    public LineSymbolizerBuilder() {
        this(null);
    }

    LineSymbolizerBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public LineSymbolizerBuilder geometry(Expression geometry) {
        this.geometry = geometry;
        return this;
    }

    public LineSymbolizerBuilder geometry(String cqlExpression) {
        return geometry(cqlExpression(cqlExpression));
    }

    public StrokeBuilder stroke() {
        unset = false;
        return strokeBuilder;
    }

    public LineSymbolizerBuilder uom(Unit<Length> uom) {
        unset = false;
        this.uom = uom;
        return this;
    }

    public LineSymbolizer build() {
        if (unset) {
            return null; // builder was constructed but never used
        }
        Stroke stroke = strokeBuilder.build();
        if (stroke == null) {
            stroke = Stroke.DEFAULT;
        }
        LineSymbolizer ls = sf.createLineSymbolizer(stroke, null);
        if (geometry != null) {
            ls.setGeometry(geometry);
        }
        if (uom != null) {
            ls.setUnitOfMeasure(uom);
        }
        if (parent == null) {
            reset();
        }
        return ls;
    }

    public LineSymbolizerBuilder reset() {
        strokeBuilder.reset();
        geometry = null;
        unset = false;
        uom = null;
        return this;
    }

    public LineSymbolizerBuilder reset(LineSymbolizer original) {
        if (original == null) {
            return unset();
        }
        geometry = original.getGeometry();
        strokeBuilder.reset(original.getStroke());
        uom = original.getUnitOfMeasure();
        return this;
    }

    public LineSymbolizerBuilder reset(org.opengis.style.LineSymbolizer original) {
        if (original instanceof LineSymbolizer) {
            return reset((LineSymbolizer) original);
        }
        if (original == null) {
            return unset();
        }
        geometry = property(original.getGeometryPropertyName());
        strokeBuilder.reset(original.getStroke());
        uom = original.getUnitOfMeasure();
        return this;
    }

    public LineSymbolizerBuilder unset() {
        return (LineSymbolizerBuilder) super.unset();
    }

    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().line().init(this);
    }

}
