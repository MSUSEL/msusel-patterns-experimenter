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
package net.sourceforge.ganttproject.test.task.hierarchy;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.test.task.TaskTestCase;

public class TestTaskDocumentOrder extends TaskTestCase {
	public void testTasksAreInDocumentOrder() {
        Task task1 = getTaskManager().createTask();
        task1.setName("1");
        Task task2 = getTaskManager().createTask();
        task2.setName("2");
        task1.move(getTaskManager().getRootTask());
        task2.move(task1);
        //
        Task task3 = getTaskManager().createTask();
        task3.setName("3");
        Task task4 = getTaskManager().createTask();
        task4.setName("4");
        task4.move(task3);
        task3.move(getTaskManager().getRootTask());
        //
        Task task5 = getTaskManager().createTask();
        task5.setName("5");
        task5.move(task2);
        //
        List expectedOrder = Arrays.asList(new Task[] {task3, task4, task1, task2, task5,});
        List actualOrder = Arrays.asList(getTaskManager().getTasks());
        assertEquals("Unexpected order of tasks returnedby TaskManager.getTasks()", expectedOrder, actualOrder);
	}
}
