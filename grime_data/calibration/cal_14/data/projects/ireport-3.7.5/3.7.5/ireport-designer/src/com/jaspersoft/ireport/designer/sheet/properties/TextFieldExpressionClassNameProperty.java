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
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import org.openide.nodes.PropertySupport;

/**
 *
 * Class to manage the JRDesignVariable.PROPERTY_VALUE_CLASS_NAME property
 */
public class TextFieldExpressionClassNameProperty  extends PropertySupport.ReadWrite {

    // FIXME: refactorize this
    private final JRDesignTextField element;
    PropertyEditor editor = null;

    
    public TextFieldExpressionClassNameProperty(JRDesignTextField element)
    {
        super(JRDesignExpression.PROPERTY_VALUE_CLASS_NAME, String.class,
              I18n.getString("Expression_Class"),
              I18n.getString("Expression_Class"));
        this.element = element;

        setValue("canEditAsText", true);
        setValue("oneline", true);
        setValue("suppressCustomEditor", false);
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {

        if (element.getExpression() == null) return "java.lang.String";
        if (element.getExpression().getValueClassName() == null) return "java.lang.String";
        return element.getExpression().getValueClassName();
    }


    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        JRDesignExpression oldExp =  (JRDesignExpression)element.getExpression();
        JRDesignExpression newExp = null;
        //System.out.println("Setting as value: " + val);

        String newVal = (val != null) ? val+"" : "";
        newVal = newVal.trim();

        if ( newVal.equals("") )
        {
            newVal = null;
        }

        newExp = new JRDesignExpression();
        newExp.setText( (oldExp != null) ? oldExp.getText() : null );
        newExp.setValueClassName( newVal );
        element.setExpression(newExp);

        ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        element,
                        "Expression",
                        JRExpression.class,
                        oldExp,newExp);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);

        //System.out.println("Done: " + val);
    }

    @Override
    public boolean isDefaultValue() {
        return element.getExpression() == null ||
               element.getExpression().getValueClassName() == null ||
               element.getExpression().getValueClassName().equals("java.lang.String");
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        setValue(null);
        editor.setValue("java.lang.String");
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyEditor getPropertyEditor() {

        if (editor == null)
        {
            java.util.List classes = new ArrayList();
            classes.add(new Tag("java.lang.String"));
            classes.add(new Tag("java.lang.Boolean"));
            classes.add(new Tag("java.lang.Byte"));
            classes.add(new Tag("java.util.Date"));
            classes.add(new Tag("java.sql.Timestamp"));
            classes.add(new Tag("java.sql.Time"));
            classes.add(new Tag("java.lang.Double"));
            classes.add(new Tag("java.lang.Float"));
            classes.add(new Tag("java.lang.Integer"));
            classes.add(new Tag("java.lang.Long"));
            classes.add(new Tag("java.lang.Short"));
            classes.add(new Tag("java.math.BigDecimal"));
            classes.add(new Tag("java.lang.Number"));

            editor = new ComboBoxPropertyEditor(false, classes);
        }
        return editor;
    }
    
}
