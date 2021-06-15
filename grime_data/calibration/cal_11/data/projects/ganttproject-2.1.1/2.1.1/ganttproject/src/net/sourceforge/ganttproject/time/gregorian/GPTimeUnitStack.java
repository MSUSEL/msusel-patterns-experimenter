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
 * Created on 08.11.2004
 */
package net.sourceforge.ganttproject.time.gregorian;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.time.TimeFrame;
import net.sourceforge.ganttproject.time.TimeUnit;
import net.sourceforge.ganttproject.time.TimeUnitGraph;
import net.sourceforge.ganttproject.time.TimeUnitPair;
import net.sourceforge.ganttproject.time.TimeUnitStack;

/**
 * @author bard
 */
public class GPTimeUnitStack implements TimeUnitStack {
    private TimeUnitGraph ourGraph = new TimeUnitGraph();

    public final TimeUnit DAY;

    public final TimeUnit WEEK;

    public final TimeUnit MONTH;

    public TimeUnit QUARTER;

    public TimeUnit YEAR = null;

    private final TimeUnitPair[] myPairs;

    private TimeUnit MONTH_FROM_WEEKS;

    public final TimeUnit WEEK_AS_BOTTOM_UNIT;

    /**
     * 
     */
    public GPTimeUnitStack(GanttLanguage i18n) {
        TimeUnit atom = ourGraph.createAtomTimeUnit("atom");
        DAY = ourGraph.createDateFrameableTimeUnit("day", atom, 1,
                new FramerImpl(Calendar.DATE));
        DAY.setTextFormatter(new DayTextFormatter());
        MONTH = ourGraph.createTimeUnitFunctionOfDate("month", DAY,
                new FramerImpl(Calendar.MONTH));
        MONTH.setTextFormatter(new MonthTextFormatter());
        WEEK = ourGraph.createDateFrameableTimeUnit("week", DAY, 7,
                new WeekFramerImpl());
        MONTH_FROM_WEEKS = ourGraph.createTimeUnitFunctionOfDate(
                "month_from_weeks", WEEK, new FramerImpl(Calendar.MONTH));
        WEEK.setTextFormatter(new WeekTextFormatter(i18n.getText("week")
                + " {0}"));
        WEEK_AS_BOTTOM_UNIT = ourGraph.createDateFrameableTimeUnit("week", DAY,
                7, new WeekFramerImpl());
        WEEK_AS_BOTTOM_UNIT.setTextFormatter(new WeekTextFormatter("{0}"));
        YEAR = ourGraph.createTimeUnitFunctionOfDate("year", DAY,
                new FramerImpl(Calendar.YEAR));
        YEAR.setTextFormatter(new YearTextFormatter());
        myPairs = new TimeUnitPair[] { new MyTimeUnitPair(WEEK, DAY),
                new MyTimeUnitPair(WEEK, DAY), new MyTimeUnitPair(MONTH, DAY),
                new MyTimeUnitPair(MONTH, WEEK_AS_BOTTOM_UNIT),
                new MyTimeUnitPair(MONTH, WEEK_AS_BOTTOM_UNIT),
                new MyTimeUnitPair(MONTH, WEEK_AS_BOTTOM_UNIT),
                new MyTimeUnitPair(MONTH, WEEK_AS_BOTTOM_UNIT),
                new MyTimeUnitPair(YEAR, WEEK_AS_BOTTOM_UNIT),
                new MyTimeUnitPair(YEAR, WEEK_AS_BOTTOM_UNIT) };
    }

    public TimeFrame createTimeFrame(Date baseDate, TimeUnit topUnit,
            TimeUnit bottomUnit) {
        // if (topUnit instanceof TimeUnitFunctionOfDate) {
        // topUnit = ((TimeUnitFunctionOfDate)topUnit).createTimeUnit(baseDate);
        // }
        return new TimeFrameImpl(baseDate, topUnit, bottomUnit);
    }

    public String getName() {
        return "default";
    }

    public TimeUnit getDefaultTimeUnit() {
        return DAY;
    }

    public TimeUnitPair[] getTimeUnitPairs() {
        return myPairs;
    }

    private class MyTimeUnitPair extends TimeUnitPair {
        MyTimeUnitPair(TimeUnit topUnit, TimeUnit bottomUnit) {
            super(topUnit, bottomUnit, GPTimeUnitStack.this);
        }
    }
}
