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
package com.jaspersoft.ireport.designer.borders;


import com.jaspersoft.ireport.designer.utils.Java2DUtils;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.widget.Widget;

/**
 * @author Giulio Toffoli
 */
public final class SimpleLineBorder implements Border {

    private  Widget widget = null;
    public static Color COLOR_1 = new Color(192, 192, 192, 128);
    
    public static final Insets INSETS = new Insets(0, 0, 0, 0); //new Insets(9, 8, 10, 9);
    
    public Insets getInsets() {
        return INSETS;
    }
    
    public SimpleLineBorder()
    {
        super();
    }
    
    public SimpleLineBorder(Widget w)
    {
        super();
        this.widget = w;
    }

    public void paint(Graphics2D gr, Rectangle bounds) {
        
        Stroke oldStroke = gr.getStroke();
                
        Stroke bs = new BasicStroke(1);
        
        if (getWidget() != null && getWidget().getScene() != null)
        {
            double zoom = getWidget().getScene().getZoomFactor();
            bs = Java2DUtils.getInvertedZoomedStroke(bs, zoom);
        }
        
        gr.setStroke(bs);
        
        gr.setPaint(COLOR_1);
        Rectangle2D r = new Rectangle2D.Double (bounds.x + INSETS.left + 0.5, bounds.y + INSETS.top + 0.5, bounds.width - INSETS.left - INSETS.right - 1.0, bounds.height - INSETS.top - INSETS.bottom - 1.0);
        gr.draw(r);
        gr.setStroke(oldStroke);
        
    }

    public boolean isOpaque() {
        return false;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }
}
