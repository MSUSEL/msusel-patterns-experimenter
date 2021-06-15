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
package com.jaspersoft.ireport.designer.sheet;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author gtoffoli
 */
public class Tag {
    
    private Object value;
    private String name = "";
    
    public Tag(Object value, String name) {
        setName( name );
        setValue(value);
    }
    
    public Tag(String value) {
        setName( value );
        setValue(value);
    }
    
    public Tag(Object value) {
        setName( value+"");
        setValue(value);
    }
    
    public Tag(int value, String name) {
        setName( name );
        setValue(value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    public void setValue(int value) {
        this.value = ""+value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String toString()
    {
        return getName();
    }
    
    /**
     * Look for the first tag with the specified name.
     */
    public static final Tag findTagByName(String name, Collection c)
    {
            if (c == null) return null;
            Iterator i = c.iterator();
            while (i.hasNext())
            {
                Tag t = (Tag)i.next();
                if ( (name == null && t.getName() == null) ||
                     (t.getName() != null && t.getName().equals(name)) )
                {
                    return t;
                }
            }
            return null;
    }
    
    /**
     * Look for the first tag with the specified name.
     */
    public static final Tag findTagByValue(Object value, Collection c)
    {
            if (c == null) return null;
            Iterator i = c.iterator();
            while (i.hasNext())
            {
                Tag t = (Tag)i.next();
                if ( (value == null && t.getValue() == value) ||
                     (t.getValue() != null && t.getValue().equals(value)) )
                {
                    return t;
                }
            }
            return null;
    }
    
}
