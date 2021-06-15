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
 AboutLicensePanel.java 
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

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.gui.options.GeneralOptionPanel;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author athomas The licence panel
 */
public class AboutLicensePanel extends GeneralOptionPanel {

    /** Constructor. */
    public AboutLicensePanel(GanttProject parent) {
        super(GanttLanguage.getInstance().getText("license"), GanttLanguage
                .getInstance().getText("settingsLicense"), parent);

        JTextArea taLicense = new JTextArea();
        StringBuffer text = new StringBuffer();
        text
                .append("This program is free software; you can redistribute it and/or modify it under the terms of the GNU General ");
        text
                .append("Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.\n\n");
        text
                .append("This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied ");
        text
                .append("warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.\n\n");
        text
                .append("You should have received a copy of the GNU General Public License along with this program; if not, write to the Free ");
        text
                .append("Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.\n\n");
        text
                .append("Browse the entire GPL license at http://www.gnu.org/copyleft/gpl.html\n");

        taLicense.setText(text.toString());
        taLicense.setEditable(false);
        taLicense.setLineWrap(true);
        taLicense.setWrapStyleWord(true);

        JPanel licensePanel = new JPanel(new BorderLayout());
        licensePanel.add(new JScrollPane(taLicense), BorderLayout.CENTER);
        licensePanel.setPreferredSize(new Dimension(400, 350));
        vb.add(licensePanel);

        applyComponentOrientation(language.getComponentOrientation());
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.ganttproject.gui.options.GeneralOptionPanel#applyChanges(boolean)
     */
    public boolean applyChanges(boolean askForApply) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.ganttproject.gui.options.GeneralOptionPanel#initialize()
     */
    public void initialize() {
    }

}
