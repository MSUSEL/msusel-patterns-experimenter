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
 * Created on 21.11.2004
 */
package net.sourceforge.ganttproject.chart;

import java.awt.Color;
import java.util.List;

import net.sourceforge.ganttproject.time.TimeFrame;
import net.sourceforge.ganttproject.time.TimeUnit;

/**
 * @author bard
 */
public class TaskGridRendererImpl extends ChartRendererBase implements
        TimeUnitVisitor {
    public TaskGridRendererImpl(ChartModelImpl model) {
        super(model);
    }

    public void beforeProcessingTimeFrames() {
        getPrimitiveContainer().clear();

        // GraphicPrimitiveContainer.Rectangle r =
        // getPrimitiveContainer().createRectangle(0, 0, getWidth(),
        // getHeight());
        // r.setBackgroundColor(Color.WHITE);
        // System.err.println("background rect="+r);
        int rowHeight = getConfig().getRowHeight();
        int ypos = rowHeight;
        List/* <Task> */tasks = ((ChartModelImpl) getChartModel())
                .getVisibleTasks();
        //boolean isLightLine = true;
        for (int i = 0; i < tasks.size(); i++) {
            // GraphicPrimitiveContainer.Rectangle nextRect =
            // getPrimitiveContainer().createRectangle(0, ypos,
            // (int)getChartModel().getBounds().getWidth(), rowHeight-1);

            // nextRect.setStyle(isLightLine ? "grid.light" : "grid.dark");
            GraphicPrimitiveContainer.Line nextLine = getPrimitiveContainer()
                    .createLine(0, ypos,
                            (int) getChartModel().getBounds().getWidth(), ypos);
            nextLine.setForegroundColor(Color.GRAY);
            //isLightLine = !isLightLine;
            ypos += rowHeight;
        }
    }

    protected int getHeight() {
        return (int) getChartModel().getBounds().getHeight()
                - getConfig().getHeaderHeight();
    }

    public void afterProcessingTimeFrames() {
    }

    public void startTimeFrame(TimeFrame timeFrame) {
    }

    public void endTimeFrame(TimeFrame timeFrame) {
    }

    public void startUnitLine(TimeUnit timeUnit) {
    }

    public void endUnitLine(TimeUnit timeUnit) {
    }

    public void nextTimeUnit(int unitIndex) {
    }

}
