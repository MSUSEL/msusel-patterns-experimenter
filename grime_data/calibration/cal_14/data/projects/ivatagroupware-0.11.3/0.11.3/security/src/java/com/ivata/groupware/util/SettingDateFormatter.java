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
package com.ivata.groupware.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.admin.setting.SettingsInitializationException;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.format.DateFormatter;
import com.ivata.mask.web.format.DateFormatterConstants;
import com.ivata.mask.web.format.DateFormatterException;


/**
 * <p>Handles conversion of dates to/from strings in a unified way
 * throughout the intranet system.</p>
 *
 * @since   2002-06-22
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2.2.1 $
 * TODO: the <code>DATE_FORMAT_RELATIVE</code> handling could be
 * extended to
 * include future dates
 */
public class SettingDateFormatter implements Serializable, DateFormatter {
    /**
     * Refer to {@link Logger}.
     */
    private static Logger log = Logger.getLogger(SettingDateFormatter.class);
    /**
     * <p>Set this to one of the
     * <code>DateFormatterConstants.DATE_FORMAT_.</code> constants to use
     * that date format.</p>
     */
    private int dateFormat = DateFormatterConstants.DATE_LONG;
    /**
     * <p>This is the pattern string which is passed to the
     * <code>SimpleDateFormat</code> which is used internally. Used to
     * display error texts.</p>
     */
    String dateFormatString = null;
    /**
     * <p>Stores how the date and time are used in the output. Change this
     * text to
     * restrict the output to just date or just time, or to change the
     * text between
     * them.</p>
     *
     * <p>The format used is the same as the format for
     * <code>java.text.MessageFormat</code> and the string
     * <code>{0}</code> will be
     * replaced with the date format chosen, <code>{1}</code> is replaced
     * with the
     * time format chosen.</p>
     */
    private String dateTimeText = "{0}, ''at'' {1}";
    /**
     * <p>This member is used to do the actual date conversions.</p>
     */
    private SimpleDateFormat format;
    /**
     * <p>Used to form the string which connects the date format with the
     * time
     * format.</p>
     */
    MessageFormat messageFormat;
    /**
     * <p>This is the pattern string which is passed to the
     * <code>SimpleDateFormat</code> which is used internally. Used to
     * display error texts.</p>
     */
    String pattern = null;

    /**
     * <p>
     * Each date formatter can only apply to one security session. This is used
     * to retrieve settings for the current user.
     * </p>
     */
    SecuritySession securitySession;
    /**
     * <p>Used to access the date formats from the system settings.</p>
     */
    private Settings settings;
    /**
     * <p>Set this to one of the
     * <code>DateFormatterConstants.TIME_FORMAT_.</code> to use that time
     * format.</p>
     */
    private int timeFormat = DateFormatterConstants.TIME_SHORT;
    /**
     * <p>This is the pattern string which is passed to the
     * <code>SimpleDateFormat</code> which is used internally. Used to
     * display error texts.</p>
     */
    String timeFormatString = null;
    /**
     * <p>holds the time zone used in this formatter to return dates
     * specific to user;s geographic location</p>
     */
    private TimeZone timeZone;

    /**
     * <p>Date format settings (as with all other settings in
     * ivata groupware) can be
     * made specific to each user. This user name identifies the user
     * whose
     * individual preferences will be applied </p>
     *
     * <p>Leave this setting <code>null</code> (the default) to use the
     * standard,
     * system-wide settings.</p>
     */
    private String userName = null;

    /**
     * Construct a date formatter.
     *
     * @param securitySession used to retrieve settings for the current user.
     * @param settings used to access the date formats from the system settings.
     */
    public SettingDateFormatter(SecuritySession securitySession,
            Settings settings) {
        this.securitySession = securitySession;
        this.settings = settings;
    }

    /**
     * <p>Used by the class internally to initialize the
     * <code>SimpleDateFormat</code> instance used when the format
     * changes.</p>
     *
     * @param date for relative date formats, the returned string is
     * dependent upon
     * the date being formatted. In these cases, this date is used to
     * create a
     * relative format.
     * @throws SettingsInitializationException if the settings for this
     * formatter
     * have not yet been set.
     * @throws DateFormatterException if the settings for this date format
     *
     * are not set, or not set correctly
     */
    private void createFormat(Date date) throws SettingsInitializationException, DateFormatterException {
        if (securitySession == null) {
            String message = "createFormat - securitySession is null";
            log.error(message);
            throw new NullPointerException(message);
        }
        // prerequisites: check that the settings are not null
        if (settings == null) {
            throw new SettingsInitializationException("ERROR in SettingDateFormatter: null settings: you must first call setSettings.");
        }
        // prerequisites: check that the userName is not null
        // TODO:  This could be a warning or a new exception, as there are cases when setting userName to null is OK
//        if (userName == null) {
//            throw new SettingsInitializationException("ERROR in SettingDateFormatter: null userName: you must first call setUserName.");
//        }
        try {
            if (messageFormat == null) {
                messageFormat = new MessageFormat(dateTimeText);
            }
            // now work out the format to use
            // if the date is relative, there is nothing we can do here without a
            // date object
            if (((dateFormat == DateFormatterConstants.DATE_RELATIVE) ||
                    (dateFormat == DateFormatterConstants.DATE_SHORT_RELATIVE)) &&
                (date == null)) {
                format = null;
                return;
            }
            // other date formats are constant
            String formatString="", dateFormatSetting="", timeFormatSetting="";

            switch (dateFormat) {
                case DateFormatterConstants.DATE_LONG:
                    dateFormatSetting = "i18nDateLong";
                    break;

                case DateFormatterConstants.DATE_LONG_YEAR:
                    dateFormatSetting = "i18nDateLongYear";
                    break;

                case DateFormatterConstants.DATE_LONG_DAY:
                    dateFormatSetting = "i18nDateLongDay";
                    break;

                case DateFormatterConstants.DATE_SHORT:
                    dateFormatSetting = "i18nDateShort";
                    break;

                case DateFormatterConstants.DATE_SHORT_YEAR:
                    dateFormatSetting = "i18nDateShortYear";
                    break;

                case DateFormatterConstants.DATE_INPUT:
                    dateFormatSetting = "i18nDateInput";
                    break;

                case DateFormatterConstants.DATE_INPUT_DISPLAY:
                    dateFormatSetting = "i18nDateInputDisplay";
                    break;

                case DateFormatterConstants.DATE_SHORT_RELATIVE:
                case DateFormatterConstants.DATE_RELATIVE:
                    GregorianCalendar now = new GregorianCalendar();

                    now.setTime(new Date());
                    GregorianCalendar yesterday = new GregorianCalendar();

                    yesterday.setTime(new Date());
                    yesterday.roll(GregorianCalendar.DATE, false);
                    GregorianCalendar lastWeek = new GregorianCalendar();

                    lastWeek.setTime(new Date());
                    lastWeek.roll(GregorianCalendar.WEEK_OF_YEAR, false);
                    GregorianCalendar compare = new GregorianCalendar();

                    compare.setTime(date);
                    // is it today?
                    if ((now.get(GregorianCalendar.YEAR) == compare.get(GregorianCalendar.YEAR)) &&
                        (now.get(GregorianCalendar.DAY_OF_YEAR) == compare.get(GregorianCalendar.DAY_OF_YEAR))) {
                        if (dateFormat == DateFormatterConstants.DATE_SHORT_RELATIVE) {
                            dateFormatSetting = "i18nDateShortToday";
                        } else {
                            // it is today: now we need to compare the time
                            int hour = compare.get(GregorianCalendar.HOUR_OF_DAY);

                            if (hour < 12) {
                                dateFormatSetting = "i18nDateThisMorning";
                            } else if (hour < 18) {
                                dateFormatSetting = "i18nDateThisAfternoon";
                            } else {
                                dateFormatSetting = "i18nDateThisEvening";
                            }
                        }
                    } else if ((now.get(GregorianCalendar.YEAR) == compare.get(GregorianCalendar.YEAR)) &&
                        (yesterday.get(GregorianCalendar.DAY_OF_YEAR) == compare.get(GregorianCalendar.DAY_OF_YEAR))) {
                        if (dateFormat == DateFormatterConstants.DATE_SHORT_RELATIVE) {
                            dateFormatSetting = "i18nDateShortYesterday";
                        } else {
                            // it is yesterday: now we need to compare the time
                            int hour = compare.get(GregorianCalendar.HOUR_OF_DAY);

                            if (hour < 12) {
                                dateFormatSetting = "i18nDateYesterdayMorning";
                            } else if (hour < 18) {
                                dateFormatSetting = "i18nDateYesterdayAfternoon";
                            } else {
                                dateFormatSetting = "i18nDateYesterdayEvening";
                            }
                        }
                    } else if ((now.get(GregorianCalendar.YEAR) == compare.get(GregorianCalendar.YEAR)) &&
                        (now.get(GregorianCalendar.WEEK_OF_YEAR) == compare.get(GregorianCalendar.WEEK_OF_YEAR))) {
                        // so it this week
                        dateFormatSetting = "i18nDateWeekDay";
                    } else if (now.get(GregorianCalendar.YEAR) == compare.get(GregorianCalendar.YEAR)) {
                        // it is this year
                        if (dateFormat == DateFormatterConstants.DATE_SHORT_RELATIVE) {
                            dateFormatSetting = "i18nDateShort";
                        } else {
                            dateFormatSetting = "i18nDateLong";
                        }
                    } else {
                        // everything else is long year format
                        if (dateFormat == DateFormatterConstants.DATE_SHORT_RELATIVE) {
                            dateFormatSetting = "i18nDateShortYear";
                        } else {
                            dateFormatSetting = "i18nDateLongYear";
                        }
                    }
                    break;
            }
            dateFormatString = settings.getStringSetting(securitySession, dateFormatSetting,
                securitySession.getUser());

            // times are simpler
            switch (timeFormat) {
                case DateFormatterConstants.TIME_LONG:
                    timeFormatSetting = "i18nTimeLong";
                    break;

                case DateFormatterConstants.TIME_SHORT:
                    timeFormatSetting = "i18nTimeShort";
                    break;

                case DateFormatterConstants.TIME_INPUT:
                    timeFormatSetting = "i18nTimeInput";
                    break;

                case DateFormatterConstants.TIME_INPUT_DISPLAY:
                    timeFormatSetting = "i18nTimeInputDisplay";
                    break;

            }
            timeFormatString = settings.getStringSetting(securitySession,
                timeFormatSetting, securitySession.getUser());

            // now just format the dateTimeText and use the result to create the
            // simple date format
            Object arguments[] = {
                    dateFormatString,
                    timeFormatString
                };
            Locale locale =
                new Locale(settings.getStringSetting(securitySession,
                    "i18nLocaleLanguage", securitySession.getUser()),
                        settings.getStringSetting(securitySession,
                            "i18nLocaleCountry", securitySession.getUser()));

            pattern = MessageFormat.format(dateTimeText, arguments);
            format = new SimpleDateFormat(pattern, locale);
            format.setLenient(false);
            format.setTimeZone(getTimeZone());
        } catch (com.ivata.mask.util.SystemException e) {
            throw new DateFormatterException("Error in SettingDateFormatter: RemoteException " + e.getMessage());
        }
    }

    /**
     * <p>Format the date provided as a string, using the date format from
     * the settings.</p>
     *
     * @param date the date to convert into a string.
     * @return date string, converted to a string, using the requested
     * format.
     * @throws SettingsInitializationException if the settings for this
     * formatter
     * have not yet been set.
     * @throws DateFormatterException if there is a problem creating the
     * string
     * because of an incorrect format pattern, for example.
     */
    public String format(final Date date) throws DateFormatterException {
        if ((format == null) ||
            (dateFormat == DateFormatterConstants.DATE_RELATIVE) ||
            (dateFormat == DateFormatterConstants.DATE_SHORT_RELATIVE)) {
            try {
                createFormat(date);
            } catch (SettingsInitializationException e1) {
                throw new DateFormatterException(e1);
            }
        }
        String returnString = "";

        try {
            returnString = format.format(date);
        } catch (Exception e) {
            throw new DateFormatterException("ERROR in SettingDateFormatter: format threw "
                    + e.getClass()
                    + " formatting '"
                    + date
                    + "', using date format '"
                    + dateFormatString
                    + "', time format '"
                    + timeFormatString
                    + "': "
                    + e.getMessage());
        }
        return returnString;
    }

    /**
     * <p>Get the number of the date format used in this object. This
     * should
     * correspond to one of the <code>DATE_FORMAT_...</code>
     * constants.</p>
     *
     * @return the current value of the date format used.
     */
    public int getDateFormat() {
        return dateFormat;
    }

    /**
     * <p>Get how the date and time are used in the output.</p>
     *
     * <p>The format used is the same as the format for
     * <code>java.text.MessageFormat</code> and the string
     * <code>{0}</code> will be
     * replaced with the date format chosen, <code>{1}</code> is replaced
     * with the
     * time format chosen.</p>
     *
     * @return the current text used to combine date and time formats.
     */
    public String getDateTimeText() {
        return dateTimeText;
    }

    /**
     * <p>Get the number of the time format used in this object. This
     * should
     * correspond to one of the <code>TIME_FORMAT_...</code>
     * constants.</p>
     *
     * @return the current value of the time format used.
     */
    public int getTimeFormat() {
        return timeFormat;
    }

    /**
     * <p>returns the private variable timeZone,  when not set yet,
     * fetches it from settings first</p>
     *
     * @return the timeZone
     * @exception DateFormatterException if there is a remote exception
     * accessing the settings, or the settings is of an unexpected data
     * type.
     */
    private TimeZone getTimeZone() throws DateFormatterException {
        assert (securitySession != null);
        if (this.timeZone == null) {
            try {
                String timeZoneID = settings.getStringSetting(securitySession,
                    "i18nTimeZone", securitySession.getUser());

                timeZone = TimeZone.getTimeZone(timeZoneID);
            } catch(SystemException e) {
                throw new DateFormatterException("Error in SettingDateFormatter getting the timeZone: RemoteException " + e.getMessage());
            }
        }
        return timeZone;
    }

    /**
     * <p>Get the user name used for date/time system settings.</p>
     *
     * <p>Date format settings (as with all other settings in
     * ivata groupware) can be
     * made specific to each user. This user name identifies the user
     * whose
     * indifvidual preferences will be applied </p>
     *
     * @return the current value of the user name used for settings.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * <p>Parse the given string to a date, using the current date
     * format.</p>
     *
     * @param formatString the string to convert into a date.
     * @return date parsed from the string provided.
     * @throws SettingsInitializationException if the settings for this
     * formatter
     * have not yet been set.
     * @throws ParseException if the string provided does not match the
     * date/time
     * format
     * @throws DateFormatterException if the settings for this date format
     *
     * are not set, or not set correctly
     */
    public Date parse(final String formatString) throws ParseException,
                DateFormatterException {
        if ((dateFormat == DateFormatterConstants.DATE_RELATIVE) ||
            (dateFormat == DateFormatterConstants.DATE_SHORT_RELATIVE)) {
            // TODO: go through all of the possible date formats and try to parse
            // each one
            return null;
        }
        if (format == null) {
            try {
                createFormat(null);
            } catch (SettingsInitializationException e1) {
                throw new DateFormatterException(e1);
            }
        }
        return format.parse(formatString);
    }

    /**
     * <p>Set the number of the date format used in this object. This
     * should
     * correspond to one of the <code>DATE_FORMAT_...</code>
     * constants.</p>
     *
     * @param dateFormat the new value of the date format used.
     * @throws DateFormatterException if the settings for this date format
     * are not set, or not set correctly
     */
    public void setDateFormat(final int dateFormat)
            throws DateFormatterException {
        this.dateFormat = dateFormat;
        try {
            // date format has changed, create the format
            createFormat(null);
        } catch (SettingsInitializationException e) {
            throw new DateFormatterException(e);
        }
    }

    /**
     * <p>Set  this text to restrict the output to just date or just time,
     * or to
     * change the text between
     * them.</p>
     *
     * <p>The format used is the same as the format for
     * <code>java.text.MessageFormat</code> and the string
     * <code>{0}</code> will be
     * replaced with the date format chosen, <code>{1}</code> is replaced
     * with the
     * time format chosen.</p>
     *
     * @param dateTimeText the new value of the text used to combine date
     * and time
     * formats.
     * @throws DateFormatterException if the settings for this date format
     * are not set, or not set correctly
     */
    public void setDateTimeText(final String dateTimeText)
            throws DateFormatterException {
        this.dateTimeText = dateTimeText;
        // this affects the message format: force a new one
        messageFormat = null;
        try {
            createFormat(null);
        } catch (SettingsInitializationException e) {
            throw new DateFormatterException(e);
        }
    }
    /**
     * This is needed by the mail implementation, which does not have a security
     * session at creation time.
     * @param securitySessionParam The securitySession to set.
     */
    public void setSecuritySession(SecuritySession securitySessionParam) {
        if (log.isDebugEnabled()) {
            log.debug(toString() + " setSecuritySession before: '"
                    + securitySession + "', after: '" + securitySessionParam
                    + "'");
        }

        securitySession = securitySessionParam;
    }

    /**
     * <p>Set the number of the time format used in this object. This
     * should
     * correspond to one of the <code>TIME_FORMAT_...</code>
     * constants.</p>
     *
     * @param timeFormat the new value of the time format used.
     * @throws SettingsInitializationException if the settings for this
     * formatter
     * have not yet been set.
     * @throws DateFormatterException if the settings for this date format
     *
     * are not set, or not set correctly
     */
    public void setTimeFormat(final int timeFormat) throws DateFormatterException {
        this.timeFormat = timeFormat;
        try {
            // time format has changed, create the format
            createFormat(null);
        } catch (SettingsInitializationException e) {
            throw new DateFormatterException(e);
        }
    }

    /**
     * <p>Set the user name used for date/time system settings.</p>
     *
     * <p>Date format settings (as with all other settings in
     * ivata groupware) can be
     * made specific to each user. This user name identifies the user
     * whose
     * individual preferences will be applied </p>
     *
     * <p>Leave this setting <code>null</code> (the default) to use the
     * standard,
     * system-wide settings.</p>
     *
     * @param userName the new value of the user name used for system
     * settings.
     * @throws SettingsInitializationException if the settings for this
     * formatter
     * have not yet been set.
     * @throws DateFormatterException if the settings for this date format
     *
     * are not set, or not set correctly
     */
    public void setUserName(String userName) throws SettingsInitializationException, DateFormatterException {
        this.userName = userName;
        // user has changed, create the format
        createFormat(null);
    }
}
