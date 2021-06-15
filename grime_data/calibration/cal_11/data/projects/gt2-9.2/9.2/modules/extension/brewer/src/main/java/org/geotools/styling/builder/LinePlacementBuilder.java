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

import org.geotools.styling.LinePlacement;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class LinePlacementBuilder extends AbstractStyleBuilder<LinePlacement> {
    private Expression offset;

    private Expression initialGap;

    private Expression gap;

    private boolean repeated;

    private boolean generalizedLine;

    private boolean aligned;

    public LinePlacementBuilder() {
        this(null);
    }

    LinePlacementBuilder(TextSymbolizerBuilder parent) {
        super(parent);
        reset();
    }

    public LinePlacement build() {
        if (unset) {
            return null;
        }
        LinePlacement linePlacement = sf.linePlacement(offset, initialGap, gap, repeated, aligned,
                generalizedLine);
        if (parent == null) {
            reset();
        }
        return linePlacement;
    }

    public LinePlacementBuilder reset() {
        this.aligned = false;
        this.generalizedLine = false;
        this.repeated = false;
        this.gap = literal(0);
        this.initialGap = literal(0);
        this.offset = literal(0);

        unset = false;
        return this;
    }

    public LinePlacementBuilder reset(LinePlacement placement) {
        if (placement == null) {
            return reset();
        }
        this.aligned = placement.isAligned();
        this.generalizedLine = placement.isGeneralizeLine();
        this.repeated = placement.isRepeated();
        this.gap = placement.getGap();
        this.initialGap = placement.getInitialGap();
        this.offset = placement.getPerpendicularOffset();

        unset = false;
        return this;
    }

    public LinePlacementBuilder unset() {
        return (LinePlacementBuilder) super.unset();
    }

    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().labelText("label").linePlacement().init(this);
    }

}
