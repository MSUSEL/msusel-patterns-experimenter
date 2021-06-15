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
package net.sourceforge.ganttproject.task.dependency.constraint;

import java.util.Date;

import net.sourceforge.ganttproject.GanttCalendar;
import net.sourceforge.ganttproject.GanttTaskRelationship;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskActivity;
import net.sourceforge.ganttproject.task.TaskMutator;
import net.sourceforge.ganttproject.task.dependency.TaskDependency;
import net.sourceforge.ganttproject.task.dependency.TaskDependencyConstraint;
import net.sourceforge.ganttproject.task.dependency.TaskDependency.ActivityBinding;

/**
 * Dependant task finishes not earlier than dependee finishes Created by
 * IntelliJ IDEA. User: bard
 */
public class FinishFinishConstraintImpl extends ConstraintImpl implements
        TaskDependencyConstraint {
    public FinishFinishConstraintImpl() {
        super(GanttTaskRelationship.FF, GanttLanguage.getInstance().getText(
                "finishfinish"));
    }

    public TaskDependencyConstraint.Collision getCollision() {
        TaskDependencyConstraint.Collision result = null;
        Task dependee = getDependency().getDependee();
        Task dependant = getDependency().getDependant();
        GanttCalendar dependeeEnd = dependee.getEnd();
        GanttCalendar dependantEnd = dependant.getEnd();
        //
        
        int difference = getDependency().getDifference();
        
        GanttCalendar comparisonDate = dependeeEnd.Clone();
        comparisonDate.add(difference);
        
        boolean isActive = getDependency().getHardness()==TaskDependency.Hardness.RUBBER ? dependantEnd
                .compareTo(comparisonDate) < 0 : dependantEnd
                .compareTo(comparisonDate) != 0;

        GanttCalendar acceptableStart = dependant.getStart().Clone();

        // GanttCalendar acceptableStart = dependant.getStart();
        if (isActive) {
            Task clone = dependee.unpluggedClone();
            TaskMutator mutator = clone.createMutator();
            mutator.shift(-dependant.getDuration().getLength());
            acceptableStart = clone.getEnd();
        }
        addDelay(acceptableStart);
        result = new TaskDependencyConstraint.DefaultCollision(acceptableStart,
                TaskDependencyConstraint.Collision.START_LATER_VARIATION,
                isActive);
        
        return result;
    }

    public ActivityBinding getActivityBinding() {
        TaskActivity[] dependantActivities = getDependency().getDependant()
                .getActivities();
        TaskActivity[] dependeeActivities = getDependency().getDependee()
                .getActivities();
        TaskActivity theDependant = dependantActivities[dependantActivities.length - 1];
        TaskActivity theDependee = dependeeActivities[dependeeActivities.length - 1];
        return new DependencyActivityBindingImpl(theDependant, theDependee,
                new Date[] { theDependant.getEnd(), theDependee.getEnd() });
    }

}
