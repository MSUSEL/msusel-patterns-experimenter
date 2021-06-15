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
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import net.sourceforge.ganttproject.GanttPreviousState;
import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.gui.DialogAligner;
import net.sourceforge.ganttproject.gui.GanttDialogInfo;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author nbohn
 * 
 */
public class GanttDialogSaveAsPreviousState extends JDialog implements
        ActionListener {
    private GanttLanguage lang = GanttLanguage.getInstance();

    private GanttSaveAsPreviousStateBean previousStateBean;

    private GanttProject myProject;

    private boolean isSaved = false;

    private GanttPreviousState myPreviousState;

    public GanttDialogSaveAsPreviousState(GanttProject project) {
        super(project, GanttLanguage.getInstance().getText("saveCurrent"), true);
        myProject = project;
        previousStateBean = new GanttSaveAsPreviousStateBean(myProject);
        previousStateBean.addActionListener(this);
        Container cp = getContentPane();
        cp.add(previousStateBean, BorderLayout.CENTER);
        this.pack();
        setResizable(false);
        DialogAligner.center(this, getParent());

        applyComponentOrientation(lang.getComponentOrientation());
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() instanceof JButton) {
            JButton button = (JButton) evt.getSource();
            if (button.getName().equals("ok")) {

                boolean overWrite = true;
                for (int i = 0; i < myProject.getPreviouStates().size(); i++)
                    if (((GanttPreviousState) myProject.getPreviouStates().get(
                            i)).getName().equals(previousStateBean.getName())) {
                        GanttDialogInfo gdi = new GanttDialogInfo(myProject,
                                GanttDialogInfo.WARNING,
                                GanttDialogInfo.YES_NO_OPTION,
                                previousStateBean.getName() + "\n"
                                        + lang.getText("msg18"), lang
                                        .getText("warning"));
                        gdi.show();
                        if (gdi.res == GanttDialogInfo.YES)
                            myProject.getPreviouStates().remove(i);
                        else
                            overWrite = false;
                        break;
                    }
                if (overWrite) {
                    try {
                        myPreviousState = new GanttPreviousState(
                                previousStateBean.getName(), myProject);
                        isSaved = true;
                        previousStateBean.setDefaultName();

                    } catch (IOException e) {
                    	myProject.getUIFacade().showErrorDialog(e);
                    }
                    this.setVisible(false);
                    dispose();
                }
            } else if (button.getName().equals("cancel")) {
                this.setVisible(false);
                dispose();
            }
        } else if (evt.getSource() instanceof JComboBox) {
            JComboBox comboBox = (JComboBox) evt.getSource();
            if (comboBox.getName().equals("nameComboBox")) {
                previousStateBean.getTextField().setText(
                        comboBox.getSelectedItem() + "");
            }
        }
    }

    public boolean isSaved() {
        return isSaved;
    }

    public GanttPreviousState getPreviousState() {
        return myPreviousState;
    }
}
