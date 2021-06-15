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

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.utils.ImageExpressionFileResolver;
import com.jaspersoft.ireport.designer.utils.ProxyFileResolver;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.File;
import net.sf.jasperreports.engine.base.JRBaseLine;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.base.JRBaseStaticText;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.export.draw.DrawVisitor;
import net.sf.jasperreports.engine.util.JRResourcesUtil;
import org.openide.filesystems.FileUtil;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class JRDesignImageWidget extends JRDesignElementWidget implements Runnable {

    ImageExpressionFileResolver resolver = null;
    BufferedImage offScreenImage = null;
    boolean needReload = false;
    boolean running = false;

    public JRDesignImageWidget(AbstractReportObjectScene scene, JRDesignImage element) {
        super(scene, element);
    }
    
    
    @Override
    protected void paintWidgetImplementation() {

        if (getElement().getHeight() <= 0 || getElement().getWidth() <= 0)
        {
                return;
        }

        if (offScreenImage == null || needReload)
        {
            needReload = false;

            offScreenImage = new BufferedImage(getElement().getWidth(), getElement().getHeight(), BufferedImage.TYPE_INT_ARGB );

            Component component = ((AbstractReportObjectScene)getScene()).getView();
            JrxmlVisualView visualView = null;
            while (component.getParent() != null) //FIXME: the component could be not installed...
            {
                if (component.getParent() instanceof JrxmlVisualView)
                {
                    visualView = (JrxmlVisualView) component.getParent();
                    break;
                }
                else
                {
                    component = component.getParent();
                }
            }

            File reportFolder = null;
            if (visualView != null)
            {
                File file = FileUtil.toFile(visualView.getEditorSupport().getDataObject().getPrimaryFile());
                if (file.getParentFile() != null)
                {
                    reportFolder = file.getParentFile();
                }
            }

            ProxyFileResolver fileResolver = new ProxyFileResolver(IReportManager.getInstance().getFileResolvers());
            //fileResolver.addResolver(new SimpleFileResolver(reportFolder));//FIXMETD can we keep the parent folder somewhere? in the draw visitor maybe?

            if (visualView != null && visualView.getModel() != null && visualView.getModel().getJasperDesign() != null)
            {
                if (resolver == null)
                {
                    resolver = new ImageExpressionFileResolver((JRDesignImage)getElement(), reportFolder+"",  visualView.getModel().getJasperDesign());
                }
                else
                {
                    // check if something is changed...
                    try {
                        resolver.setImageElement((JRDesignImage)getElement());
                        resolver.setJasperDesign(visualView.getModel().getJasperDesign());
                        resolver.setReportFolder(reportFolder+"");
                    } catch (Exception ex)
                    {
                       ex.printStackTrace();
                    }
                }

                fileResolver.addResolver(resolver);
            }
            JRResourcesUtil.setThreadFileResolver(fileResolver);

            try
            {
                DrawVisitor dv = ((AbstractReportObjectScene)this.getScene()).getDrawVisitor();
                if (dv == null) return;
                dv.setGraphics2D((Graphics2D)offScreenImage.getGraphics());
                try {
                    getElement().visit( dv );
                } catch (Exception ex){
                    System.err.println("iReport - Element rendering exception " + getElement() + " " + ex.getMessage());
                }
            }
            catch (Exception ex)
            {
                System.err.println("iReport - Error painting image: " + ex.getMessage());
            }
            finally
            {
                JRResourcesUtil.resetThreadFileResolver();
            }
        }
        getGraphics().drawImage(offScreenImage, getBounds().x,getBounds().y, getBounds().width, getBounds().height, null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        
        String propertyName = evt.getPropertyName();
        if (propertyName == null) return;

        if (propertyName.equals( JRDesignElement.PROPERTY_HEIGHT) ||
            propertyName.equals( JRDesignElement.PROPERTY_WIDTH) ||
            propertyName.equals( JRBaseStyle.PROPERTY_BACKCOLOR) ||
            propertyName.equals( JRBaseStyle.PROPERTY_FORECOLOR) ||
            propertyName.equals( JRDesignElement.PROPERTY_PARENT_STYLE) ||
            propertyName.equals( JRDesignElement.PROPERTY_PARENT_STYLE_NAME_REFERENCE) ||
            propertyName.equals( JRBaseStyle.PROPERTY_MODE ) ||
            //FIXME propertyName.equals( JRDesignGraphicElement.PROPERTY_PEN) ||
            propertyName.equals( JRBaseStyle.PROPERTY_FILL) ||
            propertyName.equals( JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT) ||
            propertyName.equals( JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT) ||
            propertyName.equals( JRBaseStyle.PROPERTY_SCALE_IMAGE) ||
            propertyName.equals( JRDesignTextField.PROPERTY_EXPRESSION) ||
            propertyName.equals("pen") ||           // Special property fired by the property sheet
            propertyName.equals("linebox") ||       // Special property fired by the property sheet
            propertyName.equals(JRBasePen.PROPERTY_LINE_COLOR) ||
            propertyName.equals(JRBasePen.PROPERTY_LINE_STYLE) ||
            propertyName.equals(JRBasePen.PROPERTY_LINE_WIDTH) ||
            propertyName.equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING) ||
            propertyName.equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING) ||
            propertyName.equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING) ||
            propertyName.equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING)
            )
        {
            // Schedule a repoaint in 3 secs...
            if (running) return;
            Thread t = new Thread(this);
            t.start();
        }

        super.propertyChange(evt);
    }

    public void run() {

        running = true;
        try {
            Thread.sleep(3000);
        } catch (Exception ex)
        {}
        // scheduled a repaint ?
        needReload = true;
        Mutex.EVENT.readAccess(new Runnable() {

            public void run() {
                repaint();
            }
        });

        running = false;
    }


}
