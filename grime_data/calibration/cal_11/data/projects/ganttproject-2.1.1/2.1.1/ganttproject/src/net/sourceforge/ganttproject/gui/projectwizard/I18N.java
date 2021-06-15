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

import java.text.MessageFormat;

import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.roles.Role;
import net.sourceforge.ganttproject.roles.RoleSet;

public class I18N {
    public I18N() {
        myDayNames = new String[7];
        for (int i = 0; i < 7; i++) {
            myDayNames[i] = GanttLanguage.getInstance().getDay(i);
        }
    }

    public String getNewProjectWizardWindowTitle() {
        return GanttLanguage.getInstance().getText("createNewProject");
    }

    public String getProjectDomainPageTitle() {
        return GanttLanguage.getInstance().getText("selectProjectDomain");
    }

    public String getProjectWeekendPageTitle() {
        return GanttLanguage.getInstance().getText("selectProjectWeekend");
    }

    public String getRolesetTooltipHeader(String roleSetName) {
        return MessageFormat.format("<html><body><h3>{0}</h3><ul>",
                (Object[]) new String[] { roleSetName });
    }

    public String getRolesetTooltipFooter() {
        return "</ul></body></html>";
    }

    public String formatRoleForTooltip(Role role) {
        return MessageFormat.format("<li>{0}</li>",
                (Object[]) new String[] { role.getName() });
    }

    String[] getDayNames() {
        return myDayNames;
        // DateFormatSymbols symbols = new
        // DateFormatSymbols(Locale.getDefault());
        // return symbols.getWeekdays();
    }

    final String[] myDayNames;

    public String getRoleSetDisplayName(RoleSet roleSet) {
        return GanttLanguage.getInstance().getText(
                "roleSet." + roleSet.getName() + ".displayName");
    }
}
