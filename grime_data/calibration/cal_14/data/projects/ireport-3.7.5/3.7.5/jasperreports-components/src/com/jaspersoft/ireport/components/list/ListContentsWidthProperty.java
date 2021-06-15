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
package com.jaspersoft.ireport.components.list;

import com.jaspersoft.ireport.designer.sheet.properties.IntegerProperty;
import net.sf.jasperreports.components.list.DesignListContents;

/**
 *
 * @author gtoffoli
 */
class ListContentsWidthProperty extends IntegerProperty {

    private DesignListContents contents = null;
    public ListContentsWidthProperty(DesignListContents contents) {
        super(contents);
        this.contents = contents;
        setName("LC" + DesignListContents.PROPERTY_WIDTH);
        setDisplayName("Item width");
    }

    @Override
    public Integer getInteger() {
        return (contents.getWidth() == null) ? new Integer(0) : contents.getWidth();
    }

    @Override
    public Integer getOwnInteger() {
        return getInteger();
    }

    @Override
    public Integer getDefaultInteger() {
        return getInteger();
    }

    @Override
    public void validateInteger(Integer value) {
    }

    @Override
    public void setInteger(Integer value) {
        if (value != null)
        contents.setWidth(value.intValue());
    }

    @Override
    public boolean supportsDefaultValue() {
        return false;
    }


}
