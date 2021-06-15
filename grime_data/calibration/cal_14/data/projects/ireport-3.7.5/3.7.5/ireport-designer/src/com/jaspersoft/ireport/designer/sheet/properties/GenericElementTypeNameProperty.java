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
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class GenericElementTypeNameProperty extends StringProperty
{
    private final JRDesignGenericElement element;

    @SuppressWarnings("unchecked")
    public GenericElementTypeNameProperty(JRDesignGenericElement element)
    {
        super(element);
        this.element = element;
        setValue("canEditAsText", true);
        setValue("oneline", true);
        setValue("suppressCustomEditor", false);
    }

    @Override
    public String getName()
    {
        return JRDesignGenericElement.PROPERTY_GENERIC_TYPE+"_name";
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.GenericTypeName");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.GenericTypeNameDetail");
    }

    @Override
    public String getString()
    {
        return getOwnString();
    }

    @Override
    public String getOwnString()
    {
        if (element.getGenericType() != null)
            return element.getGenericType().getName();
        else
            return null;
    }

    @Override
    public String getDefaultString()
    {
        return null;
    }

    @Override
    public void setString(String name)
    {
        String namespace = "";
        if (element.getGenericType() != null)
        {
            namespace = element.getGenericType().getNamespace();
        }
        JRGenericElementType type = new JRGenericElementType(namespace,name);
        element.setGenericType(type);
    }

    @Override
    public boolean supportsDefaultValue() {
        return false;
    }

}
