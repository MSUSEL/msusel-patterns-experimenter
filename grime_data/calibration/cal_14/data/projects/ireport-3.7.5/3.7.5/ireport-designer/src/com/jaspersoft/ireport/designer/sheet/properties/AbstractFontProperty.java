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

import com.jaspersoft.ireport.designer.sheet.editors.JRFontPropertyEditor;
import java.beans.PropertyEditor;
import java.util.prefs.PreferenceChangeListener;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.design.JasperDesign;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractFontProperty extends AbstractProperty
{
    protected JasperDesign jasperDesign = null;
    protected PropertyEditor editor = null;
    
    @SuppressWarnings("unchecked")
    public AbstractFontProperty(Object object, JasperDesign jasperDesign)
    {
        super(JRFont.class, object);
        this.jasperDesign = jasperDesign;
        setValue( "canEditAsText", Boolean.FALSE );
    }

    @Override
    public PropertyEditor getPropertyEditor() {
        
        if (editor == null)
        {
            editor = new JRFontPropertyEditor(jasperDesign);
        }
        return editor;
    }

    @Override
    public Object getValue()
    {
        return getPropertyValue();
    }

    @Override
    public Object getPropertyValue()
    {
        return getFont();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getFont();
    }

    @Override
    public Object getDefaultValue()
    {
        return null;
    }

    @Override
    public void validate(Object value)
    {
    }

    @Override
    public void setPropertyValue(Object value)
    {
        setFont((JRFont)value);
    }

    public abstract JRFont getFont();

    public abstract void setFont(JRFont font);
}
