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

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.ThreadUtils;
import com.jaspersoft.ireport.designer.utils.Java2DUtils;
import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.SeparatorWidget;

/**
 * A BandBorderWidget is just a line that is sensible to mouse dragging.
 * @author gtoffoli
 */
public class BandSeparatorWidget extends SeparatorWidget implements PropertyChangeListener {

    final private Stroke stroke = new BasicStroke(1.0f);
    final private JRBand band;

    public JRBand getBand() {
        return band;
    }

    public BandSeparatorWidget(ReportObjectScene scene, JRBand b) {
        super(scene, Orientation.HORIZONTAL);
        this.band=b;
        
        // We set a border to improve the sensible area....
        setBorder( BorderFactory.createEmptyBorder(0, 3) );
        setCursor( Cursor.getPredefinedCursor( Cursor.S_RESIZE_CURSOR) );
        setForeground(ReportObjectScene.DESIGN_LINE_COLOR);
        
        // Add a listener to the band changes...
        if (b instanceof JRDesignBand)
        {
            ((JRDesignBand)b).getEventSupport().addPropertyChangeListener(this);
            if (((JRDesignBand)b).getOrigin().getGroupName() != null)
            {
                String gname = ((JRDesignBand)b).getOrigin().getGroupName();
                JRDesignGroup group = (JRDesignGroup) scene.getJasperDesign().getGroupsMap().get(gname);
                if (group != null)
                {
                    group.getEventSupport().addPropertyChangeListener(JRDesignGroup.PROPERTY_NAME , this );
                }
            }
        }
        
        updateBounds();
    }
    
    public void updateBounds()
    {
        JasperDesign jd = ((ReportObjectScene)this.getScene()).getJasperDesign();
        setPreferredLocation(new Point( 0, ModelUtils.getBandLocation(band,jd) + band.getHeight()) );
        setPreferredBounds(new Rectangle( 0,-3,jd.getPageWidth(),7));
    }
    
    
    /**
     * Paints the separator widget.
     */
    @Override
    protected void paintWidget() {
        Graphics2D gr = getGraphics();
        gr.setColor (getForeground());
        Rectangle bounds = getBounds ();
        Insets insets = getBorder ().getInsets ();
        
        gr.setStroke( Java2DUtils.getInvertedZoomedStroke(stroke, 
                this.getScene().getZoomFactor()));
        if (getOrientation() == Orientation.HORIZONTAL)
        {
            Rectangle2D r = new Rectangle2D.Double(0.0,0.0, bounds.width - insets.left - insets.right, 0.0 );
            gr.draw(r);
        }
        else
        {
            Rectangle2D r = new Rectangle2D.Double(0.0,0.0,0.0,bounds.height - insets.top - insets.bottom );
            gr.draw(r);
        }

//        if (getBand() != null)
//        {
//            gr.drawString("" + ModelUtils.getMaxBandHeight((JRDesignBand)getBand(), ((ReportObjectScene)getScene()).getJasperDesign()), 0, 0);
//        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        Runnable r = null;
        
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignBand.PROPERTY_HEIGHT) ||
            evt.getPropertyName().equals( JRDesignGroup.PROPERTY_NAME))
        {
            r = new Runnable(){  
                 public void run()  {
                    ((ReportObjectScene)getScene()).refreshDocument();
                }};
        }
        else if (evt.getPropertyName().equals( JRDesignBand.PROPERTY_CHILDREN))
        {

            r = new Runnable(){  
                 public void run()  {
                    ((ReportObjectScene)getScene()).refreshElementGroup( (JRDesignBand)band);
                }};
        }
         
        if (r != null)
        {
             ThreadUtils.invokeInAWTThread(r);
        }
    }
}
