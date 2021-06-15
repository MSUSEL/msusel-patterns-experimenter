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
 * Created on 03.12.2004
 */
package net.sourceforge.ganttproject.chart;

import java.util.List;

import net.sourceforge.ganttproject.chart.GraphicPrimitiveContainer.Text;
import net.sourceforge.ganttproject.task.ResourceAssignment;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.time.TimeFrame;
import net.sourceforge.ganttproject.time.TimeUnit;

/**
 * @author bard
 */
public class ResourcesRendererImpl extends ChartRendererBase implements
        TimeUnitVisitor {

    public ResourcesRendererImpl(ChartModelImpl model) {
        super(model);
        // TODO Auto-generated constructor stub
    }

    public void beforeProcessingTimeFrames() {
        // TODO Auto-generated method stub

    }

    public void afterProcessingTimeFrames() {
        List visibleTasks = ((ChartModelImpl) getChartModel())
                .getVisibleTasks();
        int bottomY = getConfig().getRowHeight();
        for (int i = 0; i < visibleTasks.size(); i++) {
            Task nextTask = (Task) visibleTasks.get(i);
            ResourceAssignment[] assignments = nextTask.getAssignments();
            if (assignments.length > 0) {
                StringBuffer resources = new StringBuffer();
                for (int j = 0; j < assignments.length; j++) {
                    resources.append(assignments[j].getResource().getName());
                    if (j < assignments.length - 1) {
                        resources.append(", ");
                    }
                }
                Text text = getPrimitiveContainer().createText(0, bottomY,
                        resources.toString());
                text.setStyle("task.resources");
            }
            bottomY += getConfig().getRowHeight();
        }
    }

    public void startTimeFrame(TimeFrame timeFrame) {
        // TODO Auto-generated method stub

    }

    public void endTimeFrame(TimeFrame timeFrame) {
        // TODO Auto-generated method stub

    }

    public void startUnitLine(TimeUnit timeUnit) {
        // TODO Auto-generated method stub

    }

    public void endUnitLine(TimeUnit timeUnit) {
        // TODO Auto-generated method stub

    }

    public void nextTimeUnit(int unitIndex) {
        // TODO Auto-generated method stub

    }

}
