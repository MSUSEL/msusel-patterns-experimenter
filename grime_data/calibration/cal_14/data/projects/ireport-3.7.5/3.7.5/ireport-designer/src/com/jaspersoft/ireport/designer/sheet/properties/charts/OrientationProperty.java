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

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.ByteProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import org.jfree.chart.plot.PlotOrientation;


/**
 *  Class to manage the JRDesignChart.PROPERTY_TITLE_POSITION property
 */
public final class OrientationProperty extends ByteProperty
{

    public static final Byte ORIENTATION_HORIZONTAL = new Byte((byte)0);
    public static final Byte ORIENTATION_VERTICAL = new Byte((byte)1);

    private final JRBaseChartPlot plot;

    
    public OrientationProperty(JRBaseChartPlot plot)
    {
        super(plot);
        this.plot = plot;
    }

    @Override
    public String getName()
    {
        return JRBaseChartPlot.PROPERTY_ORIENTATION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Chart_orientation");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Chart_orientation.desc");
    }

    @Override
    public List getTagList() 
    {
        List tags = new ArrayList();
        tags.add(new Tag(ORIENTATION_VERTICAL, I18n.getString("Chart_orientation.Vertical")));
        tags.add(new Tag(ORIENTATION_HORIZONTAL, I18n.getString("Chart_orientation.Horizontal")));
        return tags;
    }

    @Override
    public Byte getByte()
    {
        if (plot.getOrientation() == null) return ORIENTATION_VERTICAL;
        if (plot.getOrientation().equals(PlotOrientation.HORIZONTAL)) return ORIENTATION_HORIZONTAL;
        return ORIENTATION_VERTICAL;
    }

    @Override
    public Byte getOwnByte()
    {
        return getByte();
    }

    @Override
    public Byte getDefaultByte()
    {
        return ORIENTATION_VERTICAL;
    }

    @Override
    public void setByte(Byte position)
    {
        if (position.equals(ORIENTATION_VERTICAL))
        {
            plot.setOrientation(PlotOrientation.VERTICAL);
        }
        else
        {
            plot.setOrientation(PlotOrientation.HORIZONTAL);
        }
    }

}
