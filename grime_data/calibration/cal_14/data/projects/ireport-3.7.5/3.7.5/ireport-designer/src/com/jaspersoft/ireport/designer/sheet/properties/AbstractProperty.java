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
import com.jaspersoft.ireport.designer.undo.PropertyUndoableEdit;
import java.lang.reflect.InvocationTargetException;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractProperty extends PropertySupport.ReadWrite
{
    protected final Object object;



    @SuppressWarnings("unchecked")
    public AbstractProperty(Class clazz, Object object)
    {
        super(null, clazz, null, null);
        this.object = object;
    }

    @Override
    public Object getValue()
    {
        Object value = getPropertyValue();
        return value == null ? "" : value;
    }

    @Override
    public void setValue(Object newValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException 
    {
        Object oldValue = getOwnPropertyValue();

        validate(newValue);
        
        setPropertyValue(newValue);

        PropertyUndoableEdit undo =
            new PropertyUndoableEdit(
                this,
                oldValue,
                newValue
                );
        IReportManager.getInstance().addUndoableEdit(undo);
    }

    @Override
    public boolean isDefaultValue() 
    {
        Object value = getPropertyValue();

        return
            (getDefaultValue() == null && value == null)
            || (getDefaultValue() != null && getDefaultValue().equals(value));
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException 
    {
        //FIXMETD check this super.restoreDefaultValue();
        setValue(getDefaultValue());
    }

    @Override
    public boolean supportsDefaultValue() 
    {
        return true;
    }

    public IllegalArgumentException annotateException(String msg)
    {
        IllegalArgumentException iae = new IllegalArgumentException(msg); 
        ErrorManager.getDefault().annotate(
            iae, 
            ErrorManager.EXCEPTION,
            msg,
            msg, null, null
            ); 
        return iae;
    }

    public abstract Object getPropertyValue();

    public abstract Object getOwnPropertyValue();

    public abstract Object getDefaultValue();

    public abstract void validate(Object value);

    public abstract void setPropertyValue(Object value);

}
