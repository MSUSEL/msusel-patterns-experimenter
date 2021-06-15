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
package com.ivata.groupware.business.calendar.struts;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.GregorianCalendar;

import com.ivata.groupware.business.calendar.event.EventDO;


/**
 * <p>This class is used to sort events which appear in a particular
 * day by start time (if they start on that day) or finish time (if
 * they
 * start on another day but finish on that day.</p>
 *
 * <p>This is the comparator applied in the <code>TreeSet</code>
 * instances in the <code>normalDayEvents</code> contained in
 * <code>IndexForm</code>.</p>
 *
 * @since 2003-02-07
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public class EventHourComparator implements Comparator {
    /**
     * <p>Represents the day for which the events are sorted.</p>
     */
    private GregorianCalendar day = null;
    /**
     * <p>Represents midnight the day after the day for which the events
     * are sorted. This is set automatically when <code>setDay</code> is
     * called.</p>
     */
    GregorianCalendar dayAfter = null;
    /**
     * <p>Used to format times in 24hour format, to sort them.</p>
     */
    private SimpleDateFormat orderFormat = new SimpleDateFormat("HHmmssSS");

    /**
     * <p>Compares the two parameters which are both actually instances of
     * <code>EventDO</code>.</p>.
     *
     * <p>If both events take place on the current day, start times are
     * compared, otherwise finish times are compared.</p>.
     *
     * @param arg0 first event to compare.
     * @param arg1 second event to compare
     * @return a positive number if the event <code>arg1</code> comes
     * after <code>arg0</code> otherwise a negative number.
     */
    public final int compare(final Object arg0,
            final Object arg1) {
        String key0 = getKey((EventDO) arg0);
        String key1 = getKey((EventDO) arg1);

        // check the day was set first
        if (day == null) {
            throw new NullPointerException("ERROR in EventHourComparator: day is null.");
        }
        return key0.compareTo(key1);
    }

    /**
     * <p>Always returns <code>false</code> since we do not want events to
     * be removed from the set</code>.
     *
     * @param arg0 event to compare, Always ignored by this
     * implementation.
     * @return always <code>false</code>.
     */
    public boolean equals(final Object arg0) {
        return false;
    }

    /**
     * <p>Represents the day for which the events are sorted.</p>
     *
     * @return the current value of day.
     */
    public final GregorianCalendar getDay() {
        return day;
    }

    /**
     * <p>Represents the day for which the events are sorted.</p>
     *
     * @param day the new value of day.
     */
    public final void setDay(final GregorianCalendar day) {
        this.day = day;
        dayAfter = new GregorianCalendar();
        dayAfter.setTime(day.getTime());
        // set the time to midnight tomorrow - when we compare, anything after
        // midnight is after today
        dayAfter.set(GregorianCalendar.HOUR_OF_DAY,0);
        dayAfter.set(GregorianCalendar.MINUTE,0);
        dayAfter.set(GregorianCalendar.SECOND,0);
        dayAfter.set(GregorianCalendar.MILLISECOND,0);
        dayAfter.set(GregorianCalendar.DAY_OF_YEAR,
            dayAfter.get(GregorianCalendar.DAY_OF_YEAR)+1);
    }

    /**
     * <p>Private helper method. Gets a key which is used to order the
     * events.</p>
     *
     * @param event event for which to get the key
     * @return a <code>String</code> which, when compared, will put the
     * event in the correct sequence for this day.
     */
    private String getKey(final EventDO event) {
        String key;
        // if the event starts today, use the start time to compare
        if (!event.getStart().before(day)) {
            key = orderFormat.format(event.getStart().getTime()) + event.getId().toString();

        // if the event finishes today but started before today, it is the
        // to time which is important
        } else if (!isAfterDay(event)) {
            // "to" ensures finishing events come after starting ones
            key = orderFormat.format(event.getFinish().getTime())
                + "to" + event.getId().toString();

        // if it gets here, it must be an all day event
        // just use the id to sort - "0000000000" should always come before the
        // other numbers
        } else {
            key = "0000000000" + event.getId().toString();
        }

        return key;
    }

    /**
     * <p>Find out if an event occurs after the current day.</p>
     *
     * @param event the event to compare with the current day.
     * @return <code>true</code> if the event occurs after the current day
     * otherwise <code>false</code>.
     */
    public boolean isAfterDay(final EventDO event) {
        // see if this event finishes after today (sometime tomorrow or
        // beyond)
        return (event.getFinish() != null) && !dayAfter.after(event.getFinish());
    }
}
