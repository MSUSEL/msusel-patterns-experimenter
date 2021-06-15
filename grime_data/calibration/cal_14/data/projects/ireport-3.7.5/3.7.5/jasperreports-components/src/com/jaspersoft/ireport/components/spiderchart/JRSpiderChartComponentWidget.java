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
package com.jaspersoft.ireport.components.spiderchart;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import net.sf.jasperreports.components.spiderchart.SpiderChartComponent;
import net.sf.jasperreports.components.spiderchart.SpiderPlot;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;
import net.sf.jasperreports.components.spiderchart.StandardSpiderPlot;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class JRSpiderChartComponentWidget extends JRDesignElementWidget {

    private SpiderChartIcon spiderChartIcon = null;

    public JRSpiderChartComponentWidget(AbstractReportObjectScene scene, JRDesignElement element) {
        super(scene, element);

        if (((JRDesignComponentElement)element).getComponent() instanceof SpiderChartComponent)
        {
            SpiderChartComponent c = (SpiderChartComponent)((JRDesignComponentElement)element).getComponent();
            c.getEventSupport().addPropertyChangeListener(this);
            if (c.getPlot() != null)
            {
                ((StandardSpiderPlot)c.getPlot()).getEventSupport().addPropertyChangeListener(this);
            }
            if (c.getChartSettings() != null)
            {
                ((StandardChartSettings)c.getChartSettings()).getEventSupport().addPropertyChangeListener(this);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {


        if (evt.getPropertyName().equals(SpiderChartComponent.PROPERTY_CHART_SETTINGS) ||
            evt.getPropertyName().equals(SpiderChartComponent.PROPERTY_PLOT) ||

            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_LEGEND_COLOR) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_LEGEND_BACKGROUND_COLOR) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_LEGEND_FONT) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_LEGEND_POSITION) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_TITLE_EXPRESSION) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_TITLE_COLOR) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_TITLE_POSITION) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_TITLE_FONT) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_SHOW_LEGEND) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_SUBTITLE_COLOR) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_SUBTITLE_EXPRESSION) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_SUBTITLE_FONT) ||
            evt.getPropertyName().equals(StandardChartSettings.PROPERTY_BACKCOLOR) ||

            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_AXIS_LINE_COLOR) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_AXIS_LINE_WIDTH) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_BACKCOLOR) ||

            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_BACKGROUND_ALPHA) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_FOREGROUND_ALPHA) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_HEAD_PERCENT) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_INTERIOR_GAP) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_LABEL_COLOR) ||

            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_LABEL_FONT) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_LABEL_GAP) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_MAX_VALUE_EXPRESSION) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_ROTATION) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_START_ANGLE) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_TABLE_ORDER) ||
            evt.getPropertyName().equals(StandardSpiderPlot.PROPERTY_WEB_FILLED)
            )
        {
            updateBounds();
            this.repaint();
            this.revalidate(true);
            this.getSelectionWidget().updateBounds();
            this.getSelectionWidget().revalidate(true);
            getScene().validate();
        }
        
        super.propertyChange(evt);
    }

    //@Override
    protected void paintWidgetImplementation1() {


        if (spiderChartIcon == null && ((JRDesignComponentElement)getElement()).getComponent() instanceof SpiderChartComponent)
        {
            spiderChartIcon = new SpiderChartIcon();
        }

        if (spiderChartIcon != null && spiderChartIcon.getIcon(200) != null)
        {
            Graphics2D gr = getScene().getGraphics();
            java.awt.Rectangle r = getPreferredBounds();

            AffineTransform af = gr.getTransform();
            AffineTransform new_af = (AffineTransform) af.clone();
            AffineTransform translate = AffineTransform.getTranslateInstance(
                    getBorder().getInsets().left + r.x,
                    getBorder().getInsets().top + r.y);
            new_af.concatenate(translate);
            gr.setTransform(new_af);

            JRDesignElement e = this.getElement();

            //Composite oldComposite = gr.getComposite();
            Shape oldClip = gr.getClip();
            Shape rect = new Rectangle2D.Float(0,0,e.getWidth(), e.getHeight());
            gr.clip(rect);

            gr.setPaint(
                new GradientPaint(
                    0, e.getHeight(), new Color(255,255,255,(int)(0.25*255)),
                    e.getWidth(), 0, new Color(200,200,200,(int)(0.25*255) )
                )
            );
            gr.fillRect(0, 0, e.getWidth(), e.getHeight());
            //gr.setComposite(oldComposite);
            if (e.getWidth() > 10)
            {
                // Calculate the width....


                Image img_to_paint = spiderChartIcon.getIcon(Math.min( spiderChartIcon.getIcon().getIconWidth(), e.getWidth()),
                        Math.min( spiderChartIcon.getIcon().getIconHeight(), e.getHeight())).getImage();

                Composite oldComposite = gr.getComposite();
                gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                try {
                    gr.drawImage(img_to_paint, e.getWidth()/2 - img_to_paint.getWidth(null)/2,
                                e.getHeight()/2 - img_to_paint.getHeight(null)/2, null);
                } catch (Exception ex){}
                gr.setComposite(oldComposite);
            }
            gr.setClip(oldClip);

            gr.setTransform(af);

        }


    }

}
