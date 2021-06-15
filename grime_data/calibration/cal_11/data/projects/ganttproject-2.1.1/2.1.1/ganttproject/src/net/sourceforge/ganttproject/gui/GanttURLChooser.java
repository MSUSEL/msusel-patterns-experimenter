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
 GanttURLChooser.java  -  description
 -------------------
 begin                : july 2003
 copyright            : (C) 2003 by Thomas Alexandre
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

package net.sourceforge.ganttproject.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * Class to select a file from th web
 */
public class GanttURLChooser extends JDialog {
    /** JTextField for the name of project on the server. */
    private JTextField urlField;

    private JTextField userNameField;

    private JPasswordField passwordField;

    /** La langue utilisee. */
    GanttLanguage language = GanttLanguage.getInstance();

    /** The result of the file */
    public String fileurl;

    public String userName;

    public String password;

    /** true if the ok button was pressed */
    public boolean change = false;

    /** The constructor */
    public GanttURLChooser(Frame parent, boolean opening,
            String currentURL, String currentUser, String currentPass) {
        super(parent, opening ? (GanttProject.correctLabel(GanttLanguage
                .getInstance().getText("openFromServer"))) : (GanttProject
                .correctLabel(GanttLanguage.getInstance().getText(
                        "saveToServer"))), true);

        JPanel inputPanel = new JPanel(new BorderLayout());

        Box serverBox = Box.createVerticalBox();
        serverBox.add(new JLabel(language.getText("fileFromServer")));
        String sDefaultURL = "http://ganttproject.sourceforge.net/tmp/testGantt.xml";
        urlField = new JTextField((null != currentURL) ? currentURL
                : sDefaultURL);
        serverBox.add(urlField);

        inputPanel.add(serverBox, BorderLayout.NORTH);

        Box hb = Box.createHorizontalBox(); // horizontal box

        Box ivbw = Box.createVerticalBox(); // inner vertical box west
        Box ivbe = Box.createVerticalBox(); // inner vertical box east

        ivbw.add(new JLabel(language.getText("userName")));
        userNameField = new JTextField(currentUser);
        ivbw.add(userNameField);

        ivbe.add(new JLabel(language.getText("password")));
        passwordField = new JPasswordField(currentPass);
        ivbe.add(passwordField);

        hb.add(ivbw);
        hb.add(ivbe);

        inputPanel.add(hb, BorderLayout.SOUTH);
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        getContentPane().add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        JButton ok = new JButton(language.getText("ok"));
        getRootPane().setDefaultButton(ok);
        southPanel.add(ok);

        // Listener sur le bouton ok
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setVisible(false);
                dispose();
                fileurl = urlField.getText();
                userName = userNameField.getText();
                password = new String(passwordField.getPassword());
                change = true;
            }
        });

        // bouton cancel
        JButton cancel = new JButton(language.getText("cancel"));
        southPanel.add(cancel);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setVisible(false);
                dispose();
            }
        });

        getContentPane().add(southPanel, BorderLayout.SOUTH);

        pack();
        setResizable(true);

        DialogAligner.center(this, getParent());

        applyComponentOrientation(language.getComponentOrientation());
    }

}
