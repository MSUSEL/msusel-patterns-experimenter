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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.task.TaskLength;
import net.sourceforge.ganttproject.time.TimeUnit;

/**
 * @author bard
 */
public interface GPCalendar {
    List getActivities(Date startDate, Date endDate);

    List getActivities(Date startDate, TimeUnit timeUnit, long l);

    void setWeekDayType(int day, DayType type);

    DayType getWeekDayType(int day);

    void setPublicHoliDayType(int month, int date);

    public void setPublicHoliDayType(Date curDayStart);

    public boolean isPublicHoliDay(Date curDayStart);

    public boolean isNonWorkingDay(Date curDayStart);

    public DayType getDayTypeDate(Date curDayStart);

    public void setPublicHolidays(URL calendar, GanttProject gp);

    public Collection getPublicHolidays();

    final class DayType {
        public static final DayType WORKING = new DayType();

        public static final DayType WEEKEND = new DayType();

        public static final DayType HOLIDAY = new DayType();
    }

    Date findClosestWorkingTime(Date time);

    /**
     * Adds <code>shift</code> period to <code>input</code> date taking into
     * account this calendar working/non-working time If input date corresponds
     * to friday midnight and this calendar if configured to have a weekend on
     * saturday and sunday then adding a shift of "1 day" will result to the
     * midnight of the next monday
     */
    Date shiftDate(Date input, TaskLength shift);

    GPCalendar PLAIN = new AlwaysWorkingTimeCalendarImpl();
    String EXTENSION_POINT_ID = "net.sourceforge.ganttproject.calendar";

}
