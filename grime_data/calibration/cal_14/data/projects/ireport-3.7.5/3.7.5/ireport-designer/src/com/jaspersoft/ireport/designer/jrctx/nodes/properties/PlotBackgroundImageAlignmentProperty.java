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
package com.jaspersoft.ireport.designer.jrctx.nodes.properties;

import com.jaspersoft.ireport.designer.sheet.properties.*;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.util.List;
import net.sf.jasperreports.chartthemes.simple.PlotSettings;
import org.jfree.ui.Align;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
 */
public final class PlotBackgroundImageAlignmentProperty extends IntegerProperty
{
    private final PlotSettings settings;
    private ComboBoxPropertyEditor editor;

    @SuppressWarnings("unchecked")
    public PlotBackgroundImageAlignmentProperty(PlotSettings settings)
    {
        super(settings);
        this.settings = settings;
    }

    @Override
    public String getName()
    {
        return PlotSettings.PROPERTY_backgroundImageAlignment;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property." + getName());
    }

    @Override
    public String getShortDescription()
    {
        return getDisplayName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public PropertyEditor getPropertyEditor()
    {
        if (editor == null)
        {
            editor = new ComboBoxPropertyEditor(false, getTagList());
        }
        return editor;
    }

    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(new Integer(Align.BOTTOM), I18n.getString("Global.Property.Bottom")));
        tags.add(new Tag(new Integer(Align.BOTTOM_LEFT), I18n.getString("Global.Property.BottomLeft")));
        tags.add(new Tag(new Integer(Align.BOTTOM_RIGHT), I18n.getString("Global.Property.BottomRight")));
        tags.add(new Tag(new Integer(Align.CENTER), I18n.getString("Global.Property.Center")));
        tags.add(new Tag(new Integer(Align.FIT), I18n.getString("Global.Property.Fit")));
        tags.add(new Tag(new Integer(Align.FIT_HORIZONTAL), I18n.getString("Global.Property.FitHorizontal")));
        tags.add(new Tag(new Integer(Align.FIT_VERTICAL), I18n.getString("Global.Property.FitVertical")));
        tags.add(new Tag(new Integer(Align.LEFT), I18n.getString("Global.Property.Left")));
        tags.add(new Tag(new Integer(Align.RIGHT), I18n.getString("Global.Property.Right")));
        tags.add(new Tag(new Integer(Align.TOP), I18n.getString("Global.Property.Top")));
        tags.add(new Tag(new Integer(Align.TOP_LEFT), I18n.getString("Global.Property.TopLeft")));
        tags.add(new Tag(new Integer(Align.TOP_RIGHT), I18n.getString("Global.Property.TopRight")));
        return tags;
    }

    @Override
    public Integer getInteger()
    {
        return settings.getBackgroundImageAlignment();
    }

    @Override
    public Integer getOwnInteger()
    {
        return settings.getBackgroundImageAlignment();
    }

    @Override
    public Integer getDefaultInteger()
    {
        return null;
    }

    @Override
    public void setInteger(Integer value)
    {
        settings.setBackgroundImageAlignment(value);
    }


    @Override
    public void validateInteger(Integer value)
    {
    }

}
