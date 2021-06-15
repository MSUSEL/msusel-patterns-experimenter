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
package com.jaspersoft.ireport.designer.errorhandler;

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import java.awt.Component;
import java.beans.BeanInfo;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import org.openide.nodes.Node;

/**
 *
 * @author gtoffoli
 */
public class NodeCellRenderer extends DefaultTableCellRenderer {

    javax.swing.ImageIcon expressionIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/variables-16.png"));
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
       JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    
       label.setIcon(null);
       
       if (value instanceof Node)
       {
           Node node = (Node)value;
           String text = node.getDisplayName();
           
           if (node instanceof ElementNode)
           {
               text = "";
               JRDesignElement element = ((ElementNode)node).getElement();
               if (element instanceof JRBreak) text += "Break";
               if (element instanceof JRChart) text += "Chart";
               if (element instanceof JRCrosstab) text += "Crosstab";
               if (element instanceof JRElementGroup) text += "Element Group";
               if (element instanceof JREllipse) text += "Ellipse";
               if (element instanceof JRFrame) text += "Frame";
               if (element instanceof JRImage) text += "Image";
               if (element instanceof JRLine) text += "Line";
               if (element instanceof JRRectangle) text += "Rectangle";
               if (element instanceof JRStaticText) text += "Static Text";
               if (element instanceof JRSubreport) text += "Subreport";
               if (element instanceof JRTextField) text += "Text Field";
               
               JRElementGroup parent = ModelUtils.getTopElementGroup(element);
               if (parent != null && parent instanceof JRDesignBand) text += " in band " + ModelUtils.nameOf(((JRDesignBand)parent).getOrigin());
               else if (parent != null && parent instanceof JRDesignCellContents) text += " in cell " + ModelUtils.nameOf(((JRDesignCellContents)parent).getOrigin());
           
               text += " " + node.getDisplayName();
           }
           
           label.setText(text);
           label.setIcon( new ImageIcon(node.getIcon(BeanInfo.ICON_COLOR_16x16)));
       }
       else if (value instanceof JRDesignExpression)
       {
           JRDesignExpression exp = (JRDesignExpression)value;
           label.setText(exp.getText());
           label.setIcon(expressionIcon);
       }
       
       return label;
    }

    
    
}
