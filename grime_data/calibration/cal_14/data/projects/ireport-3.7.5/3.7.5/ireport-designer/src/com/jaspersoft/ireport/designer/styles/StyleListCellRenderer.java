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
package com.jaspersoft.ireport.designer.styles;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.BeanInfo;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTemplateReference;
import org.openide.explorer.view.Visualizer;
import org.openide.nodes.Node;

/**
 *
 * @author gtoffoli
 */
public class StyleListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        Node node = Visualizer.findNode(value);
        if (node != null) value = node;
        
        if (value instanceof LibraryStyleNode)
        {
            JRStyle style = ((LibraryStyleNode)value).getStyle();


            String fontName = style.getFontName();
            if (fontName == null) fontName = "SansSerif";
            int size = style.getFontSize() == null ? 10 : style.getFontSize();
            int font_style = 0;
            if (style.isBold() != null && style.isBold().booleanValue()) font_style |= Font.BOLD;
            if (style.isItalic() != null && style.isItalic().booleanValue()) font_style |= Font.ITALIC;

            label.setFont(new Font(fontName,font_style, size));

            String text = style.getName();
            if (style.isStrikeThrough() != null && style.isStrikeThrough().booleanValue()) text = "<s>" + text + "</s>";
            if (style.isUnderline() != null && style.isUnderline().booleanValue()) text = "<u>" + text + "</u>";
            label.setIcon(null);
            label.setText("<html>" +text);

            if (!isSelected)
            {
                label.setForeground(style.getForecolor());
                if (style.getMode() != null && style.getMode().byteValue() == JRElement.MODE_OPAQUE)
                {
                    label.setBackground(style.getBackcolor());
                    label.setOpaque(true);
                }
            }
            
        }
        else if (value instanceof LibraryTemplateReferenceNode)
        {
            JRTemplateReference template = ((LibraryTemplateReferenceNode)value).getReference();
            label.setText( ((LibraryTemplateReferenceNode)value).getDisplayName() );
            Image img = ((LibraryTemplateReferenceNode)value).getIcon(BeanInfo.ICON_COLOR_16x16);
            if (img != null)
            {
                label.setIcon( new ImageIcon(img));
            }
            else
            {
                label.setIcon(null);
            }
        }

        label.setPreferredSize(null);
        Dimension d = label.getPreferredSize();
        if (d != null)
        {
            d.height += 4;
            setPreferredSize(d);
        }

        return label;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.lightGray);
        g.drawLine(4, this.getHeight()-2, this.getWidth()-8, this.getHeight()-2);
    }




}
