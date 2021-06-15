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

import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.calendar.Calendar;
import com.ivata.groupware.business.calendar.event.EventDO;
import com.ivata.groupware.business.calendar.event.meeting.MeetingDO;
import com.ivata.groupware.business.calendar.event.meeting.category.MeetingCategoryDO;
import com.ivata.groupware.business.calendar.event.publicholiday.PublicHolidayDO;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.CollectionHandling;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p>Invoked when the user edits, displays or enters a new event or
 * public holiday.</p>
 *
 * @since 2003-02-02
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.6 $
 */
public class EventAction extends MaskAction {
    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(EventAction.class);

    /**
     * <p>Private helper method to remove empty categories from the
     * meeting. Empty in this case means the name/heading is empty and
     * there
     * are neither minute texts nor agenda points.</p>
     *
     * <p>If there <em>are</em> agenda points or minute texts but the
     * name/heading is empty, it is replaced with a default localized text
     * from the application resources.</p>
     *
     * <p>Note this method is static as it is also used from the library.</p>
     *
     * @param meeting the meeting to remove categories from.
     * @param messages used to calculate default localized category
     * name.
     * @param locale used to calculate default localized category
     * name.
     */
    public static void removeEmptyMeetingCategories(MeetingDO meeting, MessageResources messages, Locale locale) {
        int count = 1;
        Set categories = meeting.getCategories();
        Iterator categoryIterator = categories.iterator();
        List toRemove = new Vector();
        while (categoryIterator.hasNext()) {
            MeetingCategoryDO category = (MeetingCategoryDO) categoryIterator.next();

            // note: the default name does not count as empty
            String defaultName = messages.getMessage(locale, "default.category", new Integer(count++));

            if (StringHandling.isNullOrEmpty(category.getName())) {
                // see if there are points or minutes
                Set agendaPoints = category.getAgendaPoints();
                if ((agendaPoints == null)
                        || (agendaPoints.size() == 0)) {
                    toRemove.add(category);
                } else {
                    // default the text to heading + number
                    category.setName(defaultName);
                }
            }
        }
        // now go thro' and remove all the ones which are empty
        categoryIterator = toRemove.iterator();
        while(categoryIterator.hasNext()) {
            categories.remove(categoryIterator.next());
        }
    }
    private AddressBook addressBook;
    private Calendar calendar;
    /**
     * TODO
     *
     * @param calendar
     * @param addressBook
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public EventAction(Calendar calendar, AddressBook addressBook,
            MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.calendar = calendar;
        this.addressBook = addressBook;
    }

    /**
     * <p>Called when the clear button is pressed, or after an ok or
     * delete button, where the session should be restored to its default
     * state.</p>
     *
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param userName valid, non-null user name from session.
     * @param settings valid, non-null settings from session.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     *
     */
    public void clear(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session) throws SystemException {
        // and remove the current tab from the session
        session.removeAttribute("eventTab_activeTab");
        EventForm eventForm = (EventForm)form;
        try {
            eventForm.clear();
        } catch (OperationNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Overridden to see which type of event to create when creating a
     * new event.</p>
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
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        UserDO user = securitySession.getUser();
        PicoContainer container = securitySession.getContainer();
        MessageResources messages = getResources(request, "calendar");
        Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);

        // look for a request parameter identifying the type - that means new
        String type = request.getParameter("type");
        EventForm eventForm = (EventForm) form;
        if (type != null) {
            if ("meeting".equals(type)) {
                MeetingDO meeting = new MeetingDO();
                eventForm.setEvent(meeting);
                eventForm.setTitleKey("event.title.new.meeting");
                eventForm.setHelpKey("calendar.meeting.event");

                // initially make one agenda category
                Set meetingCategories = meeting.getCategories();
                if (meetingCategories == null) {
                    meetingCategories = new HashSet();
                    meeting.setCategories(meetingCategories);
                }
                if (meetingCategories.size() == 0) {
                    MeetingCategoryDO newCategory = new MeetingCategoryDO();
                    newCategory.setMeeting(meeting);
                    newCategory.setName(messages.getMessage(locale, "default.category", "1"));
                    meetingCategories.add(newCategory);
                }
                PersonDO person;
                person = addressBook.findPersonByUserName(securitySession,
                        user.getName());
                meeting.setChairPerson(person);

            } else if ("publicHoliday".equals(type)) {
                eventForm.setEvent(new PublicHolidayDO());
                eventForm.setTitleKey("event.title.new.publicHoliday");
                eventForm.setHelpKey("calendar.publicHoliday");
            } else {
                eventForm.setEvent(new EventDO());
                eventForm.setTitleKey("event.title.new");
                eventForm.setHelpKey("calendar.event");
            }
            return null;
        }

        // --tabs-- currently only meetings have tabs
        // the request overrides the current setting in the form
        Integer tab = eventForm.getEventTab_activeTab();
        Integer requestTab = StringHandling.integerValue(request.getParameter("eventTab_activeTab"));
        if (requestTab != null) {
            eventForm.setEventTab_activeTab(tab = requestTab);
        }
        EventDO event = eventForm.getEvent();

        // now choose the right page to show based on the tab
        if (tab.equals(new Integer(1))) {
            eventForm.setTabPage("/calendar/meetingPeople.jsp");
            eventForm.setHelpKey("calendar.meeting.people");
        } else if (tab.equals(new Integer(2))) {
            eventForm.setTabPage("/calendar/meetingAgenda.jsp");
            eventForm.setHelpKey("calendar.meeting.agenda");
        } else {
            eventForm.setTabPage("/calendar/eventDetails.jsp");
            if (event instanceof PublicHolidayDO) {
                eventForm.setHelpKey("calendar.publicHoliday");
            } else if (event instanceof MeetingDO) {
                eventForm.setHelpKey("calendar.meeting.event");
            } else {
                eventForm.setHelpKey("calendar.event");
            }
        }

        // meeting handling
        if (event instanceof MeetingDO) {
            MeetingDO meeting = (MeetingDO) event;
            // go thro' all the categories and discard empty ones
            removeEmptyMeetingCategories(meeting, messages, locale);
            // check there is a collection for categories
            Set categories = meeting.getCategories();
            if (categories == null) {
                meeting.setCategories(categories = new HashSet());
            }

            // if the add button was pressed, add an empty category
            // if the new arrays are empty, add a solitary one
            int size = categories.size();
            if (!StringHandling.isNullOrEmpty(eventForm.getAddCategory())
                    || (size == 0)) {
                MeetingCategoryDO category = new MeetingCategoryDO();
                category.setName(messages.getMessage(locale, "default.category",
                    new Integer(size + 1)));
                category.setMeeting(meeting);
                categories.add(category);
            }
        }

        return null;
    }

    /**
     * <p>This method is called if the ok or apply buttons are pressed.</p>
     *
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param userName valid, non-null user name from session.
     * @param settings valid, non-null settings from session.
     * @param ok <code>true</code> if the ok button was pressed, otherwise
     * <code>false</code> if the apply button was pressed.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     *
     */
    public String onConfirm(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session,
            final String defaultForward) throws SystemException {
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        UserDO user = securitySession.getUser();
        EventForm eventForm = (EventForm) form;
        EventDO event = eventForm.getEvent();

        // meeting ?
        if (event instanceof MeetingDO) {
            MeetingDO meeting = (MeetingDO) event;

            // set the attendee ids
            List attendeeIdStrings = new Vector(CollectionHandling.convertFromLines(eventForm.getAttendees(), ";"));
            // convert the id strings to Interers
            Set attendees = new HashSet();
            for (Iterator i = attendeeIdStrings.iterator(); i.hasNext(); ) {
                String attendeeId = (String) i.next();
                PersonDO attendee = addressBook.findPersonByPrimaryKey(
                        securitySession, attendeeId);
                attendees.add(attendee);
            }
            meeting.setAttendees(attendees);

            // go thro' all the categories and discard empty ones
            removeEmptyMeetingCategories(meeting,
                getResources(request, "calendar"), getLocale(request));

            // if there is no chair person default it to this user's person
            PersonDO chairPerson = meeting.getChairPerson();
            if (chairPerson == null) {
                chairPerson = addressBook.findPersonByUserName(securitySession,
                        user.getName());
            }
        }
        // if it doesn't have an id, it is a new event
        if (event.getId() == null) {
            event = calendar.addEvent(securitySession, event);
        } else {
            // amend an existing event
            event = calendar.amendEvent(securitySession, event);
        }
        eventForm.setEvent(event);

        request.setAttribute("openerPage", "/calendar/index.action");

        return defaultForward;
    }

    /**
     * <p>This method is called if the delete (confirm, not warn) button
     * is pressed.</p>
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param log valid logging object to write messages to.
     * @param userName valid, non-null user name from session.
     * @param settings valid, non-null settings from session.
     *
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     *
     */
    public String onDelete(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session, final String defaultForward) throws SystemException {
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        EventForm eventForm = (EventForm) form;
        EventDO event = eventForm.getEvent();
        calendar.removeEvent(securitySession, event);
        request.setAttribute("openerPage", "/calendar/index.action");
        return defaultForward;
    }
}
