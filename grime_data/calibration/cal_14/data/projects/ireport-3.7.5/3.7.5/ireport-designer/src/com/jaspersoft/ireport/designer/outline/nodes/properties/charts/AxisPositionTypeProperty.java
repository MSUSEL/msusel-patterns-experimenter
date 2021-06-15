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
package com.jaspersoft.ireport.designer.outline.nodes.properties.charts;

import com.jaspersoft.ireport.designer.sheet.properties.*;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.charts.design.JRDesignChartAxis;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
 */
public final class AxisPositionTypeProperty extends ByteProperty
{
    private final JRDesignChartAxis element;

    @SuppressWarnings("unchecked")
    public AxisPositionTypeProperty(JRDesignChartAxis element)
    {
        super(element);
        this.element = element;
    }

    @Override
    public String getName()
    {
        return JRDesignChartAxis.PROPERTY_POSITION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.AxisPositionType");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.AxisPositionType.detail");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(new Byte(JRDesignChartAxis.POSITION_LEFT_OR_TOP), I18n.getString("Global.Property.AxisPositionType.LeftTop")));
        tags.add(new Tag(new Byte(JRDesignChartAxis.POSITION_RIGHT_OR_BOTTOM), I18n.getString("Global.Property.AxisPositionType.RightBottom")));
        return tags;
    }

    @Override
    public Byte getByte()
    {
        return element.getPosition();
    }

    @Override
    public Byte getOwnByte()
    {
        return element.getPosition();
    }

    @Override
    public Byte getDefaultByte()
    {
        return JRDesignChartAxis.POSITION_LEFT_OR_TOP;
    }

    @Override
    public void setByte(Byte positionType)
    {
        element.setPosition(positionType);
    }

}
