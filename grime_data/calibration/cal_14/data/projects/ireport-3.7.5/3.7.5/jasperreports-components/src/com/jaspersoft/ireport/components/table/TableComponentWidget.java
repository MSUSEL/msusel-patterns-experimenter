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
package com.jaspersoft.ireport.components.table;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class TableComponentWidget extends JRDesignElementWidget {

    private Image tableImage = null;

    public TableComponentWidget(AbstractReportObjectScene scene, JRDesignElement element) {
        super(scene, element);
    }

    
    @Override
    protected void paintWidgetImplementation() {

        //super.paintWidgetImplementation();

        Graphics2D gr = getScene().getGraphics();

        
        // Move the gfx 10 pixel ahead...
        Rectangle r = getPreferredBounds();

        AffineTransform af = gr.getTransform();
        AffineTransform new_af = (AffineTransform) af.clone();
        AffineTransform translate = AffineTransform.getTranslateInstance(
                getBorder().getInsets().left + r.x,
                getBorder().getInsets().top + r.y);
        new_af.concatenate(translate);
        gr.setTransform(new_af);

        //Composite oldComposite = gr.getComposite();
        Paint oldPaint = gr.getPaint();
        gr.setPaint(new Color(232,232,234,64));
        gr.fillRect(0, 0, getElement().getWidth(), getElement().getHeight());
        gr.setPaint(oldPaint);
        //gr.setComposite(oldComposite);
        Shape oldClip = gr.getClip();
        Shape rect = new Rectangle2D.Float(0,0,getElement().getWidth(), getElement().getHeight());
        gr.clip(rect);
        gr.drawImage(getTableIcon(), 4, 4, null);
        gr.setClip(oldClip);

        gr.setTransform(af);
    }
    
    

    public java.awt.Image getTableIcon() {

        if (tableImage == null)
        {
            tableImage = Misc.loadImageFromResources("/com/jaspersoft/ireport/components/table/table-48.png", this.getClass().getClassLoader());
        }
        return tableImage;
    }


}
