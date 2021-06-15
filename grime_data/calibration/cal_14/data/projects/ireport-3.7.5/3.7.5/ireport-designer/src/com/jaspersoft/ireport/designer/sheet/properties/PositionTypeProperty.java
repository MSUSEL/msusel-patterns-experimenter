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
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignElement;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
 */
public final class PositionTypeProperty extends ByteProperty
{
    private final JRDesignElement element;

    @SuppressWarnings("unchecked")
    public PositionTypeProperty(JRDesignElement element)
    {
        super(element);
        this.element = element;
    }

    @Override
    public String getName()
    {
        return JRDesignElement.PROPERTY_POSITION_TYPE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.PositionType");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.PositionTypedetail");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(new Byte(JRDesignElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP), I18n.getString("Global.Property.FixTop")));
        tags.add(new Tag(new Byte(JRDesignElement.POSITION_TYPE_FLOAT), I18n.getString("Global.Property.PositionFloat")));
        tags.add(new Tag(new Byte(JRDesignElement.POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM), I18n.getString("Global.Property.FixBottom")));
        return tags;
    }

    @Override
    public Byte getByte()
    {
        return element.getPositionType();
    }

    @Override
    public Byte getOwnByte()
    {
        return element.getPositionType();
    }

    @Override
    public Byte getDefaultByte()
    {
        return JRDesignElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP;
    }

    @Override
    public void setByte(Byte positionType)
    {
        element.setPositionType(positionType);
    }

}
