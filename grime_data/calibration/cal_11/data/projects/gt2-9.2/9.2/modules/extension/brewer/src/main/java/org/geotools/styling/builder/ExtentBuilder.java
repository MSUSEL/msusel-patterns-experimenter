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
import org.geotools.styling.Extent;
import org.geotools.styling.StyleFactory;

/**
 * 
 *
 * @source $URL$
 */
public class ExtentBuilder<P> implements Builder<Extent> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    boolean unset = true; // current value is null

    private String name;

    private String value;

    public ExtentBuilder() {
        this(null);
    }

    public ExtentBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public Extent build() {
        if (unset) {
            return null;
        }
        Extent extent = sf.createExtent(name, value);
        return extent;
    }

    public P end() {
        return parent;
    }

    public ExtentBuilder<P> reset() {
        this.name = null;
        this.value = null;
        unset = false;
        return this;
    }

    public ExtentBuilder<P> reset(Extent extent) {
        if (extent == null) {
            return reset();
        }
        this.value = extent.getValue();
        this.name = extent.getName();
        unset = false;
        return this;
    }

    public ExtentBuilder<P> unset() {
        this.name = null;
        this.value = null;
        unset = true;
        return this;
    }

}
