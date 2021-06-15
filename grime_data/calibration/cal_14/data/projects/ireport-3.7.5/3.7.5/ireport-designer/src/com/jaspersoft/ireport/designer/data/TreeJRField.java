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
package com.jaspersoft.ireport.designer.data;

import net.sf.jasperreports.engine.design.JRDesignField;

/**
 * This class is used in extended datasource tree.
 * @author  Administrator
 */
public class TreeJRField
{
    private JRDesignField field= null;
    
    private Class obj = null;
   
    @Override
    public String toString()
    {
        if (field != null)
        {
            return field.getName() + " (" + this.getObj().getName() +")";
        }
        return "???";
    }
    
    /**
     * Getter for property field.
     * @return Value of property field.
     */
    public JRDesignField getField() {
        return field;
    }
    
    /**
     * Setter for property field.
     * @param field New value of property field.
     */
    public void setField(JRDesignField field) {
        this.field = field;
    }
    
    /**
     * Getter for property obj.
     * @return Value of property obj.
     */
    public java.lang.Class getObj() {
        return obj;
    }
    
    /**
     * Setter for property obj.
     * @param obj New value of property obj.
     */
    public void setObj(java.lang.Class obj) {
        this.obj = obj;
    }
    
}
