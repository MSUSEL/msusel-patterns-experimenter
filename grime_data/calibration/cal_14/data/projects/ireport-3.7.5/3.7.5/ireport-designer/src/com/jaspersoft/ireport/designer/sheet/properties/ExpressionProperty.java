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

import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.editors.ExpressionPropertyEditor;
import java.beans.PropertyEditor;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class ExpressionProperty extends AbstractProperty
{
    private PropertyEditor editor = null;
    
    public ExpressionProperty(Object object, ExpressionContext context)
    {
        super(String.class, object);
        this.setValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT, context);
    }
    
    @SuppressWarnings("unchecked")
    public ExpressionProperty(Object object, JRDesignDataset dataset)
    {
        this(object, new ExpressionContext(dataset));
    }

    @Override
    public Object getPropertyValue()
    {
        JRDesignExpression expression = getExpression();
        
        if (expression == null)
            return null;
        
        return expression.getText();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getPropertyValue();
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
        setExpression(createExpression((String)value));
    }

    @Override
    public PropertyEditor getPropertyEditor() 
    {
        if (editor == null)
        {
            editor = new ExpressionPropertyEditor();
        }
        return editor;
    }
    
    private JRDesignExpression createExpression(String text)
    {
        JRDesignExpression newExp = null;
        
        JRDesignExpression oldExp = getExpression();
        String defaultValueClassName = getDefaultExpressionClassName();
        String valueClassName = oldExp == null || oldExp.getValueClassName() == null  ? defaultValueClassName : oldExp.getValueClassName();

        if (
            (text != null && text.trim().length() > 0) 
            || !defaultValueClassName.equals(valueClassName)
            )
        {
            newExp = new JRDesignExpression();
            newExp.setText(text);
            newExp.setValueClassName(valueClassName);
        }

        return newExp;
    }

    public abstract String getDefaultExpressionClassName();

    public abstract JRDesignExpression getExpression();

    public abstract void setExpression(JRDesignExpression expression);
}
