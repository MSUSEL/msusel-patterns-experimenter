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
package com.jaspersoft.ireport.designer.editor;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;

/**
 *
 * @author gtoffoli
 */
public class ExpObject {
    
    public static final int TYPE_FIELD = 0;
    public static final int TYPE_VARIABLE = 1;
    public static final int TYPE_PARAM = 2;
    
    private String name = "";
    private int type = TYPE_FIELD;
    private String  classType = "java.lang.String";
    
    /** Creates a new instance of ExpObject */
    public ExpObject(String name, int type, String classType) {
        
        this.setName(name);
        this.setType(type);
        this.setClassType(classType);
    }

    public ExpObject()
    {
    }
    
    
    public ExpObject(Object obj) {
        if (obj instanceof JRField)
        {
            this.name = ((JRField)obj).getName();
            this.type = TYPE_FIELD;
            this.classType = ((JRField)obj).getValueClassName();
        }
        else if (obj instanceof JRParameter)
        {
            this.name = ((JRParameter)obj).getName();
            this.type = TYPE_PARAM;
            this.classType = ((JRParameter)obj).getValueClassName();
        }
        else if (obj instanceof JRVariable)
        {
            this.name = ((JRVariable)obj).getName();
            this.type = TYPE_VARIABLE;
            this.classType = ((JRVariable)obj).getValueClassName();
        }
        else
        {
            this.name = "" + obj;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }
    
    public String getExpression()
    {
        if (getType() == TYPE_FIELD) return "$F{" + getName() + "}";
        if (getType() == TYPE_VARIABLE) return "$V{" + getName() + "}";
        if (getType() == TYPE_PARAM) return "$P{" + getName() + "}";
        return getName();
    }

}
