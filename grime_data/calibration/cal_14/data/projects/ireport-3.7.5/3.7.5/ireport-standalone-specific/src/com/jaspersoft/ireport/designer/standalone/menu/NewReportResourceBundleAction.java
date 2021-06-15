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
package com.jaspersoft.ireport.designer.standalone.menu;

import com.jaspersoft.ireport.designer.standalone.IReportStandaloneManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.wizards.CustomTemplateWizard;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Dialog;
import java.io.File;
import org.openide.DialogDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

public final class NewReportResourceBundleAction extends CallableSystemAction {

    public void performAction() {
      
            CustomTemplateWizard wizardDescriptor = new CustomTemplateWizard();

            File targetFolder = Misc.findStartingDirectory();
            DataFolder df = DataFolder.findFolder(FileUtil.toFileObject(targetFolder));
            wizardDescriptor.setTargetFolder(df);
            wizardDescriptor.setTargetName("Bundle"); // NOI18N
            

            try {
                FileObject templateFileObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject("Templates/Resource_Bundle/Bundle.properties");
                wizardDescriptor.setTemplate(DataObject.find(templateFileObject));
            } catch (DataObjectNotFoundException ex) {
            }
            
            wizardDescriptor.setTitle("New Report Resource Bundle");
            Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
            dialog.setVisible(true);
            dialog.toFront();
         
    }

    public String getName() {
        return I18n.getString( IReportStandaloneManager.class, "CTL_NewReportBundle"); // NOI18N
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE); // NOI18N
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
