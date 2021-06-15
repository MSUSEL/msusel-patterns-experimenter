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

import org.geotools.styling.Mark;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class MarkBuilder extends AbstractStyleBuilder<Mark> {
    StrokeBuilder strokeBuilder = new StrokeBuilder(this).unset();

    FillBuilder fill = new FillBuilder(this).unset();

    ExternalMarkBuilder externalMark = new ExternalMarkBuilder(this);

    Expression wellKnownName;

    public MarkBuilder() {
        this(null);
    }

    MarkBuilder(GraphicBuilder parent) {
        super(parent);
        reset();
    }

    public MarkBuilder name(Expression name) {
        this.wellKnownName = name;
        this.externalMark.unset();
        return this;
    }

    public MarkBuilder name(String name) {
        return name(literal(name));
    }

    public ExternalMarkBuilder externalMark() {
        return externalMark;
    }

    public StrokeBuilder stroke() {
        return strokeBuilder;
    }

    public FillBuilder fill() {
        return fill;
    }

    public MarkBuilder reset() {
        // TODO: where is the default mark?
        this.wellKnownName = literal("square");
        this.externalMark.unset();
        this.strokeBuilder.reset();
        this.fill.unset();
        this.stroke().unset();
        this.unset = false;

        return this;
    }

    public Mark build() {
        if (unset) {
            return null;
        }

        Mark mark = null;
        if (!externalMark.isUnset()) {
            mark = sf.mark(externalMark.build(), fill.build(), strokeBuilder.build());
        }
        if (wellKnownName != null) {
            mark = sf.mark(wellKnownName, fill.build(), strokeBuilder.build());
        }
        if (parent == null) {
            reset();
        }
        return mark;
    }

    public MarkBuilder reset(Mark mark) {
        return reset((org.opengis.style.Mark) mark);
    }

    public MarkBuilder reset(org.opengis.style.Mark mark) {
        if (mark == null) {
            return unset();
        }
        this.wellKnownName = mark.getWellKnownName();
        this.externalMark.reset(mark.getExternalMark());
        this.strokeBuilder.reset(mark.getStroke());
        this.fill.reset(mark.getFill());
        this.unset = false;

        return this;
    }

    public MarkBuilder unset() {
        return (MarkBuilder) super.unset();
    }

    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().point().graphic().mark().init(this);
    }

}
