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
import java.util.List;

import org.geotools.styling.ColorReplacement;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class ColorReplacementBuilder extends AbstractStyleBuilder<ColorReplacement> {
    private Expression propertyName;

    private List<Expression> mapping = new ArrayList<Expression>();

    public ColorReplacementBuilder() {
        this(null);
    }

    public ColorReplacementBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public ColorReplacement build() {
        if (unset) {
            return null;
        }
        Expression array[] = mapping.toArray(new Expression[mapping.size()]);
        ColorReplacement replacement = sf.colorReplacement(propertyName, array);
        if (parent == null) {
            reset();
        }
        return replacement;
    }

    public ColorReplacementBuilder reset() {
        propertyName = property("Raster");
        mapping.clear();
        unset = false;
        return this;
    }

    @Override
    public ColorReplacementBuilder reset(ColorReplacement original) {
        return reset((org.opengis.style.ColorReplacement) original);
    }

    public ColorReplacementBuilder reset(org.opengis.style.ColorReplacement replacement) {
        if (replacement == null) {
            return unset();
        }
        mapping.clear();
        if (replacement.getRecoding() != null
                && replacement.getRecoding().getParameters().size() > 0) {
            List<Expression> params = replacement.getRecoding().getParameters();
            propertyName = params.get(0);
            for (int i = 0; i < params.size(); i++) {
                mapping.add(params.get(i));
            }
        }

        unset = false;
        return this;
    }

    public ColorReplacementBuilder unset() {
        return (ColorReplacementBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        // TODO: build a raster style
        throw new UnsupportedOperationException("Can't build a style out of a color replacement");
    }

}
