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
package com.jaspersoft.ireport.designer.sheet.properties.charts;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;

/**
 *  Class to manage the JRDesignChart.PROPERTY_EVALUATION_TIME property
 * @author gtoffoli
 */
public final class ImageEvaluationTimeProperty extends PropertySupport
{
        private final JRDesignDataset dataset;
        private final JRDesignChart element;
        private ComboBoxPropertyEditor editor;

       
        public ImageEvaluationTimeProperty(JRDesignChart element, JRDesignDataset dataset)
        {
            // TODO: Replace WhenNoDataType with the right constant
            super( JRDesignChart.PROPERTY_EVALUATION_TIME,Byte.class, I18n.getString("Evaluation_Time"), I18n.getString("When_the_image_expression_should_be_evaluated."), true, true);
            this.element = element;
            this.dataset = dataset;
            setValue(I18n.getString("suppressCustomEditor"), Boolean.TRUE);
        }

        @Override
        public boolean isDefaultValue() {
            return element.getEvaluationTime() == JRExpression.EVALUATION_TIME_NOW;
        }

        @Override
        public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
            setPropertyValue(JRExpression.EVALUATION_TIME_NOW);
        }

        @Override
        public boolean supportsDefaultValue() {
            return true;
        }

        @Override
      
        public PropertyEditor getPropertyEditor() {

            if (editor == null)
            {
                java.util.ArrayList l = new java.util.ArrayList();

                l.add(new Tag(new Byte(JRExpression.EVALUATION_TIME_NOW), I18n.getString("Now")));
                l.add(new Tag(new Byte(JRExpression.EVALUATION_TIME_REPORT), I18n.getString("Report")));
                l.add(new Tag(new Byte(JRExpression.EVALUATION_TIME_PAGE), I18n.getString("Page")));
                l.add(new Tag(new Byte(JRExpression.EVALUATION_TIME_COLUMN), I18n.getString("Column")));
                l.add(new Tag(new Byte(JRExpression.EVALUATION_TIME_GROUP), I18n.getString("Group")));
                l.add(new Tag(new Byte(JRExpression.EVALUATION_TIME_BAND), I18n.getString("Band")));
                l.add(new Tag(new Byte(JRExpression.EVALUATION_TIME_AUTO), I18n.getString("Auto")));

                editor = new ComboBoxPropertyEditor(false, l);
            }
            return editor;
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return new Byte(element.getEvaluationTime());
        }

        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (val instanceof Byte)
            {
                 setPropertyValue((Byte)val);
            }
        }

        private void setPropertyValue(Byte val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException 
        {
                Byte oldValue = element.getEvaluationTime();
                Byte newValue = val;



                ObjectPropertyUndoableEdit urob =
                        new ObjectPropertyUndoableEdit(
                            element,
                            I18n.getString("EvaluationTime"), 
                            Byte.TYPE,
                            oldValue,newValue);

                JRGroup oldGroupValue = element.getEvaluationGroup();
                JRGroup newGroupValue = null;
                if ( (val).byteValue() == JRExpression.EVALUATION_TIME_GROUP )
                {
                    if (dataset.getGroupsList().size() == 0)
                    {
                        IllegalArgumentException iae = annotateException(I18n.getString("No_groups_are_defined_to_be_used_with_this_element")); 
                        throw iae; 
                    }

                    newGroupValue = (JRGroup)dataset.getGroupsList().get(0);
                }

                element.setEvaluationTime(newValue);

                if (oldGroupValue != newGroupValue)
                {
                    ObjectPropertyUndoableEdit urobGroup =
                            new ObjectPropertyUndoableEdit(
                                element,
                                I18n.getString("EvaluationGroup"), 
                                JRGroup.class,
                                oldGroupValue,newGroupValue);
                    element.setEvaluationGroup(newGroupValue);
                    urob.concatenate(urobGroup);
                }

                // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
        }

        public IllegalArgumentException annotateException(String msg)
        {
            IllegalArgumentException iae = new IllegalArgumentException(msg); 
            ErrorManager.getDefault().annotate(iae, 
                                    ErrorManager.EXCEPTION,
                                    msg,
                                    msg, null, null); 
            return iae;
        }
}
