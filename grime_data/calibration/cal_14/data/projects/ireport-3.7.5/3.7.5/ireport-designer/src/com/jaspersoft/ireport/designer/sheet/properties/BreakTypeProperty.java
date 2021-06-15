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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.engine.base.JRBaseBreak;
import net.sf.jasperreports.engine.design.JRDesignBreak;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class BreakTypeProperty extends ByteProperty
{
    private final JRDesignBreak breakElement;

    @SuppressWarnings("unchecked")
    public BreakTypeProperty(JRDesignBreak breakElement)
    {
        super(breakElement);
        this.breakElement = breakElement;
    }

    @Override
    public String getName()
    {
        return JRBaseBreak.PROPERTY_TYPE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.BreakType");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.BreakType");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(new Byte(JRBaseBreak.TYPE_PAGE), I18n.getString("Global.Property.BreakTypePage")));
        tags.add(new Tag(new Byte(JRBaseBreak.TYPE_COLUMN), I18n.getString("Global.Property.BreakTypeColumn")));
        return tags;
    }

    @Override
    public Byte getByte()
    {
        return breakElement.getType();
    }

    @Override
    public Byte getOwnByte()
    {
        return breakElement.getType();
    }

    @Override
    public Byte getDefaultByte()
    {
        return JRBaseBreak.TYPE_PAGE;
    }

    @Override
    public void setByte(Byte type)
    {
        breakElement.setType(type);

        if (IReportManager.getPreferences().getBoolean("designer_debug_mode", false))
        {
            System.out.println(new java.util.Date() + ": setting break type to: " + type + ". If the value is unattended or null, please report this notification to http://jasperforge.org/plugins/mantis/view.php?id=4139");
            Thread.dumpStack();
        }

        
    }

}
