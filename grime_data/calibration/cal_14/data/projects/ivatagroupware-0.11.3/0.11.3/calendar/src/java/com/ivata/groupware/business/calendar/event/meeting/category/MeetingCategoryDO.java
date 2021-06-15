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
package com.ivata.groupware.business.calendar.event.meeting.category;

import java.util.Set;

import com.ivata.groupware.business.calendar.event.meeting.MeetingDO;
import com.ivata.groupware.container.persistence.BaseDO;

/**
 * <p>The many agenda points which make up the meeting are grouped into
 * categories by this class.</p>
 *
 * @since 2002-06-18
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @hibernate.class
 *      table="meeting_category"
 * @hibernate.cache
 *      usage="read-write"
 */
public class MeetingCategoryDO  extends BaseDO {

    /**
     * <p>The agenda points contained int his category.</p>
     */
    private Set agendaPoints;

    /**
     * <p>The meeting this category refers to.</p>
     */
    private MeetingDO meeting;
    /**
     * <p>Name of this meeting category.</p>
     */
    private String name;
    /**
     * <p>Get the agenda points contained int his category.</p>
     *
     * @return the agenda points contained int his category.
     *
     * @hibernate.set
     * @hibernate.collection-key
     *      column="category"
     * @hibernate.collection-one-to-many
     *      class="com.ivata.groupware.business.calendar.event.meeting.agendapoint.AgendaPointDO"
     */
    public final Set getAgendaPoints() {
        return agendaPoints;
    }
    /**
     * <p>Get the meeting this category refers to.</p>
     *
     * @return the meeting this category refers to.
     *
     * @hibernate.many-to-one
     */
    public final MeetingDO getMeeting() {
        return meeting;
    }
    /**
     * <p>Get the name of this meeting category.</p>
     *
     * @return name of this meeting category
     *
     * @hibernate.property
     */
    public final String getName() {
        return name;
    }

    /**
     * <p>Aet the agenda points contained int his category.</p>
     *
     * @param agendaPoints the agenda points contained int his category.
     */
    public final void setAgendaPoints(final Set agendaPoints) {
        this.agendaPoints = agendaPoints;
    }

    /**
     * <p>Set the meeting this category refers to.</p>
     *
     * @param meeting the meeting this category refers to.
     */
    public final void setMeeting(final MeetingDO meeting) {
        this.meeting = meeting;
    }

    /**
     * <p>Set the name of this meeting category.</p>
     *
     * @param name name of this meeting category
     */
    public final void setName(final String name) {
        this.name = name;
    }
}
