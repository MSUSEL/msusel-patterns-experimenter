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
package com.jaspersoft.ireport.jasperserver.ui.actions;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.widgets.JRDesignImageWidget;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepoImageCache;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.Collection;
import java.util.List;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRExpressionUtil;
import org.netbeans.api.visual.widget.Widget;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.EditorCookie;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Mutex;
import org.openide.util.RequestProcessor;

/**
 *
 * @author gtoffoli
 */
public class JrxmLookupListener implements LookupListener {
    
    private final Lookup.Result <JasperDesign> result;
    private RepositoryReportUnit parentReportUnit = null;
    private JServer server = null;
    
    /**
     * This is a pointer to the list that holds this object preventig it is removed
     * from the garbage collector. This class will remove hisself from the list
     * to destroy itself when his job is done.
     */
    List jrxmlListeners = null;
    
    public JrxmLookupListener(DataObject obj, 
                              List jrxmlListeners,
                              RepositoryReportUnit parentReportUnit,
                              JServer server)
    {
        this.server = server;
        this.parentReportUnit = parentReportUnit;
        
        result = obj.getLookup().lookup(new Lookup.Template(JasperDesign.class));
        result.addLookupListener(this);
        result.allItems();
        resultChanged(null);
        this.jrxmlListeners = jrxmlListeners;
                                
    }
    
    public void resultChanged(LookupEvent ev) {
        
        
        Collection<? extends JasperDesign> jds = result.allInstances();
        if (jds.size() > 0)
        {
            // JasperDesign found...
            final JasperDesign jd = jds.iterator().next();

            // Look inside the jasperdesign for all the images having a simple expression
            // starting with repo: ....
            Runnable run = new Runnable() {

                public void run() {

                    // Set the properties useful to run this report...
                    if (parentReportUnit != null)
                    {
                        jd.setProperty("ireport.jasperserver.reportUnit", parentReportUnit.getDescriptor().getUriString());
                    }
                    else jd.getPropertiesMap().removeProperty("ireport.jasperserver.reportUnit");

                    jd.setProperty("ireport.jasperserver.url", server.getUrl());


                    List<JRDesignElement> elements = ModelUtils.getAllElements(jd, true);
                    for (JRDesignElement ele : elements)
                    {
                        if (ele instanceof JRDesignImage)
                        {
                            JRDesignImage img = (JRDesignImage)ele;
                            if (img.getExpression() != null &&
                                img.getExpression().getText() != null &&
                                img.getExpression().getValueClassName().equals("java.lang.String"))
                            {
                                String s = JRExpressionUtil.getSimpleExpressionText(img.getExpression());
                                if (s.startsWith("repo:"))
                                {
                                    String uri = s.substring(5);
                                    
                                    ResourceDescriptor rd = new ResourceDescriptor();
                                    if (!uri.startsWith("/") && getParentReportUnit() != null)
                                    {
                                        uri = getParentReportUnit().getDescriptor().getUriString() + "_files/" + uri;
                                    }
                                    rd.setUriString(uri);
                                    try {
                                        rd = getServer().getWSClient().get(rd, null);
                                        String fname = JasperServerManager.createTmpFileName("img","");
                                        rd = getServer().getWSClient().get(rd, new File(fname));
                                        RepoImageCache.getInstance().put(s, new File(fname));
                                        System.out.println("Added to the image cache the file: " + s + " " + fname + " " + new File(fname).length());
                                        System.out.flush();
                                    } catch (Exception ex)
                                    {
                                       ex.printStackTrace(); 
                                    }
                                }
                            }
                        }
                    }
                    
                    Mutex.EVENT.readAccess(new Runnable() {
                        public void run() {
                            try {
                                ReportObjectScene scene = IReportManager.getInstance().getActiveVisualView().getReportDesignerPanel().getScene();
                                //scene.refreshBands();
                                List<Widget> widgets = scene.getElementsLayer().getChildren();
                                for (Widget w : widgets)
                                {
                                    if (w instanceof JRDesignImageWidget)
                                    {
                                        ((JRDesignImageWidget)w).propertyChange(new PropertyChangeEvent(this, JRDesignImage.PROPERTY_EXPRESSION, null, null));
                                    }
                                }


                                // Display warning in case of Ad Hoc document...

                                if (jd.getProperty("com.jaspersoft.ji.adhoc") != null &&
                                    jd.getProperty("com.jaspersoft.ji.adhoc").equals("1"))
                                {
                                    try {

                                        SwingUtilities.invokeLater( new Runnable() {

                                           public void run()
                                           {
                                               if (javax.swing.JOptionPane.showConfirmDialog(Misc.getMainFrame(),
                                                JasperServerManager.getString("messages.adhoc", "You have selected to edit an Ad Hoc report.\n" +
                                               "If you continue, the report will lose its sorting and grouping.\n" +
                                               "Furthermore, any changes you make in iReport will be lost\n" +
                                               "next Time you edit it via the Ad Hoc report editor.\nContinue anyway?"),
                                               JasperServerManager.getString("alert","Alert!"),
                                               javax.swing.JOptionPane.YES_NO_OPTION,
                                               javax.swing.JOptionPane.WARNING_MESSAGE) == javax.swing.JOptionPane.NO_OPTION)
                                               {
                                                   // Close the document...
                                                   DataObject dobj = IReportManager.getInstance().getActiveVisualView().getLookup().lookup(DataObject.class);
                                                   EditorCookie cc = dobj.getCookie(EditorCookie.class);
                                                   if (cc != null)
                                                   {
                                                        cc.close();
                                                   }
                                                   else
                                                   {
                                                       System.out.println("Unable to close the window!!!");
                                                       System.out.flush();
                                                   }
                                               }
                                           }

                                        });

                                    } catch (Exception ex)
                                    {

                                    }

                                }

                            } catch (Exception ex) { }
                        }
                    });
                }
            };
            
            RequestProcessor.getDefault().post(run);
            
            result.removeLookupListener(this);
            if (jrxmlListeners != null)
            {
                jrxmlListeners.remove(this);
            }
        }
    }

    public RepositoryReportUnit getParentReportUnit() {
        return parentReportUnit;
    }

    public void setParentReportUnit(RepositoryReportUnit parentReportUnit) {
        this.parentReportUnit = parentReportUnit;
    }

    public JServer getServer() {
        return server;
    }

    public void setServer(JServer server) {
        this.server = server;
    }

}
