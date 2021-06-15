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
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.engine.JRBox;
import org.openide.nodes.PropertySupport;

    
/**
 *
 */
public final class BoxBorderColorProperty extends PropertySupport.ReadWrite {

    JRBox box = null;

    /**
     * property can be border, topBorder, 
     **/
    @SuppressWarnings("unchecked")
    public BoxBorderColorProperty(JRBox box, String propertyName, String propertyDisplayName, 
                                  String propertyDesc)
    {
        super(propertyName, java.awt.Color.class,
              propertyDisplayName,
              propertyDesc);
        this.box = box;
    }

    @Override
    public Object getValue() {
//FIXME
//            if (this.getName().equals( JRBaseStyle.PROPERTY_BORDER_COLOR)) return box.getBorderColor();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_BORDER_COLOR)) return box.getTopBorderColor();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_BORDER_COLOR)) return box.getLeftBorderColor();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_BORDER_COLOR)) return box.getRightBorderColor();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_BORDER_COLOR)) return box.getBottomBorderColor();
        return null;
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (val == null || val instanceof Color)
        {
            Color newValue =  (Color)val;
            Color oldValue = null;
            String methodName = "BorderColor";
//FIXME
//                if (this.getName().equals( JRBaseStyle.PROPERTY_BORDER_COLOR))
//                {
//                    methodName = "BorderColor";
//                    oldValue = box.getOwnBorderColor();
//                    box.setBorderColor(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_BORDER_COLOR)){
//                    methodName = "TopBorderColor";
//                    oldValue = box.getOwnTopBorderColor();
//                    box.setTopBorderColor(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_BORDER_COLOR)){
//                    methodName = "LeftBorderColor";
//                    oldValue = box.getOwnLeftBorderColor();
//                    box.setLeftBorderColor(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_BORDER_COLOR)){
//                    methodName = "RightBorderColor";
//                    oldValue = box.getOwnRightBorderColor();
//                    box.setRightBorderColor(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_BORDER_COLOR)){
//                    methodName = "BottomBorderColor";
//                    oldValue = box.getOwnBottomBorderColor();
//                    box.setBottomBorderColor(newValue);
//                }

            ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        box,
                        methodName, 
                        Color.class,
                        oldValue,newValue);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    @Override
    public boolean isDefaultValue() {
//FIXME
//            if (this.getName().equals( JRBaseStyle.PROPERTY_BORDER_COLOR)) return null == box.getOwnBorderColor();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_BORDER_COLOR)) return null == box.getOwnTopBorderColor();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_BORDER_COLOR)) return null == box.getOwnLeftBorderColor();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_BORDER_COLOR)) return null == box.getOwnRightBorderColor();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_BORDER_COLOR)) return null == box.getOwnBottomBorderColor();
        return true;
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        setValue(null);
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }
}
