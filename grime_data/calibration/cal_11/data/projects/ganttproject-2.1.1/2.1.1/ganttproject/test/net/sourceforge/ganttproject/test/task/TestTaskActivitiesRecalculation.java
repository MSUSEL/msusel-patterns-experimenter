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
package net.sourceforge.ganttproject.test.task;

import net.sourceforge.ganttproject.GanttCalendar;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskActivity;
import net.sourceforge.ganttproject.task.TaskMutator;

/**
 * @author bard
 */
public class TestTaskActivitiesRecalculation extends TaskTestCase {
    public void testRecalculateOnChangingDurationByMutator() {
        Task task = getTaskManager().createTask();
        {
            task.setStart(new GanttCalendar(2000, 0, 3));
            task.setDuration(getTaskManager().createLength(1));
            TaskActivity[] activities = task.getActivities();
            assertEquals("Unexpected length of activities", 1,
                    activities.length);
            assertEquals("Unexpected end of the las activity",
                    new GanttCalendar(2000, 0, 4).getTime(), activities[0]
                            .getEnd());
        }
        //
        {
            TaskMutator mutator = task.createMutator();
            mutator.setDuration(getTaskManager().createLength(2));
            TaskActivity[] activities = task.getActivities();
            assertEquals("Unexpected length of activities", 1,
                    activities.length);
            assertEquals("Unexpected end of the last activity",
                    new GanttCalendar(2000, 0, 5).getTime(), activities[0]
                            .getEnd());
        }
    }
    
    public void testRecalculateOnChangingStartByFixingDurationMutator() {
        Task task = getTaskManager().createTask();
        {
            task.setStart(new GanttCalendar(2000, 0, 3));
            task.setDuration(getTaskManager().createLength(3));
        }
        {
            TaskMutator mutator = task.createMutatorFixingDuration();
            mutator.setStart(new GanttCalendar(2000, 0, 4));
            mutator.commit();
            TaskActivity[] activities = task.getActivities();
            assertEquals("Unexpected length of activities", 1,
                    activities.length);
            assertEquals("Unexpected end of the last activity",
                    new GanttCalendar(2000, 0, 7).getTime(), activities[0]
                            .getEnd());
        }
    }
}
