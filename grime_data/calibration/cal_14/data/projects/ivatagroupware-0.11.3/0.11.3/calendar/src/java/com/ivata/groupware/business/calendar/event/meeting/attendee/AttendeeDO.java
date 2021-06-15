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
package com.ivata.groupware.business.calendar.event.meeting.attendee;

import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.calendar.event.meeting.MeetingDO;
import com.ivata.groupware.container.persistence.BaseDO;

/**
 * <p>Every meeting in ivata groupware may have many attendees, each of whom may
 * or may not be intranet users themselves. This class records which of the
 * invited attendees have confirmed they will attend.</p>
 *
 * @since 2002-06-18
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @hibernate.class
 *      table="meeting_attendee"
 * @hibernate.cache
 *      usage="read-write"
 */
public class AttendeeDO  extends BaseDO {
    /**
     * <p>Find out whether or not this attendess has confirmed s/he will attend.</p>
     */
    private boolean confirmed;

    /**
     * <p>The meeting this attendee refers to.</p>
     */
    private MeetingDO meeting;

    /**
     * <p>Person who should attend.</p>
     */
    private PersonDO person;
    /**
     * <p>Find out whether or not this attendess has confirmed s/he will attend.</p>
     *
     * @return <code>true</code> if the attendee will attend, otherwise
     * <code>false</code>.
     *
     * @hibernate.property
     */
    public final boolean getConfirmed() {
        return confirmed;
    }
    /**
     * <p>Get the meeting this attendee refers to.</p>
     *
     * @return the meeting this attendee refers to.
     *
     * @hibernate.many-to-one
     *      class = com.ivata.groupware.business.calendar.event.EventDO
     */
    public final MeetingDO getMeeting() {
        return meeting;
    }
    /**
     * <p>Get person who should attend.</p>
     *
     * @return person who should attend.
     *
     * @hibernate.many-to-one
     */
    public PersonDO getPerson() {
        return person;
    }

    /**
     * <p>Set whether or not this attendess has confirmed s/he will attend.</p>
     *
     * @param confirmed <code>true</code> if the attendee will attend, otherwise
     * <code>false</code>.
     */
    public final void setConfirmed(final boolean confirmed) {
        this.confirmed = confirmed;
    }

    /**
     * <p>Set the meeting this attendee refers to.</p>
     *
     * @param meeting meeting this attendee refers to.
     */
    public final void setMeeting(final MeetingDO meeting) {
        this.meeting = meeting;
    }

    /**
     * <p>Set person who should attend.</p>
     *
     * @param person person who should attend.
     */
    public final void setPerson(final PersonDO person) {
        this.person = person;
    }
}
