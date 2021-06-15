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

import java.awt.Color;
import java.util.List;

import net.sourceforge.ganttproject.GanttCalendar;
import net.sourceforge.ganttproject.GanttTaskRelationship;
import net.sourceforge.ganttproject.shape.ShapePaint;
import net.sourceforge.ganttproject.task.dependency.TaskDependencySlice;

/**
 * Created by IntelliJ IDEA.
 *
 * @author bard Date: 27.01.2004
 */
public interface Task extends MutableTask {
    TaskMutator createMutator();
    TaskMutator createMutatorFixingDuration();
    // main properties
    int getTaskID();

    String getName();

    boolean isMilestone();

    int getPriority();

    TaskActivity[] getActivities();

    GanttCalendar getStart();

    GanttCalendar getEnd();

    TaskLength getDuration();

    TaskLength translateDuration(TaskLength duration);

    int getCompletionPercentage();

    ShapePaint getShape();

    Color getColor();

    String getNotes();

    boolean getExpand();

    //
    // relationships with other entities
    GanttTaskRelationship[] getPredecessors();

    GanttTaskRelationship[] getSuccessors();

    // HumanResource[] getAssignedHumanResources();
    ResourceAssignment[] getAssignments();

    TaskDependencySlice getDependencies();

    TaskDependencySlice getDependenciesAsDependant();

    TaskDependencySlice getDependenciesAsDependee();

    ResourceAssignmentCollection getAssignmentCollection();

    //
    Task getSupertask();

    Task[] getNestedTasks();

    void move(Task targetSupertask);

    void delete();

    TaskManager getManager();

    Task unpluggedClone();

    // Color DEFAULT_COLOR = new Color( 140, 182, 206); not used

    CustomColumnsValues getCustomValues();

    boolean isCritical();

    GanttCalendar getThird();

    void applyThirdDateConstraint();

    int getThirdDateConstraint();

    void setThirdDate(GanttCalendar thirdDate);

    void setThirdDateConstraint(int dateConstraint);

    TaskInfo getTaskInfo();

	boolean isProjectTask ();

	List/*<Document>*/ getAttachments();

}
