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

import net.sourceforge.ganttproject.GanttCalendar;
import net.sourceforge.ganttproject.shape.ShapePaint;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author bard Date: 06.02.2004
 */
public interface MutableTask {
    void setName(String name);

    void setMilestone(boolean isMilestone);

    void setPriority(int priority);

    void setStart(GanttCalendar start);

    void setEnd(GanttCalendar end);

    void setDuration(TaskLength length);

    void shift(TaskLength shift);

    void setCompletionPercentage(int percentage);

//    void setStartFixed(boolean isFixed);

//    void setFinishFixed(boolean isFixed);

    void setShape(ShapePaint shape);

    void setColor(Color color);

    void setNotes(String notes);

    void addNotes(String notes);

    void setExpand(boolean expand);

    /**
     * Sets the task as critical or not. The method is used be TaskManager after
     * having run a CriticalPathAlgorithm to set the critical tasks. When
     * painted, the tasks are rendered as critical using Task.isCritical(). So,
     * a task is set as critical only if critical path is displayed.
     * 
     * @param critical
     *            <code>true</code> if this is critical, <code>false</code>
     *            otherwise.
     */
    void setCritical(boolean critical);

    void setTaskInfo(TaskInfo taskInfo);
	
	void setProjectTask (boolean projectTask);

}
