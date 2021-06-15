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
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class HorizontalAlignmentProperty extends ByteProperty
{
    private final JRAlignment element;

    @SuppressWarnings("unchecked")
    public HorizontalAlignmentProperty(JRAlignment element)
    {
        super(element);
        this.element = element;
    }

    @Override
    public String getName()
    {
        return JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.HorizontalAlignment");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.HorizontalAlignmentdetail");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(new Byte(JRAlignment.HORIZONTAL_ALIGN_LEFT), I18n.getString("Global.Property.Left")));
        tags.add(new Tag(new Byte(JRAlignment.HORIZONTAL_ALIGN_CENTER), I18n.getString("Global.Property.Center")));
        tags.add(new Tag(new Byte(JRAlignment.HORIZONTAL_ALIGN_RIGHT), I18n.getString("Global.Property.Right")));
        tags.add(new Tag(new Byte(JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED), I18n.getString("Global.Property.Justified")));
        return tags;
    }

    @Override
    public Byte getByte()
    {
        return element.getHorizontalAlignment();
    }

    @Override
    public Byte getOwnByte()
    {
        return element.getOwnHorizontalAlignment();
    }

    @Override
    public Byte getDefaultByte()
    {
        return null;
    }

    @Override
    public void setByte(Byte alignment)
    {
        element.setHorizontalAlignment(alignment);

        if (IReportManager.getPreferences().getBoolean("designer_debug_mode", false))
        {
            System.out.println(new java.util.Date() + ": setting HorizontalAlignment to: " + alignment + ". If the value is unattended or null, please report this notification to http://jasperforge.org/plugins/mantis/view.php?id=4139");
            Thread.dumpStack();
        }
    }

}
