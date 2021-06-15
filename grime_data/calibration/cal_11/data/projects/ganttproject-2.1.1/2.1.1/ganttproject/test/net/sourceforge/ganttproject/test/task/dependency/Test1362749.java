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

import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskMutator;
import net.sourceforge.ganttproject.task.dependency.TaskDependencyConstraint;
import net.sourceforge.ganttproject.task.dependency.constraint.FinishFinishConstraintImpl;
import net.sourceforge.ganttproject.task.dependency.constraint.FinishStartConstraintImpl;
import net.sourceforge.ganttproject.test.task.TaskTestCase;

public class Test1362749 extends TaskTestCase{
    public void testBugReport1362749() throws Exception {
        Task t1 = getTaskManager().createTask();
        Task t2 = getTaskManager().createTask();
        Task t3 = getTaskManager().createTask();
        Task t4 = getTaskManager().createTask();
        getTaskManager().getDependencyCollection().createDependency(t4, t3, new FinishStartConstraintImpl());
        getTaskManager().getDependencyCollection().createDependency(t1, t3, new FinishFinishConstraintImpl());
        getTaskManager().getDependencyCollection().createDependency(t2, t4, new FinishFinishConstraintImpl());
        TaskMutator mutator = t3.createMutator();
        mutator.setDuration(getTaskManager().createLength(3));
        mutator.commit();
        getTaskManager().getAlgorithmCollection().getRecalculateTaskScheduleAlgorithm().run(t3);
        //
        assertEquals("Task="+t4+" is expected to start when task="+t3+" finishes", t3.getEnd(), t4.getStart());
        assertEquals("Task="+t1+" is expected to finish together with task="+t3, t1.getEnd(), t3.getEnd());
        assertEquals("Task="+t2+" is expected to finish together with task="+t4, t2.getEnd(), t4.getEnd());
        assertTrue("It is expected that start date of task="+t1+" is less than start date of task="+t2, t1.getStart().compareTo(t2.getStart())<0);
    }
}
