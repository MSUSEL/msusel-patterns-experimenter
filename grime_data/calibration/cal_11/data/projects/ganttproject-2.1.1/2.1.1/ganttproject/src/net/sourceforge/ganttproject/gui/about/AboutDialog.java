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
 AboutDialog.java 
 ------------------------------------------
 begin                : 29 juin 2004
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
package net.sourceforge.ganttproject.gui.about;

import javax.swing.Box;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.gui.GeneralDialog;
import net.sourceforge.ganttproject.gui.options.TopPanel;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author athomas New dialog box for about GanttProject :)
 */
public class AboutDialog extends GeneralDialog {

    /** Constructor. */
    public AboutDialog(GanttProject parent) {
        super(parent, GanttProject.correctLabel(GanttLanguage.getInstance()
                .getText("about"))
                + " - Ganttproject", true, new AboutPanel(parent));

        // hide the cancel button
        cancelButton.hide();
    }

    /** Callback for the tree selection event. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) (e.getPath()
                .getLastPathComponent());
        String sNode = (String) (node.getUserObject());

        // - ask the settingPanel if parameters are changed
        // boolean bHasChange = settingPanel.applyChanges(true); //no change to
        // do on this panel

        // - remove the settingPanel
        mainPanel2.remove(0);

        // - Create the new panel
        if (sNode.equals(GanttProject.correctLabel(language.getText("about"))))
            settingPanel = new AboutPanel(appli);
        else if (sNode.equals(language.getText("authors")))
            settingPanel = new AboutAuthorPanel(appli);
        else if (sNode.equals(language.getText("jinfos")))
            settingPanel = new AboutJavaInfosPanel(appli);
        else if (sNode.equals(language.getText("license")))
            settingPanel = new AboutLicensePanel(appli);
        else if (sNode.equals(language.getText("library")))
            settingPanel = new AboutLibraryPanel(appli);

        Box vb = Box.createVerticalBox();
        vb.add(new TopPanel("  " + settingPanel.getTitle(), settingPanel
                .getComment()));
        vb.add(settingPanel);
        settingPanel.initialize();

        // - add the settingPanel into the main Panel
        mainPanel2.add(vb, 0);
        mainPanel2.repaint();
        mainPanel2.validate(); // valide the changes
    }

    /** Coonstruct the menu settings. */
    public void constructSections() {
        DefaultMutableTreeNode aboutNode = addObject(GanttProject
                .correctLabel(language.getText("about")), null);
        DefaultMutableTreeNode authorsNode = addObject(language
                .getText("authors"), null);
        DefaultMutableTreeNode jinfosNode = addObject(language
                .getText("jinfos"), null);
        DefaultMutableTreeNode licenseNode = addObject(language
                .getText("license"), null);
        DefaultMutableTreeNode libraryNode = addObject(language
                .getText("library"), null);
    }

}
