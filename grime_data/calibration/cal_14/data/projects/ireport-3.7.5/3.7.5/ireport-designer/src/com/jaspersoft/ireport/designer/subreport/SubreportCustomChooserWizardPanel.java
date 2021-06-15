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
package com.jaspersoft.ireport.designer.subreport;

import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.wizards.*;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.TemplateWizard;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;

public class SubreportCustomChooserWizardPanel extends CustomChooserWizardPanel {
   
    public SubreportCustomChooserWizardPanel(WizardDescriptor wizard)
    {
        super(wizard);
    }
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private SubreportCustomChooserVisualPanel component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    public Component getComponent() {
        if (component == null) {
            component = new SubreportCustomChooserVisualPanel(this);
        }
        return component;
    }
    
    public CustomChooserVisualPanel getSuperComponent() {
        return (CustomChooserVisualPanel)super.getComponent();
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
                    getSuperComponent().setTargetDirectory( Misc.getDataFolderPath(((TemplateWizard) settings).getTargetFolder()) );
                    if (((TemplateWizard) settings).getTargetName() != null)
                    {
                        getSuperComponent().setReportName(  ((TemplateWizard) settings).getTargetName() );
                    }
                    else
                    {
                        // Look for the first available reportX.jrxml
                        for (int i=1; i<100; ++i)
                        {
                            File f = new File( Misc.getDataFolderPath(((TemplateWizard) settings).getTargetFolder()), "subreport" + i +".jrxml");
                            if (f.exists()) continue;

                            getSuperComponent().setReportName("subreport" + i);
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
        
        if (settings instanceof TemplateWizard)
        {
            File f = new File(getSuperComponent().getFileName());
            ((TemplateWizard) settings).setTargetFolder( DataFolder.findFolder( FileUtil.toFileObject(f.getParentFile())) );
            
            ((TemplateWizard) settings).setTargetName(getSuperComponent().getReportName());
        }
        ((WizardDescriptor)settings).putProperty("filename", getSuperComponent().getFileName() );
        ((WizardDescriptor)settings).putProperty("reportname", getSuperComponent().getReportName() );
        
        ((WizardDescriptor)settings).putProperty("addsubreportparameter", component.isAddSubreportParameter() );
        ((WizardDescriptor)settings).putProperty("subreportexpression", component.getSubreportExpression() );
        
        
    }

}

