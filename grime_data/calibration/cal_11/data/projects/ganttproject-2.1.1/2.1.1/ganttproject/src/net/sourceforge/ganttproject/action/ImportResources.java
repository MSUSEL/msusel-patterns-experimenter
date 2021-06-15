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
 ImportResources.java  -  description
 -------------------
 begin                : may 2003

 ***************************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/

package net.sourceforge.ganttproject.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.Mediator;
import net.sourceforge.ganttproject.gui.OpenFileDialog;
import net.sourceforge.ganttproject.io.GanttXMLOpen;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.parser.DependencyTagHandler;
import net.sourceforge.ganttproject.parser.ResourceTagHandler;
import net.sourceforge.ganttproject.parser.RoleTagHandler;
import net.sourceforge.ganttproject.resource.ResourceManager;
import net.sourceforge.ganttproject.roles.RoleManager;
import net.sourceforge.ganttproject.task.TaskManager;

/**
 * Action connected to the menu item for importe some resources
 */
public class ImportResources extends AbstractAction {
    private final TaskManager myTaskManager;

    private final ResourceManager myResourceManager;

    private final GanttProject myproject;

    private final RoleManager myRoleManager;

    private File startFile = null;

    public ImportResources(ResourceManager resourceManager,
            TaskManager taskManager, RoleManager roleManager,
            GanttProject project) {
        myTaskManager = taskManager;
        myRoleManager = roleManager;
        GanttLanguage language = GanttLanguage.getInstance();

        this.putValue(AbstractAction.NAME, language.getText("importResources"));
        myResourceManager = resourceManager;

        URL iconUrl = this.getClass().getClassLoader().getResource(
                "icons/impres_16.gif");
        if (iconUrl != null) {
            this.putValue(Action.SMALL_ICON, new ImageIcon(iconUrl));
        }

        myproject = project;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {
        final File file = getResourcesFile();
        if (file != null) {
            Mediator.getUndoManager().undoableEdit("Import Resources",
                    new Runnable() {
                        public void run() {
                            GanttXMLOpen loader = new GanttXMLOpen(
                                    myTaskManager);
                            ResourceTagHandler tagHandler = new ResourceTagHandler(
                                    myResourceManager, myRoleManager, myproject.getResourceCustomPropertyManager());
                            DependencyTagHandler dependencyHandler = new DependencyTagHandler(
                                    loader.getContext(), myTaskManager, myproject.getUIFacade());
                            RoleTagHandler rolesHandler = new RoleTagHandler(
                                    RoleManager.Access.getInstance());
                            loader.addTagHandler(tagHandler);
                            loader.addTagHandler(dependencyHandler);
                            loader.addTagHandler(rolesHandler);
                            loader.load(file);
                            // myproject.setQuickSave (true);
                            // myproject.quickSave ("Import Resources");
                        }
                    });
        }
    }

    private File getResourcesFile() {
        OpenFileDialog openDialog;
        if (startFile != null)
            openDialog = new OpenFileDialog(startFile.getPath());
        else
            openDialog = new OpenFileDialog(myproject);
        File result = openDialog.show();
        if (result != null)
            startFile = result;
        return result;
    }

}
