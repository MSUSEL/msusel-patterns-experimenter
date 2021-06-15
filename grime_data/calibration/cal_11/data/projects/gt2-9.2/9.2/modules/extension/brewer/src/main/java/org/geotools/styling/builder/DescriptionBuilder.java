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

import org.geotools.styling.Description;
import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;

/**
 * 
 *
 * @source $URL$
 */
public class DescriptionBuilder extends AbstractStyleBuilder<Description> {

    private InternationalString title;

    private InternationalString description;

    public DescriptionBuilder() {
        this(null);
    }

    public DescriptionBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public Description build() {
        if (unset) {
            return null;
        }
        Description descript = sf.description(title, description);
        if (parent == null) {
            reset();
        }
        return descript;
    }

    public DescriptionBuilder reset() {
        unset = false;
        title = null;
        description = null;
        return this;
    }

    public DescriptionBuilder title(InternationalString title) {
        this.title = title;
        unset = false;
        return this;
    }

    public DescriptionBuilder title(String title) {
        return title(new SimpleInternationalString(title));
    }

    public DescriptionBuilder description(InternationalString description) {
        this.description = description;
        unset = false;
        return this;
    }

    public DescriptionBuilder description(String description) {
        return description(new SimpleInternationalString(description));
    }

    public DescriptionBuilder reset(Description original) {
        unset = false;
        title = original.getTitle();
        description = original.getAbstract();
        return this;
    }

    public DescriptionBuilder unset() {
        unset = true;
        title = null;
        description = null;
        return this;
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        throw new UnsupportedOperationException(
                "Does not make sense to build a style out of a description");

    }

}
