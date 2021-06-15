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

import com.jaspersoft.ireport.designer.sheet.properties.BooleanProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.chartthemes.simple.ChartSettings;

/**
 * Class to manage the JRBaseStyle.PROPERTY_UNDERLINE property
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */

public class ChartBorderVisibleProperty extends BooleanProperty {

    private final ChartSettings settings;

    @SuppressWarnings("unchecked")
    public ChartBorderVisibleProperty(ChartSettings settings)
    {
        super(settings);
        this.settings = settings;
    }
    @Override
    public String getName()
    {
        return ChartSettings.PROPERTY_borderVisible;
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

    @Override
    public Boolean getBoolean()
    {
        return settings.getBorderVisible();
    }

    @Override
    public Boolean getOwnBoolean()
    {
        return settings.getBorderVisible();
    }

    @Override
    public Boolean getDefaultBoolean()
    {
        return null;
    }

    @Override
    public void setBoolean(Boolean b)
    {
    	settings.setBorderVisible(b);
    }
    
}
