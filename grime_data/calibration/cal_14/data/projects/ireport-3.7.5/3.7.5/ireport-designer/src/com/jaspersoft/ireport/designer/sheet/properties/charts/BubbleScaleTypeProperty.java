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
import net.sf.jasperreports.charts.design.JRDesignBubblePlot;
import org.openide.nodes.PropertySupport;
    
    
/**
 *  Class to manage the JRDesignChart.PROPERTY_TITLE_POSITION property
 */
public final class BubbleScaleTypeProperty extends PropertySupport//FIXMETD
{
        private final JRDesignBubblePlot element;
        private ComboBoxPropertyEditor editor;
        
        @SuppressWarnings("unchecked")
        public BubbleScaleTypeProperty(JRDesignBubblePlot element)
        {
            // TODO: Replace WhenNoDataType with the right constant
            super(JRDesignBubblePlot.PROPERTY_SCALE_TYPE,Integer.class, I18n.getString("Global.Property.ScaleType"), I18n.getString("Global.Property.ScaleType"), true, true);
            this.element = element;
            setValue("suppressCustomEditor", Boolean.TRUE);
        }

        @Override
        @SuppressWarnings("unchecked")
        public PropertyEditor getPropertyEditor() {

            if (editor == null)
            {
                java.util.ArrayList l = new java.util.ArrayList();
                l.add(new Tag(new Integer(org.jfree.chart.renderer.xy.XYBubbleRenderer.SCALE_ON_BOTH_AXES), I18n.getString("Global.Property.BothAxes")));
                l.add(new Tag(new Integer(org.jfree.chart.renderer.xy.XYBubbleRenderer.SCALE_ON_DOMAIN_AXIS), I18n.getString("Global.Property.DomainAxis")));
                l.add(new Tag(new Integer(org.jfree.chart.renderer.xy.XYBubbleRenderer.SCALE_ON_RANGE_AXIS), I18n.getString("Global.Property.RangeAxis")));
                editor = new ComboBoxPropertyEditor(false, l);
            }
            return editor;
        }
        
        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return new Integer(element.getScaleType());
        }

        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            setPropertyValue(val);
        }

    
        private void setPropertyValue(Object val)
        {
            if (val instanceof Integer)
            {
                int oldValue = element.getScaleType();
                int newValue = ((Integer)val).intValue();
                element.setScaleType(newValue);

                ObjectPropertyUndoableEdit urob =
                        new ObjectPropertyUndoableEdit(
                            element,
                            "ScaleType", 
                            Integer.TYPE,
                            oldValue,newValue);
                // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
            }
        }
        
        @Override
        public boolean isDefaultValue() {
            return element.getScaleType() == org.jfree.chart.renderer.xy.XYBubbleRenderer.SCALE_ON_RANGE_AXIS;
        }

        @Override
        public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
            setValue(new Integer( org.jfree.chart.renderer.xy.XYBubbleRenderer.SCALE_ON_RANGE_AXIS) );
        }

        @Override
        public boolean supportsDefaultValue() {
            return true;
        }
}
