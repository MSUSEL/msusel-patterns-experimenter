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
package com.jaspersoft.ireport.components.list;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.utils.Java2DUtils;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import net.sf.jasperreports.components.list.DesignListContents;
import net.sf.jasperreports.components.list.ListComponent;
import net.sf.jasperreports.components.list.StandardListComponent;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class JRListComponentWidget extends JRDesignElementWidget {

    private static final BasicStroke DOTTED_LINE = new BasicStroke(
      1f,
      BasicStroke.CAP_ROUND,
      BasicStroke.JOIN_ROUND,
      1f,
      new float[] {2f},
      0f);

    private Image chartImage = null;

    public JRListComponentWidget(AbstractReportObjectScene scene, JRDesignElement element) {
        super(scene, element);

        if (((JRDesignComponentElement)element).getComponent() instanceof ListComponent)
        {
            ListComponent c = (ListComponent) ((JRDesignComponentElement)element).getComponent();
            DesignListContents contents = (DesignListContents) c.getContents();
            contents.getEventSupport().addPropertyChangeListener(this);
        }

        PropertyChangeListener pcl = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                    ListComponent c = (ListComponent) ((JRDesignComponentElement)getElement()).getComponent();
                    DesignListContents contents = (DesignListContents) c.getContents();
                    contents.setHeight( getElement().getHeight() );
                    contents.setWidth( getElement().getWidth() );
            }
        };

        getElement().getEventSupport().addPropertyChangeListener(JRDesignElement.PROPERTY_HEIGHT, pcl);
        getElement().getEventSupport().addPropertyChangeListener(JRDesignElement.PROPERTY_WIDTH, pcl);

    }




    
    @Override
    protected void paintWidgetImplementation() {

        super.paintWidgetImplementation();

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

        JRDesignElement element = this.getElement();
        
        StandardListComponent component = (StandardListComponent) ((JRDesignComponentElement)getElement()).getComponent();
        
        Shape oldClip = gr.getClip();
        Shape rect = new Rectangle2D.Float(0,0,element.getWidth(), element.getHeight());

        Stroke oldStroke = gr.getStroke();
        gr.clip(rect);
        gr.setStroke(DOTTED_LINE);
        gr.setColor(ReportObjectScene.DESIGN_LINE_COLOR );
        gr.drawLine( 0, component.getContents().getHeight() , element.getWidth(), component.getContents().getHeight());

        if (component.getContents().getWidth() != null && component.getContents().getWidth().intValue() != 0)
        {
            gr.drawLine( component.getContents().getWidth(), 0 , component.getContents().getWidth(), element.getHeight());
        }

        gr.setClip(oldClip);
        gr.setStroke(oldStroke);

        gr.setTransform(af);
    }
    
    

    public java.awt.Image getChartImage() {

        if (chartImage == null)
        {
            chartImage = Misc.loadImageFromResources("/com/jaspersoft/ireport/components/list/component.png", this.getClass().getClassLoader());
        }
        return chartImage;
    }

    @Override
    public List getChildrenElements() {

        JRDesignComponentElement component = (JRDesignComponentElement)getElement();
        if (component.getComponent() instanceof ListComponent)
        {
            ListComponent c = (ListComponent) component.getComponent();
            DesignListContents contents = (DesignListContents) c.getContents();
            return contents.getChildren();
        }
        
        return null;
    }

    /**
     * If a widget can have sub elements, this is the way the elements are
     * added.
     * @param element
     */
    public void addElement(JRDesignElement element)
    {
        JRDesignComponentElement component = (JRDesignComponentElement)getElement();
        if (component.getComponent() instanceof ListComponent)
        {
            ListComponent c = (ListComponent) component.getComponent();
            DesignListContents contents = (DesignListContents) c.getContents();
            contents.addElement(element);
        }
    }


}
