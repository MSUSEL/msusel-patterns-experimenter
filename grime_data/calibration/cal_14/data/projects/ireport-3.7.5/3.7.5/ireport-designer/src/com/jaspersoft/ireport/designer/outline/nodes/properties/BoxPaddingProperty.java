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
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.engine.JRBox;
import org.openide.nodes.PropertySupport;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_PRINT_REPEATED_VALUES property
 */
public final class BoxPaddingProperty extends PropertySupport.ReadWrite {

    JRBox box = null;

    /**
     *
     **/
    @SuppressWarnings("unchecked")
    public BoxPaddingProperty(JRBox box, String propertyName, String propertyDisplayName, 
                                  String propertyDesc)
    {
        super(propertyName, Integer.class,
              propertyDisplayName,
              propertyDesc);
        this.box = box;
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
//FIXME
//            if (this.getName().equals( JRBaseStyle.PROPERTY_PADDING)) return box.getPadding();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_PADDING)) return box.getTopPadding();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_PADDING)) return box.getLeftPadding();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_PADDING)) return box.getRightPadding();
//            if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_PADDING)) return box.getBottomPadding();
        return null;
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (val == null || val instanceof Integer)
        {
            Integer newValue =  (Integer)val;
            Integer oldValue = null;
            String methodName = "Padding";
//FIXME
//                if (this.getName().equals( JRBaseStyle.PROPERTY_PADDING))
//                {
//                    methodName = "Padding";
//                    oldValue = box.getOwnPadding();
//                    box.setPadding(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_PADDING)){
//                    methodName = "TopPadding";
//                    oldValue = box.getOwnTopPadding();
//                    box.setTopPadding(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_PADDING)){
//                    methodName = "LeftBorderColor";
//                    oldValue = box.getOwnLeftPadding();
//                    box.setLeftPadding(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_PADDING)){
//                    methodName = "RightBorderColor";
//                    oldValue = box.getOwnRightPadding();
//                    box.setRightPadding(newValue);
//                }
//                if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_PADDING)){
//                    methodName = "BottomBorderColor";
//                    oldValue = box.getOwnBottomPadding();
//                    box.setBottomPadding(newValue);
//                }

            ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        box,
                        methodName, 
                        Integer.class,
                        oldValue,newValue);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    @Override
    public boolean isDefaultValue() {
//FIXME
//            if (this.getName().equals( JRBaseStyle.PROPERTY_PADDING)) return null == box.getOwnPadding();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_PADDING)) return null == box.getOwnTopPadding();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_PADDING)) return null == box.getOwnLeftPadding();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_PADDING)) return null == box.getOwnRightPadding();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_PADDING)) return null == box.getOwnBottomPadding();
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
