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
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.SeparatorWidget;

/**
 * @author Teodor Danciu (teodor@users.sourceforge.net)
 */
public class BandWidget extends SeparatorWidget implements PropertyChangeListener 
{
    //final private Stroke stroke = new BasicStroke(1.0f);
    final private JRBand band;

    public BandWidget(ReportObjectScene scene, JRBand band) 
    {
        super(scene, Orientation.HORIZONTAL);
        this.band = band;
        
        // We set a border to improve the sensible area....
//        setBorder( BorderFactory.createEmptyBorder(0, 3) );
//        setCursor( Cursor.getPredefinedCursor( Cursor.S_RESIZE_CURSOR) );
//        setForeground(ReportObjectScene.DESIGN_LINE_COLOR);
        
        // Add a listener to the band changes...
        if (band instanceof JRDesignBand)
        {
            ((JRDesignBand)band).getEventSupport().addPropertyChangeListener(this);
        }
        
        setToolTipText(ModelUtils.nameOf(band, scene.getJasperDesign()));
        
        updateBounds();
    }
    
    public JRBand getBand() {
        return band;
    }

    public void updateBounds()
    {
        JasperDesign jasperDesign = ((ReportObjectScene)this.getScene()).getJasperDesign();
        setPreferredLocation(new Point( 0, ModelUtils.getBandLocation(band,jasperDesign)) );
        setPreferredBounds(new Rectangle(0, 0, jasperDesign.getPageWidth(), band.getHeight()));
    }
    
    
    /**
     * Paints the separator widget.
     */
    @Override
    protected void paintWidget() 
    {
//        Graphics2D gr = getGraphics();
//        gr.setColor( new Color((int)(255*Math.random()), (int)(255*Math.random()), (int)(255*Math.random())) );
//        Rectangle bounds = getBounds ();
//        Insets insets = getBorder ().getInsets ();
//        
//        gr.fillRect((int)bounds.getX(), (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        Runnable r = null;
        
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignBand.PROPERTY_HEIGHT))
        {
            r = new Runnable(){  
                 public void run()  {
                    ((ReportObjectScene)getScene()).refreshDocument();
                }};
        }
//      The children update is performed by the band separators widget!!!!  
//        else if (evt.getPropertyName().equals( JRDesignBand.PROPERTY_CHILDREN))
//        {
//            r = new Runnable(){  
//                 public void run()  {
//                    ((ReportObjectScene)getScene()).refreshElementGroup( (JRDesignBand)band);
//                }};
//        }
         
        if (r != null)
        {
             ThreadUtils.invokeInAWTThread(r);
        }
    }
}
