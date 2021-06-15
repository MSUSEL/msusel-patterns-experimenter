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
import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
    
    
/**
 *  Class to manage the JRDesignMeterPlot.PROPERTY_SHAPE property
 */
public final class MeterShapeProperty extends ByteProperty
{
    private final JRDesignMeterPlot element;

    
    public MeterShapeProperty(JRDesignMeterPlot element)
    {
        super(element);
        this.element = element;
    }

    @Override
    public String getName()
    {
        return JRDesignMeterPlot.PROPERTY_SHAPE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Shape");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Shape.");
    }

    @Override
    public List getTagList() 
    {
        List tags = new ArrayList();
        tags.add(new Tag(new Byte(JRDesignMeterPlot.SHAPE_PIE), I18n.getString("Pie")));
        tags.add(new Tag(new Byte(JRDesignMeterPlot.SHAPE_CIRCLE), I18n.getString("Circle")));
        tags.add(new Tag(new Byte(JRDesignMeterPlot.SHAPE_CHORD), I18n.getString("Chord")));
        tags.add(new Tag(new Byte(JRDesignMeterPlot.SHAPE_DIAL), I18n.getString("Dial")));
        return tags;
    }

    @Override
    public Byte getByte()
    {
        return element.getShapeByte();
    }

    @Override
    public Byte getOwnByte()
    {
        return element.getShapeByte();
    }

    @Override
    public Byte getDefaultByte()
    {
        return null;
    }

    @Override
    public void setByte(Byte position)
    {
        try
        {
            element.setShape(position);
        }
        catch(JRException e)
        {
            throw new JRRuntimeException(e);
        }
    }
    
}
