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
package com.jaspersoft.ireport.designer.jrtx;

import com.jaspersoft.ireport.designer.GenericCloseOperationHandler;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.xml.JRXmlTemplateWriter;
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
import org.openide.util.Task;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author gtoffoli
 */
public class JRTXEditorSupport extends DataEditorSupport implements OpenCookie, EditorCookie, EditCookie, SaveAsCapable  {

    private static Logger LOG = Logger.getLogger(JRTXEditorSupport.class.getName());
    
    private InstanceContent specialNodeLookupIC = null;
    private Lookup specialNodeLookup = null;
    
    private final SaveCookie saveCookie = new SaveCookie() {
        /** Implements <code>SaveCookie</code> interface. */
            public void save() throws IOException {
                saveDocument();
            }
        };

    final MultiViewDescription[] descriptions = {
        new JRTXVisualView(this),
        new JRTXTextView(this)
        //new JrxmlPreviewView(this)
    };
     
    
    private JRSimpleTemplate currentModel = null;

    @Override
    protected Task reloadDocument() {
        
        // Force a document refresh...
        ((JRTXVisualView)descriptions[0]).refreshModel();
        return super.reloadDocument();
    }


    public MultiViewDescription[] getDescriptions()
    {
        return descriptions;
    }

    
    private JRTXEditorSupport(JRTXDataObject obj) {
        super(obj, new JRTXEnv(obj));
        specialNodeLookupIC = new InstanceContent();
        specialNodeLookupIC.add(this);
        specialNodeLookup = new AbstractLookup(specialNodeLookupIC);
    }
    
    public static JRTXEditorSupport create(JRTXDataObject obj) {
         JRTXEditorSupport ed = new JRTXEditorSupport(obj);
         ed.setMIMEType("text/xml");
         return ed;
    }



    protected CloneableEditorSupport.Pane createPane() {
        return (CloneableEditorSupport.Pane)MultiViewFactory.
                createCloneableMultiView(descriptions, descriptions[0], new GenericCloseOperationHandler(this));
    }
    
    protected boolean notifyModified() {
        boolean retValue;
        retValue = super.notifyModified();
        if (retValue) {
            JRTXDataObject obj = (JRTXDataObject)getDataObject();
            if(obj.getCookie(SaveCookie.class) == null) {
                obj.getIc().add( saveCookie );
                specialNodeLookupIC.add(saveCookie);
                obj.setModified(true);
            }
        }
        return retValue;
    }

    public void notifyModelChangeToTheView()
    {
        if (getCurrentModel() != null)
        {
            ((JRTXVisualView)descriptions[0]).modelChanged();
            notifyModified();
        }
    }


    protected void notifyUnmodified() {
        super.notifyUnmodified();
        JRTXDataObject obj = (JRTXDataObject)getDataObject();
        
        Cookie cookie = obj.getCookie(SaveCookie.class);

        if(cookie != null && cookie.equals( saveCookie )) {
            obj.getIc().remove(saveCookie);
            specialNodeLookupIC.remove(saveCookie);
            obj.setModified(false);
        }
    }
    
    public DataEditorSupport.Env  getEnv()
    {
        return (Env)this.env;
    }
    
    public void saveDocument() throws IOException {
            
            if (getCurrentModel() != null)
            {
                //set the document content...
                JRSimpleTemplate jrtx = getCurrentModel();
                String content = null;
                try {
                    content = JRXmlTemplateWriter.writeTemplate(jrtx, "UTF-8"); // IReportManager.getInstance().getProperty("jrxmlEncoding", System.getProperty("file.encoding") ));
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(), "Error saving the JRTX: " + ex.getMessage() + "\nSee the log file for more details.", "Error saving", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    return;
                }

                if (content != null)
                {
                    try {
                        getDocument().remove(0, getDocument().getLength());
                        getDocument().insertString(0, content, null);
                        ((JRTXVisualView) descriptions[0]).setNeedModelRefresh(false);
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }

            Charset cs = FileEncodingQuery.getDefaultEncoding();
            String fileEncoding = System.getProperty("file.encoding", "UTF-8");
            try {

                FileEncodingQuery.setDefaultEncoding(Charset.forName("UTF-8"));
                //System.setProperty("file.encoding", "UTF-8");
            } catch (Exception ex)
            {
                //System.out.println("UTF-8 encoding not supported!");
                //System.out.flush();
            }

            //getDataObject().getPrimaryFile().setAttribute(EDITOR_MODE, this)
            try {
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
    
    public static final class JRTXEnv extends DataEditorSupport.Env {
    
        public JRTXEnv(JRTXDataObject obj) {
            super(obj);
        }
        
        protected FileObject getFile() {
            return super.getDataObject().getPrimaryFile();
        }
        
        protected FileLock takeLock() throws IOException {
            return ((JRTXDataObject)super.getDataObject()).getPrimaryEntry().takeLock();
        }

    }

    public JRSimpleTemplate getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(JRSimpleTemplate currentModel) {
        
        // Update the lookup...
        if (this.currentModel != null)
        {
            ((JRTXDataObject)getDataObject()).getIc().remove(this.currentModel);
        }
        this.currentModel = currentModel;
        if (this.currentModel != null)
        {
            ((JRTXDataObject)getDataObject()).getIc().add(this.currentModel);
        }
    }


}
