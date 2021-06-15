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
package org.archive.crawler.settings.refinements;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.TestCase;


/**
 *
 * @author John Erik Halse
 *
 */
public class TimespanCriteriaTest extends TestCase {
    public final void testIsWithinRefinementBounds() throws ParseException {
        DateFormat timeFormat;
        TimeZone TZ = TimeZone.getTimeZone("GMT");
        timeFormat = new SimpleDateFormat("HHmm");
        timeFormat.setTimeZone(TZ);
        Date now = timeFormat.parse(timeFormat.format(new Date()));

        String nowTime = timeFormat.format(now);
        String beforeTime1 = timeFormat.format(new Date(now.getTime() -
            1000 * 60 * 2));
        String beforeTime2 = timeFormat.format(new Date(now.getTime() -
            1000 * 60 * 1));
        String afterTime1 = timeFormat.format(new Date(now.getTime() +
            1000 * 60 * 1));

        // now is inside and before is less than after
        TimespanCriteria c = new TimespanCriteria(beforeTime1, afterTime1);
        assertTrue(c.isWithinRefinementBounds(null));

        // now is equal to before and less than after
        c = new TimespanCriteria(nowTime, afterTime1);
        assertTrue(c.isWithinRefinementBounds(null));

        // now is equal to before and less than after
        c = new TimespanCriteria(beforeTime1, nowTime);
        assertTrue(c.isWithinRefinementBounds(null));

        // now is outside and before is less than after
        c = new TimespanCriteria(beforeTime1, beforeTime2);
        assertFalse(c.isWithinRefinementBounds(null));

        // now is outside and before is greater than after
        c = new TimespanCriteria(afterTime1, beforeTime1);
        assertFalse(c.isWithinRefinementBounds(null));

        // now is inside and before is greater than after
        c = new TimespanCriteria(beforeTime2, beforeTime1);
        assertTrue(c.isWithinRefinementBounds(null));

        // now is equal to before and before is greater than after
        c = new TimespanCriteria(nowTime, beforeTime1);
        assertTrue(c.isWithinRefinementBounds(null));

        // now is equal to before and before is greater than after
        c = new TimespanCriteria(afterTime1, nowTime);
        assertTrue(c.isWithinRefinementBounds(null));
}

}
