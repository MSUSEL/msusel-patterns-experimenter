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

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.opengis.filter.FilterFactory2;

abstract class AbstractSLDBuilder<T> implements Builder<T> {

    protected StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    protected AbstractSLDBuilder<?> parent;

    protected static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2(null);

    protected boolean unset = false;

    public AbstractSLDBuilder(AbstractSLDBuilder<?> parent) {
        this.parent = parent;
    }

    public StyledLayerDescriptor buildSLD() {
        if (parent != null) {
            return parent.buildSLD();
        } else {
            StyledLayerDescriptorBuilder sb = new StyledLayerDescriptorBuilder();
            buildSLDInternal(sb);
            return sb.buildSLD();
        }
    }

    public Object buildRoot() {
        if (parent != null) {
            return parent.build();
        } else {
            return build();
        }
    }

    protected abstract void buildSLDInternal(StyledLayerDescriptorBuilder sb);

    protected void init(Builder<T> other) {
        reset(other.build());
    }

    public AbstractSLDBuilder<T> unset() {
        reset();
        unset = true;
        return this;
    }

    boolean isUnset() {
        return unset;
    }
}
