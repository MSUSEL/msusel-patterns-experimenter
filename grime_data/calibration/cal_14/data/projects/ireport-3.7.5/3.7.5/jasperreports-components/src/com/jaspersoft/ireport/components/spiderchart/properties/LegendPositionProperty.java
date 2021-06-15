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
package com.jaspersoft.ireport.components.spiderchart.properties;

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.AbstractProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;
    
    
/**
 *  Class to manage the JRBaseChart.PROPERTY_LEGEND_POSITION property
 */
public final class LegendPositionProperty extends AbstractProperty
{
    private final StandardChartSettings chart;

    
    public LegendPositionProperty(StandardChartSettings chart)
    {
        super(EdgeEnum.class, chart);
        this.chart = chart;
    }

    @Override
    public String getName()
    {
        return StandardChartSettings.PROPERTY_LEGEND_POSITION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.LegendPosition");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.LegendPosition");
    }

    public List getTagList() 
    {
        List tags = new ArrayList();
        tags.add(new Tag(EdgeEnum.TOP, I18n.getString("Global.Property.Top")));
        tags.add(new Tag(EdgeEnum.BOTTOM, I18n.getString("Global.Property.Bottom")));
        tags.add(new Tag(EdgeEnum.LEFT, I18n.getString("Global.Property.Left")));
        tags.add(new Tag(EdgeEnum.RIGHT, I18n.getString("Global.Property.Right")));
        return tags;
    }

    @Override
    public Object getPropertyValue() {
        return chart.getLegendPosition();
    }

    @Override
    public Object getOwnPropertyValue() {
        return chart.getLegendPosition();
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

    @Override
    public void validate(Object value) {

    }

    @Override
    public void setPropertyValue(Object value) {
        chart.setLegendPosition( (EdgeEnum)value);
    }

}
