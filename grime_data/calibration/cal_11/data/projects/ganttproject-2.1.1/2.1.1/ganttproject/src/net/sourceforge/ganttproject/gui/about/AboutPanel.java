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
 AboutPanel.java
 -----------------
 begin                : 28 juin 2004
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.GanttSplash;
import net.sourceforge.ganttproject.gui.options.GeneralOptionPanel;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.util.BrowserControl;

/**
 * @author athomas The About panel.
 */
public class AboutPanel extends GeneralOptionPanel {

    /** Constructor. */
    public AboutPanel(GanttProject parent) {
        super(GanttProject.correctLabel(GanttLanguage.getInstance().getText(
                "about")), GanttLanguage.getInstance().getText("settingsAbout")
                + " " + GanttProject.version, parent);
        GanttSplash splash = new GanttSplash();
        JLabel jLabelImage = splash.getSplashComponent();
        // JPanel imagePanel = new JPanel(new BorderLayout());
        // imagePanel.add(jLabelImage, BorderLayout.CENTER);
        vb.add(new JPanel());
        vb.add(jLabelImage);
        vb.add(new JPanel());
        JButton bHomePage = new JButton(GanttProject.correctLabel(language
                .getText("webPage")), new ImageIcon(getClass().getResource(
                "/icons/home_16.gif")));
        bHomePage.setToolTipText(GanttProject.getToolTip(language
                .getText("goTo")
                + " " + "http://ganttproject.biz")); // add a simple tool tip
        // text :)
        bHomePage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BrowserControl.displayURL("http://ganttproject.biz/");
            }
        });

        vb.add(bHomePage);
        applyComponentOrientation(language.getComponentOrientation());

    }

    /** This method check if the value has changed, and assk for commit changes. */
    public boolean applyChanges(boolean askForApply) {
        // nothing to do :)
        return bHasChange = false;
    }

    /** Initialize the component. */
    public void initialize() {
        // nothing to do :)
    }
}
