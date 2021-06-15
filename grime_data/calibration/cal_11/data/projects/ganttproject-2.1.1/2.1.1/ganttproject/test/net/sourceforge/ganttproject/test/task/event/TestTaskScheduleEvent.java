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
package net.sourceforge.ganttproject.test.task.event;

import net.sourceforge.ganttproject.test.task.TaskTestCase;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskMutator;
import net.sourceforge.ganttproject.task.event.TaskListenerAdapter;
import net.sourceforge.ganttproject.task.event.TaskScheduleEvent;
import net.sourceforge.ganttproject.GanttCalendar;
import net.sourceforge.ganttproject.time.gregorian.GregorianTimeUnitStack;

/**
 * Created by IntelliJ IDEA. User: bard
 */
public class TestTaskScheduleEvent extends TaskTestCase {
    public void testTaskScheduleEventIsSendOnTaskEndChange() {
        TaskManager taskManager = getTaskManager();
        Task task1 = taskManager.createTask();
        TaskListenerImpl listener = new TaskListenerImpl(taskManager);
        taskManager.addTaskListener(listener);
        GanttCalendar taskEnd = task1.getEnd().Clone();
        taskEnd.add(1);
        TaskMutator mutator = task1.createMutator();
        mutator.setEnd(taskEnd);
        mutator.commit();
        assertTrue("Listener has not been called on task end change", listener
                .hasBeenCalled());
    }

    public void testTaskScheduleEventIsSendOnTaskStartChange() {
        TaskManager taskManager = getTaskManager();
        Task task1 = taskManager.createTask();
        TaskListenerImpl listener = new TaskListenerImpl(taskManager);
        taskManager.addTaskListener(listener);
        GanttCalendar taskStart = task1.getStart().Clone();
        taskStart.add(-1);
        TaskMutator mutator = task1.createMutator();
        mutator.setStart(taskStart);
        mutator.commit();
        assertTrue("Listener has not been called on task start change",
                listener.hasBeenCalled());
    }

    public void testTaskScheduleEventIsSendOnTaskDurationChange() {
        TaskManager taskManager = getTaskManager();
        Task task1 = taskManager.createTask();
        TaskListenerImpl listener = new TaskListenerImpl(taskManager);
        taskManager.addTaskListener(listener);
        TaskMutator mutator = task1.createMutator();
        mutator.setDuration(taskManager.createLength(GregorianTimeUnitStack.DAY,
                5));
        mutator.commit();
        assertTrue("Listener has not been called on task duration change",
                listener.hasBeenCalled());
    }

    private static class TaskListenerImpl extends TaskListenerAdapter {
        private boolean hasBeenCalled;

        public TaskListenerImpl(TaskManager taskManager) {
            super();
        }

        public void taskScheduleChanged(TaskScheduleEvent e) {
            hasBeenCalled = true;
        }

        boolean hasBeenCalled() {
            return hasBeenCalled;
        }

    }
}
