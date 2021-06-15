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

import javax.swing.Icon;

import org.geotools.styling.ExternalMark;
import org.opengis.metadata.citation.OnLineResource;

/**
 * 
 *
 * @source $URL$
 */
public class ExternalMarkBuilder extends AbstractStyleBuilder<ExternalMark> {
    private Icon inline;

    private String format;

    private int index;

    private OnLineResource resource;

    public ExternalMarkBuilder() {
        this(null);
    }

    public ExternalMarkBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public ExternalMarkBuilder inline(Icon icon) {
        this.unset = false;
        this.inline = icon;
        return this;
    }

    public ExternalMarkBuilder format(String format) {
        this.unset = false;
        this.format = format;
        return this;
    }

    public ExternalMarkBuilder index(int index) {
        this.unset = false;
        this.index = index;
        return this;
    }

    public ExternalMarkBuilder resouce(OnLineResource resource) {
        this.unset = false;
        this.resource = resource;
        return this;
    }

    public ExternalMark build() {
        if (unset) {
            return null;
        }
        if (inline != null) {
            return sf.externalMark(inline);
        } else {
            return sf.externalMark(resource, format, index);
        }
    }

    public ExternalMarkBuilder reset() {
        this.format = null;
        this.resource = null;
        this.index = 0;
        this.inline = null;
        unset = false;
        return this;
    }

    public ExternalMarkBuilder reset(org.opengis.style.ExternalMark mark) {
        if (mark == null) {
            return reset();
        }
        this.format = mark.getFormat();
        this.index = mark.getMarkIndex();
        this.inline = mark.getInlineContent();
        this.resource = mark.getOnlineResource();
        this.unset = false;

        return this;
    }

    public ExternalMarkBuilder reset(ExternalMark mark) {
        if (mark == null) {
            return reset();
        }
        this.format = mark.getFormat();
        this.index = mark.getMarkIndex();
        this.inline = mark.getInlineContent();
        this.resource = mark.getOnlineResource();
        this.unset = false;

        return this;
    }

    public ExternalMarkBuilder unset() {
        return (ExternalMarkBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().point().graphic().mark().externalMark().init(this);

    }

}
