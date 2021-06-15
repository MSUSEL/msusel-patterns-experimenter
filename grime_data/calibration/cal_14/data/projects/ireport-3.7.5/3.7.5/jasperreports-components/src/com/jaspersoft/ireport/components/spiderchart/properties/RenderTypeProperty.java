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
import com.jaspersoft.ireport.designer.sheet.properties.StringListProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;
import net.sf.jasperreports.engine.JRChart;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class RenderTypeProperty extends StringListProperty
{
    private final StandardChartSettings chart;

    
    public RenderTypeProperty(StandardChartSettings chart)
    {
        super(chart);
        this.chart = chart;
    }

    @Override
    public String getName()
    {
        return JRChart.PROPERTY_CHART_RENDER_TYPE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Render_Type");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("The_render_type_of_the_chart.");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(JRChart.RENDER_TYPE_DRAW, JRChart.RENDER_TYPE_DRAW));
        tags.add(new Tag(JRChart.RENDER_TYPE_IMAGE, JRChart.RENDER_TYPE_IMAGE));
        tags.add(new Tag(JRChart.RENDER_TYPE_SVG, JRChart.RENDER_TYPE_SVG));
        return tags;
    }

    @Override
    public String getString()
    {
        return chart.getRenderType();
    }

    @Override
    public String getOwnString()
    {
        return chart.getRenderType();
    }

    @Override
    public String getDefaultString()
    {
        return null;
    }

    @Override
    public void setString(String renderType)
    {
        chart.setRenderType(renderType);
    }

}
