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
package net.sourceforge.ganttproject.calendar;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.task.TaskLength;
import net.sourceforge.ganttproject.time.TimeUnit;

/**
 * @author bard
 */
public class AlwaysWorkingTimeCalendarImpl extends GPCalendarBase implements
        GPCalendar {
    public List getActivities(Date startDate, Date endDate) {
        return Collections.singletonList(new CalendarActivityImpl(startDate,
                endDate, true));
    }

    protected List getActivitiesForward(Date startDate, TimeUnit timeUnit,
            long l) {
        Date activityStart = timeUnit.adjustLeft(startDate);
        Date activityEnd = activityStart;
        for (; l > 0; l--) {
            activityEnd = timeUnit.adjustRight(activityEnd);
        }
        return Collections.singletonList(new CalendarActivityImpl(
                activityStart, activityEnd, true));
    }

    protected List getActivitiesBackward(Date startDate, TimeUnit timeUnit,
            long unitCount) {
        Date activityEnd = timeUnit.adjustLeft(startDate);
        Date activityStart = activityEnd;
        while (unitCount-- > 0) {
            activityStart = timeUnit.jumpLeft(activityStart);
        }
        return Collections.singletonList(new CalendarActivityImpl(
                activityStart, activityEnd, true));
    }

    public void setWeekDayType(int day, DayType type) {
        if (type == GPCalendar.DayType.WEEKEND) {
            throw new IllegalArgumentException(
                    "I am always working time calendar, I don't accept holidays!");
        }
    }

    public DayType getWeekDayType(int day) {
        return GPCalendar.DayType.WORKING;
    }

    public Date findClosestWorkingTime(Date time) {
        return time;
    }

    public void setPublicHoliDayType(int month, int date) {
        // TODO Auto-generated method stub

    }

    public boolean isPublicHoliDay(Date curDayStart) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isNonWorkingDay(Date curDayStart) {
        // TODO Auto-generated method stub
        return false;
    }

    public DayType getDayTypeDate(Date curDayStart) {
        // TODO Auto-generated method stub
        return GPCalendar.DayType.WORKING;
    }

    public void setPublicHoliDayType(Date curDayStart) {
        // TODO Auto-generated method stub

    }

    public void setPublicHolidays(URL calendar, GanttProject gp) {
        // TODO Auto-generated method stub

    }

    public Collection getPublicHolidays() {
        // TODO Auto-generated method stub
        return null;
    }

    public List getActivities(Date startingFrom, TaskLength period) {
        return getActivities(startingFrom, period.getTimeUnit(), period
                .getLength());
    }
}
