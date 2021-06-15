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
/*
 * Created on 23.10.2005
 */
package net.sourceforge.ganttproject.action.task;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.ganttproject.action.GPAction;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.TaskSelectionManager;
import net.sourceforge.ganttproject.task.TaskSelectionManager.Listener;

abstract class TaskActionBase extends GPAction implements Listener {
    private final TaskManager myTaskManager;
    private List mySelection;
    private final UIFacade myUIFacade;

    protected TaskActionBase(TaskManager taskManager, TaskSelectionManager selectionManager, UIFacade uiFacade) {
        myTaskManager = taskManager;
        selectionManager.addSelectionListener(this);
        selectionChanged(selectionManager.getSelectedTasks());
        myUIFacade = uiFacade;
    }

    public void actionPerformed(ActionEvent e) {
        final List selection = new ArrayList(mySelection);
        myUIFacade.getUndoManager().undoableEdit(getLocalizedName(), new Runnable() {
            public void run() {
                try {
                    TaskActionBase.this.run(selection);
                } catch (Exception e) {
                    getUIFacade().showErrorDialog(e);
                }
            }
        });
    }
    public void selectionChanged(List currentSelection) {
        setEnabled(isEnabled(currentSelection));
        mySelection = currentSelection;
    }
	public void userInputConsumerChanged(Object newConsumer) {
	}

    protected TaskManager getTaskManager() {
        return myTaskManager;
    }

    protected UIFacade getUIFacade() {
        return myUIFacade;
    }
    protected abstract boolean isEnabled(List selection);
    protected abstract void run(List selection) throws Exception ;
}
