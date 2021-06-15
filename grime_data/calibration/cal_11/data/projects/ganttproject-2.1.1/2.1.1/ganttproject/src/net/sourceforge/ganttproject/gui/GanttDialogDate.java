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
 GanttDialogDate.java  -  description
 -------------------
 begin                : dec 2002
 copyright            : (C) 2002 by Thomas Alexandre
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sourceforge.ganttproject.GanttCalendar;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * Dialog allow you to select a date
 */
public class GanttDialogDate extends JDialog {
    private GanttLanguage language = GanttLanguage.getInstance();

    public static final int OK = 0;

    public static final int CANCEL = 1;

    private int value = CANCEL;

    /** Graphic area for display the year */
    // private GanttDialogDateDay ddd;
    /** Save the date */
    // private GanttCalendar save;
    private GanttPanelDate panel;

    /** buttons to handles the change of date EV 20031027 * */
    private JButton jbPrevMonth, jbNextMonth;

    private JFormattedTextField jtDate;

    private JButton jbPrevYear, jbNextYear;

    private boolean myFixedDate;

    /** Constructor */
    public GanttDialogDate(JDialog parent, GanttCalendar date) {
        this(parent, date, false);
    }

    public GanttDialogDate(JDialog parent, GanttCalendar date,
            boolean showFixedDateCheckBox) {
        super(parent, GanttLanguage.getInstance().getText("chooseDate"), true);
        myFixedDate = date.isFixed();
        setResizable(false);
        panel = new GanttPanelDate(date);

        // Container contentPane = getContentPane();

        JComponent contentPane = Box.createVerticalBox();
        // contentPane.setLayout(new GridLayout(3,1, 5, 5));
        JPanel p = new JPanel();
        JButton ok = new JButton(language.getText("ok"));
        getRootPane().setDefaultButton(ok);
        p.add(ok);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setVisible(false);
                value = OK;
            }
        });
        JButton cancel = new JButton(language.getText("cancel"));
        p.add(cancel);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setVisible(false);
                panel.cancel();
            }
        });
        contentPane.add(panel);
        if (showFixedDateCheckBox) {
            final JCheckBox fixedDate = new JCheckBox(language
                    .getText("fixedDate"));
            fixedDate.setSelected(myFixedDate);
            fixedDate.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    myFixedDate = fixedDate.isSelected();
                }
            });
            contentPane.add(fixedDate);
        }
        contentPane.add(p);
        getContentPane().add(contentPane);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                panel.cancel();
            }
        });

        pack();
        setResizable(false);
        DialogAligner.center(this, getParent());
        applyComponentOrientation(language.getComponentOrientation());
    }

    /** Return The selected date. */
    public GanttCalendar getDate() {
        GanttCalendar result = panel.getDate();
        // System.err.println("[GanttDialogDate] getDate():
        // fixed="+myFixedDate);
        result.setFixed(myFixedDate);
        return result;
    }

    public int getValue() {
        return value;
    }

}
