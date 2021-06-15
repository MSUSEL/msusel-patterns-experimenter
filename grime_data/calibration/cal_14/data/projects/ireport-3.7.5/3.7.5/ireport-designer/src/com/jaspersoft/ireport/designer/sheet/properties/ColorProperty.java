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

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class ColorProperty extends AbstractProperty
{
    @SuppressWarnings("unchecked")
    public ColorProperty(Object object)
    {
        super(Color.class, object);
    }

    @Override
    public Object getPropertyValue()
    {
        return getColor();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getOwnColor();
    }

    @Override
    public Object getDefaultValue()
    {
        return getDefaultColor();
    }

    @Override
    public void validate(Object value)
    {
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        setPropertyValue(getDefaultColor());
    }

    @Override
    public void setPropertyValue(Object value)
    {
        setColor((Color)value);
    }

    public abstract Color getColor();

    public abstract Color getOwnColor();

    public abstract Color getDefaultColor();

    public abstract void setColor(Color color);

}
