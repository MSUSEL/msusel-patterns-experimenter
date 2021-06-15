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

import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.base.JRBaseSubreport;
import net.sf.jasperreports.engine.design.JRDesignSubreport;

/**
 *  Class to manage the JRDesignTextElement.PROPERTY_ITALIC property
 * @author gtoffoli
 */
public final class SubreportUsingCacheProperty extends BooleanProperty
{
    private final JRDesignSubreport subreport;

    @SuppressWarnings("unchecked")
    public SubreportUsingCacheProperty(JRDesignSubreport subreport)
    {
        super(subreport);
        this.subreport = subreport;
    }
    @Override
    public String getName()
    {
        return JRBaseSubreport.PROPERTY_USING_CACHE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.UsingCache");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.UsingCachedetail");
    }

    @Override
    public Boolean getBoolean()
    {
        return subreport.isUsingCache();
    }

    @Override
    public Boolean getOwnBoolean()
    {
        return subreport.isOwnUsingCache();
    }

    @Override
    public Boolean getDefaultBoolean()
    {
        return null;
    }

    @Override
    public void setBoolean(Boolean isUsingCache)
    {
        subreport.setUsingCache(isUsingCache);
    }

}
