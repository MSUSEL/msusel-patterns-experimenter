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
 * Created on 31.10.2004
 */
package net.sourceforge.ganttproject.test.task.calendar;

import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.time.gregorian.GregorianTimeUnitStack;

/**
 * @author bard
 */
public class TestSetLength extends TestWeekendCalendar {
    public void testTaskStartingOnFridayLastingTwoDaysEndsOnTuesday() {
        Task t = getTaskManager().createTask();
        t.setStart(newFriday());
        t.setDuration(getTaskManager().createLength(GregorianTimeUnitStack.DAY,
                2));
        assertEquals(
                "unXpected end of task which starts on friday and is 2 days long",
                newTuesday(), t.getEnd());
    }

    public void testTaskStartingOnSaturdayLastingOneDayEndsOnTuesday() {
        Task t = getTaskManager().createTask();
        t.setStart(newSaturday());
        t.setDuration(getTaskManager().createLength(GregorianTimeUnitStack.DAY,
                1));
        assertEquals(
                "unXpected end of task which starts on saturday and is 1 day long",
                newTuesday(), t.getEnd());
    }

    public void testTaskStartingOnSundayLastingOneDayEndsOnTuesday() {
        Task t = getTaskManager().createTask();
        t.setStart(newSunday());
        t.setDuration(getTaskManager().createLength(GregorianTimeUnitStack.DAY,
                1));
        assertEquals(
                "unXpected end of task which starts on sunday and is 1 day long",
                newTuesday(), t.getEnd());

    }

}
