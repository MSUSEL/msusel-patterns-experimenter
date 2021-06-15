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
package com.jaspersoft.ireport.designer.sheet.editors.box;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import net.sf.jasperreports.engine.JRPen;

/**
 *
 * @author gtoffoli
 */
public class LineStyleListCellRenderer extends JComponent implements ListCellRenderer {

    private Color selectionBackground = null;
    private Color  background = null;
    private Color  selectionForeground = null;
    private Color  foreground = null;
    
    private Byte styleName = null;
    
    public LineStyleListCellRenderer()
    {
        setOpaque(true);
        setBackground(Color.WHITE);
        selectionBackground = UIManager.getColor("List.selectionBackground");
        background = UIManager.getColor("List.background");
        selectionForeground = UIManager.getColor("List.selectionForeground");
        foreground = UIManager.getColor("List.foreground");
        
        
        setMinimumSize(new Dimension(20,16));
        setPreferredSize(new Dimension(20,16));
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        if (isSelected)
        {
            setForeground(selectionForeground);
            setBackground(selectionBackground);
        }
        else 
        {
            setForeground(foreground);
            setBackground(background);
        }
        if (value instanceof Byte)
        {
            styleName = (Byte)value;
        }
        else
        {
            styleName = null;
        }
        repaint();
        return this;
        
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        g.setColor(getBackground());
        g.fillRect(0,0,getWidth(),getHeight());
        if (styleName != null)
        {
            Stroke oldStroke = ((Graphics2D)g).getStroke();
            
            Stroke stroke = null;
            float penWidth = 1f;
            if (styleName == JRPen.LINE_STYLE_SOLID)
            {
                stroke = (Stroke) new BasicStroke(penWidth);
            }
            else if (styleName == JRPen.LINE_STYLE_DASHED)
            {
                stroke =  (Stroke) new BasicStroke(penWidth, 
                                            BasicStroke.CAP_BUTT, 
                                            BasicStroke.JOIN_BEVEL, 0f, 
                                            new float[] { 5f, 3f }, 0f);
            }
            else if (styleName == JRPen.LINE_STYLE_DOTTED)
            {
                stroke =  (Stroke) new BasicStroke(penWidth, 
                                            BasicStroke.CAP_BUTT, 
                                            BasicStroke.JOIN_BEVEL, 0f, 
                                            new float[] { 1f*penWidth, 1f*penWidth }, 0f);
            }
            else if (styleName == JRPen.LINE_STYLE_DOUBLE)
            {
                stroke =  (Stroke) new BasicStroke((penWidth/3f));
            }
            
            if (stroke != null)
            {
                ((Graphics2D)g).setStroke(stroke);

                g.setColor(getForeground());
                
                if (styleName != JRPen.LINE_STYLE_DOUBLE)
                {
                    ((Graphics2D)g).drawLine(5, getHeight()/2, getWidth()-5, getHeight()/2);
                }
                else
                {
                    ((Graphics2D)g).drawLine(5, (getHeight()/2) - 1, getWidth()-5, (getHeight()/2) - 1);
                    ((Graphics2D)g).drawLine(5, (getHeight()/2) + 1, getWidth()-5, (getHeight()/2) + 1);
                }
            }
            ((Graphics2D)g).setStroke(oldStroke);
        }
        
        g.setPaintMode();
        g.setColor(Color.LIGHT_GRAY);
        ((Graphics2D)g).drawLine(0, getHeight()-1,  getWidth(), getHeight() - 1);
    }
    
    
}
