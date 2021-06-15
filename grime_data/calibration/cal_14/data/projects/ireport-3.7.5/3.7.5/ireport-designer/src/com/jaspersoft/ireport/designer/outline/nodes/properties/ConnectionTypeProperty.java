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
package com.jaspersoft.ireport.designer.outline.nodes.properties;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import org.openide.nodes.PropertySupport;

    
/**
 *  Gost property to enable/disable datasource/connection expressions (PROPERTY_CONNECTION_TYPE)
 */
public final class ConnectionTypeProperty extends PropertySupport.ReadWrite {

    private final JRDesignSubreport element;
    PropertyEditor editor = null;

    @SuppressWarnings("unchecked")
    public ConnectionTypeProperty(JRDesignSubreport element)
    {
        super("PROPERTY_CONNECTION_TYPE", Integer.class,
              "Connection type",
              "You can choose to fill this subreport using a connection or a datasource or without providing any data.");
        this.element = element;

        setValue("suppressCustomEditor", true);
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {

        if (element.getConnectionExpression() == null &&
            element.getDataSourceExpression() == null) return new Integer(2);

        if (element.getConnectionExpression() == null &&
            element.getDataSourceExpression() != null) return new Integer(1);

        return new Integer(0);
    }


    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        if (val instanceof Integer)
        {
            switch ( ((Integer)val).intValue() )
            {
                case 0: 
                {
                    // Save old datasource expression if not null.. 
                    JRDesignExpression oldExp = (JRDesignExpression)element.getDataSourceExpression();
                    JRDesignExpression newExp = new JRDesignExpression();

                    ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(
                            element, "ConnectionExpression", JRExpression.class,
                            null,newExp);

                    if (oldExp != null)
                    {
                        ObjectPropertyUndoableEdit urob2 = new ObjectPropertyUndoableEdit(
                                element, "DataSourceExpression", JRExpression.class,
                                oldExp,null);

                        urob.concatenate(urob2);
                    }
                    element.setConnectionExpression(newExp);

                    IReportManager.getInstance().addUndoableEdit(urob);

                    break;
                }
                case 1:
                {
                    // Save old datasource expression if not null.. 
                    JRDesignExpression oldExp = (JRDesignExpression)element.getConnectionExpression();
                    JRDesignExpression newExp = new JRDesignExpression();

                    ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(
                            element, "DataSourceExpression", JRExpression.class,
                            null,newExp);

                    if (oldExp != null)
                    {
                        ObjectPropertyUndoableEdit urob2 = new ObjectPropertyUndoableEdit(
                                element, "ConnectionExpression", JRExpression.class,
                                oldExp,null);

                        urob.concatenate(urob2);
                    }
                    element.setDataSourceExpression(newExp);

                    IReportManager.getInstance().addUndoableEdit(urob);

                    break;
                }
                case 2:
                {
                    // Save old datasource expression if not null.. 
                    JRDesignExpression oldExp = (JRDesignExpression)element.getConnectionExpression();
                    ObjectPropertyUndoableEdit urob = null;
                    if (oldExp != null)
                    {
                        urob = new ObjectPropertyUndoableEdit(
                                element, "ConnectionExpression", JRExpression.class,
                                oldExp,null);
                    }


                    oldExp = (JRDesignExpression)element.getDataSourceExpression();
                    ObjectPropertyUndoableEdit urob2 = null;
                    if (oldExp != null)
                    {
                        urob2 = new ObjectPropertyUndoableEdit(
                                element, "DataSourceExpression", JRExpression.class,
                                oldExp,null);
                    }

                    if (urob != null && urob2 != null) urob.concatenate(urob2);
                    else if (urob2 != null) urob = urob2;

                    element.setDataSourceExpression(null);
                    element.setConnectionExpression(null);

                    IReportManager.getInstance().addUndoableEdit(urob);
                    break;
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyEditor getPropertyEditor() {

        if (editor == null)
        {
            java.util.List classes = new ArrayList();
            classes.add(new Tag(new Integer(0), "Use a connection expression"));
            classes.add(new Tag(new Integer(1), "Use a datasource expression"));
            classes.add(new Tag(new Integer(2), "Don't pass data."));

            editor = new ComboBoxPropertyEditor(false, classes);
        }
        return editor;
    }
}
