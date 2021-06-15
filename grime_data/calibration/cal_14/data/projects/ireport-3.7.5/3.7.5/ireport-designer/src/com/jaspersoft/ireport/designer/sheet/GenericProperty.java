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

import com.jaspersoft.ireport.designer.ModelUtils;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignPropertyExpression;

/**
 *
 * @author gtoffoli
 */
public class GenericProperty {

    private String key = null;
    private Object value = null;
    private boolean useExpression = false;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isUseExpression() {
        return useExpression;
    }

    public void setUseExpression(boolean useExpression) {
        this.useExpression = useExpression;
    }
    
    public GenericProperty(String key, Object value)
    {
        this.key = key;
        this.value = value;
    }
    
    public GenericProperty(String key, JRDesignExpression exp)
    {
        this.key = key;
        this.value = ModelUtils.cloneExpression(exp);
        this.useExpression = true;
    }
    public GenericProperty()
    {
        this.key = "";
    }
    
    public JRDesignExpression getExpression()
    {
        return (value instanceof JRDesignExpression) ? (JRDesignExpression)value : null;
    }
    
    public JRDesignPropertyExpression toPropertyExpression()
    {
        JRDesignPropertyExpression pp = new JRDesignPropertyExpression();
        pp.setName(key);
        pp.setValueExpression(getExpression());
        return pp;
    }
    
    @Override
    public String toString()
    {
        return getKey();
    }
    
}
