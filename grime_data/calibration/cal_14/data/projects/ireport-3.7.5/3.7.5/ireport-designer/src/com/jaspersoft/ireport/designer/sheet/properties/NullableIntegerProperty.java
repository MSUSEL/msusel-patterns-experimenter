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

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class NullableIntegerProperty extends AbstractProperty
{
    @SuppressWarnings("unchecked")
    public NullableIntegerProperty(Object object)
    {
        super(Float.class, object);
        setValue("suppressCustomEditor", Boolean.TRUE);
    }

    @Override
    public Object getPropertyValue()
    {
        return getInteger();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getOwnInteger();
    }

    @Override
    public Object getDefaultValue()
    {
        return getDefaultInteger();
    }

    @Override
    public void validate(Object value)
    {
        validateInteger(convert(value));
    }

    @Override
    public void setPropertyValue(Object value)
    {
        setInteger(convert(value));
    }

    private Integer convert(Object value)
    {
        Number number = (Number)value;
        return number == null ? null : new Integer(number.intValue());
    }

    public abstract Integer getInteger();

    public abstract Integer getOwnInteger();

    public abstract Integer getDefaultInteger();

    public abstract void validateInteger(Integer value);

    public abstract void setInteger(Integer value);

}
