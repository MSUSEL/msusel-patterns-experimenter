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
package com.jaspersoft.ireport.designer.sheet.properties;

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.locale.I18n;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignGraphicElement;

/**
 * 
 *  Class to manage the JRDesignGraphicElement.PROPERTY_PEN property
 */
public final class PenProperty extends ByteProperty
{
    private final JRDesignGraphicElement element;

    @SuppressWarnings("unchecked")
    public PenProperty(JRDesignGraphicElement element)
    {
        super(element);
        this.element = element;
    }

    @Override
    public String getName()
    {
        //FIXME: get an appropriate String constant
        return null;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("PenProperty.Property.Pen");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("PenProperty.Property.DescriptionPen");
    }

    @Override
    public List getTagList() 
    {
        List tags = new ArrayList();
        tags.add(new Tag(new Byte(JRDesignGraphicElement.PEN_THIN), I18n.getString("Global.Property.Thin")));
        tags.add(new Tag(new Byte(JRDesignGraphicElement.PEN_1_POINT), I18n.getString("Global.Property.1_point")));
        tags.add(new Tag(new Byte(JRDesignGraphicElement.PEN_2_POINT), I18n.getString("Global.Property.2_point")));
        tags.add(new Tag(new Byte(JRDesignGraphicElement.PEN_4_POINT), I18n.getString("Global.Property.4_point")));
        tags.add(new Tag(new Byte(JRDesignGraphicElement.PEN_DOTTED), I18n.getString("Global.Property.Dotted")));
        tags.add(new Tag(new Byte(JRDesignGraphicElement.PEN_NONE), I18n.getString("Global.Property.None")));
        return tags;
    }

    @Override
    public Byte getByte()
    {
        return element.getPen();
    }

    @Override
    public Byte getOwnByte()
    {
        return element.getOwnPen();
    }

    @Override
    public Byte getDefaultByte()
    {
        return null;
    }

    @Override
    public void setByte(Byte pen)
    {
        element.setPen(pen);
    }

}
