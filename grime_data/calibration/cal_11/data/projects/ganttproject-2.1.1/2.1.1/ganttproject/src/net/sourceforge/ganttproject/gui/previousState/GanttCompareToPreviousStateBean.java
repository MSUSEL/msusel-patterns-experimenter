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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import net.sourceforge.ganttproject.GanttPreviousState;
import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.gui.TestGanttRolloverButton;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author nbohn
 * 
 */
public class GanttCompareToPreviousStateBean extends JPanel {
    private GanttLanguage lang = GanttLanguage.getInstance();

    private ArrayList myPreviousStates;

    private JPanel southPanel;

    private JButton okButton, cancelButton, removeButton;

    private JComboBox nameComboBox;

    public GanttCompareToPreviousStateBean(GanttProject project) {
        myPreviousStates = project.getPreviouStates();
        init();
    }

    public void init() {
        setLayout(new BorderLayout());
        add(constructGeneralPanel(), BorderLayout.NORTH);
        add(constructSouthPanel(), BorderLayout.SOUTH);
    }

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

    private JPanel constructGeneralPanel() {
        JPanel generalPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING,
                40, 20));
        nameComboBox = new JComboBox();
        nameComboBox.setName("nameComboBox");
        nameComboBox.addItem(lang.getText("none"));
        for (int i = 0; i < myPreviousStates.size(); i++)
            nameComboBox.addItem(((GanttPreviousState) myPreviousStates.get(i))
                    .getName());
        nameComboBox.setSelectedIndex(myPreviousStates.size());
        nameComboBox.setName("nameComboBox");
        removeButton = new TestGanttRolloverButton(new ImageIcon(getClass()
                .getResource("/icons/delete_16.gif")));
        removeButton.setToolTipText(GanttProject.getToolTip(lang
                .getText("delete")));
        removeButton.setName("remove");
        generalPanel.add(nameComboBox);
        generalPanel.add(removeButton);
        generalPanel.setBorder(new TitledBorder(new EtchedBorder(), lang
                .getText("previousStates")));
        return generalPanel;
    }

    public void addActionListener(ActionListener l) {

        okButton.addActionListener(l);

        cancelButton.addActionListener(l);

        removeButton.addActionListener(l);

        nameComboBox.addActionListener(l);

    }

    public int getSelected() {
        return nameComboBox.getSelectedIndex();
    }

    public void removeItem() {
        int index = nameComboBox.getSelectedIndex();
        nameComboBox.removeItemAt(index);
        ((GanttPreviousState) myPreviousStates.get(index - 1)).remove();
        myPreviousStates.remove(index - 1);
    }

    public void setEnabled(boolean b) {
        removeButton.setEnabled(b);
    }
}
