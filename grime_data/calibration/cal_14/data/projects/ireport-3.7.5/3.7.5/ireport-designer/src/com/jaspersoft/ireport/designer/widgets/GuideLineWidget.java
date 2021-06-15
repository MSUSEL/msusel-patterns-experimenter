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
package com.jaspersoft.ireport.designer.widgets;

import com.jaspersoft.ireport.designer.utils.Java2DUtils;
import com.jaspersoft.ireport.designer.ruler.GuideLine;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import javax.swing.JComponent;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class GuideLineWidget extends Widget {
    
    private static final Stroke guideLineStroke = new BasicStroke (1.0f, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT, 5.0f, new float[] { 3.0f, 2.0f }, 0.0f);

    
    private GuideLine guideLine = null;

    public GuideLine getGuideLine() {
        return guideLine;
    }

    public void setGuideLine(GuideLine guideLine) {
        this.guideLine = guideLine;
    }
    
    public GuideLineWidget(Scene scene, GuideLine guideLine)
    {
        super(scene);
        this.guideLine = guideLine;
    }
    
    @Override
    protected void paintWidget() {
        
        Graphics2D g = this.getGraphics();
        
        Stroke oldStroke = g.getStroke();
        double zoom = getScene().getZoomFactor();
        Stroke bs = Java2DUtils.getInvertedZoomedStroke(guideLineStroke, zoom);
        g.setStroke(bs);
        g.setPaint(new Color(0,0,255,128));
        JComponent view = getScene().getView();
        
        Rectangle b = getBounds();
        g.drawLine( b.x, b.y, b.width, b.height);
        
/*
        if (getGuideLine().isVertical())
        {
            int w = (int)(view.getWidth()/getScene().getZoomFactor());
            if (getScene() instanceof ReportObjectScene)
            {
                ReportObjectScene ros = (ReportObjectScene)getScene();
                if (ros.getJasperDesign() != null)
                {
                    int w2 = ros.getJasperDesign().getPageWidth();
                    w = Math.min(w, w2);
                }
            }
            g.drawLine( (int)(-10*zoom), 0, w, 0);
        }
        else
        {
            int h = (int)(view.getHeight()/getScene().getZoomFactor());
            if (getScene() instanceof ReportObjectScene)
            {
                ReportObjectScene ros = (ReportObjectScene)getScene();
                if (ros.getJasperDesign() != null)
                {
                    int h2 = ModelUtils.getDesignHeight(ros.getJasperDesign());
                    h = Math.min(h, h2);
                }
            }
            g.drawLine( 0, (int)(-10*zoom), 0, h);
        }
        */
        
        
        g.setStroke(oldStroke);
    }
    
}
