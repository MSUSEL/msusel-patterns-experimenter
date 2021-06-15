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
package com.jaspersoft.ireport.designer.jrctx;

import com.jaspersoft.ireport.designer.GenericCloseOperationHandler;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import net.sf.jasperreports.chartthemes.simple.ChartThemeSettings;
import net.sf.jasperreports.chartthemes.simple.XmlChartTheme;
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
public class JRCTXEditorSupport extends DataEditorSupport implements OpenCookie, EditorCookie, EditCookie, SaveAsCapable  {

    private static Logger LOG = Logger.getLogger(JRCTXEditorSupport.class.getName());
    
    private InstanceContent specialNodeLookupIC = null;
    private Lookup specialNodeLookup = null;
    
    private final SaveCookie saveCookie = new SaveCookie() {
        /** Implements <code>SaveCookie</code> interface. */
            public void save() throws IOException {
                saveDocument();
            }
        };

    final MultiViewDescription[] descriptions = {
        new JRCTXVisualView(this),
        new JRCTXTextView(this)
        //new JrxmlPreviewView(this)
    };
     
    
    private ChartThemeSettings currentModel = null;

    @Override
    protected Task reloadDocument() {
        
        // Force a document refresh...
        ((JRCTXVisualView)descriptions[0]).refreshModel();
        return super.reloadDocument();
    }


    public MultiViewDescription[] getDescriptions()
    {
        return descriptions;
    }

//    @Override
//    public void saveAs(FileObject arg0, String arg1) throws IOException {
//
//        JOptionPane.showMessageDialog(null, "My save as!!!!");
//        super.saveAs(arg0, arg1);
//    }


    
    private JRCTXEditorSupport(JRCTXDataObject obj) {
        super(obj, new JRCTXEnv(obj));
        specialNodeLookupIC = new InstanceContent();
        specialNodeLookupIC.add(this);
        specialNodeLookup = new AbstractLookup(specialNodeLookupIC);
    }
    
    public static JRCTXEditorSupport create(JRCTXDataObject obj) {
         JRCTXEditorSupport ed = new JRCTXEditorSupport(obj);
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
            JRCTXDataObject obj = (JRCTXDataObject)getDataObject();
            if(obj.getCookie(SaveCookie.class) == null) {
                obj.getIc().add( saveCookie );
                specialNodeLookupIC.add(saveCookie);
                obj.setModified(true);
            }
        }
        return retValue;
    }
    
    protected void notifyUnmodified() {
        super.notifyUnmodified();
        JRCTXDataObject obj = (JRCTXDataObject)getDataObject();
        
        Cookie cookie = obj.getCookie(SaveCookie.class);

        if(cookie != null && cookie.equals( saveCookie )) {
            obj.getIc().remove(saveCookie);
            specialNodeLookupIC.remove(saveCookie);
            obj.setModified(false);
        }
    }

    public void notifyModelChangeToTheView()
    {
        if (getCurrentModel() != null)
        {
            ((JRCTXVisualView)descriptions[0]).modelChanged();
            notifyModified();
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
                ChartThemeSettings jrctx = getCurrentModel();
                String content = null;
                try {

                    //TODO Save the file and read from it... no good actually...
                    // Save in the primary file...
                    content = XmlChartTheme.saveSettings(jrctx);

                    //FileUtil.toFile(getDataObject().getPrimaryFile())
                    //content = FileUtils.readFully( new FileReader( FileUtil.toFile(getDataObject().getPrimaryFile())));

                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(), "Error saving the JRCTX: " + ex.getMessage() + "\nSee the log file for more details.", "Error saving", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    return;
                }

                if (content != null)
                {
                    try {
                        getDocument().remove(0, getDocument().getLength());
                        getDocument().insertString(0, content, null);
                        ((JRCTXVisualView) descriptions[0]).setNeedModelRefresh(false);
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }

            super.saveDocument();
       }

    public Lookup getSpecialNodeLookup() {
        return specialNodeLookup;
    }

    public void setSpecialNodeLookup(Lookup specialNodeLookup) {
        this.specialNodeLookup = specialNodeLookup;
    }
    
    public static final class JRCTXEnv extends DataEditorSupport.Env {
    
        public JRCTXEnv(JRCTXDataObject obj) {
            super(obj);
        }
        
        protected FileObject getFile() {
            return super.getDataObject().getPrimaryFile();
        }
        
        protected FileLock takeLock() throws IOException {
            return ((JRCTXDataObject)super.getDataObject()).getPrimaryEntry().takeLock();
        }

    }

    public ChartThemeSettings getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(ChartThemeSettings currentModel) {
        
        // Update the lookup...
        if (this.currentModel != null)
        {
            ((JRCTXDataObject)getDataObject()).getIc().remove(this.currentModel);
        }
        this.currentModel = currentModel;
        if (this.currentModel != null)
        {
            ((JRCTXDataObject)getDataObject()).getIc().add(this.currentModel);
        }
    }


}
