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
package net.sourceforge.ganttproject.time.gregorian;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import net.sourceforge.ganttproject.time.DateFrameable;
import net.sourceforge.ganttproject.time.TimeFrame;
import net.sourceforge.ganttproject.time.TimeUnit;
import net.sourceforge.ganttproject.time.TimeUnitFunctionOfDate;
import net.sourceforge.ganttproject.time.TimeUnitGraph;
import net.sourceforge.ganttproject.time.TimeUnitPair;
import net.sourceforge.ganttproject.time.TimeUnitStack;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author bard Date: 01.02.2004
 */
public class GregorianTimeUnitStack implements TimeUnitStack {
    private static TimeUnitGraph ourGraph = new TimeUnitGraph();

    private static final DateFrameable DAY_FRAMER = new FramerImpl(
            Calendar.DATE);

    private static final DateFrameable MONTH_FRAMER = new FramerImpl(
            Calendar.MONTH);

    private static final DateFrameable HOUR_FRAMER = new FramerImpl(
            Calendar.HOUR);

    private static final DateFrameable MINUTE_FRAMER = new FramerImpl(
            Calendar.MINUTE);

    private static final DateFrameable SECOND_FRAMER = new FramerImpl(
            Calendar.SECOND);

    public static final TimeUnit SECOND;// =

    // ourGraph.createAtomTimeUnit("second");

    public static final TimeUnit MINUTE;// = ourGraph.createTimeUnit("minute",

    // SECOND, 60);

    public static final TimeUnit HOUR;// = ourGraph.createTimeUnit("hour",

    // MINUTE, 60);

    public static final TimeUnit DAY;

    public static final TimeUnitFunctionOfDate MONTH;

    private static final TimeUnit ATOM_UNIT;

    private static final HashMap ourUnit2field = new HashMap();
    static {
        SECOND = ourGraph.createAtomTimeUnit("second");
        MINUTE = ourGraph.createDateFrameableTimeUnit("minute", SECOND, 60,
                MINUTE_FRAMER);
        HOUR = ourGraph.createDateFrameableTimeUnit("hour", MINUTE, 60,
                HOUR_FRAMER);
        ;
        DAY = ourGraph.createDateFrameableTimeUnit("day", HOUR, 24, DAY_FRAMER);
        DAY.setTextFormatter(new DayTextFormatter());
        MONTH = ourGraph.createTimeUnitFunctionOfDate("month", DAY,
                MONTH_FRAMER);
        MONTH.setTextFormatter(new MonthTextFormatter());
        ATOM_UNIT = SECOND;
        ourUnit2field.put(DAY, new Integer(Calendar.DAY_OF_MONTH));
        ourUnit2field.put(HOUR, new Integer(Calendar.HOUR_OF_DAY));
        ourUnit2field.put(MINUTE, new Integer(Calendar.MINUTE));
        ourUnit2field.put(SECOND, new Integer(Calendar.SECOND));
    }

    public GregorianTimeUnitStack() {

    }

    public TimeFrame createTimeFrame(Date baseDate, TimeUnit topUnit,
            TimeUnit bottomUnit) {
        if (topUnit instanceof TimeUnitFunctionOfDate) {
            topUnit = ((TimeUnitFunctionOfDate) topUnit)
                    .createTimeUnit(baseDate);
        }
        return new TimeFrameImpl(baseDate, topUnit, bottomUnit);
    }

    public TimeUnit getDefaultTimeUnit() {
        return DAY;
    }

    public TimeUnitPair[] getTimeUnitPairs() {
        return null;
    }

    public String getName() {
        return null;
    }
}
