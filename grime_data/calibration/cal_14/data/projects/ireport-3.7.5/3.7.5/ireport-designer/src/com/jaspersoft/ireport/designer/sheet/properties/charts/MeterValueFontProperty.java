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
package com.jaspersoft.ireport.designer.sheet.properties.charts;

import com.jaspersoft.ireport.designer.sheet.properties.AbstractFontProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
import net.sf.jasperreports.charts.design.JRDesignValueDisplay;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.design.JasperDesign;
    
    
/**
 * Class to manage the JRDesignValueDisplay.PROPERTY_FONT
 */
public final class MeterValueFontProperty extends AbstractFontProperty
{
    private final JRDesignMeterPlot plot;
        
    public MeterValueFontProperty(JRDesignMeterPlot plot, JasperDesign jasperDesign)
    {
        super(plot, jasperDesign);
        this.plot = plot;
    }
    
    @Override
    public String getName()
    {
        return JRDesignValueDisplay.PROPERTY_FONT;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Value_Font");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Value_Font");
    }

    @Override
    public JRFont getFont()
    {
        return plot.getValueDisplay() == null ? null : plot.getValueDisplay().getFont();
    }

    @Override
    public void setFont(JRFont font)
    {
        JRDesignValueDisplay newValue = 
            new JRDesignValueDisplay(plot.getValueDisplay(), plot.getChart());
        newValue.setFont(font);
        plot.setValueDisplay(newValue);
    }

}
