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

import com.jaspersoft.ireport.designer.jrctx.nodes.editors.PaintProviderPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.*;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.chartthemes.simple.PaintProvider;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class PaintProviderProperty extends AbstractProperty
{
    private PaintProviderPropertyEditor editor = null;
    
    @SuppressWarnings("unchecked")
    public PaintProviderProperty(Object object)
    {
        super(PaintProvider.class, object);
        
        setValue("canEditAsText", Boolean.FALSE);
    }

    @Override
    public Object getPropertyValue()
    {
        return getPaintProvider();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getOwnPaintProvider();
    }

    @Override
    public Object getDefaultValue()
    {
        return getDefaultPaintProvider();
    }

    @Override
    public PropertyEditor getPropertyEditor()
    {
        if(editor == null)
        {
            editor = new PaintProviderPropertyEditor();
        }
        return editor;
    }

    @Override
    public void validate(Object value)
    {
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        setPropertyValue(getDefaultPaintProvider());
    }

    @Override
    public void setPropertyValue(Object value)
    {
        setPaintProvider((PaintProvider)value);
    }

    public abstract PaintProvider getPaintProvider();

    public abstract PaintProvider getOwnPaintProvider();

    public abstract PaintProvider getDefaultPaintProvider();

    public abstract void setPaintProvider(PaintProvider paintProvider);

}
