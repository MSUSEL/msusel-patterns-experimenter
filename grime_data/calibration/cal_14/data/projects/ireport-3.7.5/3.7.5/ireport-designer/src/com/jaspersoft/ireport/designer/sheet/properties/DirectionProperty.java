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
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.base.JRBaseLine;
import net.sf.jasperreports.engine.design.JRDesignLine;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class DirectionProperty extends ByteProperty
{
    private final JRDesignLine line;

    @SuppressWarnings("unchecked")
    public DirectionProperty(JRDesignLine line)
    {
        super(line);
        this.line = line;
    }

    @Override
    public String getName()
    {
        return JRBaseLine.PROPERTY_DIRECTION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.Direction");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.Directiondetail");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(new Byte(JRDesignLine.DIRECTION_TOP_DOWN), I18n.getString("Global.Property.TopDown")));
        tags.add(new Tag(new Byte(JRDesignLine.DIRECTION_BOTTOM_UP), I18n.getString("Global.Property.BottomUp")));
        return tags;
    }

    @Override
    public Byte getByte()
    {
        return line.getDirection();
    }

    @Override
    public Byte getOwnByte()
    {
        return line.getDirection();
    }

    @Override
    public Byte getDefaultByte()
    {
        return JRLine.DIRECTION_TOP_DOWN;
    }

    @Override
    public void setByte(Byte direction)
    {
        line.setDirection(direction);
    }

}
