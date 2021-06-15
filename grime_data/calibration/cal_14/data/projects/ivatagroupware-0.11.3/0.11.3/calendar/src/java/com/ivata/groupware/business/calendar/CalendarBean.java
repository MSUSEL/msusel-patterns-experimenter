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

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.calendar.event.EventDO;
import com.ivata.groupware.business.calendar.event.meeting.MeetingDO;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationErrors;


/**
 * @author Jan Boros <janboros@sourceforge.net>
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/**
 * <p>Calendar facade to enter, retrieve and delete events and meetings.</p>
 *
 * @since 2002-07-03
 * @author Jan Boros <janboros@sourceforge.net>
 * @version $Revision: 1.3 $
 *
 * @ejb.bean
 *      name="Calendar"
 *      display-name="Calendar"
 *      type="Stateless"
 *      view-type="remote"
 *      jndi-name="CalendarRemote"
 *
 * @ejb.transaction
 *      type = "Required"
 *
 *  @ejb.home
 *      generate="false"
 *      remote-class="com.ivata.groupware.business.calendar.CalendarRemoteHome"
 *
 *  @ejb.interface
 *      remote-class="com.ivata.groupware.business.calendar.CalendarRemote"
 */
public class CalendarBean implements SessionBean, Calendar {
    /**
     * <p>Provides the session bean with container-specific information.</p>
     */
    SessionContext sessionContext;

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
            throws SystemException {
        return getCalendar().addEvent(securitySession, eventDO);
    }

    /** <p>method using to modifi event</p>
     *
     * @param eventDO event to AMEND
     * @return amend event
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public EventDO amendEvent(final SecuritySession securitySession,
            final EventDO eventDO)
            throws SystemException {
        return getCalendar().amendEvent(securitySession, eventDO);
    }

    /**
     * <p>Called by the container to notify an entity object it has been
     * activated.</p>
     */
    public void ejbActivate() {}

    /**
     * <p>Called by the container just after the bean has been created.</p>
     *
     * @exception CreateException if any error occurs. Never thrown by this
     *     class.
     *
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {}

    /**
     * <p>Called by the container to notify the entity object it will be
     * deactivated. Called just before deactivation.</p>
     */
    public void ejbPassivate() {}

    /**
     * <p>This method is called by the container when the bean is about
     * to be removed.</p>
     *
     * <p>This method will be called after a client calls the <code>remove</code>
     * method of the remote/local home interface.</p>
     *
     * @throws RemoveException if any error occurs. Currently never thrown by
     *     this class.
     */
    public void ejbRemove() {}

    /**
     * <p>find eventDO for that Id</p>
     *
     *
     * @param
     * @return Vector of events
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public EventDO findEventByPrimaryKey(final SecuritySession securitySession,
            final String id)
            throws SystemException {
        return getCalendar().findEventByPrimaryKey(securitySession, id);
    }

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
            throws SystemException {
        return getCalendar().findEventsForDay(securitySession, day);
    }

    /**
     * Get the Calendar implementation.
     */
    private Calendar getCalendar()
            throws SystemException {
        PicoContainer container = PicoContainerFactory.getInstance()
            .getGlobalContainer();
        return (Calendar) container.getComponentInstance(Calendar.class);
    }

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
            throws SystemException {
        getCalendar().removeEvent(securitySession, event);
    }

    /**
     * <p>Set up the context for this entity object. The session bean stores the
     * context for later use.</p>
     *
     * @param sessionContext the new context which the session object should
     *     store.
     */
    public final void setSessionContext(final SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

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
            throws SystemException {
        return getCalendar().validate(securitySession, eventDO);
    }

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
            throws SystemException {
        return getCalendar().validate(securitySession, meetingDO);
    }
}
