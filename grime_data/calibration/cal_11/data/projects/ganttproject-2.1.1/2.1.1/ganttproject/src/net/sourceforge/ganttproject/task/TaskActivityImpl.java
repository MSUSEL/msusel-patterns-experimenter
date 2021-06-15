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
 * Created on 18.10.2004
 */
package net.sourceforge.ganttproject.task;

import java.util.Date;

/**
 * @author bard
 */
class TaskActivityImpl implements TaskActivity {

    private final Date myEndDate;

    private final Date myStartDate;

    private final TaskLength myDuration;

    private float myIntensity;

    private final TaskImpl myTask;

    TaskActivityImpl(TaskImpl task, Date startDate, Date endDate) {
        this(task, startDate, endDate, 1.0f);
    }

    TaskActivityImpl(TaskImpl task, Date startDate, Date endDate,
            float intensity) {
        myStartDate = startDate;
        myEndDate = endDate;
        myDuration = task.getManager().createLength(
                task.getDuration().getTimeUnit(), startDate, endDate);
        myIntensity = intensity;
        myTask = task;
    }

    public Date getStart() {
        return myStartDate;
    }

    public Date getEnd() {
        return myEndDate;
    }

    public TaskLength getDuration() {
        return myDuration;
    }

    public float getIntensity() {
        return myIntensity;
    }

    public String toString() {
        return myTask.toString() + "[" + getStart() + ", " + getEnd() + "]";
    }

    public Task getTask() {
        return myTask;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.ganttproject.task.TaskActivity#isFirst()
     */
    public boolean isFirst() {
        return this == getTask().getActivities()[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.ganttproject.task.TaskActivity#isLast()
     */
    public boolean isLast() {
        TaskActivity[] all = getTask().getActivities();
        return this == all[all.length - 1];
    }
}
