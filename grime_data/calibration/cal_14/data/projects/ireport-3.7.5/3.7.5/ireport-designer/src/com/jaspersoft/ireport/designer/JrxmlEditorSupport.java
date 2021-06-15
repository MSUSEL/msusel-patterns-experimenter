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
package com.jaspersoft.ireport.designer;

import com.jaspersoft.ireport.JrxmlDataObject;
import com.jaspersoft.ireport.designer.compatibility.JRXmlWriterHelper;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import org.netbeans.api.queries.FileEncodingQuery;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.cookies.EditCookie;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.loaders.SaveAsCapable;
import org.openide.nodes.Node.Cookie;
import org.openide.text.CloneableEditorSupport;
import org.openide.text.DataEditorSupport;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Mutex;
import org.openide.util.Task;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author gtoffoli
 */
public class JrxmlEditorSupport extends DataEditorSupport implements OpenCookie, EditorCookie, EditCookie, SaveAsCapable  {

    
    private static Logger LOG = Logger.getLogger(JrxmlEditorSupport.class.getName());
    
    private InstanceContent specialNodeLookupIC = null;
    private Lookup specialNodeLookup = null;
    
    private final SaveCookie saveCookie = new SaveCookie() {
        /** Implements <code>SaveCookie</code> interface. */
            public void save() throws IOException {
                saveDocument();
            }
        };


    final MultiViewDescription[] descriptions;
    
    private JasperDesign currentModel = null;

    @Override
    protected Task reloadDocument() {
        
        // Force a document refresh...
        ((JrxmlVisualView)descriptions[0]).refreshModel();
        return super.reloadDocument();
    }
    
    public MultiViewDescription[] getDescriptions()
    {
        return descriptions;
    }
    
    private JrxmlEditorSupport(JrxmlDataObject obj) {
        super(obj, new JrxmlEnv(obj));
        
        JrxmlVisualView visualview = new JrxmlVisualView(this);
        descriptions = new MultiViewDescription[]{
            visualview,
            new JrxmlTextView(this),
            new JrxmlPreviewView(this,visualview)
        };
        
        specialNodeLookupIC = new InstanceContent();
        specialNodeLookup = new AbstractLookup(specialNodeLookupIC);

    }
    
    public static JrxmlEditorSupport create(JrxmlDataObject obj) {
         JrxmlEditorSupport ed = new JrxmlEditorSupport(obj);
         ed.setMIMEType("text/xml");
         return ed;
    }



    protected CloneableEditorSupport.Pane createPane() {
        return (CloneableEditorSupport.Pane)MultiViewFactory.
                createCloneableMultiView(descriptions, descriptions[0], new GenericCloseOperationHandler(this));
    }
    
    public boolean notifyModified() {
        boolean retValue;
        retValue = super.notifyModified();
        if (retValue) {
            JrxmlDataObject obj = (JrxmlDataObject)getDataObject();
            if(obj.getCookie(SaveCookie.class) == null) {
                obj.getIc().add( saveCookie );
                specialNodeLookupIC.add(saveCookie);
                obj.setModified(true);
                //((JrxmlDataNode)obj.getNodeDelegate()).cookieSetChanged();
            }
            ((JrxmlPreviewView)descriptions[2]).setNeedRefresh(true);
            //((JrxmlVisualView)descriptions[0]).fireModelChange();
        }
        return retValue;
    }
    
    public void notifyUnmodified() {
        super.notifyUnmodified();
        JrxmlDataObject obj = (JrxmlDataObject)getDataObject();
        
        Cookie cookie = obj.getCookie(SaveCookie.class);

        if(cookie != null && cookie.equals( saveCookie )) {
            obj.getIc().remove(saveCookie);
            specialNodeLookupIC.remove(saveCookie);
            obj.setModified(false);
            //((JrxmlDataNode)obj.getNodeDelegate()).cookieSetChanged();
        }
    }

    @Override
    public void saveAs(FileObject folder, String fileName) throws IOException {

        if (getCurrentModel() != null)
        {
            //set the document content...
            JasperDesign jd = getCurrentModel();

            // Call the decorators...



            String content = null;
            try {

                final String compatibility = IReportManager.getPreferences().get("compatibility", "");

                if (compatibility.length() == 0)
                {
                    content = JRXmlWriter.writeReport(jd, "UTF-8"); // IReportManager.getInstance().getProperty("jrxmlEncoding", System.getProperty("file.encoding") ));
                }
                else
                {
                    content = JRXmlWriterHelper.writeReport(jd, "UTF-8", compatibility);
                }
            } catch (Exception ex)
            {
                content = null;
            }

            if (content != null)
            {
                try {
                    getDocument().remove(0, getDocument().getLength());
                    getDocument().insertString(0, content, null);
                } catch (BadLocationException ex) {

                }
            }
        }


        super.saveAs(folder, fileName);
    }




    public DataEditorSupport.Env  getEnv()
    {
        return (Env)this.env;
    }
    
    public void saveDocument() throws IOException {
            
            if (getCurrentModel() != null)
            {
                //set the document content...
                JasperDesign jd = getCurrentModel();
                
                // Store some info like zoom factor a position...
                if ((getDescriptions()[0]) != null &&
                     ((JrxmlVisualView)getDescriptions()[0]).getModel() != null)
                {
                    ReportObjectScene scene = ((JrxmlVisualView)getDescriptions()[0]).getReportDesignerPanel().getScene();
                    double zoomFactor = scene.getZoomFactor();
                    int x = scene.getView().getVisibleRect().x;
                    int y = scene.getView().getVisibleRect().y;

                    if (IReportManager.getPreferences().getBoolean("save_zoom_and_location", true))
                    {
                        jd.setProperty("ireport.zoom", ""+zoomFactor);
                        jd.setProperty("ireport.x", ""+x);
                        jd.setProperty("ireport.y", ""+y);
                    }
                }

                String content = null;
                try {
                    // Check the compatibility...
                    final String compatibility = IReportManager.getPreferences().get("compatibility", "");

                    if (compatibility.length() == 0)
                    {
                        content = JRXmlWriter.writeReport(jd, "UTF-8"); // IReportManager.getInstance().getProperty("jrxmlEncoding", System.getProperty("file.encoding") ));
                    }
                    else
                    {
                        content = JRXmlWriterHelper.writeReport(jd, "UTF-8", compatibility);
                    }
                    
                } catch (final Exception ex)
                {
                    Mutex.EVENT.readAccess(new Runnable() {

                    public void run() {
                        JOptionPane.showMessageDialog(Misc.getMainWindow(), "Error saving the JRXML: " + ex.getMessage() + "\nSee the log file for more details.", "Error saving", JOptionPane.ERROR_MESSAGE);

                    }
                   });
                    ex.printStackTrace();
                    return;
                }

                if (content != null)
                {
                    try {
                        getDocument().remove(0, getDocument().getLength());
                        getDocument().insertString(0, content, null);
                        ((JrxmlVisualView) descriptions[0]).setNeedModelRefresh(false);
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }

            //System.out.println("Saved with the encoding: " + FileEncodingQuery.getEncoding(getDataObject().getPrimaryFile()) + "  system def: " + System.getProperty("file.encoding") );
            //System.out.flush();


            Charset cs = FileEncodingQuery.getDefaultEncoding();
            String fileEncoding = System.getProperty("file.encoding", "UTF-8");
            try {

                FileEncodingQuery.setDefaultEncoding(Charset.forName("UTF-8"));
                System.setProperty("file.encoding", "UTF-8");
            } catch (Exception ex)
            {
                System.out.println("UTF-8 encoding not supported!");
                System.out.flush();
            }

            //getDataObject().getPrimaryFile().setAttribute(EDITOR_MODE, this)
            try {
                this.getDataObject().getPrimaryFile().setAttribute(EDITOR_MODE, cs);
                super.saveDocument();
            } finally
            {
                FileEncodingQuery.setDefaultEncoding(cs);
                System.setProperty("file.encoding", fileEncoding);
            }

        }

    public Lookup getSpecialNodeLookup() {
        return specialNodeLookup;
    }

    public void setSpecialNodeLookup(Lookup specialNodeLookup) {
        this.specialNodeLookup = specialNodeLookup;
    }
    
    public static final class JrxmlEnv extends DataEditorSupport.Env {
    
        public JrxmlEnv(JrxmlDataObject obj) {
            super(obj);
        }
        
        protected FileObject getFile() {
            return super.getDataObject().getPrimaryFile();
        }
        
        protected FileLock takeLock() throws IOException {
            return ((JrxmlDataObject)super.getDataObject()).getPrimaryEntry().takeLock();
        }

    }

    public JasperDesign getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(JasperDesign currentModel) {
        
        // Update the lookup...
        if (this.currentModel != null)
        {
            ((JrxmlDataObject)getDataObject()).getIc().remove(this.currentModel);
        }

        this.currentModel = currentModel;
        if (this.currentModel != null)
        {
            ((JrxmlDataObject)getDataObject()).getIc().add(this.currentModel);
        }

    }
}
