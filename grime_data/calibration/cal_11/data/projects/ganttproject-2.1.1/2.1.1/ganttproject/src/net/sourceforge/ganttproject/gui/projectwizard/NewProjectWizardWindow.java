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
package net.sourceforge.ganttproject.gui.projectwizard;

import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.calendar.GPCalendar;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.roles.RoleSet;

public class NewProjectWizardWindow extends WizardImpl {
    private I18N myI18n;

    public NewProjectWizardWindow(UIFacade uiFacade, I18N i18n) {
        super(uiFacade, i18n.getNewProjectWizardWindowTitle());
        myI18n = i18n;
    }

    public void addRoleSetPage(RoleSet[] roleSets) {
        WizardPage roleSetPage = new RoleSetPage(roleSets, myI18n);
        addPage(roleSetPage);
    }

    public void addProjectNamePage(IGanttProject project) {
        WizardPage projectNamePage = new ProjectNamePage(null, project, myI18n);
        addPage(projectNamePage);
    }

    public void addWeekendConfigurationPage(GPCalendar calendar,
            IGanttProject project) {
        WizardPage weekendPage;
        try {
            weekendPage = new WeekendConfigurationPage(calendar, myI18n,
                    project);
            addPage(weekendPage);
        } catch (Exception e) {
            getUIFacade().showErrorDialog(e);
        }
    }

}
