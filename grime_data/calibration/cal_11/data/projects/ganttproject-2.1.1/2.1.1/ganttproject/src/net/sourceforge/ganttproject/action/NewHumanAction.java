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
package net.sourceforge.ganttproject.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.resource.ResourceManager;
import net.sourceforge.ganttproject.roles.RoleManager;

/**
 * Action connected to the menu item for insert a new resource
 */
public class NewHumanAction extends ResourceAction {
    private final RoleManager myRoleManager;

    private final GanttProject myProject;

    public NewHumanAction(ResourceManager hrManager, RoleManager roleManager,
            JFrame projectFrame, GanttProject project) {
        super(hrManager);
        myRoleManager = roleManager;
        myProjectFrame = projectFrame;
        myProject = project;

        this.putValue(AbstractAction.NAME, GanttProject
                .correctLabel(getLanguage().getText("newHuman")));
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                KeyEvent.VK_H, MENU_MASK));
        URL iconUrl = this.getClass().getClassLoader().getResource(
                "icons/insert_16.gif");
        if (iconUrl != null) {
            this.putValue(Action.SMALL_ICON, new ImageIcon(iconUrl));
        }
    }

    public void actionPerformed(ActionEvent event) {
        // final HumanResource people =
        // ((HumanResourceManager)getManager()).newHumanResource();
        // people.setRole(myRoleManager.getDefaultRole());
        // GanttDialogPerson dp = new GanttDialogPerson(getProjectFrame(),
        // getLanguage(), people);
        // dp.show();
        // if(dp.result()) {
        //
        // myProject.getUndoManager().undoableEdit("new Resource", new
        // Runnable(){
        // public void run() {
        // getManager().add(people);
        // }});
        // myProject.quickSave ("new Resource");
        // }
        myProject.newHumanResource();
    }

    public void languageChanged() {
        this.putValue(AbstractAction.NAME, GanttProject
                .correctLabel(getLanguage().getText("newHuman")));
    }

    private JFrame getProjectFrame() {
        return myProjectFrame;
    }

    private final int MENU_MASK = Toolkit.getDefaultToolkit()
            .getMenuShortcutKeyMask();

    private JFrame myProjectFrame;
}
