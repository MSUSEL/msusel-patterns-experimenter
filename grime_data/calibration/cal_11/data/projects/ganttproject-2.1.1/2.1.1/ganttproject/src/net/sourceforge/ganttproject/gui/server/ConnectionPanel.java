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
 GanttProject.java
 -----------------
 begin                : 1 juil. 2004
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
package net.sourceforge.ganttproject.gui.server;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.gui.options.GeneralOptionPanel;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author athomas The connection panel for http acces.
 */
public class ConnectionPanel extends GeneralOptionPanel {

    JTextField tfUrl;

    JTextField tfLogin;

    JPasswordField tfPassword;

    /** Constructor. */
    public ConnectionPanel(GanttProject parent) {
        super(GanttProject.correctLabel(GanttLanguage.getInstance().getText(
                "openFromServer")), GanttLanguage.getInstance().getText(
                "settingsConnection"), parent);

        Box vbServer = Box.createVerticalBox();

        // url textfield
        JPanel urlPanel = new JPanel(new BorderLayout());
        urlPanel.add(new JLabel(language.getText("fileFromServer")),
                BorderLayout.WEST);
        // urlPanel.add(tfUrl = new JTextField(), BorderLayout.CENTER);
        // vb.add(urlPanel);
        // vb.add(new JPanel());
        vbServer.add(urlPanel);
        vbServer.add(tfUrl = new JTextField("http://"));
        vbServer.add(new JPanel());

        // login textfield
        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.add(new JLabel(language.getText("userName")),
                BorderLayout.WEST);
        /*
         * loginPanel.add(tfLogin = new JTextField(), BorderLayout.CENTER);
         * vb.add(loginPanel); vb.add(new JPanel());
         */
        vbServer.add(loginPanel);
        vbServer.add(tfLogin = new JTextField());
        vbServer.add(new JPanel());

        // password textfield
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.add(new JLabel(language.getText("userName")),
                BorderLayout.WEST);
        /*
         * passwordPanel.add(tfPassword = new JPasswordField(),
         * BorderLayout.CENTER); vb.add(passwordPanel); vb.add(new JPanel());
         */
        vbServer.add(passwordPanel);
        vbServer.add(tfPassword = new JPasswordField());
        vbServer.add(new JPanel());

        JPanel serverPanel = new JPanel(new BorderLayout());
        serverPanel.add(vbServer, BorderLayout.NORTH);
        vb.add(serverPanel);

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
