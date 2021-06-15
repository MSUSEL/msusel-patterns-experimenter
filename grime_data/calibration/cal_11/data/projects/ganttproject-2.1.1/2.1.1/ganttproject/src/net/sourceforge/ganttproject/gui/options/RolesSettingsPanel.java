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
 RolesSettingsPanel.java 
 ------------------------------------------
 begin                : 27 juin 2004
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

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.gui.RolesTableModel;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.roles.RoleManager;

/**
 * @author athomas Panel to edit the roles for resources of the project.
 */
public class RolesSettingsPanel extends GeneralOptionPanel {

    RolesTableModel myRolesModel;

    JTable rolesTable;

    private GanttProject appli;

    /** Constructor. */
    public RolesSettingsPanel(GanttProject parent) {
        super(GanttProject.correctLabel(GanttLanguage.getInstance().getText(
                "resourceRole")), GanttLanguage.getInstance().getText(
                "settingsRoles"), parent);

        appli = parent;
        myRolesModel = new RolesTableModel();
        rolesTable = new JTable(myRolesModel);
        rolesTable.setPreferredScrollableViewportSize(new Dimension(400, 350));
        rolesTable.setRowHeight(23);
        rolesTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        rolesTable.getColumnModel().getColumn(1).setPreferredWidth(370);

        vb.add(new JScrollPane(rolesTable));

        applyComponentOrientation(language.getComponentOrientation());
    }

    /** This method check if the value has changed, and assk for commit changes. */
    public boolean applyChanges(boolean askForApply) {
        System.err.println("[RolesSettingsPanel] applyChanges(): ");
        RoleManager roleManager = myRolesModel.getRoleManager();
        bHasChange = myRolesModel.hasChanges();
        if (!bHasChange) {
            System.err
                    .println("[RolesSettingsPanel] applyChanges(): no changes");
            return bHasChange;
        }
        myRolesModel.applyChanges();
        appli.setAskForSave(true);
        return bHasChange;
    }

    /** Initialize the component. */
    public void initialize() {
        // automatic initialize with the role model
    }

}
