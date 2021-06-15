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
package net.sourceforge.ganttproject.test.time;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.ganttproject.time.gregorian.WeekFramerImpl;
import junit.framework.TestCase;

/**
 * @author bard
 */
public class TestWeekFramer extends TestCase {
    public void testAdjustLeft() {
        WeekFramerImpl framer = new WeekFramerImpl();
        Date adjusted = framer.adjustLeft(newMonday());
        Calendar c = (Calendar) Calendar.getInstance().clone();
        c.setTime(adjusted);
        c.add(Calendar.MILLISECOND, -1);
        assertEquals("Unexpected day of week", Calendar.SUNDAY, c
                .get(Calendar.DAY_OF_WEEK));
        //
        Date adjustedSunday = framer.adjustLeft(newSunday());
        assertEquals(
                "Adjusted sunday is expected to be equal to adjusted monday",
                adjusted, adjustedSunday);
    }

    public void testAdjustRight() {
        WeekFramerImpl framer = new WeekFramerImpl();
        Date adjustedMonday = framer.adjustRight(newMonday());
        Date adjustedSunday = framer.adjustRight(newSunday());
        assertEquals(adjustedMonday, adjustedSunday);
        Calendar c = (Calendar) Calendar.getInstance().clone();
        c.setTime(adjustedMonday);
        assertEquals("Unexpected day of week", Calendar.MONDAY, c
                .get(Calendar.DAY_OF_WEEK));
        c.add(Calendar.MILLISECOND, -1);
        assertEquals("Unexpected day of week", Calendar.SUNDAY, c
                .get(Calendar.DAY_OF_WEEK));
    }

    public void testJumpLeft() {
        WeekFramerImpl framer = new WeekFramerImpl();
        Date adjustedMonday = framer.jumpLeft(newMonday());
        Date adjustedSunday = framer.jumpLeft(newSunday());
        assertNotSame(adjustedMonday, adjustedSunday);
        Calendar c = (Calendar) Calendar.getInstance().clone();
        c.setTime(adjustedMonday);
        assertTrue("Unexpected day of week, date=" + c.getTime(),
                Calendar.MONDAY == c.get(Calendar.DAY_OF_WEEK));
        assertNotSame(adjustedMonday, newMonday());
        c.setTime(adjustedSunday);
        assertEquals("Unexpected day of week, date=" + c.getTime(),
                Calendar.SUNDAY, c.get(Calendar.DAY_OF_WEEK));
        assertNotSame(adjustedMonday, newSunday());
    }

    private Date newMonday() {
        Calendar c = (Calendar) Calendar.getInstance().clone();
        c.clear();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.NOVEMBER);
        c.set(Calendar.DAY_OF_MONTH, 8);
        return c.getTime();
    }

    private Date newSunday() {
        Calendar c = (Calendar) Calendar.getInstance().clone();
        c.clear();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.NOVEMBER);
        c.set(Calendar.DAY_OF_MONTH, 14);
        return c.getTime();

    }
}
