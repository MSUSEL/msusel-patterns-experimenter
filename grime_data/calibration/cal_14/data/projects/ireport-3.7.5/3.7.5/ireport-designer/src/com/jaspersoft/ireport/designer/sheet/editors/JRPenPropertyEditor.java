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
package com.jaspersoft.ireport.designer.sheet.editors;

import com.jaspersoft.ireport.designer.sheet.editors.box.BoxBorderSelectionPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import java.beans.PropertyEditorSupport;

// bugfix# 9219 for attachEnv() method
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import java.beans.FeatureDescriptor;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.base.JRBasePrintLine;
import net.sf.jasperreports.engine.export.draw.LineDrawer;
import net.sf.jasperreports.engine.util.JRPenUtil;
import org.openide.nodes.Node;


/**
 * A property editor for String class.
 * @author   Ian Formanek
 * @version  1.00, 18 Sep, 1998
 */
public class JRPenPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor
{
    public boolean isEditable(){
        return false;
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(Graphics grx, Rectangle box) 
    {
        JRPen pen = getValue() instanceof JRPen ? (JRPen)getValue() : null;
        if (pen == null)
        {
            super.paintValue(grx, box);
        }
        else
        {
//            //grx.clearRect(box.x, box.y, box.width, box.height);
//            grx.setColor(pen.getLineColor() == null ? Color.BLACK : pen.getLineColor());
//            //Stroke s = BoxBorderSelectionPanel.createStroke(pen);
//            Stroke stroke = JRPenUtil.getStroke(pen, BasicStroke.CAP_SQUARE);
//            if (stroke != null)
//            {
//                ((Graphics2D)grx).setStroke(stroke);
//                grx.drawLine(box.x + 4, box.y + box.height / 2, box.x + box.width - 4, box.y + box.height / 2);
//            }
            JRPrintLine line = new JRBasePrintLine(null);
            line.setX(box.x + 4);
            line.setY(box.y + box.height / 2);
            line.setWidth(box.width - 8);
            line.setHeight(1);
            line.getLinePen().setLineColor(pen.getLineColor());
            line.getLinePen().setLineStyle(pen.getLineStyle());
            line.getLinePen().setLineWidth(pen.getLineWidth());
            new LineDrawer().draw((Graphics2D)grx, line, 0, 0);
        }
        
    }
    
    /** sets new value */
    @Override
    public String getAsText() {
        return "";
    }
    
    /** sets new value */
    @Override
    public void setAsText(String s) {
        return;
    }

    @Override
    public boolean supportsCustomEditor () {
        return customEd;
    }

    @Override
    public java.awt.Component getCustomEditor () 
    {
        JRPen pen = getValue() instanceof JRPen ? (JRPen)getValue() : null;
        return new JRPenPropertyCustomEditor(pen, false, null, this, env); // NOI18N
    }

    //private String instructions=null;
    //private boolean oneline=false;
    private boolean customEd=true;
    private PropertyEnv env;

    // bugfix# 9219 added attachEnv() method checking if the user canWrite in text box 
    public void attachEnv(PropertyEnv env) {

        FeatureDescriptor desc = env.getFeatureDescriptor();
        if (desc instanceof Node.Property){
            Node.Property prop = (Node.Property)desc;
            //enh 29294 - support one-line editor & suppression of custom
            //editor
            //instructions = (String) prop.getValue ("instructions"); //NOI18N
            //oneline = Boolean.TRUE.equals (prop.getValue ("oneline")); //NOI18N
            customEd = !Boolean.TRUE.equals (prop.getValue ("suppressCustomEditor")); //NOI18N
        }
        this.env = env;
        
        

    }
}

