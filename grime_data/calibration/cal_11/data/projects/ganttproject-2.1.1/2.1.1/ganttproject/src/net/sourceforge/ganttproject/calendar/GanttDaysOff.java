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
/**
 * 
 */
package net.sourceforge.ganttproject.calendar;

import java.util.Date;

import net.sourceforge.ganttproject.GanttCalendar;

/**
 * @author nbohn
 */
public class GanttDaysOff {
    private final GanttCalendar myStart, myFinish;

    public GanttDaysOff(Date start, Date finish) {
        myStart = new GanttCalendar(start);
        myFinish = new GanttCalendar(finish);
    }
    public GanttDaysOff(GanttCalendar start, GanttCalendar finish) {
        myStart = new GanttCalendar(start.getYear(), start.getMonth(), start
                .getDate());
        myFinish = finish;
    }

    public String toString() {
        return (myStart + " -> " + myFinish);
    }

    public boolean equals(GanttDaysOff dayOffs) {
        return ((dayOffs.getStart().equals(myStart)) && (dayOffs.getFinish()
                .equals(myFinish)));
    }

    public GanttCalendar getStart() {
        return myStart;
    }

    public GanttCalendar getFinish() {
        return myFinish;
    }

    public boolean isADayOff(GanttCalendar date) {
        return (date.equals(myStart) || date.equals(myFinish) || (date
                .before(myFinish) && date.after(myStart)));
    }

    public boolean isADayOff(Date date) {
        return (date.equals(myStart.getTime())
                || date.equals(myFinish.getTime()) || (date.before(myFinish
                .getTime()) && date.after(myStart.getTime())));
    }

    public int isADayOffInWeek(Date date) {
        GanttCalendar start = myStart.Clone();
        GanttCalendar finish = myFinish.Clone();
        for (int i = 0; i < 7; i++) {
            start.add(-1);
            finish.add(-1);
            if (date.equals(start.getTime())
                    || date.equals(finish.getTime())
                    || (date.before(finish.getTime()) && date.after(start
                            .getTime())))
                return i + 1;
        }
        return -1;
    }

    public int getDuration() {
        return (myStart.diff(myFinish)) + 1;
    }

    public Object clone() {
        return new GanttDaysOff(myStart.Clone(), myFinish.Clone());
    }

}
