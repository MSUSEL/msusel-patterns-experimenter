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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.FeatureDescriptor;
import java.beans.PropertyEditorSupport;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.nodes.Node;

import java.beans.PropertyEditorSupport;

// bugfix# 9219 for attachEnv() method
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import org.openide.nodes.Node;


/**
 * A property editor for String class.
 * @author   Ian Formanek
 * @version  1.00, 18 Sep, 1998
 */
public class MeterIntervalsPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor
{
    public boolean isEditable(){
        return false;
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(Graphics gfx, Rectangle box) {
        //super.paintValue(gfx, box);
        List colors = (List)getValue();
        if (colors == null) super.paintValue(gfx, box);
        else
        {
            gfx.clearRect(box.x, box.y, box.width, box.height);
            int cols = colors.size();
            int bw = 10;
            
            while (cols > 0 && ((bw+2)*cols) > box.width)
            {
                if (bw > 4) bw--;
                else
                {
                    cols--;
                }
            }
            
            int x = box.x+1;
            Iterator it = colors.iterator();
            for (int i=0; i< cols && it.hasNext(); ++i)
            {
                JRMeterInterval color = (JRMeterInterval)it.next();
                if (color.getBackgroundColor() == null)
                {
                    gfx.setColor(Color.WHITE);
                    gfx.fillRect(x, box.y + 3, bw, Math.min(box.height-6, 10));
                    gfx.setColor(Color.BLACK);
                    gfx.drawLine(x, box.y + 3 + Math.min(box.height-6, 10), x + bw, box.y + 3);
                }
                else
                {
                    gfx.setColor(color.getBackgroundColor());
                    gfx.fillRect(x, box.y + 3, bw, Math.min(box.height-6, 10));
                }
                
                gfx.setColor(Color.BLACK);
                gfx.drawRect(x, box.y + 3, bw, Math.min(box.height-6, 10));
                
                x += bw+2;
            }
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
    public java.awt.Component getCustomEditor () {
        List val = (List)getValue();
        return new MeterIntervalsPropertyCustomEditor(val, this, env); // NOI18N
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

