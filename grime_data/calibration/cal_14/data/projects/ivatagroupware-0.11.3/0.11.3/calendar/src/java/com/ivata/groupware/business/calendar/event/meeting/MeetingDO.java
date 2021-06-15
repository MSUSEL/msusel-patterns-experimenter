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
package com.ivata.groupware.business.calendar.event.meeting;


import java.util.Set;

import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.calendar.event.EventDO;


/**
 * <p>Represents a meeting which takes place in the organization. This is
 * responsible for storing the details of the meeting, and interacting with the
 * library to create the minutes and agenda.</p>
 *
 * @since 2002-06-18
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 * @see MeetingBean
 *
 * @hibernate.joined-subclass
 *      table="meeting"
 * @hibernate.joined-subclass-key
 *      column="calendar_event"
 */
public class MeetingDO extends EventDO {
    /**
     * <p>Stores all people who will attend the meeting.</p>
     */
    private Set attendees;

    /**
     * <p>Stores categories of meeting minutes.</p>
     */
    private Set categories;

    /**
     * <p>Stores person in charge of the meeting.</p>
     */
    private PersonDO chairPerson;

    /**
     * <p>Stores the client-specific text describing the location of the
     * meeting.</p>
     */
    private String location;

    /**
     * <p>Get all people who will attend the meeting.</p>
     *
     * @return people who will attend the meeting.
     *
     * @nosuch.hibernate.set
     * @nosuch.hibernate.collection-key
     *      column="meeting"
     * @nosuch.hibernate.collection-one-to-many
     *      class="com.ivata.groupware.business.calendar.event.meeting.attendee.AttendeeDO"
     */
    public final Set getAttendees() {
        return attendees;
    }

    /**
     * <p>Get all meeting categories.</p>
     *
     * @return all meeting categories.
     *
     * @nosuch.hibernate.set
     * @nosuch.hibernate.collection-key
     *      column="meeting"
     * @nosuch.hibernate.collection-one-to-many
     *      class="com.ivata.groupware.business.calendar.event.meeting.category.MeetingCategoryDO"
     */
    public final Set getCategories() {
        return categories;
    }

    /**
     * <p>Get the person who is in charge of the meeting.</p>
     *
     * @return current value of the person in charge of the meeting.
     * @hibernate.many-to-one
     *      column="chair_person"
     */
    public PersonDO getChairPerson() {
        return chairPerson;
    }

    /**
     * <p>Get the location of the meeting.</p>
     *
     * @return current value of client-specific location text.
     * @hibernate.property
     */
    public final String getLocation() {
        return location;
    }

    /**
     * <p>Set all people who will attend the meeting.</p>
     *
     * @param people who will attend the meeting.
     */
    public final void setAttendees(final Set attendees) {
        this.attendees = attendees;
    }

    /**
     * <p>Set all of meeting categories.</p>
     *
     * @param categories new value of category names.
     */
    public final void setCategories(final Set categories) {
        this.categories = categories;
    }

    /**
     * <p>Set the person who is in charge of the meeting.</p>
     *
     * @param chairPerson new value of the person in
     *     charge of the meeting.
     */
    public final void setChairPerson(final PersonDO chairPerson) {
        this.chairPerson = chairPerson;
    }

    /**
     * <p>Set the location of the meeting.</p>
     *
     * @param location new value of client-specific location text.
     */
    public final void setLocation(final String location) {
        this.location = location;
    }
}
