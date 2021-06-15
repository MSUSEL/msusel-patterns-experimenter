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
/**
 * 
 */
package net.sourceforge.ganttproject.gui.previousState;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import net.sourceforge.ganttproject.GanttPreviousState;
import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author nbohn
 * 
 */
public class GanttSaveAsPreviousStateBean extends JPanel {
    private JPanel generalPanel, southPanel;

    private JButton okButton, cancelButton;

    private JTextField nameTextField;

    private JLabel previousStateLabel;

    private JComboBox nameComboBox;

    private ArrayList myPreviousStates;

    private GanttLanguage lang = GanttLanguage.getInstance();

    private static String defaultName = GanttLanguage.getInstance().getText(
            "save");

    private static int count = 1;

    public GanttSaveAsPreviousStateBean(GanttProject project) {
        myPreviousStates = project.getPreviouStates();
        if (myPreviousStates.size() == 0) {
            defaultName = GanttLanguage.getInstance().getText("save");
            count = 1;
        }
        init();
    }

    public void init() {
        setLayout(new BorderLayout());
        add(constructNamePanel(), BorderLayout.CENTER);
        add(constructSouthPanel(), BorderLayout.SOUTH);
        nameTextField.requestFocusInWindow();
    }

    /** Construct the south panel */

    private JPanel constructSouthPanel() {
        okButton = new JButton(lang.getText("ok"));

        okButton.setName("ok");

        if (getRootPane() != null)
            getRootPane().setDefaultButton(okButton); // set ok the defuault
        // button when press
        // "enter" --> check
        // because
        // getRootPane()==null
        // !!!

        cancelButton = new JButton(lang.getText("cancel"));

        cancelButton.setName("cancel");

        southPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));

        southPanel.add(okButton);

        southPanel.add(cancelButton);

        return southPanel;
    }

    private Box constructNamePanel() {
        Box box = Box.createVerticalBox();
        JPanel previousStatePanel;
        nameComboBox = new JComboBox();
        if (myPreviousStates.size() != 0) {
            previousStatePanel = new JPanel(new FlowLayout(FlowLayout.TRAILING,
                    30, 20));
            previousStateLabel = new JLabel(lang.getText("previousStates"));
            for (int i = 0; i < myPreviousStates.size(); i++)
                nameComboBox.addItem(((GanttPreviousState) myPreviousStates
                        .get(i)).getName());
            nameComboBox.setName("nameComboBox");
            nameComboBox.setSelectedIndex(-1);
            previousStatePanel.add(previousStateLabel);
            previousStatePanel.add(nameComboBox);
            box.add(previousStatePanel);
        }
        nameTextField = new JTextField(20);
        nameTextField.setText(defaultName + "_" + count);
        nameTextField.selectAll();
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 30,
                20));
        namePanel.add(nameTextField);
        box.add(namePanel);
        box
                .setBorder(new TitledBorder(new EtchedBorder(), lang
                        .getText("name")));
        return box;
    }

    public String getName() {
        return nameTextField.getText();
    }

    public void addActionListener(ActionListener l) {

        okButton.addActionListener(l);

        cancelButton.addActionListener(l);

        nameComboBox.addActionListener(l);

    }

    public JTextField getTextField() {
        return nameTextField;
    }

    public void setDefaultName() {
        if (!nameTextField.getText().equals(defaultName + "_" + count)) {
            defaultName = nameTextField.getText();
            count = 0;
        }
        count++;
    }
}
