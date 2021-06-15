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

import java.text.ParseException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.calendar.Calendar;
import com.ivata.groupware.business.calendar.event.EventDO;
import com.ivata.groupware.business.calendar.event.meeting.MeetingDO;
import com.ivata.groupware.business.calendar.event.publicholiday.PublicHolidayDO;
import com.ivata.groupware.util.SettingDateFormatter;
import com.ivata.mask.Mask;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationError;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.web.format.DateFormatterConstants;
import com.ivata.mask.web.format.DateFormatterException;
import com.ivata.mask.web.struts.DialogForm;


/**
 * <p>This form is wrapper for <code>EventDO</code>. It is used in
 * <code>event.jsp</code>.</p>
 *
 * @since 2003-01-31
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.5 $
 *
 */
public class EventForm extends DialogForm {
    /**
     * <p>If not <code>null</code> and not empty, specifies a new, empty
     * category should be added.</p>
     */
    private String addCategory = "";
    /**
     * <p>If this event is a meeting, this is a semicolon (;) deliminated
     * string of all attendee identifers.</p>
     */
    private String attendees = "";
    private Calendar calendar;
    private SettingDateFormatter dateFormatter;
    /**
     * <p>Instance of event data object, containing all values
     * of event being submitted.</p>
     */
    private EventDO event;
    /**
     * <p>Stores the number of the active tab page.</p>
     */
    private Integer eventTab_activeTab = new Integer(0);
    /**
     * <p>Specifies just the date part of the end date/time.</p>
     */
    private String finishDate = "";
    /**
     * <p>Specifies just the time part of the end date/time.</p>
     */
    private String finishTime = "";
    /**
     * <p>Specifies just the date part of the start date/time.</p>
     */
    private String startDate = "";
    /**
     * <p>Specifies just the time part of the start date/time.</p>
     */
    private String startTime = "";
    /**
     * <p>Page to currently display in the tab control.</p>
     */
    private String tabPage = "/calendar/eventDetails.jsp";
    /**
     * <p>Key used to access localized title string.</p>
     */
    private String titleKey = "event.title.new";
    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     */
    private Class baseClass;
    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     */
    private Mask mask;
    /**
     * @param maskParam Refer to {@link DialogForm#DialogForm}.
     * @param baseClassParam Refer to {@link DialogForm#DialogForm}.
     * @param settings ivata settings implementation - used in validation.
     */
    public EventForm(final Calendar calendar,
            final SettingDateFormatter dateFormatter,
            MaskFactory maskFactory) {
        this.calendar = calendar;
        this.dateFormatter = dateFormatter;
        mask = maskFactory.getMask(EventDO.class);
    }

    /**
     * <p>
     * Return all form state to initial values.
     * </p>
     *
     * @see com.ivata.mask.web.struts.MaskForm#clear()
     */
    protected void clear() throws OperationNotSupportedException {
        super.clear();
        addCategory = "";
        attendees = "";
        event = new EventDO();
        eventTab_activeTab = new Integer(0);
        finishDate = "";
        finishTime = "";
        startDate = "";
        startTime = "";
        tabPage = "/calendar/eventDetails.jsp";
        titleKey = "event.title.new";
    }

    /**
     * <p>If not <code>null</code> and not empty, specifies a new, empty
     * category should be added.</p>
     *
     * @return the current value of addCategory.
     */
    public final String getAddCategory() {
        return addCategory;
    }

    /**
     * <p>If this event is a meeting, this is a semicolon (;) deliminated
     * string of all attendee identifers.</p>
     *
     * @return the current value of attendees.
     */
    public final String getAttendees() {
        return attendees;
    }

    /**
     * <p>Get the values of the event this form refers to as a dependent
     * value object.</p>
     *
     * @return event values this form refers to.
     *
     *
     */
    public final EventDO getEvent() {
        return event;
    }

    /**
     * <p>Stores the number of the active tab page.</p>
     *
     * @return the current value of eventTab_activeTab.
     */
    public final Integer getEventTab_activeTab() {
        return eventTab_activeTab;
    }

    /**
     * <p>Specifies just the date part of the end date/time.</p>
     *
     * @return the current value of finishDate.
     */
    public final String getFinishDate() {
        return finishDate;
    }

    /**
     * <p>Specifies just the time part of the end date/time.</p>
     *
     * @return the current value of finishTime.
     */
    public final String getFinishTime() {
        return finishTime;
    }

    /**
     * <p>Specifies just the date part of the start date/time.</p>
     *
     * @return the current value of startDate.
     */
    public final String getStartDate() {
        return startDate;
    }

    /**
     * <p>Specifies just the time part of the start date/time.</p>
     *
     * @return the current value of startTime.
     */
    public final String getStartTime() {
        return startTime;
    }

    /**
     * <p>Page to currently display in the tab control.</p>
     *
     * @return the current value of tabPage.
     */
    public final String getTabPage() {
        return tabPage;
    }

    /**
     * <p>Key used to access localized title string.</p>
     *
     * @return the current value of titleKey.
     */
    public final String getTitleKey() {
        return titleKey;
    }

    /**
     * <p>Find out if this is a meeting.</p>
     *
     * @return <code>true</code> if it is a meeting, otherwise
     * <code>false</code>.
     */
    public boolean isMeeting() {
        return event instanceof MeetingDO;
    }

    /**
     * <p>Find out if this is an event rather than a public holiday.</p>
     *
     * @return <code>true</code> if it is an event, otherwise
     * <code>false</code> if it is a public holiday or meeting.
     */
    public boolean isPlainEvent() {
        return !((event instanceof MeetingDO) || (event instanceof PublicHolidayDO));
    }

    /**
     * <p>Find out if this is a public holiday.</p>
     *
     * @return <code>true</code> if it is a public holiday, otherwise
     * <code>false</code>.
     */
    public boolean isPublicHoliday() {
        return event instanceof PublicHolidayDO;
    }


    /**
     * <p>Reset all bean properties to their default state.  This method
     * is called before the properties are repopulated by the controller
     * servlet.</p>
     *
     * @param calendar valid calendar remote instance.
     * @param request The servlet request we are processing.
     * @param session The servlet session we are processing.
     *
     *
     */
    public void reset(final ActionMapping mapping,
            final HttpServletRequest request) {
        // if there is no event yet, nothing to reset
        if (event == null) {
            return;
        }

        MeetingDO meeting = null;
        if (event instanceof MeetingDO) {
            meeting = (MeetingDO) event;
        }

        // what we reset depends on the tab page
        // meeting attendees affects the chairperson and the list of attendees
        if (tabPage.equals("/calendar/meetingPeople.jsp")) {
            attendees = "";
            meeting.setChairPerson(null);

        // meeting agenda affects the list of agenda points
        } else if (tabPage.equals("/calendar/meetingAgenda.jsp")) {
            // TODO: clear agenda points and categories here

        // standard event
        } else {
            event.setSubject("");
            startDate = "";
            startTime = "";
            finishDate = "";
            finishTime = "";
            event.setAllDayEvent(false);
            event.setDescription("");
            if (meeting != null) {
                meeting.setLocation("");
            }
        }

    }

    /**
     * <p>If not <code>null</code> and not empty, specifies a new, empty
     * category should be added.</p>
     *
     * @param addCategory the new value of addCategory.
     */
    public final void setAddCategory(final String addCategory) {
        this.addCategory = addCategory;
    }

    /**
     * <p>If this event is a meeting, this is a semicolon (;) deliminated
     * string of all attendee identifers.</p>
     *
     * @param attendees the new value of attendees.
     */
    public final void setAttendees(final String attendees) {
        this.attendees = attendees;
    }

    /**
     * <p>Private helper method to parse the date and time from strings,
     * and set the results in a <code>GregorianCalendar</code>.</p>
     *
     * @param dateFormatter used to parse the strings
     * @param dateParam string representation of the calendar date (without
     * time).
     * @param timeParam string representation of the time of day.
     * @param calendar target for results (if there are no errors).
     * @return <code>ValidationError</code> instance if there is a
     * problem, otherwise <code>null</code>.
     */
    private ValidationError setDateTime(
            final SettingDateFormatter dateFormatter,
            final String dateParam,
            final String timeParam,
            final GregorianCalendar calendar) {
        ValidationError error = null;

        try {
            if (!StringHandling.isNullOrEmpty(dateParam)) {
                String date = StringHandling.getNotNull(dateParam, "");
                String time = StringHandling.getNotNull(timeParam, "");
                try {
                    calendar.setTime(dateFormatter.parse(date
                            + " "
                            + time));
                } catch (ParseException e) {
                    // find out which went wrong - date or time
                    try {
                        dateFormatter.setDateTimeText("{0}");
                        dateFormatter.parse(dateParam);
                        String[] parameters = {time};

                        error = new ValidationError(
                                "event",
                                Calendar.BUNDLE_PATH,
                                mask.getField("time"),
                                "errors.time",
                                Arrays.asList(parameters));
                    } catch (ParseException e2) {
                        String[] parameters = {date};

                        error = new ValidationError(
                                "event",
                                Calendar.BUNDLE_PATH,
                                mask.getField("date"),
                                "errors.date",
                                Arrays.asList(parameters));
                    }
                }
            }
        } catch (DateFormatterException e) {
            throw new RuntimeException(e);
        }
        return error;
    }

    /**
     * <p>Set the values of the event this form refers to as a dependent
     * value object.</p>
     *
     * @param event new event values this form refers to.
     *
     *
     */
    public final void setEvent(final EventDO event) {
        this.event = event;
    }

    /**
     * <p>Stores the number of the active tab page.</p>
     *
     * @param eventTab_activeTab the new value of eventTab_activeTab.
     */
    public final void setEventTab_activeTab(final Integer eventTab_activeTab) {
        this.eventTab_activeTab = eventTab_activeTab;
    }

    /**
     * <p>Specifies just the date part of the end date/time.</p>
     *
     * @param finishDate the new value of finishDate.
     */
    public final void setFinishDate(final String finishDate) {
        this.finishDate = finishDate;
    }

    /**
     * <p>Specifies just the time part of the end date/time.</p>
     *
     * @param finishTime the new value of finishTime.
     */
    public final void setFinishTime(final String finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * <p>Specifies just the date part of the start date/time.</p>
     *
     * @param startDate the new value of startDate.
     */
    public final void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    /**
     * <p>Specifies just the time part of the start date/time.</p>
     *
     * @param startTime the new value of startTime.
     */
    public final void setStartTime(final String startTime) {
        this.startTime = startTime;
    }

    /**
     * <p>Page to currently display in the tab control.</p>
     *
     * @param tabPage the new value of tabPage.
     */
    public final void setTabPage(final String tabPage) {
        this.tabPage = tabPage;
    }

    /**
     * <p>Key used to access localized title string.</p>
     *
     * @param titleKey the new value of titleKey.
     */
    public final void setTitleKey(final String titleKey) {
        this.titleKey = titleKey;
    }

    /**
     * <p>Validates the properties that have been set for this HTTP
     * request, and return an <code>ActionMessages</code> object that
     * encapsulates any validation errors that have been found.  If no
     * errors are found,</p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     * @return <code>null</code> or an <code>ActionMessages</code> object
     * with no recorded error messages.
     *
     *
     */
    public ValidationErrors validate(final HttpServletRequest request,
            final HttpSession session) {
        ValidationError startDateError = null;
        ValidationError finishDateError = null;
        SecuritySession securitySession = (SecuritySession)
            session.getAttribute("securitySession");
        Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);

        try {
            dateFormatter.setDateFormat(DateFormatterConstants.DATE_INPUT);
            dateFormatter.setTimeFormat(DateFormatterConstants.TIME_INPUT);
        } catch (DateFormatterException e) {
            throw new RuntimeException(e);
        }

        GregorianCalendar start = new GregorianCalendar();

        if (StringHandling.isNullOrEmpty(startDate)) {
           event.setStart(null);
        } else {
            if (StringHandling.isNullOrEmpty(startTime)) {
                try {
                    dateFormatter.setDateTimeText("{0}");
                } catch (DateFormatterException e1) {
                    throw new RuntimeException(e1);
                }
            } else {
                try {
                    dateFormatter.setDateTimeText("{0} {1}");
                } catch (DateFormatterException e1) {
                    throw new RuntimeException(e1);
                }
            }
            startDateError = setDateTime(dateFormatter, startDate, startTime, start);
            if (startDateError == null) {
                event.setStart(start);
                // when start time was not set, mark it as an all day event
                if (StringHandling.isNullOrEmpty(startTime)) {
                    event.setAllDayEvent(true);
                }
            }
        }

        GregorianCalendar finish = new GregorianCalendar();
        if (StringHandling.isNullOrEmpty(finishDate)) {
           event.setFinish(null);
        } else {
            if (StringHandling.isNullOrEmpty(finishTime)) {
                try {
                    dateFormatter.setDateTimeText("{0}");
                } catch (DateFormatterException e1) {
                    throw new RuntimeException(e1);
                }
            } else {
                try {
                    dateFormatter.setDateTimeText("{0} {1}");
                } catch (DateFormatterException e1) {
                    throw new RuntimeException(e1);
                }
            }
            finishDateError = setDateTime(dateFormatter, finishDate, finishTime, finish);
            // if the finish is before start, that has too mean the finish date
            // was the same as start date, but the time was unspecified
            if (finishDateError == null && finish.after(start)) {
                if (StringHandling.isNullOrEmpty(finishTime)) {
                  finish.add(GregorianCalendar.MINUTE, 24*60 - 1);
                }
                event.setFinish(finish);
            }
        }

        // now perform server-side validation
        ValidationErrors validationErrors = null;
        try {
            validationErrors = calendar.validate(securitySession, event);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }

        // if validation errors is null and we have a start date or an end date
        // error, create a new colllection object so we can add that to the errors
        if (((startDateError != null)
                    || (finishDateError != null))
                && (validationErrors == null)) {
            validationErrors = new ValidationErrors();
        }
        if (startDateError != null) {
            validationErrors.add(startDateError);
        }
        if (finishDateError != null) {
            validationErrors.add(finishDateError);
        }
        return validationErrors;
    }

    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     *
     * @return base class of all objects in the value object list.
     */
    public final Class getBaseClass() {
        return baseClass;
    }

    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     *
     * @return mask containing all the field definitions for this list.
     */
    public final Mask getMask() {
        return mask;
    }
}
