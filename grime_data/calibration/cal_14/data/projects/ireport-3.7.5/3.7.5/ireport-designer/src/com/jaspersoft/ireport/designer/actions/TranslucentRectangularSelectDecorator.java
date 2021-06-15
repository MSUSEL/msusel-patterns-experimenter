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
package com.jaspersoft.ireport.designer.actions;

import com.jaspersoft.ireport.designer.borders.ThinLineBorder;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import org.netbeans.api.visual.action.RectangularSelectDecorator;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class TranslucentRectangularSelectDecorator implements RectangularSelectDecorator {

    private Scene scene;

    public TranslucentRectangularSelectDecorator (Scene scene) {
        this.scene = scene;
    }

    public Widget createSelectionWidget () {
        Widget widget = new Widget(scene);
        //widget.setBorder (scene.getLookFeel ().getMiniBorder (ObjectState.createNormal ().deriveSelected (true)));
        widget.setBorder ( new ThinLineBorder(Color.BLUE));
        widget.setBackground(new Color(0,0,255,60));
        widget.setOpaque(true);
        return widget;
    }
    
    class RectangularWidget extends Widget
    {
        public RectangularWidget(Scene scene)
        {
            super(scene);
        }

        @Override
        protected void paintBackground() {
            
            Graphics2D g = this.getGraphics();
            g.setPaint(getBackground());
            Rectangle b = getBounds();
            Rectangle2D r = new Rectangle2D.Double(b.x +0.5, b.y+0.5, b.width-1.0, b.height-1.0);
            g.fill(r);
        }
        
    }

}
