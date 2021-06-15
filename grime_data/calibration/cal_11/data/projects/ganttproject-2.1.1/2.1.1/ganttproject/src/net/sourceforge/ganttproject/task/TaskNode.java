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

import javax.swing.tree.DefaultMutableTreeNode;

import net.sourceforge.ganttproject.GanttCalendar;

/**
 * This class is the class to use to discribe the tasks hierarchy.
 * 
 * @author bbaranne (Benoit Baranne)
 */
public class TaskNode extends DefaultMutableTreeNode {
    /**
     * The reference task
     */
    private final Task task;

    /**
     * Creates an instance of TaskNode with the given task as reference.
     * 
     * @param t
     *            Task of reference.
     */
    public TaskNode(Task t) {
        super(t);
        task = t;
    }

    /**
     * Sets the priority of the task.
     * 
     * @param priority
     *            The priority to be set.
     */
    public void setPriority(int priority) {
        task.setPriority(priority);
    }

    /**
     * Returns the priority of the task.
     * 
     * @return The priority of the task.
     */
    public int getPriority() {
        return task.getPriority();
    }

    /**
     * Sets the name of the task.
     * 
     * @param newName
     *            The name to be set.
     */
    public void setName(String newName) {
        task.setName(newName);
    }

    /**
     * Returns the name of the task.
     * 
     * @return The name of the task.
     */
    public String getName() {
        return task.getName();
    }

    /**
     * Sets the start date of the task.
     * 
     * @param startDate
     *            The start date of the task to be set.
     */
    public void setStart(GanttCalendar startDate) {
        TaskMutator mutator = task.createMutatorFixingDuration();
        mutator.setStart(startDate);
        mutator.commit();
    }

    /**
     * Returns the start date of the task.
     * 
     * @return The start date of the task.
     */
    public GanttCalendar getStart() {
        return task.getStart();
    }

    /**
     * Sets the end date of the task.
     * 
     * @param endDate
     *            The end date of the task to be set.
     */
    public void setEnd(GanttCalendar endDate) {
    	TaskMutator mutator = task.createMutator();
        mutator.setEnd(endDate);
        mutator.commit();
    }

    /**
     * Returns the end date of the task.
     * 
     * @return The end date of the task.
     */
    public GanttCalendar getEnd() {
        return task.getEnd();
    }

    /**
     * Sets the duration of the task.
     * 
     * @param length
     *            The duration to be set.
     */
    public void setDuration(TaskLength length) {
        TaskMutator mutator = task.createMutator();
        mutator.setDuration(length);
        mutator.commit();
    }

    /**
     * Returns the duration of the task.
     * 
     * @return The duration of the task.
     */
    public int getDuration() {
        return (int) task.getDuration().getValue();
    }

    /**
     * Sets the completion percentage of the task.
     * 
     * @param percentage
     *            The percentage to be set.
     */
    public void setCompletionPercentage(int percentage) {
        task.setCompletionPercentage(percentage);
    }

    /**
     * Returns the completion percentage of the task.
     * 
     * @return The completion percentage of the task.
     */
    public int getCompletionPercentage() {
        return task.getCompletionPercentage();
    }

    public void setTaskInfo(TaskInfo info) {
        task.setTaskInfo(info);
    }

    public TaskInfo getTaskInfo() {
        return task.getTaskInfo();
    }

    /**
     * @inheritDoc
     */
    public String toString() {
        return task.getName();
    }

    /**
     * @inheritDoc
     */
    public Object getUserObject() {
        return task;
    }

    public void applyThirdDateConstraint() {
        if (task.getThird() != null)
            switch (task.getThirdDateConstraint()) {
            case TaskImpl.EARLIESTBEGIN:
                if (task.getThird().after(getStart())) {
                    task.setStart(task.getThird().newAdd(0));
                }
            }
    }

}
