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
package net.sourceforge.ganttproject.chart;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Date;

import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskContainmentHierarchyFacade;
import net.sourceforge.ganttproject.time.TimeUnit;

/**
 * @author dbarashev
 */
public interface ChartModel {
    ChartHeader getChartHeader();

    void setBounds(Dimension bounds);

    void setStartDate(Date startDate);

    /**
     * This method calculates the end date of this chart. It is a function of
     * (start date, bounds, bottom time unit, top time unit, bottom unit width)
     * so it expects that all these parameters are set correctly.
     */
    Date getEndDate();

    Date getStartDate();

    void setBottomUnitWidth(int pixelsWidth);

    void setRowHeight(int rowHeight);

    void setTopTimeUnit(TimeUnit topTimeUnit);

    void setBottomTimeUnit(TimeUnit bottomTimeUnit);

    void setVisibleTasks(java.util.List/* <Task> */visibleTasks);

    void paint(Graphics g);

    void setTaskContainment(TaskContainmentHierarchyFacade taskContainment);

    Task findTaskWithCoordinates(int x, int y);

    Rectangle getBoundingRectangle(Task task);

    float calculateLength(int fromX, int toX, int y);

    void setVerticalOffset(int i);
}
