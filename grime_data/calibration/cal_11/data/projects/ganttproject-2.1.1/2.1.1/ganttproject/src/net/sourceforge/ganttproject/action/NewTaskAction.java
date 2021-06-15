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
package net.sourceforge.ganttproject.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.Mediator;
import net.sourceforge.ganttproject.language.GanttLanguage;

public class NewTaskAction extends AbstractAction implements
        GanttLanguage.Listener {

    private final IGanttProject myProject;

    public NewTaskAction(IGanttProject project) {
        myProject = project;
        setText(project.getI18n());
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        URL iconUrl = this.getClass().getClassLoader().getResource(
                "icons/insert_16.gif");
        if (iconUrl != null) {
            this.putValue(Action.SMALL_ICON, new ImageIcon(iconUrl));
        }
        project.getI18n().addListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        Mediator.getUndoManager().undoableEdit("New Task", new Runnable() {
            public void run() {
                myProject.newTask();
            }
        });

    }

    public void languageChanged(GanttLanguage.Event event) {
        setText(event.getLanguage());
    }

    /**
     * @param language
     */
    private void setText(GanttLanguage language) {
        this.putValue(AbstractAction.NAME, GanttProject.correctLabel(language
                .getText("newTask")));
    }

}
