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
package com.jaspersoft.ireport.components.barcode.barcode4j;

import com.jaspersoft.ireport.designer.sheet.properties.DoubleProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.barcode4j.POSTNETComponent;


public final class Barcode4JShortBarHeightProperty  extends DoubleProperty
{
        private POSTNETComponent component;

        @SuppressWarnings("unchecked")
        public Barcode4JShortBarHeightProperty(POSTNETComponent component)
        {
           super(component);
           this.component = component;
           setName(POSTNETComponent.PROPERTY_SHORT_BAR_HEIGHT);
           setDisplayName(I18n.getString("barcode4j.property.shortBarHeight.name"));
           setShortDescription(I18n.getString("barcode4j.property.shortBarHeight.description"));
        }



    @Override
    public Double getDouble() {
        return component.getShortBarHeight();
    }

    @Override
    public Double getOwnDouble() {
        return getDouble();
    }

    @Override
    public Double getDefaultDouble() {
        return null;
    }

    @Override
    public void validateDouble(Double value) {
//        if (value != null && value.doubleValue() <= 1.0)
//        {
//            throw annotateException("Wide factor must be > 1.0");
//        }
    }

    @Override
    public void setDouble(Double value) {
        component.setShortBarHeight(value);
    }
}

