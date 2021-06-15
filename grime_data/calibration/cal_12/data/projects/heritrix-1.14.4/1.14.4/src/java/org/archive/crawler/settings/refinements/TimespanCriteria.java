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

import org.archive.net.UURI;

/**
 * A refinement criteria that checks if a URI is requested within a specific
 * time frame. <p/>
 *
 * The timeframe's resolution is minutes and always operates in 24h GMT. The
 * format is <code>hhmm</code>, exmaples:
 * <p>
 * <code> 1200</code> for noon GMT <br>
 * <code> 1805</code> for 5 minutes past six in the afternoon GMT.
 *
 * @author John Erik Halse
 */
public class TimespanCriteria implements Criteria {

    private static DateFormat timeFormat;
    static {
        final TimeZone TZ = TimeZone.getTimeZone("GMT");
        timeFormat = new SimpleDateFormat("HHmm");
        timeFormat.setTimeZone(TZ);
    }

    private Date from;

    private Date to;

    /**
     * Create a new instance of TimespanCriteria.
     *
     * @param from start of the time frame (inclusive).
     * @param to end of the time frame (inclusive).
     * @throws ParseException
     */
    public TimespanCriteria(String from, String to) throws ParseException {
        setFrom(from);
        setTo(to);
    }

    public boolean isWithinRefinementBounds(UURI uri) {
        try {
            Date now = timeFormat.parse(timeFormat.format(new Date()));
            if (from.before(to)) {
                if (now.getTime() >= from.getTime()
                        && now.getTime() <= to.getTime()) {
                    return true;
                }
            } else {
                if (!(now.getTime() > to.getTime() && now.getTime() < from
                        .getTime())) {
                    return true;
                }
            }
        } catch (ParseException e) {
            // Should never happen since we are only parsing system time at
            // this place.
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Get the beginning of the time frame to check against.
     *
     * @return Returns the from.
     */
    public String getFrom() {
        return timeFormat.format(from);
    }

    /**
     * Set the beginning of the time frame to check against.
     *
     * @param from The from to set.
     * @throws ParseException
     */
    public void setFrom(String from) throws ParseException {
        this.from = timeFormat.parse(from);
    }

    /**
     * Get the end of the time frame to check against.
     *
     * @return Returns the to.
     */
    public String getTo() {
        return timeFormat.format(to);
    }

    /**
     * Set the end of the time frame to check against.
     *
     * @param to The to to set.
     * @throws ParseException
     */
    public void setTo(String to) throws ParseException {
        this.to = timeFormat.parse(to);
    }

    public boolean equals(Object o) {
        if (o instanceof TimespanCriteria) {
            TimespanCriteria other = (TimespanCriteria) o;
            if (this.from.equals(other.from) && this.to.equals(other.to)) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.settings.refinements.Criteria#getName()
     */
    public String getName() {
        return "Time of day criteria";
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.settings.refinements.Criteria#getDescription()
     */
    public String getDescription() {
        return "Accept any URIs between the hours of " + getFrom() + "(GMT) and "
            + getTo() + "(GMT) each day.";
    }
}