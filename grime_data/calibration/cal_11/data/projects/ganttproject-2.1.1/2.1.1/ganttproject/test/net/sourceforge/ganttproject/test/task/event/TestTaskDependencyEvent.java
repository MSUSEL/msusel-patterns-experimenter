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
 LICENSE:
 
 This program is free software; you can redistribute it and/or modify  
 it under the terms of the GNU General Public License as published by  
 the Free Software Foundation; either version 2 of the License, or     
 (at your option) any later version.                                   
 
 Copyright (C) 2004, GanttProject Development Team
 */
package net.sourceforge.ganttproject.test.task.event;

import net.sourceforge.ganttproject.test.task.TaskTestCase;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.dependency.TaskDependencyException;
import net.sourceforge.ganttproject.task.dependency.TaskDependency;
import net.sourceforge.ganttproject.task.dependency.constraint.FinishStartConstraintImpl;
import net.sourceforge.ganttproject.task.event.TaskListenerAdapter;
import net.sourceforge.ganttproject.task.event.TaskDependencyEvent;

/**
 * Created by IntelliJ IDEA. User: bard
 */
public class TestTaskDependencyEvent extends TaskTestCase {
    public void testDependencyEventIsSentOnDependencyCreation()
            throws TaskDependencyException {
        TaskManager taskManager = getTaskManager();
        TaskListenerImpl listener = new TaskListenerImpl() {
            public void dependencyAdded(TaskDependencyEvent e) {
                setHasBeenCalled(true);
            }
        };
        taskManager.addTaskListener(listener);
        Task task1 = taskManager.createTask();
        Task task2 = taskManager.createTask();
        //
        taskManager.getDependencyCollection().createDependency(task2, task1,
                new FinishStartConstraintImpl());
        assertTrue(
                "Listener is expected to be called when dependency is added",
                listener.hasBeenCalled());
    }

    public void testDependencyEventIsSentOnDependencyRemoval()
            throws TaskDependencyException {
        TaskManager taskManager = getTaskManager();
        TaskListenerImpl listener = new TaskListenerImpl() {
            public void dependencyRemoved(TaskDependencyEvent e) {
                setHasBeenCalled(true);
            }
        };
        taskManager.addTaskListener(listener);
        Task task1 = taskManager.createTask();
        Task task2 = taskManager.createTask();
        //
        TaskDependency dep = taskManager
                .getDependencyCollection()
                .createDependency(task2, task1, new FinishStartConstraintImpl());
        dep.delete();
        assertTrue(
                "Listener is expected to be called when dependency is deleted",
                listener.hasBeenCalled());
    }

    private static class TaskListenerImpl extends TaskListenerAdapter {
        private boolean hasBeenCalled;

        boolean hasBeenCalled() {
            return hasBeenCalled;
        }

        protected void setHasBeenCalled(boolean called) {
            hasBeenCalled = called;
        }
    }
}
