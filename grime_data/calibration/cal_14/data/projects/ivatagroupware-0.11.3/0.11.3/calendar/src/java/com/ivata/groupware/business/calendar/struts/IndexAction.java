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

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.admin.setting.SettingsDataTypeException;
import com.ivata.groupware.business.calendar.Calendar;
import com.ivata.groupware.business.calendar.event.EventDO;
import com.ivata.groupware.util.SettingDateFormatter;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.format.DateFormatterConstants;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p>This action is  invoked from the main calendar index, to create
 * the appropriate view.</p>
 *
 * @since 2003-02-02
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 */
public class IndexAction extends MaskAction {
    private Calendar calendar;
    private SettingDateFormatter dateFormatter;
    private Settings settings;
    /**
     * TODO
     *
     * @param calendar
     * @param settings
     * @param dateFormatter
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public IndexAction(Calendar calendar, Settings settings, SettingDateFormatter
            dateFormatter, MaskFactory maskFactory,
            MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.calendar = calendar;
        this.settings = settings;
        this.dateFormatter = dateFormatter;
    }

    /**
     * <p>Invoked when the user views a calendar list.</p>
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
        IndexForm indexForm = (IndexForm) form;
        TimeZone timeZone;
        timeZone =
            TimeZone.getTimeZone(
                settings.getStringSetting(
                    securitySession,
                    "i18nTimeZone",
                    user));

        GregorianCalendar day = indexForm.getCurrentDay();
        day.setTimeZone(timeZone);

        // see if there is a view from before
        Integer view = indexForm.getView();
        // request overrides the form
        Integer requestView = StringHandling.integerValue(request.getParameter("view"));
        if (requestView != null) {
            indexForm.setView(view = requestView);
        }

        // if this is the first trip to the calendar, or if you asked for it
        // make the current day today
        if ((view == null)
                || (request.getParameter("today") != null)) {
            day.setTime(new java.util.Date());
            day.set(GregorianCalendar.HOUR_OF_DAY, 0);
            day.set(GregorianCalendar.MINUTE, 0);
            day.set(GregorianCalendar.SECOND, 0);
            day.set(GregorianCalendar.MILLISECOND, 0);

            // if there is no view parameter, take the current view from the
            // settings
            if (view == null) {
                try {
                    indexForm.setView(view = settings.getIntegerSetting(securitySession,
                        "calendarDefaultView", user));
                } catch (SettingsDataTypeException e2) {
                    throw new SystemException(e2);
                }
            }
        }


        // if someone clicked to day link in week view
        if (request.getParameter("day") != null) {
            day.set( GregorianCalendar.DATE,
                new Integer(request.getParameter("day")).intValue());
            day.set( GregorianCalendar.MONTH,
                new Integer(request.getParameter("month")).intValue());
            day.set( GregorianCalendar.YEAR,
                new Integer(request.getParameter("year")).intValue());
            day.set(GregorianCalendar.HOUR_OF_DAY, 0);
            day.set(GregorianCalendar.MINUTE, 0);
            day.set(GregorianCalendar.SECOND, 0);
            day.set(GregorianCalendar.MILLISECOND, 0);
        }

        // sort out the help key, include page, window size and events from
        // the view

        // ------- DAY -------
        if (IndexFormConstants.VIEW_DAY.equals(view)) {
            indexForm.setViewPage("/calendar/dayview.jsp");
            indexForm.setHelpKey("calendar.day");

            // look for next, previous day
            if (request.getParameter("next") != null) {
                day.set(GregorianCalendar.DAY_OF_YEAR,
                    day.get(GregorianCalendar.DAY_OF_YEAR) + 1);
            } else if (request.getParameter("previous") != null) {
                day.set(GregorianCalendar.DAY_OF_YEAR,
                    day.get(GregorianCalendar.DAY_OF_YEAR)-1);
            }
            Map eventsDay = new HashMap();
            Map [] events = {eventsDay};
            indexForm.setEvents(events);
            Vector allDayEventsDay = new Vector();
            Vector [] allDayEvents = {allDayEventsDay};
            indexForm.setAllDayEvents(allDayEvents);
            findDayEvents(securitySession, indexForm, day, allDayEventsDay, eventsDay);

        } else {
            // in the settings table, 1=MONDAY  0=SUNDAY  --%>
            int firstDay;
            firstDay =
                (settings
                    .getIntegerSetting(
                        securitySession,
                        "calendarFirstWeekDay",
                        user))
                    .intValue();

            // if this is a 5 day view, monday is always the first
            int numberOfDays = 7;
            if (IndexFormConstants.VIEW_WORK_WEEK.equals(view)) {
                firstDay = 1;
                numberOfDays = 5;
            }

            // now work out the first day of the week to show - note for
            // month/year views it is only the day of the week for this
            // day which is significant (not its actual date)
            GregorianCalendar firstWeekDay = new GregorianCalendar(timeZone);
            GregorianCalendar firstMonthDay = null;

            // if this is month view, go to the start of the month
            if (IndexFormConstants.VIEW_MONTH.equals(view)) {
                // look for next, previous month
                if (request.getParameter("next") != null) {
                    day.set(GregorianCalendar.MONTH,
                        day.get(GregorianCalendar.MONTH) + 1);
                } else if (request.getParameter("previous") != null) {
                    day.set(GregorianCalendar.MONTH,
                        day.get(GregorianCalendar.MONTH) - 1);
                }

                firstMonthDay = new GregorianCalendar(timeZone);
                firstMonthDay.setTime(day.getTime());
                firstMonthDay.set(GregorianCalendar.DAY_OF_MONTH, 1);
                firstWeekDay.setTime(firstMonthDay.getTime());
            } else {
                // look for next, previous week
                if (request.getParameter("next") != null) {
                    day.set(GregorianCalendar.DAY_OF_YEAR,
                        day.get(GregorianCalendar.DAY_OF_YEAR) + 7);
                } else if (request.getParameter("previous") != null) {
                    day.set(GregorianCalendar.DAY_OF_YEAR,
                        day.get(GregorianCalendar.DAY_OF_YEAR)-7);
                }

                // if this is a week view, just go to the current day
                firstWeekDay.setTime(day.getTime());
            }

            firstWeekDay.set(GregorianCalendar.DAY_OF_YEAR,
                    firstWeekDay.get(GregorianCalendar.DAY_OF_YEAR)
                    - ((firstWeekDay.get(GregorianCalendar.DAY_OF_WEEK) + 6 - firstDay) % 7) );
            firstWeekDay.set(GregorianCalendar.HOUR_OF_DAY, 0);
            firstWeekDay.set(GregorianCalendar.MINUTE, 0);
            firstWeekDay.set(GregorianCalendar.SECOND, 0);
            firstWeekDay.set(GregorianCalendar.MILLISECOND, 0);
            indexForm.setFirstWeekDay(firstWeekDay);

            // temporary variable used for counting forward
            GregorianCalendar thisDay = new GregorianCalendar(timeZone);

            // basically now it breaks down into month or week
            // ------- MONTH -------
            if (IndexFormConstants.VIEW_MONTH.equals(view)) {
                // month view doesn't separate all day events from normal
                // events - just go thro' all events each day and add them
                // to a tree set sorted by hour

                // first work out how many weeks there are
                int dayOfWeek = firstWeekDay.get(GregorianCalendar.DAY_OF_WEEK);
                int dayBeforeOfWeek = dayOfWeek - 1;
                if (dayBeforeOfWeek < 0) {
                    dayBeforeOfWeek += 7;
                }
                int twoDaysBeforeOfWeek = dayBeforeOfWeek - 1;
                if (twoDaysBeforeOfWeek < 0) {
                    twoDaysBeforeOfWeek += 7;
                }
                int numberOfWeeks = 0;

                // if the first day is first day of week and 28 days in
                // month then we need 4 weeks
                if ((firstMonthDay.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) == 28)
                        && (firstMonthDay.get(GregorianCalendar.DAY_OF_WEEK) == dayOfWeek)) {
                    numberOfWeeks = 4;
                } else if (((firstMonthDay.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)==31)
                            && (firstMonthDay.get(GregorianCalendar.DAY_OF_WEEK) == twoDaysBeforeOfWeek))
                        || ((firstMonthDay.get(GregorianCalendar.DAY_OF_WEEK) == dayBeforeOfWeek)
                            && (firstMonthDay.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) >=30))) {
                    // e.g. if you have a sunday as the first day of the week,
                    // friday as the first day with 31 days or saturday as
                    // the first day with 30/31 will mean 6 weeks
                    // if you have a monday as the first day of the week,
                    // saturday as the first day with 31 days or sunday as
                    // the first day with 30/31 will mean 6 weeks
                    numberOfWeeks = 6;
                // default: we need 5 weeks
                } else {
                    numberOfWeeks = 5;
                }

                Map [] events = new Map[numberOfWeeks];
                indexForm.setEvents(events);
                // go thro' all the weeks
                int thisMonth = firstMonthDay.get(GregorianCalendar.MONTH);
                thisDay.setTime(firstWeekDay.getTime());
                for (int week = 0; week < numberOfWeeks; ++week) {
                    events[week] = new HashMap();
                    // go thro' all the days
                    for (int dayNumber = 0; dayNumber < 7; ++dayNumber) {
                        // if this day is in the month, set the events
                        // (it might be in the month before or after -
                        // the value for their keys are null to set them
                        // apart)
                        if (thisMonth==thisDay.get(GregorianCalendar.MONTH)) {
                            // set the events for each day
                            EventHourComparator comparator = new EventHourComparator();
                            comparator.setDay(thisDay);
                            TreeSet eventsThisDay = new TreeSet(comparator);
                            eventsThisDay.addAll(calendar.findEventsForDay(securitySession, thisDay));
                            events[week].put(new Integer(dayNumber), eventsThisDay);
                        }
                        thisDay.set(GregorianCalendar.DAY_OF_YEAR,
                          thisDay.get(GregorianCalendar.DAY_OF_YEAR) + 1);
                    }
                }

                indexForm.setViewPage("/calendar/monthview.jsp");
                indexForm.setHelpKey("calendar.month");

            // ------- WEEK -------
            } else {
                // if chosen day is saturday or sunday make 7 days view
                if ((day.get(GregorianCalendar.DAY_OF_WEEK)==GregorianCalendar.SATURDAY)
                        || (day.get(GregorianCalendar.DAY_OF_WEEK)==GregorianCalendar.SUNDAY)) {
                    indexForm.setView(view = IndexFormConstants.VIEW_WEEK);
                }

                // go thro' all of the days and create the maps of tree sets
                // and the vectors
                Map [] events = new Map[numberOfDays];
                indexForm.setEvents(events);
                Vector [] allDayEvents = new Vector[numberOfDays];
                indexForm.setAllDayEvents(allDayEvents);
                thisDay.setTime(firstWeekDay.getTime());
                for (int index = 0; index < numberOfDays; ++index) {
                    events[index] = new HashMap();
                    allDayEvents[index] = new Vector();
                    findDayEvents(securitySession, indexForm, thisDay,
                            allDayEvents[index], events[index]);
                    thisDay.set(GregorianCalendar.DAY_OF_YEAR,
                      thisDay.get(GregorianCalendar.DAY_OF_YEAR) + 1);
                }

                indexForm.setViewPage("/calendar/weekview.jsp");
                indexForm.setHelpKey("calendar.week");
            }

        }

        // this form always returns to the input screen
        // I think this is not necesary because day is not new instance !!
        //indexForm.setCurrentDay(day);
        return null;
    }

    /**
     * <p>This helper method finds the events for the day specified and
     * splits them into all day events, and normal events. Normal events
     * are
     * sorted in a hash map, indexed by the hour of day they either start
     * or
     * (if they start on a day before the current day) end.</p>
     *
     * @param calendar remote interface instance to the server-side
     * calendar facade.
     * @param indexForm the current index form with details to be listed.
     * @param day defines the day to retrieve events for.
     * @param allDayEvents a <code>Vector</code> into which all of  the
     * events marked as all day events are loaded.
     * @param events <code>Map</code> into which normal events
     * are stored indexed by the time they happen.
     * @param userName current user name from session. .
     * @param settings valid, non-null settings from session.
     * @exception SystemException if there is any remote
     * excepotion accessing server-side methods.
     */
    private void findDayEvents(final SecuritySession securitySession,
            final IndexForm indexForm,
            final GregorianCalendar day,
            final Vector allDayEvents,
            final Map events) throws SystemException {
        // this comparator will be used to construct the tree maps
        EventHourComparator comparator = new EventHourComparator();
        comparator.setDay(day);

        Collection eventsForDay;
        eventsForDay = calendar.findEventsForDay(securitySession, day);
        String fromKey, toKey;

        // TODO:
        dateFormatter.setDateFormat(DateFormatterConstants.DATE_LONG_DAY);
        dateFormatter.setTimeFormat(DateFormatterConstants.TIME_SHORT);

        // sort all events into all day events and ordinary ones
        // all day events go into the special vector, ordinary ones
        // go into the map to sort them
        for (Iterator i = eventsForDay.iterator(); i.hasNext(); ) {
            EventDO event = (EventDO) i.next();

            Integer key = null;
            // does the event start before today and finish after today?
            // OR it is marked as an all day event?
            if (event.isAllDayEvent()
                    || (comparator.isAfterDay(event) && (event.getStart().before(day)))) {
                allDayEvents.add(event);

            // if ordinary events start today, it's the start time which is
            // important
            } else if (!event.getStart().before(day)) {
                // the start hour is our key
                key = new Integer(event.getStart().get(GregorianCalendar.HOUR_OF_DAY));

                // set up starting hour of the first event this day
                if (event.getStart().get(GregorianCalendar.HOUR_OF_DAY) < indexForm.getDayStartHour()) {
                    indexForm.setDayStartHour(event.getStart().get(GregorianCalendar.HOUR_OF_DAY));
                }
                // set up starting hour of the last event this day
                if (event.getStart().get(GregorianCalendar.HOUR_OF_DAY) > indexForm.getDayFinishHour()) {
                    indexForm.setDayFinishHour(event.getStart().get(GregorianCalendar.HOUR_OF_DAY));

                }

            // if the event finishes today but started before today, it is the
            // to time which is important
            } else if (!comparator.isAfterDay(event)) {
                // the finish hour is our key
                key = new Integer(event.getFinish().get(GregorianCalendar.HOUR_OF_DAY));

                // set up finishing hour of the last event at the day
                if (event.getFinish().get(GregorianCalendar.HOUR_OF_DAY) > indexForm.getDayFinishHour()) {
                    indexForm.setDayFinishHour(event.getFinish().get(GregorianCalendar.HOUR_OF_DAY));
                }
                if (event.getFinish().get(GregorianCalendar.HOUR_OF_DAY) < indexForm.getDayStartHour()) {
                    indexForm.setDayStartHour(event.getFinish().get(GregorianCalendar.HOUR_OF_DAY));
                }
            }
            // if this was a normal event, set it in the tree set
            if (key != null) {
                // see if there is already a tree set at this hour
                TreeSet treeSet = (TreeSet) events.get(key);
                if (treeSet == null) {
                    // if there is no tree set at this hour, make an empty one
                    events.put(key, treeSet = new TreeSet(comparator));
                }
                treeSet.add(event);
            }
        }
    }
}
