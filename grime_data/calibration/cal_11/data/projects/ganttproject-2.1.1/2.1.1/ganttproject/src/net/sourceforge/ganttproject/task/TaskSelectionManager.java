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
package net.sourceforge.ganttproject.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.ganttproject.gui.TaskSelectionContext;

/**
 * This class manages the selected tasks.
 *
 * @author bbaranne
 */
public class TaskSelectionManager implements TaskSelectionContext {
    public interface Listener {
        void selectionChanged(List currentSelection);
		void userInputConsumerChanged(Object newConsumer);
    }
    /**
     * List of the selected tasks.
     */
    private final List selectedTasks = new ArrayList();
    private final List myListeners = new ArrayList();
	private Object myUserInputConsumer;
    /**
     * Creates an instance of TaskSelectionManager
     */
    public TaskSelectionManager() {
    }

    public void setUserInputConsumer(Object consumer) {
    	if (consumer!=myUserInputConsumer) {
    		fireUserInputConsumerChanged();
    	}
    	myUserInputConsumer = consumer;
    }

	/**
     * Adds <code>task</code> to the selected tasks.
     *
     * @param task
     *            A task to add to the selected tasks.
     */
    public void addTask(Task task) {
        if (!selectedTasks.contains(task)) {
            selectedTasks.add(task);
            fireSelectionChanged();
        }
    }

    /**
     * Removes <code>task</code> from the selected tasks;
     *
     * @param task
     *            A task to remove from the selected tasks.
     */
    public void removeTask(Task task) {
        if (selectedTasks.contains(task)) {
            selectedTasks.remove(task);
            fireSelectionChanged();
        }
    }

    /**
     * Returns <code>true</code> if <code>task</code> is selected,
     * <code>false</code> otherwise.
     *
     * @param task
     *            The task to test.
     * @return <code>true</code> if <code>task</code> is selected,
     *         <code>false</code> otherwise.
     */
    public boolean isTaskSelected(Task task) {
        return selectedTasks.contains(task);
    }

    /**
     * Returns the selected tasks list.
     *
     * @return The selected tasks list.
     */
    public List getSelectedTasks() {
        return Collections.unmodifiableList(selectedTasks);
    }

    /**
     * Returns the earliest start date.
     *
     * @return The earliest start date.
     */
    public Date getEarliestStart() {
        Date res = null;
        Iterator it = selectedTasks.iterator();
        while (it.hasNext()) {

            Task task = (Task) it.next();
            Date d = task.getStart().getTime();
            if (res == null) {
                res = d;
                continue;
            }
            if (d.before(res))
                res = d;
        }
        return res;
    }

    /**
     * Returns the latest end date.
     *
     * @return The latest end date.
     */
    public Date getLatestEnd() {
        Date res = null;
        Iterator it = selectedTasks.iterator();
        while (it.hasNext()) {
            Task task = (Task) it.next();
            Date d = task.getEnd().getTime();
            if (res == null) {
                res = d;
                continue;
            }
            if (d.after(res))
                res = d;
        }
        return res;
    }

    /**
     * Clears the selected tasks list.
     */
    public void clear() {
        selectedTasks.clear();
        fireSelectionChanged();
    }

    public void addSelectionListener(Listener listener) {
        myListeners.add(listener);
    }

    public void removeSelectionListener(Listener listener) {
        myListeners.remove(listener);
    }

    private void fireSelectionChanged() {
        for (int i=0; i<myListeners.size(); i++) {
            Listener next = (Listener) myListeners.get(i);
            next.selectionChanged(Collections.unmodifiableList(selectedTasks));
        }
    }
    private void fireUserInputConsumerChanged() {
        for (int i=0; i<myListeners.size(); i++) {
            Listener next = (Listener) myListeners.get(i);
            next.userInputConsumerChanged(myUserInputConsumer);
        }
	}
}
