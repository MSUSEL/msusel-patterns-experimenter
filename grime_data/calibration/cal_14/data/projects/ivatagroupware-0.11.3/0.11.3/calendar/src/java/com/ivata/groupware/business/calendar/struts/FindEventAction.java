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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.calendar.Calendar;
import com.ivata.groupware.business.calendar.event.EventDO;
import com.ivata.groupware.business.calendar.event.meeting.MeetingDO;
import com.ivata.groupware.business.calendar.event.publicholiday.PublicHolidayDO;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.groupware.util.SettingDateFormatter;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.CollectionHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.format.DateFormatterConstants;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p>This action is  invoked from the main calendar viewpage. This
 * action locates the event and prepares the form for
 * <code>event.jsp</code>.</p>
 *
 * @since 2003-02-02
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class FindEventAction extends MaskAction {
    private Calendar calendar;
    private SettingDateFormatter dateFormatter;
    /**
     * TODO
     *
     * @param calendar
     * @param dateFormatter
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public FindEventAction(Calendar calendar, SettingDateFormatter dateFormatter,
            MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.calendar = calendar;
        this.dateFormatter = dateFormatter;
    }

    /**
     * <p>Invoked when the user clicks on an event in the calendar.</p>
     *
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param userName current user name from session. .
     * @param settings valid, non-null settings from session.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     *
     *
     */
    public String execute(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session) throws SystemException {
        // create a new event form then call reset to make sure the calendar is there
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        EventForm eventForm = (EventForm)
            PicoContainerFactory.getInstance().instantiateOrOverride(
                    securitySession.getContainer(),
                    EventForm.class);

        eventForm.reset(mapping, request);
        String id = request.getParameter("id");

        if (id == null) {
            throw new SystemException("ERROR in FindEventAction: id is null");
        }
        EventDO event = calendar.findEventByPrimaryKey(securitySession, id);
        // ok, this is not very efficient. it kinda grew this way out of
        // hysteric reasons :-)
        // the only alternative I can see is to add a 'type' request parameter
        // and I don't want to do this as it will make all the views more
        // complicated
        if (event instanceof MeetingDO) {
            MeetingDO meeting = (MeetingDO) event;
            Set attendees = meeting.getAttendees();
            if (attendees == null) {
                meeting.setAttendees(attendees = new HashSet());
            }
            List attendeeIds =  new Vector(attendees.size());
            // make strings of Integers
            for (Iterator i = attendees.iterator(); i.hasNext(); ) {
                PersonDO attendee = (PersonDO) i.next();
                attendeeIds.add(attendee.getId().toString());
            }
            eventForm.setAttendees(CollectionHandling.convertToLines(attendeeIds, ';'));
        }
        dateFormatter.setDateTimeText("{0}");
        // TODO: replace this with settings
        dateFormatter.setDateFormat(DateFormatterConstants.DATE_INPUT_DISPLAY);
        dateFormatter.setTimeFormat(DateFormatterConstants.TIME_INPUT_DISPLAY);
        eventForm.setStartDate(dateFormatter.format(event.getStart().getTime()));
        if (event.getFinish() == null) {
            eventForm.setFinishDate("");
        } else {
            eventForm.setFinishDate(dateFormatter.format(event.getFinish().getTime()));
        }
        dateFormatter.setDateTimeText("{1}");
        eventForm.setStartTime(dateFormatter.format(event.getStart().getTime()));
        if (event.getFinish() == null) {
            eventForm.setFinishTime("");
        } else {
            eventForm.setFinishTime(dateFormatter.format(event.getFinish().getTime()));
        }

        // go thro' all the types and set the form up for the right one
        // note if this request parameter is set, it indicates a new event
        // existing events are retrieved in FindEventAction.java
        if (event instanceof MeetingDO) {
            eventForm.setTitleKey("event.title.amend.meeting");
            eventForm.setHelpKey("calendar.meeting.event");
        } else if (event instanceof PublicHolidayDO) {
            eventForm.setTitleKey("event.title.amend.publicHoliday");
            eventForm.setHelpKey("calendar.publicHoliday");
        } else {
            // default is standard event
            eventForm.setHelpKey("calendar.event");
            eventForm.setTitleKey("event.title.amend");
        }
        eventForm.setEvent(event);
        session.setAttribute("calendarEventForm", eventForm);
        return "calendarEvent";
    }
}
