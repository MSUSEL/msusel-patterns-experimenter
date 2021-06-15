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
package com.ivata.groupware.business.calendar;

import java.util.Collection;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.calendar.event.EventDO;
import com.ivata.groupware.business.calendar.event.meeting.MeetingDO;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationErrors;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since 24-Mar-2004
 * @version $Revision: 1.3 $
 */
public interface Calendar {
    public final static String BUNDLE_PATH = "calendar";

    /** <p>method using to add NEW event</p>
     *
     * @param eventDO event to ADD
     * @return added event
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public EventDO addEvent(final SecuritySession securitySession,
            final EventDO eventDO)
        throws SystemException;

    /**
     * <p>method using to modifi event</p>
     *
     * @param eventDO event to AMEND
     * @return amend event
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public EventDO amendEvent(final SecuritySession securitySession,
            final EventDO eventDO)
        throws SystemException;

    /**
     * <p>find eventDO for that id</p>
     *
     *
     * @param day evens for this DAY
     * @return Vector of events
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public EventDO findEventByPrimaryKey(final SecuritySession securitySession,
            final String id)
            throws SystemException;

    /**
     * <p>this method giving Events for DAY. DAY has to be initial at clients site
     * with DAY, MOUTH, YEAR</p>
     *
     * @param day evens for this DAY
     * @return Vector of events
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public Collection findEventsForDay(final SecuritySession securitySession,
            final java.util.Calendar day)
            throws SystemException;

    /**
     * <p>method usig to remove event</p>
     *
     * @param eventDO event to REMOVE
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public void removeEvent(final SecuritySession securitySession,
            final EventDO event)
            throws SystemException;

    /**
     * <p>Confirm all of the elements of the event are present and valid.</p>
     *
     * @param eventDO data object to check for consistency and
     *     completeness.
     * @return a collection of validation errors if any of the
     *     mandatory fields are missing, or if fields contain invalid values.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public ValidationErrors validate(final SecuritySession securitySession,
            final EventDO eventDO)
            throws SystemException;

    /**
     * <p>Confirm all of the elements of the meeting and associated event are
     * present and valid.</p>
     *
     * @param meetingDO data object to check for consistency and
     *     completeness.
     * @param locale locale to show field errors for.
     * @return a collection of validation errors if any of the
     *     mandatory fields are missing, or if fields contain invalid values.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public ValidationErrors validate(final SecuritySession securitySession,
            final MeetingDO meetingDO)
            throws SystemException;
}