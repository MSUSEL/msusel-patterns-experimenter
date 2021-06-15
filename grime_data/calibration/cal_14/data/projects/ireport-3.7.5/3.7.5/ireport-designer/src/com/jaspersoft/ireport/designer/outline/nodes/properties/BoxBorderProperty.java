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
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRGraphicElement;
import org.openide.nodes.PropertySupport;

    
/**
 * 
 */
public final class BoxBorderProperty  extends PropertySupport {

    JRBox box = null;
    private ComboBoxPropertyEditor editor;

    /**
     * property can be border, topBorder, 
     **/
    @SuppressWarnings("unchecked")
    public BoxBorderProperty(JRBox box, String propertyName, String propertyDisplayName, 
                                  String propertyDesc)
    {
        super(propertyName, Byte.class,
              propertyDisplayName,
              propertyDesc, true, true);
        this.box = box;

        //setValue("canEditAsText", Boolean.FALSE);
        setValue("suppressCustomEditor", Boolean.TRUE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyEditor getPropertyEditor() {

        if (editor == null)
        {
            java.util.ArrayList l = new java.util.ArrayList();
            //l.add(new Tag( null , "Default"));
            l.add(new Tag( new Byte(JRGraphicElement.PEN_NONE) , "None"));
            l.add(new Tag( new Byte(JRGraphicElement.PEN_THIN) , "Thin"));
            l.add(new Tag( new Byte(JRGraphicElement.PEN_1_POINT) , "1 Point"));
            l.add(new Tag( new Byte(JRGraphicElement.PEN_2_POINT) , "2 Points"));
            l.add(new Tag( new Byte(JRGraphicElement.PEN_4_POINT) , "4 Points"));
            l.add(new Tag( new Byte(JRGraphicElement.PEN_DOTTED) , "Dotted"));

            editor = new ComboBoxPropertyEditor( false, l);
        }
        return editor;
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
//FIXME
//            if (this.getName().equals( JRBaseStyle.PROPERTY_BORDER)) return box.getBorder();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_BORDER)) return box.getTopBorder();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_BORDER)) return box.getLeftBorder();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_BORDER)) return box.getRightBorder();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_BORDER)) return box.getBottomBorder();
        return null;
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        if (val == null || val instanceof Byte)
        {

            Byte newValue = (Byte)val;
            Byte oldValue = box.getOwnBorder();

            String methodName = "Border";
//FIXME
//                if (this.getName().equals( JRBaseStyle.PROPERTY_BORDER))
//                {
//                    methodName = "Border";
//                    oldValue = box.getOwnBorder();
//                    box.setBorder(newValue);
//                }
//                else if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_BORDER)){
//                    methodName = "TopBorder";
//                    oldValue = box.getOwnTopBorder();
//                    box.setTopBorder(newValue);
//                }
//                else if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_BORDER)){
//                    methodName = "LeftBorder";
//                    oldValue = box.getOwnLeftBorder();
//                    box.setLeftBorder(newValue);
//                }
//                else if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_BORDER)){
//                    methodName = "RightBorder";
//                    oldValue = box.getOwnRightBorder();
//                    box.setRightBorder(newValue);
//                }
//                else if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_BORDER)){
//                    methodName = "BottomBorder";
//                    oldValue = box.getOwnBottomBorder();
//                    box.setBottomBorder(newValue);
//                }

            ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        box,
                        methodName, 
                        Byte.class,
                        oldValue,newValue);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    @Override
    public boolean isDefaultValue() {
//FIXME
//            if (this.getName().equals( JRBaseStyle.PROPERTY_BORDER)) return null == box.getOwnBorder();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_TOP_BORDER)) return null == box.getOwnTopBorder();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_LEFT_BORDER)) return null == box.getOwnLeftBorder();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_RIGHT_BORDER)) return null == box.getOwnRightBorder();
//            else if (this.getName().equals( JRBaseStyle.PROPERTY_BOTTOM_BORDER)) return null == box.getOwnBottomBorder();
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
