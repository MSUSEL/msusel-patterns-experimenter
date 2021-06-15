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
package com.jaspersoft.ireport.designer.data;

import java.awt.Color;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import org.springframework.ui.jasperreports.JasperReportsUtils;

/**
 *
 * @author gtoffoli
 */
public class SortFieldCellRenderer extends DefaultListCellRenderer {
    
    static ImageIcon ascIcon;
    static ImageIcon descIcon;
    
    /** Creates a new instance of SortFieldCellRenderer */
    public SortFieldCellRenderer() {
        if (ascIcon == null) ascIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/data/arrow_up.png"));
        if (descIcon == null) descIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/data/arrow_down.png"));
    }

    @Override
    public java.awt.Component getListCellRendererComponent(javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        java.awt.Component retValue;
        
        retValue = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof JRDesignSortField && retValue instanceof JLabel)
        {
            JRDesignSortField sf = (JRDesignSortField)value;
            JLabel label = (JLabel)retValue;

            String t = sf.getType() == SortFieldTypeEnum.FIELD ? "Field" : "Variable";

            Color cf = Color.green.darker().darker();
            Color cv = Color.BLUE;
            String c = sf.getType() == SortFieldTypeEnum.FIELD ? getEncodedColor(cf) : getEncodedColor(cv);

            String text = "<html>" + sf.getName() + " " + ((isSelected) ? t : "<font color=\"" + c + "\">" + t + "</font>");


            label.setText( text);
            
            label.setIcon( sf.getOrder() == JRDesignSortField.SORT_ORDER_DESCENDING ? descIcon : ascIcon );
        }
        
        return retValue;
    }
    
    
    public static String getEncodedColor(java.awt.Color c) {
        String nums = "0123456789ABCDEF";
        String s = "#";
        s += nums.charAt( c.getRed()/16 );
        s += nums.charAt( c.getRed()%16 );
        s += nums.charAt( c.getGreen()/16 );
        s += nums.charAt( c.getGreen()%16 );
        s += nums.charAt( c.getBlue()/16 );
        s += nums.charAt( c.getBlue()%16 );
        return s;
    }


}
