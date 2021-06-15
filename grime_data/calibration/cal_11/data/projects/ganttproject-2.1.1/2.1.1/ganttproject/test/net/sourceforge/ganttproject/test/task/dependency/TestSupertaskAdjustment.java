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
package net.sourceforge.ganttproject.test.task.dependency;

import java.util.Calendar;

import net.sourceforge.ganttproject.test.task.TaskTestCase;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskMutator;
import net.sourceforge.ganttproject.task.algorithm.RecalculateTaskScheduleAlgorithm;
import net.sourceforge.ganttproject.task.algorithm.AdjustTaskBoundsAlgorithm;
import net.sourceforge.ganttproject.task.dependency.TaskDependency;
import net.sourceforge.ganttproject.task.dependency.TaskDependencyException;
import net.sourceforge.ganttproject.task.dependency.constraint.FinishStartConstraintImpl;
import net.sourceforge.ganttproject.GanttCalendar;

public class TestSupertaskAdjustment extends TaskTestCase {
    public void testSupetaskDurationGrowsWhenNestedTasksGrow()
            throws TaskDependencyException {
        TaskManager taskManager = getTaskManager();
        Task supertask = taskManager.createTask();
        Task task1 = taskManager.createTask();
        Task task2 = taskManager.createTask();
        //
        task1.move(supertask);
        task2.move(supertask);
        //
        task1.setStart(new GanttCalendar(2000, 01, 01));
        task1.setEnd(new GanttCalendar(2000, 01, 03));
        task2.setStart(new GanttCalendar(2000, 01, 03));
        task2.setEnd(new GanttCalendar(2000, 01, 04));
        supertask.setStart(new GanttCalendar(2000, 01, 01));
        supertask.setEnd(new GanttCalendar(2000, 01, 04));
        //
        TaskDependency dep = taskManager
                .getDependencyCollection()
                .createDependency(task2, task1, new FinishStartConstraintImpl());
        //
        task1.setEnd(new GanttCalendar(2000, 01, 04));
        RecalculateTaskScheduleAlgorithm alg = taskManager
                .getAlgorithmCollection().getRecalculateTaskScheduleAlgorithm();
        alg.run(task1);
        //
        assertEquals("Unexpected start of supertask=" + supertask,
                new GanttCalendar(2000, 01, 01), supertask.getStart());
        assertEquals("Unexpected end of supertask=" + supertask,
                new GanttCalendar(2000, 01, 05), supertask.getEnd());
    }

    public void testSupertaskDurationShrinksWhenNestedTasksShrink() {
        TaskManager taskManager = getTaskManager();
        Task supertask = taskManager.createTask();
        Task task1 = taskManager.createTask();
        Task task2 = taskManager.createTask();
        //
        task1.move(supertask);
        task2.move(supertask);
        //
        task1.setStart(new GanttCalendar(2000, 01, 01));
        task1.setEnd(new GanttCalendar(2000, 01, 03));
        task2.setStart(new GanttCalendar(2000, 01, 03));
        task2.setEnd(new GanttCalendar(2000, 01, 04));
        supertask.setStart(new GanttCalendar(2000, 01, 01));
        supertask.setEnd(new GanttCalendar(2000, 01, 04));
        //
        task1.setStart(new GanttCalendar(2000, 01, 02));
        task2.setStart(new GanttCalendar(2000, 01, 02));
        task2.setEnd(new GanttCalendar(2000, 01, 03));
        //
        AdjustTaskBoundsAlgorithm alg = taskManager.getAlgorithmCollection()
                .getAdjustTaskBoundsAlgorithm();
        alg.run(new Task[] { task1, task2 });
        //
        assertEquals("Unexpected start of supertask=" + supertask,
                new GanttCalendar(2000, 01, 02), supertask.getStart());
        assertEquals("Unexpected end of supertask=" + supertask,
                new GanttCalendar(2000, 01, 03), supertask.getEnd());
    }
    
    public void testTaskDurationChangeIsPropagatedTwoLevelsUp() {
    	TaskManager taskManager = getTaskManager();
    	Task supertask = taskManager.createTask();
    	supertask.move(taskManager.getRootTask());
    	//
    	Task level1task1 = taskManager.createTask();
    	level1task1.move(supertask);
    	Task level1task2 = taskManager.createTask();
    	level1task2.move(supertask);
    	//
    	Task level2task1 = taskManager.createTask();
    	level2task1.move(level1task2);
    	//
    	supertask.setStart(newMonday());
    	supertask.setEnd(newTuesday());
    	level1task1.setStart(newMonday());
    	level1task1.setEnd(newTuesday());
    	level1task2.setStart(newMonday());
    	level1task2.setEnd(newTuesday());
    	level2task1.setStart(newMonday());
    	level2task1.setEnd(newTuesday());
    	//
    	level2task1.setEnd(newWendesday());
    	//
        AdjustTaskBoundsAlgorithm alg = taskManager.getAlgorithmCollection().getAdjustTaskBoundsAlgorithm();
        alg.run(new Task[] { level2task1 });
    	//
        assertEquals("Unexpected end of the topleveltask="+supertask, newWendesday(), supertask.getEnd());
    }
    
}
