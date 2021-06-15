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
import net.sf.jasperreports.components.barcode4j.BarcodeComponent;
import net.sf.jasperreports.components.barcode4j.Code39Component;
import net.sf.jasperreports.components.barcode4j.FourStateBarcodeComponent;
import net.sf.jasperreports.components.barcode4j.POSTNETComponent;


public final class Barcode4JIntercharGapWidthProperty  extends DoubleProperty
{
        private BarcodeComponent component;

        @SuppressWarnings("unchecked")
        public Barcode4JIntercharGapWidthProperty(BarcodeComponent component)
        {
           super(component);
           this.component = component;
           setName(FourStateBarcodeComponent.PROPERTY_INTERCHAR_GAP_WIDTH);
           setDisplayName(I18n.getString("barcode4j.property.intercharGapWidth.name"));
           setShortDescription(I18n.getString("barcode4j.property.intercharGapWidth.description"));
        }



    @Override
    public Double getDouble() {
        return getComponentValue();
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
        setComponentValue(value);
    }


    public Double getComponentValue()
    {
        if (component instanceof FourStateBarcodeComponent)
        {
            return ((FourStateBarcodeComponent)component).getIntercharGapWidth();
        }
        else if (component instanceof Code39Component)
        {
            return ((Code39Component)component).getIntercharGapWidth();
        }
        else if (component instanceof POSTNETComponent)
        {
            return ((POSTNETComponent)component).getIntercharGapWidth();
        }
        return null;
    }

    public void setComponentValue(Double d)
    {
        if (component instanceof FourStateBarcodeComponent)
        {
            ((FourStateBarcodeComponent)component).setIntercharGapWidth(d);
        }
        else if (component instanceof Code39Component)
        {
            ((Code39Component)component).setIntercharGapWidth(d);
        }
        else if (component instanceof POSTNETComponent)
        {
            ((POSTNETComponent)component).setIntercharGapWidth(d);
        }
    }
}

