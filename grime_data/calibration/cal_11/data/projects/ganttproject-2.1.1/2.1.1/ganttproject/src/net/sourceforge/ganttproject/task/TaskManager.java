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
 * Created on 05.07.2003
 *
 */
package net.sourceforge.ganttproject.task;

import java.util.Date;
import java.util.Map;

import net.sourceforge.ganttproject.GanttTask;
import net.sourceforge.ganttproject.calendar.GPCalendar;
import net.sourceforge.ganttproject.resource.ResourceManager;
import net.sourceforge.ganttproject.task.TaskContainmentHierarchyFacade.Factory;
import net.sourceforge.ganttproject.task.algorithm.AlgorithmCollection;
import net.sourceforge.ganttproject.task.dependency.TaskDependencyCollection;
import net.sourceforge.ganttproject.task.dependency.TaskDependencyConstraint;
import net.sourceforge.ganttproject.task.event.TaskListener;
import net.sourceforge.ganttproject.time.TimeUnit;

/**
 * @author bard
 */
public interface TaskManager {
    Task[] getTasks();

    public Task getRootTask();

    public void clear();

    public GanttTask getTask(int taskId);

    public void registerTask(Task task);

    public GanttTask createTask();

    public GanttTask createTask(int taskId);

    public TaskLength createLength(long length);

    TaskLength createLength(TimeUnit unit, float length);

    public TaskLength createLength(TimeUnit timeUnit, Date startDate,
            Date endDate);

    TaskDependencyCollection getDependencyCollection();

    AlgorithmCollection getAlgorithmCollection();

    TaskDependencyConstraint createConstraint(int constraintID);

    GPCalendar getCalendar();

    TaskContainmentHierarchyFacade getTaskHierarchy();

    void addTaskListener(TaskListener listener);

    public class Access {
        public static TaskManager newInstance(
                TaskContainmentHierarchyFacade.Factory containmentFacadeFactory,
                TaskManagerConfig config) {
            return new TaskManagerImpl(containmentFacadeFactory, config,null);
        }

		public static TaskManager newInstance(
				Factory factory, 
				TaskManagerConfig taskConfig, 
				CustomColumnsStorage customColumnsStorage) {
            return new TaskManagerImpl(factory, taskConfig, customColumnsStorage);
		}
    }

    public TaskLength getProjectLength();

    public int getTaskCount();

    public Date getProjectStart();
    public Date getProjectEnd();

    public TaskManager emptyClone();

    public Map importData(TaskManager taskManager);

    public void importAssignments(TaskManager importedTaskManager,
            ResourceManager hrManager, Map original2importedTask,
            Map original2importedResource);

    /**
     * Processes the critical path findind on <code>root</code> tasks.
     * 
     * @param root
     *            The root of the tasks to consider in the critical path
     *            finding.
     */
    public void processCriticalPath(TaskNode root);

    // /**
    // * Resets the critical path, i.e. reset all critical tasks to 'normal'
    // tasks.
    // * @param root The root of the tasks to consider.
    // */
    // public void resetCriticalPath(TaskNode root);

    public void deleteTask(Task tasktoRemove);

    CustomColumnsStorage getCustomColumnStorage();
}
