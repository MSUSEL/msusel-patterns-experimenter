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

import net.sourceforge.ganttproject.GanttCalendar;
import net.sourceforge.ganttproject.calendar.WeekendCalendarImpl;
import net.sourceforge.ganttproject.task.dependency.TaskDependency;

/**
 * Created by IntelliJ IDEA. User: bard
 */
public class ConstraintImpl implements Cloneable{
    private final int myID;

    private final String myName;

    private TaskDependency myDependency;

    public ConstraintImpl(int myID, String myName) {
        this.myID = myID;
        this.myName = myName;
    }

    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    protected TaskDependency getDependency() {
        return myDependency;
    }

    public void setTaskDependency(TaskDependency dependency) {
        myDependency = dependency;
    }

    public String getName() {
        return myName;
    }

    public int getID() {
        return myID;
    }

    public String toString() {
        return getName();
    }

    public void addDelay(GanttCalendar calendar) {
        int difference = myDependency.getDifference();
        calendar.add(difference);
        GanttCalendar solutionStart = calendar.Clone();
        solutionStart.add(-1 * myDependency.getDifference());
        for (int i = 0; i <= difference; i++) {
            if ((myDependency.getDependant()
                    .getManager().getCalendar()).isNonWorkingDay(solutionStart
                    .getTime())) {
                calendar.add(1);
                difference++;
            }
            solutionStart.add(1);
        }
    }
}
