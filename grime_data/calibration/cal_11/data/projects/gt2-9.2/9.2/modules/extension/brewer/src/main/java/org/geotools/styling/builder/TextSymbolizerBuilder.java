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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.Builder;
import org.geotools.styling.Font;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class TextSymbolizerBuilder extends AbstractStyleBuilder<TextSymbolizer> {
    FillBuilder fill = new FillBuilder(this).unset();

    List<FontBuilder> fonts = new ArrayList<FontBuilder>();

    FontBuilder font;

    HaloBuilder halo = new HaloBuilder(this).unset();

    Expression label;

    String geometry;

    Unit<Length> uom;

    Builder<? extends LabelPlacement> placement = new PointPlacementBuilder(this).unset();

    private Map<String, String> options = new HashMap<String, String>();

    public TextSymbolizerBuilder() {
        this(null);
    }

    public TextSymbolizerBuilder(RuleBuilder parent) {
        super(parent);
        reset();
    }

    public TextSymbolizerBuilder geometry(String geometry) {
        this.geometry = geometry;
        return this;
    }

    public HaloBuilder halo() {
        unset = false;
        return halo;
    }

    public FillBuilder fill() {
        unset = false;
        return fill;
    }

    public FontBuilder newFont() {
        unset = false;
        FontBuilder font = new FontBuilder(this);
        fonts.add(font);
        return font;
    }

    public LinePlacementBuilder linePlacement() {
        if (!(placement instanceof LinePlacementBuilder)) {
            placement = new LinePlacementBuilder(this);
        }
        unset = false;
        return (LinePlacementBuilder) placement;
    }

    public PointPlacementBuilder pointPlacement() {
        if (!(placement instanceof PointPlacementBuilder)) {
            placement = new PointPlacementBuilder(this);
        }
        unset = false;
        return (PointPlacementBuilder) placement;
    }

    public TextSymbolizerBuilder uom(Unit<Length> uom) {
        unset = false;
        this.uom = uom;
        return this;
    }

    public TextSymbolizer build() {
        if (unset) {
            return null;
        }
        Font[] array = new Font[fonts.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = fonts.get(i).build();
        }
        TextSymbolizer ts = sf.createTextSymbolizer(fill.build(), array, halo.build(), label,
                placement.build(), geometry);
        if (uom != null) {
            ts.setUnitOfMeasure(uom);
        }
        if (ts instanceof TextSymbolizer2 && options != null) {
            TextSymbolizer2 ts2 = (TextSymbolizer2) ts;
            ts2.getOptions().putAll(options);
        }
        reset();
        return ts;
    }

    public TextSymbolizerBuilder unset() {
        return (TextSymbolizerBuilder) super.unset();
    }

    public TextSymbolizerBuilder reset() {
        fill.reset(); // TODO: default fill for text?
        halo.unset(); // no default halo
        label = null;
        geometry = null;
        placement.reset();
        options.clear();
        uom = null;
        unset = false;
        return this;
    }

    public TextSymbolizerBuilder reset(TextSymbolizer symbolizer) {
        fill.reset(symbolizer.getFill());
        halo.reset(symbolizer.getHalo());
        label = symbolizer.getLabel();
        geometry = symbolizer.getGeometryPropertyName();
        LabelPlacement otherPlacement = symbolizer.getLabelPlacement();
        if (symbolizer.getLabelPlacement() instanceof PointPlacement) {
            PointPlacementBuilder builder = new PointPlacementBuilder(this);
            builder.reset((PointPlacement) otherPlacement);
            placement = builder;
        } else if (symbolizer.getLabelPlacement() instanceof LabelPlacement) {
            LinePlacementBuilder builder = new LinePlacementBuilder(this);
            builder.reset((LinePlacement) otherPlacement);
            placement = builder;
        } else {
            throw new IllegalArgumentException("Unrecognized label placement: " + otherPlacement);
        }
        unset = false;
        return this;
    }

    public TextSymbolizerBuilder option(String name, Object value) {
        options.put(name, value.toString());
        unset = false;
        return this;
    }

    public TextSymbolizerBuilder label(Expression label) {
        unset = false;
        this.label = label;
        return this;
    }

    public TextSymbolizerBuilder label(String cqlExpression) {
        return label(cqlExpression(cqlExpression));
    }

    public TextSymbolizerBuilder labelText(String text) {
        return label(literal(text));
    }

    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().init(this);
    }

}
