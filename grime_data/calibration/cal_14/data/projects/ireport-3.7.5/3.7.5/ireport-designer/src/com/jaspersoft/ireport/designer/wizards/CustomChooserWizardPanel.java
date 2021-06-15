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
package com.jaspersoft.ireport.designer.wizards;

import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.TemplateWizard;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;

public class CustomChooserWizardPanel implements WizardDescriptor.Panel {

    private WizardDescriptor wizard;
    
    public CustomChooserWizardPanel(WizardDescriptor wizard)
    {
        this.wizard = wizard;
    }
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private CustomChooserVisualPanel component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    public Component getComponent() {
        if (component == null) {
            component = new CustomChooserVisualPanel(this);
        }
        return component;
    }

    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
    // If you have context help:
    // return new HelpCtx(SampleWizardPanel1.class);
    }

    public boolean isValid() {
        if (component == null) return false;
        
        try {
            component.validateForm();
            getWizard().putProperty("WizardPanel_errorMessage", null);
            return true;
        } catch (Exception ex)
        {
            getWizard().putProperty("WizardPanel_errorMessage", ex.getMessage());
        }
        return false;
    }

    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0
        public final void addChangeListener(ChangeListener l) {
            synchronized (listeners) {
                listeners.add(l);
        }
    }
    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
    protected final void fireChangeEvent() {
        Iterator<ChangeListener> it;
        synchronized (listeners) {
            it = new HashSet<ChangeListener>(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            it.next().stateChanged(ev);
        }
    }
     

    // You can use a settings object to keep track of state. Normally the
    // settings object will be the WizardDescriptor, so you can use
    // WizardDescriptor.getProperty & putProperty to store information entered
    // by the user.
    public void readSettings(Object settings) {
    
        if (settings instanceof TemplateWizard)
        {
            try {
                if (((TemplateWizard) settings).getTargetFolder() != null)
                {
                    ((CustomChooserVisualPanel)getComponent()).setTargetDirectory( Misc.getDataFolderPath( ((TemplateWizard) settings).getTargetFolder()) );

                    String name = //((TemplateWizard) settings).getTargetName();
                            ((TemplateWizard) settings).getTemplate().getPrimaryFile().getNameExt();

                    
                    // check by extension...
                    if (name == null) name = "file";


                    String ext = "";



                    if (name.lastIndexOf(".") > 0)
                    {
                        ext = name.substring(name.lastIndexOf("."), name.length());
                        name = name.substring(0,name.lastIndexOf("."));
                    }
                    
                    DataObject dObj = ((TemplateWizard) settings).getTemplate();
                    if (dObj != null && dObj.getPrimaryFile().getAttribute("extension") != null)
                    {
                        ext = ""+dObj.getPrimaryFile().getAttribute("extension");
                        if (!ext.startsWith(".")) ext = "." + ext;
                    }
                    

                    if (ext.toLowerCase().equals(".properties"))
                    {
                        for (int i=0; i<1000; ++i)
                        {
                            String tmpName = "Bundle" + ((i>0) ? i+"" : "") ;
                            File f = new File( Misc.getDataFolderPath(  ((TemplateWizard) settings).getTargetFolder()), tmpName + ".properties");
                            if (f.exists()) continue;

                            component.setReportName(tmpName);
                            component.setExtension(".properties");
                            component.setNameLabel(I18n.getString("CustomChooserVisualPanel.Label.BundleName"));
                            break;
                        }
                    }
                    else if (ext.toLowerCase().equals(".jrxml"))
                    {
                        // Look for the first available reportX.jrxml
                        for (int i=1; i<1000; ++i)
                        {
                            // get the file extension...\
                            File f = new File( Misc.getDataFolderPath(  ((TemplateWizard) settings).getTargetFolder()), "report" + i + ".jrxml");
                            if (f.exists()) continue;

                            component.setReportName("report" + i);
                            component.setExtension(".jrxml");
                            component.setNameLabel(I18n.getString("CustomChooserVisualPanel.Label.ReportName"));
                            break;
                        }
                    }
                    else
                    {
                        for (int i=1; i<1000; ++i)
                        {
                            // get the file extension...\
                            File f = new File( Misc.getDataFolderPath(  ((TemplateWizard) settings).getTargetFolder()), name + i + ext);
                            if (f.exists()) continue;

                            component.setReportName(name + i);
                            component.setExtension(ext);
                            component.setNameLabel(I18n.getString("CustomChooserVisualPanel.Label.Name"));
                            break;
                        }
                    }
                }
                
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    public void storeSettings(Object settings) {
        
        ((WizardDescriptor)settings).putProperty("filename", component.getFileName() );
        ((WizardDescriptor)settings).putProperty("reportname", component.getReportName() );
        if (settings instanceof TemplateWizard)
        {
            File f = new File(component.getFileName());
            if (f.getParentFile() != null && f.getParentFile().exists())
            {
                IReportManager.getPreferences().put( IReportManager.CURRENT_DIRECTORY, f.getParent());
            }

            try {
                ((TemplateWizard)settings).setTargetFolder(DataFolder.findFolder( FileUtil.toFileObject(f.getParentFile())));
                ((TemplateWizard)settings).setTargetName(component.getReportName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public WizardDescriptor getWizard() {
        return wizard;
    }

    public void setWizard(WizardDescriptor wizard) {
        this.wizard = wizard;
    }
    
}

