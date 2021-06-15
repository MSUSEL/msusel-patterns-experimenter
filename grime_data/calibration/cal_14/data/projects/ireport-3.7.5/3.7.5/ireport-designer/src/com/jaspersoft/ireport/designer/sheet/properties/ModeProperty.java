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
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class ModeProperty extends BooleanProperty
{
    private JRDesignElement element = null;

    @SuppressWarnings("unchecked")
    public ModeProperty(JRDesignElement element)
    {
        super(element);
        this.element = element;
    }
    @Override
    public String getName()
    {
        return JRBaseStyle.PROPERTY_MODE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.Opaque");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.Opaquedetail");
    }

    @Override
    public Boolean getBoolean()
    {
        return element.getMode() == JRElement.MODE_OPAQUE;
    }

    @Override
    public Boolean getOwnBoolean()
    {
        if (element.getOwnMode() == null)
            return null;
        return element.getOwnMode() == JRElement.MODE_OPAQUE;
    }

    @Override
    public Boolean getDefaultBoolean()
    {
        return null;
    }

    @Override
    public void setBoolean(Boolean isPrint)
    {

        element.setMode(isPrint == null ? null : (isPrint ? JRElement.MODE_OPAQUE : JRElement.MODE_TRANSPARENT));

        if (IReportManager.getPreferences().getBoolean("designer_debug_mode", false))
        {
            System.out.println(new java.util.Date() + ": setting mode to: " + isPrint + ". If the value is unattended or null, please report this notification to http://jasperforge.org/plugins/mantis/view.php?id=4139");
            Thread.dumpStack();
        }
    }

}
