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
package com.jaspersoft.ireport.components.barcode.barbecue;

import com.jaspersoft.ireport.designer.sheet.properties.IntegerProperty;
import net.sf.jasperreports.components.barbecue.StandardBarbecueComponent;

/**
 *
 * @author gtoffoli
 */
class BarbecueBarHeightProperty extends IntegerProperty {

    private StandardBarbecueComponent component = null;
    public BarbecueBarHeightProperty(StandardBarbecueComponent component) {
        super(component);
        this.component = component;
        setName(StandardBarbecueComponent.PROPERTY_BAR_HEIGTH);
        setDisplayName("Bar Height");
    }

    @Override
    public Integer getInteger() {
        return (component.getBarHeight() == null) ? new Integer(0) : component.getBarHeight();
    }

    @Override
    public Integer getOwnInteger() {
        return component.getBarHeight();
    }

    @Override
    public Integer getDefaultInteger() {
        return new Integer(0);
    }

    @Override
    public void validateInteger(Integer value) {
        if (value != null && value.intValue() < 0)
        {
            throw new IllegalArgumentException("Bar height must be a positive number");
        }
    }

    @Override
    public void setInteger(Integer value) {
        if (value != null && value.intValue() <= 0) value = new Integer(0);
        component.setBarHeight(value);
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }


}
