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
 * This code is provided under the terms of GPL version 2.
 * Please see LICENSE file for details
 * (C) Dmitry Barashev, GanttProject team, 2004-2008
 */
package net.sourceforge.ganttproject.chart;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sourceforge.ganttproject.calendar.CalendarFactory;
import net.sourceforge.ganttproject.calendar.GPCalendar;
import net.sourceforge.ganttproject.chart.ChartModelBase.Offset;
import net.sourceforge.ganttproject.chart.GraphicPrimitiveContainer.Line;
import net.sourceforge.ganttproject.gui.UIConfiguration;
import net.sourceforge.ganttproject.gui.options.model.BooleanOption;
import net.sourceforge.ganttproject.gui.options.model.GPOption;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.time.TimeUnitText;
import net.sourceforge.ganttproject.time.gregorian.FramerImpl;

/**
 * Renders chart timeline.
 */
class ChartHeaderImpl extends ChartRendererBase implements ChartHeader {

    private GraphicPrimitiveContainer myPrimitiveContainer;
    private final BooleanOption myRedlineOption;
    private final BooleanOption myProjectDatesOption;
    private GPOptionGroup myOptions;
    private final FramerImpl myDayFramer = new FramerImpl(Calendar.DAY_OF_MONTH);
    private Date myToday;

    ChartHeaderImpl(ChartModelBase model, final UIConfiguration projectConfig) {
        super(model);
        myRedlineOption = projectConfig.getRedlineOption();
        myProjectDatesOption= projectConfig.getProjectBoundariesOption();
        myOptions = new ChartOptionGroup(
                "ganttChartGridDetails",
                new GPOption[] {myRedlineOption, myProjectDatesOption},
                model.getOptionEventDispatcher());
        myPrimitiveContainer = new GraphicPrimitiveContainer();
    }

    GPOptionGroup getOptions() {
        return myOptions;
    }

    public GraphicPrimitiveContainer getPrimitiveContainer() {
        return myPrimitiveContainer;
    }

    public void beforeProcessingTimeFrames() {
        myPrimitiveContainer = new GraphicPrimitiveContainer();
        myPrimitiveContainer.clear();
        createGreyRectangleWithNiceBorders();
        myToday = myDayFramer.adjustLeft(CalendarFactory.newCalendar().getTime());

    }

    /** Draws the timeline box
     */
    private void createGreyRectangleWithNiceBorders() {
        int sizex = getWidth();
        int spanningHeaderHeight = getChartModel().getChartUIConfiguration()
                .getSpanningHeaderHeight();

        GraphicPrimitiveContainer.Rectangle headerRectangle = myPrimitiveContainer
                .createRectangle(0, 0, sizex, spanningHeaderHeight * 2);
        headerRectangle.setBackgroundColor(getChartModel()
                .getChartUIConfiguration().getSpanningHeaderBackgroundColor());
        //
        GraphicPrimitiveContainer.Rectangle spanningHeaderBorder = myPrimitiveContainer
                .createRectangle(0, 0, sizex - 1, spanningHeaderHeight);
        spanningHeaderBorder.setForegroundColor(getChartModel()
                .getChartUIConfiguration().getHeaderBorderColor());
        //
        GraphicPrimitiveContainer.Rectangle timeunitHeaderBorder = myPrimitiveContainer
                .createRectangle(0, spanningHeaderHeight, sizex - 1,
                        spanningHeaderHeight);
        timeunitHeaderBorder.setForegroundColor(getChartModel()
                .getChartUIConfiguration().getHeaderBorderColor());
        //
        GraphicPrimitiveContainer.Line middleGutter1 = myPrimitiveContainer
                .createLine(1, spanningHeaderHeight - 1, sizex - 2,
                        spanningHeaderHeight - 1);
        middleGutter1.setForegroundColor(getChartModel()
                .getChartUIConfiguration().getHorizontalGutterColor1());
        //
        GraphicPrimitiveContainer.Line bottomGutter = myPrimitiveContainer
                .createLine(0, spanningHeaderHeight * 2 - 2, sizex - 2,
                        spanningHeaderHeight * 2 - 2);
        bottomGutter.setForegroundColor(getChartModel()
                .getChartUIConfiguration().getHorizontalGutterColor1());
        //
        GraphicPrimitiveContainer.Line topGutter = myPrimitiveContainer
                .createLine(1, 1, sizex - 2, 1);
        topGutter.setForegroundColor(getChartModel().getChartUIConfiguration()
                .getHorizontalGutterColor2());
        //
        myPrimitiveContainer.createLine(
                0, spanningHeaderHeight + 1, sizex - 2, spanningHeaderHeight + 1);
        topGutter.setForegroundColor(getChartModel().getChartUIConfiguration()
                .getHorizontalGutterColor2());
    }

    public GraphicPrimitiveContainer paint() {
        return myPrimitiveContainer;
    }

    public void render() {
        beforeProcessingTimeFrames();
        renderTopUnits();
        renderBottomUnits();
    }

    /** Draws cells of the top line in the time line
     */
    private void renderTopUnits() {
        int curX = 0;
        Date curDate = getChartModel().getStartDate();
        final int topUnitHeight = getChartModel().getChartUIConfiguration().getSpanningHeaderHeight();
        for (int i=0; i<getChartModel().getTopUnitOffsets().size(); i++) {
        	Offset nextOffset = (Offset) getChartModel().getTopUnitOffsets().get(i);
            TimeUnitText timeUnitText = nextOffset.getOffsetUnit().format(curDate);
            String unitText = timeUnitText.getText(-1);
            int posY = topUnitHeight - 5;
            GraphicPrimitiveContainer.Text text = myPrimitiveContainer.createText(curX + 2, posY, unitText);
            myPrimitiveContainer.bind(text, timeUnitText);
            text.setMaxLength(nextOffset.getOffsetPixels() - curX);
            text.setFont(getChartModel().getChartUIConfiguration().getSpanningHeaderFont());
            myPrimitiveContainer.createLine(curX, 0, curX, topUnitHeight);
            curX = nextOffset.getOffsetPixels();
            curDate = nextOffset.getOffsetEnd();
        }
    }

    /** Draws cells of the bottom line in the time line
     */
    private void renderBottomUnits() {
        int curX = 0;
        Date curDate = getChartModel().getStartDate();
        final int topUnitHeight = getChartModel().getChartUIConfiguration().getSpanningHeaderHeight();
        boolean firstWeekendDay = true;
        for (int i=0; i<getChartModel().getBottomUnitOffsets().size(); i++) {
        	Offset nextOffset = (Offset) getChartModel().getBottomUnitOffsets().get(i);
            TimeUnitText timeUnitText = nextOffset.getOffsetUnit().format(curDate);
            String unitText = timeUnitText.getText(-1);
            int posY = 2*topUnitHeight - 5;
            GraphicPrimitiveContainer.Text text = myPrimitiveContainer.createText(curX + 2, posY, unitText);
            myPrimitiveContainer.bind(text, timeUnitText);
            text.setMaxLength(nextOffset.getOffsetPixels() - curX);
            text.setFont(getChartModel().getChartUIConfiguration().getSpanningHeaderFont());
            myPrimitiveContainer.createLine(curX, topUnitHeight, curX, 2*topUnitHeight);
            firstWeekendDay = true;
            curX = nextOffset.getOffsetPixels();
            curDate = nextOffset.getOffsetEnd();
        }
    }
    private boolean isProjectBoundariesOptionOn() {
        return myProjectDatesOption.isChecked();
    }

}
