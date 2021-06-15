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

import net.sourceforge.ganttproject.calendar.CalendarFactory;
import net.sourceforge.ganttproject.time.DateFrameable;

public class WeekFramerImpl implements DateFrameable {
    private final FramerImpl myDayFramer = new FramerImpl(Calendar.DATE);

    public Date adjustRight(Date baseDate) {
        Calendar c = CalendarFactory.newCalendar();
        do {
            baseDate = myDayFramer.adjustRight(baseDate);
            c.setTime(baseDate);
        } while (c.get(Calendar.DAY_OF_WEEK) != c.getFirstDayOfWeek());
        return c.getTime();
    }

    public Date adjustLeft(Date baseDate) {
        Calendar c = CalendarFactory.newCalendar();
        c.setTime(myDayFramer.adjustLeft(baseDate));
        while (c.get(Calendar.DAY_OF_WEEK) != c.getFirstDayOfWeek()) {
            c
                    .setTime(myDayFramer.adjustLeft(myDayFramer.jumpLeft(c
                            .getTime())));
        }
        return c.getTime();
    }

    public Date jumpLeft(Date baseDate) {
        Calendar c = CalendarFactory.newCalendar();
        c.setTime(myDayFramer.adjustLeft(baseDate));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        do {
            baseDate = myDayFramer.jumpLeft(baseDate);
            c.setTime(baseDate);
        } while (c.get(Calendar.DAY_OF_WEEK) != dayOfWeek);
        return c.getTime();
    }
}