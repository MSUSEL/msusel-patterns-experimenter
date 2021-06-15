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
import com.jaspersoft.ireport.designer.templates.TemplatesFrame;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.wizards.CustomTemplateWizard;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.io.File;
import java.util.Enumeration;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.DialogDisplayer;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.loaders.TemplateWizard;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

public final class NewReportAction extends CallableSystemAction {


    static final String TEMPLATES_DIR="Templates/Report/";
    private FileObject nodesFileObject = null;

    private JMenu menu = null;

    public void performAction(String targetName, String templateName) {
      
            TemplateWizard wizardDescriptor = new CustomTemplateWizard();

            //wizardDescriptor.putProperty("useCustomChooserPanel", "true"); // NOI18N
            File targetFolder = Misc.findStartingDirectory();
            DataFolder df = DataFolder.findFolder(FileUtil.toFileObject(targetFolder));
            wizardDescriptor.setTargetFolder(df);
            wizardDescriptor.setTargetName(targetName); // NOI18N
            

            try {
                FileObject templateFileObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject(templateName);
                wizardDescriptor.setTemplate(DataObject.find(templateFileObject));
            } catch (DataObjectNotFoundException ex) {
            }
            
//            EmptyReportWizardIterator wIterator = new EmptyReportWizardIterator();
//            wIterator.initialize(wizardDescriptor);
//            wizardDescriptor.setPanelsAndSettings(wIterator, wizardDescriptor);

            // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
//            wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));

            wizardDescriptor.setTitle("New"); 
            Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
            dialog.setVisible(true);
            dialog.toFront();
         
    }

    public String getName() {
        return I18n.getString( IReportStandaloneManager.class, "CTL_NewReportAction"); // NOI18N
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

    /*
    @Override
    public JMenuItem getMenuPresenter() {
        
        refreshMenu();

        return menu;
    }
    */

    public void refreshMenu()
    {

        if (menu == null)
        {
            menu = new JMenu(I18n.getString( IReportStandaloneManager.class, "CTL_NewReportAction"));
            if (nodesFileObject == null)
            {
                nodesFileObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject(TEMPLATES_DIR);
                
                if (nodesFileObject != null)
                {
                    nodesFileObject.addFileChangeListener(new FileChangeListener() {

                        public void fileFolderCreated(FileEvent arg0) {
                        }

                        public void fileDataCreated(FileEvent arg0) {
                        }

                        public void fileChanged(FileEvent arg0) {
                            refreshMenu();
                        }

                        public void fileDeleted(FileEvent arg0) {
                            refreshMenu();
                        }

                        public void fileRenamed(FileRenameEvent arg0) {
                            refreshMenu();
                        }

                        public void fileAttributeChanged(FileAttributeEvent arg0) {
                        }
                    });
                }
            }
        }

        if (menu == null) return;
        
        menu.removeAll();
        // Load all the templates available under Templates/Report....
        
        if (nodesFileObject == null) return;
        DataFolder nodesDataFolder = DataFolder.findFolder(nodesFileObject);
        if (nodesDataFolder == null) return;

        Enumeration<DataObject> enObj = nodesDataFolder.children();
        while (enObj.hasMoreElements())
        {
            DataObject dataObject = enObj.nextElement();
            FileObject fileObject = dataObject.getPrimaryFile();
            final String name = fileObject.getNameExt();

            final String filePath= TEMPLATES_DIR + fileObject.getNameExt();

            JMenuItem subMenu = new JMenuItem(I18n.getString(filePath));
            subMenu.setIcon( new ImageIcon( dataObject.getNodeDelegate().getIcon( BeanInfo.ICON_COLOR_16x16)));
            subMenu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    performAction(name,filePath);
                }
            });

            menu.add(subMenu);
        }
    }



    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public void performAction() {
        // do nothing...
        TemplatesFrame td = new TemplatesFrame(Misc.getMainFrame(), true);
        td.setVisible(true);
    }
}
