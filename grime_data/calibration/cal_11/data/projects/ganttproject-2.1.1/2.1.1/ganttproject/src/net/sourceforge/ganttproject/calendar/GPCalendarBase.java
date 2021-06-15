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
 * Created on 10.05.2005
 */
package net.sourceforge.ganttproject.calendar;

import java.util.Date;
import java.util.List;

import net.sourceforge.ganttproject.task.TaskLength;
import net.sourceforge.ganttproject.time.TimeUnit;

/**
 * @author bard
 */
abstract class GPCalendarBase {
    public Date shiftDate(Date input, TaskLength shift) {
        List activities = getActivities(input, shift);
        if (activities.isEmpty()) {
            throw new RuntimeException(
                    "FIXME: Failed to compute calendar activities in time period="
                            + shift + " starting from " + input);
        }
        Date result;
        if (shift.getValue() >= 0) {
            GPCalendarActivity lastActivity = (GPCalendarActivity) activities
                    .get(activities.size() - 1);
            result = lastActivity.getEnd();
        } else {
            GPCalendarActivity firstActivity = (GPCalendarActivity) activities
                    .get(0);
            result = firstActivity.getStart();
        }
        return result;

    }

    public List getActivities(Date startDate, TimeUnit timeUnit, long unitCount) {
        return unitCount > 0 ? getActivitiesForward(startDate, timeUnit,
                unitCount) : getActivitiesBackward(startDate, timeUnit,
                -unitCount);
    }

    protected abstract List getActivitiesBackward(Date startDate,
            TimeUnit timeUnit, long l);

    protected abstract List getActivitiesForward(Date startDate,
            TimeUnit timeUnit, long l);

    public List/* <GPCalendarActivity> */getActivities(Date startingFrom,
            TaskLength period) {
        return getActivities(startingFrom, period.getTimeUnit(), period
                .getLength());
    }

}
