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
package com.ivata.groupware.business.calendar.event;

import java.util.Calendar;

import com.ivata.groupware.container.persistence.TimestampDO;

/**
 * <p>Event for Calendar</p>
 *
 * @since 2002-07-05
 * @author Jan Boros <janboros@sourceforge.net>
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @hibernate.class
 *      table="calendar_event"
 * @hibernate.cache
 *      usage="read-write"
 */
public class EventDO extends TimestampDO {
    /**
     * <p>Store whether or not this is an 'all-day' event.</p>
     */
    private boolean allDayEvent;

    /**
     * <p>Store the description with additional information about the event.</p>
     */
    private String description;

    /**
     * <p>Store the finish Calendar/time of the event.</p>
     */
    private Calendar finish;

    /**
     * <p>Store the start Calendar/time of the event.</p>
     */
    private Calendar start;

    /**
     * <p>Store the subject of this event.</p>
     */
    private String subject;

    /**
     * <p>Get the description with additional information about the event.</p>
     *
     * @return current value of description.
     * @hibernate.property
     */
    public final String getDescription() {
        return description;
    }

    /**
     * <p>Get the time the event will finish.</p>
     *
     * @return time the event will finish.
     * @hibernate.property
     */
    public final Calendar getFinish() {
        return finish;
    }

    /**
     * <p>Get the time the event will start.</p>
     *
     * @return time the event will start.
     * @hibernate.property
     */
    public final Calendar getStart() {
        return start;
    }

    /**
     * <p>Get the brief subject describing the purpose of the event.</p>
     *
     * @return current value of subject.
     * @hibernate.property
     */
    public final String getSubject() {
        return subject;
    }

    /**
     * <p>Get whether or not this is an 'all-day' event.</p>
     *
     * @return <code>true</code> if this is an 'all-day' event,
     *     otherwise <code>false</code>.
     *
     * TODO: change the column in the table
     * @hibernate.property
     *      column="dayevent"
     */
    public boolean isAllDayEvent() {
        return allDayEvent;
    }

    /**
     * <p>Set whether or not this is an 'all-day' event.</p>
     *
     * @param allDayEvent <code>true</code> if this is an 'all-day' event,
     *     otherwise <code>false</code>.
     */
    public final void setAllDayEvent(final boolean allDayEvent) {
        this.allDayEvent = allDayEvent;
    }

    /**
     * <p>Set the description with additional information about the event.</p>
     *
     * @param description new value of description.
     */
    public final void setDescription(final String description) {
        this.description = description;
    }

    /**
     * <p>Set the time the event will finish.</p>
     */
    public final void setFinish(final Calendar finish) {
        this.finish = finish;
    }

    /**
     * <p>Set the time the event will start.</p>
     *
     * @param start new time the event will start.
     */
    public final void setStart(final Calendar start) {
        this.start = start;
    }
    /**
     * <p>Set the brief subject describing the purpose of the event.</p>
     *
     * @param subject new value of subject.
     */
    public final void setSubject(final String subject) {
        this.subject = subject;
    }

}
