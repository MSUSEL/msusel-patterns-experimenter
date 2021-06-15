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
/***************************************************************************
 SettingsDialog.java    
 -----------------------------------------------------
 begin                : jun 2004
 copyright            : (C) 2004 by Thomas Alexandre
 email                : alexthomas(at)ganttproject.org
 ***************************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/

package net.sourceforge.ganttproject.gui.options;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.Mediator;
import net.sourceforge.ganttproject.gui.GeneralDialog;
import net.sourceforge.ganttproject.gui.options.model.OptionPageProvider;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author athomas Dialog to edit the preferences for the application.
 */
public class SettingsDialog extends GeneralDialog implements ActionListener

{

    boolean reinit = false; // If restart the initialization

    JButton restoreButton = null;

    /** Constructor. */
    public SettingsDialog(GanttProject parent) {
        super(parent, GanttProject.correctLabel(GanttLanguage.getInstance()
                .getText("settings")), true, new WelcomeSettingsPanel(parent));

        
        restoreButton = new JButton(language.getText("restoreDefaults"));
        restoreButton.setName("restore");
        restoreButton.addActionListener(this);
        if (southPanel != null) {
            southPanel.add(restoreButton);
        }
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (settingPanel!=null) {
                    settingPanel.rollback();
                }
            }
            
        });
    }

    /** Construct the menu settings. */
    public void constructSections() {
        addPagesFromProviders();
        DefaultMutableTreeNode projectNode = addObject(GanttProject
                .correctLabel(language.getText("project")), null);
        DefaultMutableTreeNode generalNode = addObject(language
                .getText("general"), null);
        DefaultMutableTreeNode exportNode = addObject(GanttProject
                .correctLabel(language.getText("export")), null);
        DefaultMutableTreeNode roleNode = addObject(language
                .getText("resourceRole"), null);

        // general section
        addObject(language.getText("parameters"), generalNode);
        addObject(language.getText("looknfeel"), generalNode);
        addObject(language.getText("languages"), generalNode);
        //addObject(language.getText("colors"), generalNode);

        // Export section
        // addObject ("html", exportNode);
        addObject("csv", exportNode);

        // Just to see the first level of the tree
        treeSections.scrollPathToVisible(new TreePath(projectNode.getPath()));
    }

    private void addPagesFromProviders() {
        Object[] extensions = Mediator.getPluginManager().getExtensions("net.sourceforge.ganttproject.OptionPageProvider", OptionPageProvider.class);
        for (int i=0; i<extensions.length; i++) {
            OptionPageProvider nextProvider = (OptionPageProvider) extensions[i];
            addObject(nextProvider, null);
        }
    }

    /** Callback for the tree selection event. */
    public void valueChanged(TreeSelectionEvent e) {
        if (reinit)
            return;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) (e.getPath()
                .getLastPathComponent());
        boolean bHasChange = settingPanel.applyChanges(true);

        // construct the sections
        if ((settingPanel instanceof LanguageSettingsPanel) && bHasChange) {
            reinit = true;
            rootNode.removeAllChildren();
            treeModel.setRoot(rootNode);
            treeModel.reload();
            constructSections();
            reinit = false;
        }

        // - remove the settingPanel
        mainPanel2.remove(0);
        
        Box vb = Box.createVerticalBox();        
        Object userObject = node.getUserObject();
        if (userObject instanceof OptionPageProvider) {
            settingPanel = new OptionPageProviderPanel((OptionPageProvider) userObject, getProject(), getUIFacade());
        }
        else {

        // - ask the settingPanel if parameters are changed

        // - Create the new panel
            String sNode = (String) (node.getUserObject());
            if (sNode.equals(language.getText("languages")))
                settingPanel = new LanguageSettingsPanel(appli);
    
            else if (sNode.equals(GanttProject.correctLabel(language
                    .getText("project"))))
                settingPanel = new ProjectSettingsPanel((Frame) getOwner(),
                        getProject());
    
            else if (sNode.equals(GanttProject.correctLabel(language
                    .getText("parameters"))))
                settingPanel = new ParametersSettingsPanel(appli);
    
            else if (sNode.equals(GanttProject.correctLabel(language
                    .getText("resourceRole"))))
                settingPanel = new RolesSettingsPanel(appli);
    
            else if (sNode.equals(language.getText("looknfeel")))
                settingPanel = new LnFSettingsPanel(appli);
    
            else if (sNode.equals(GanttProject.correctLabel(language
                    .getText("export"))))
                settingPanel = new ExportSettingsPanel(appli);
    
            else if (sNode.equals("csv"))
                settingPanel = new CSVSettingsPanel(appli);
    
            else
                settingPanel = new WelcomeSettingsPanel(appli);
            vb.add(new TopPanel("  " + settingPanel.getTitle(), settingPanel
                    .getComment()));
        }    
        // - initialize the panel
        settingPanel.initialize();
        vb.add(settingPanel.getComponent());

        // - add the settingPanel into the main Panel
        mainPanel2.add(vb, 0);
        mainPanel2.repaint();
        mainPanel2.validate(); // valide the changes
    }

    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() == restoreButton) {
            appli.restoreOptions();
            // refresh the current panel
            settingPanel.rollback();
        }
    }
    
    

}
