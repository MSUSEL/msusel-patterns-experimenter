<%--

    The MIT License (MIT)

    MSUSEL Arc Framework
    Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
    Software Engineering Laboratory and Idaho State University, Informatics and
    Computer Science, Empirical Software Engineering Laboratory

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

--%>
<%@page contentType="text/plain;charset=UTF-8"%>
<%@page import='java.text.*'%>
<%@page import='java.util.*'%>
<%@page import='org.apache.struts.Globals' %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: date.jsp,v 1.2 2005/04/09 17:19:39 colinmacleod Exp $
//
// Perform date validation. This is anattempt to use the same format as the
// java.text.SimpleDateFormat class, with minor exceptions.
// The format string consists of the following abbreviations:
//
// Field        | Full Form           | Short Form
// -------------+---------------------+-----------------------
// Year         | yyyy (4 digits)     | yy (2 digits), y (2 or 4 digits)
// Month        | MMMM (name)         | MMM (abbreviation) MM (2 digits), M (1 or 2 digits)
// Day of Month | dd (2 digits)       | d (1 or 2 digits)
// Day of Week  | EEEE (name)         | EEE (abbr)
// Hour (1-12)  | hh (2 digits)       | h (1 or 2 digits)
// Hour (0-23)  | HH (2 digits)       | H (1 or 2 digits)
// Hour (0-11)  | KK (2 digits)       | K (1 or 2 digits)
// Hour (1-24)  | kk (2 digits)       | k (1 or 2 digits)
// Minute       | mm (2 digits)       | m (1 or 2 digits)
// Second       | ss (2 digits)       | s (1 or 2 digits)
// AM/PM        | a                   |
//
// Since: ivata groupware 0.9 (2003-02-10)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.2 $
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2001 - 2005 ivata limited.
// All rights reserved.
// =========================================================
// ivata groupware may be redistributed under the GNU General Public
// License as published by the Free Software Foundation;
// version 2 of the License.
//
// These programs are free software; you can redistribute them and/or
// modify them under the terms of the GNU General Public License
// as published by the Free Software Foundation; version 2 of the License.
//
// These programs are distributed in the hope that they will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//
// See the GNU General Public License in the file LICENSE.txt for more
// details.
//
// If you would like a copy of the GNU General Public License write to
//
// Free Software Foundation, Inc.
// 59 Temple Place - Suite 330
// Boston, MA 02111-1307, USA.
//
//
// To arrange commercial support and licensing, contact ivata at
//                  http://www.ivata.com/contact.jsp
//
////////////////////////////////////////////////////////////////////////////////
//
// $Log: date.jsp,v $
// Revision 1.2  2005/04/09 17:19:39  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1  2005/03/10 19:23:44  colinmacleod
// Moved to ivata groupware.
//
// Revision 1.2  2004/03/21 21:16:24  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1  2004/03/03 21:32:34  colinmacleod
// Moved core files to core subproject
//
// Revision 1.1.1.1  2004/01/27 20:58:17  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.1.1.1  2003/10/13 20:50:50  colin
// Restructured portal into subprojects
//
// Revision 1.5  2003/03/05 11:38:59  peter
// fixed formatDate for quoted format substrings
//
// Revision 1.4  2003/03/03 16:57:13  colin
// converted localization to automatic paths
// added labels
// added mandatory fieldName attribute
//
// Revision 1.3  2003/02/21 18:31:31  peter
// tidied up, general methods moved here
//
// Revision 1.2  2003/02/20 14:55:15  peter
// fixed the Locale constructor
//
// Revision 1.1  2003/02/12 12:41:43  colin
// first version in cvs
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%
  SimpleDateFormat nameFormat;
  GregorianCalendar thisCalendar = new GregorianCalendar();
  thisCalendar.set(Calendar.YEAR, 1999);
  thisCalendar.set(Calendar.MONTH, 0);
  thisCalendar.set(Calendar.DAY_OF_MONTH, 1);
  thisCalendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
  thisCalendar.set(GregorianCalendar.MINUTE, 0);
  thisCalendar.set(GregorianCalendar.SECOND, 0);
  thisCalendar.set(GregorianCalendar.MILLISECOND, 0);
  Locale locale = (java.util.Locale)session.getAttribute(Globals.LOCALE_KEY);
  if (locale == null) {
      locale = new Locale("en","US");
  }
%>

<%-- WATCH OUT - the next bit is mixed java/javascript to get the month/day names localized --%>
var MONTH_LONG_NAMES = new Array(<%
    nameFormat = new SimpleDateFormat("MMMMM", locale);
    for (int month = 0; month < 12; ++month) {
        thisCalendar.set(Calendar.MONTH, month);

        // only put out a comma between entries - after the first time
        if (month > 0) {
            out.print(", ");
        }
        out.print("'");
        out.print(nameFormat.format(thisCalendar.getTime()));
        out.print("'");
    }
    %>);
var MONTH_SHORT_NAMES = new Array(<%
    nameFormat = new SimpleDateFormat("MMM", locale);
    for (int month = 0; month < 12; ++month) {
        thisCalendar.set(Calendar.MONTH, month + Calendar.JANUARY);

        // only put out a comma between entries - after the first time
        if (month > 0) {
            out.print(", ");
        }
        out.print("'");
        out.print(nameFormat.format(thisCalendar.getTime()));
        out.print("'");
    }
    %>);
var DAY_LONG_NAMES=new Array(<%
    nameFormat = new SimpleDateFormat("EEEE", locale);
    for (int dayOfWeek = 0; dayOfWeek < 7; ++dayOfWeek) {
        thisCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        // only put out a comma between entries - after the first time
        if (dayOfWeek > 0) {
            out.print(", ");
        }
        out.print("'");
        out.print(nameFormat.format(thisCalendar.getTime()));
        out.print("'");
    }
    %>);
var DAY_SHORT_NAMES=new Array(<%
    nameFormat = new SimpleDateFormat("EEE", locale);
    for (int dayOfWeek = 0; dayOfWeek < 7; ++dayOfWeek) {
        thisCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        // only put out a comma between entries - after the first time
        if (dayOfWeek > 0) {
            out.print(", ");
        }
        out.print("'");
        out.print(nameFormat.format(thisCalendar.getTime()));
        out.print("'");
    }
    %>);

<%--
// <p>Cut off year for 1900-2000 conversion. 2 digit years above this number are
// in 20th century. Values below this number are in 21st Century.</p>
--%>
var y2kCutOff = 30;

<%--
// <p>Add a leading zero. This method takes a 1 or two digit number and turns
// it into a two-character string by possibly adding a leading zero (if the
// number is < 10).</p>
//
// @param number one or two digit numeric value.
// @return two-character formatted string.
--%>
function leadingZero(number) {
    return ((number < 0 || number > 9 ? "" : "0") + number);
}

<%--
// <p>Check to see if a string value matches a valid date.</p>
//
// @param string the string to match.
// @param format the date format to check the string against.
// @return <code>true</code> if the value in <code>string</code> is a valid
//     date of the given format, otherwise <code>false</code>.
--%>
function isValidDate(string, format) {
    var date = getDateFromFormat(string, format);
    return (date != 0);
}

<%--
// <p>Add minutes to the given date string, and return the new date string
// formatted in the given way.</p>
//
// @param minutes number of minutes to add to the date string.
// @param string the string to match.
// @param formatIn the date format for both input and output strings.
// @return date equivalent to the given number of minutes ahead of the
//     input string, or 0 if there is a format error in the input.
--%>
function addMinutes(minutes, string, format) {
    var milliSeconds = minutes * 60000;
    var date = getDateFromFormat(string, format);
    if (date == 0) {
        return 0;
    }
    date.setTime(date.getTime() + milliSeconds);
    return date;
}

<%--
// <p>Compare two date strings to see which comes later.</p>
//
// @param date1 first date string to compare.
// @param format1 the format of <code>dateString1</code>.
// @param date2 second date to compare.
// @param format2 the format of <code>dateString2</code>.
// @return positive number if <code>dateString1</code> comes after <code>dateString2</code>,
//    <code>0</code> if the dates are equivalent, otherwise a negative number.
--%>
function compareDates(dateString1, format1, dateString2, format2) {
    var date1 = getDateFromFormat(dateString1, format1);
    var date2 = getDateFromFormat(dateString2, format2);

    if ((date1 == 0) || (date2 == 0) || (date2 > date1)) {
        return -1;
    } else if (date1 > date2) {
        return 1;
    }
    return 0;
}

<%--
// <p>Format a date object as a string.</p>
//
// @param date valid date object to format as a string.
// @param format the date format to use in converting.
// @return a date in the output format specified.
--%>
function formatDate(date, format) {
    format = format + "";
    var result = "";
    var index = 0;
    var ch = "";
    var token="";
    var year = date.getYear() + "";
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var weekDay = date.getDay();
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();

<%--
    // Convert real date parts into formatted versions
--%>
    var value = new Object();

<%--
    // if the year doesn't contain century, add it in - note 2030 is the break
    // point
--%>
    if (year.length < 4) {
        year= "" + ( year - 0 + (year > y2kCutOff ? 1900 : 2000));
    }

    value["y"] = "" + year;
    value["yyyy"] = year;
    value["yy"] = year.substring(2, 4);
    value["M"] = month;
    value["MM"] = leadingZero(month);
    value["MMM"] = MONTH_SHORT_NAMES[month-1];
    value["MMMM"] = MONTH_LONG_NAMES[month-1];
    value["d"] = day;
    value["dd"] = leadingZero(day);
    value["EEE"] = DAY_SHORT_NAMES[weekDay + 7];
    value["EEEE"]=DAY_LONG_NAMES[weekDay];
    value["H"] = hours;
    value["HH"] = leadingZero(hours);

<%--
    // convert 24h clock to 12h, starting with 1
--%>

    if (hours == 0) {
        value["h"] = 12;
    } else if (hours > 12) {
        value["h"] = hours - 12;
    } else {
        value["h"] = hours;
    }

    value["hh"] = leadingZero(value["h"]);

<%--
    // convert 24h to 12h, starting with 0
--%>
    if (hours > 11) {
        value["K"] = hours-12;
    } else {
        value["K"] = hours;
    }

    value["k"] = hours + 1;

    value["KK"]=leadingZero(value["K"]);
    value["kk"]=leadingZero(value["k"]);

<%--
    // am/pm
--%>
    if (hours > 11) {
        value["a"] = "PM";
    } else {
        value["a"]="AM";
    }

    value["m"] = minutes;
    value["mm"] = leadingZero(minutes);
    value["s"] = seconds;
    value["ss"] = leadingZero(seconds);

    while (index < format.length) {
            ch = format.charAt(index);
            token="";


        // escape quotes
        if (ch == "'") {
            while((index < format.length)
                    && ((ch = format.charAt(++index)) != "'")) {
                result=result + ch;
            }
            index++;
        }


<%--
        // look for like characters and group them as tokens
--%>
        while ((format.charAt(index) == ch) && (index < format.length)) {
            token += format.charAt(index++);
        }

<%--
        // if the token is in our table, translate it, otherwise use it
        // directly
--%>
        if (value[token] != null) {
            result=result + value[token];
        } else {
            result=result + token;
        }
    }

    return result;
}

<%--
// <p>Helper method to check if the given variable contains an integer.</p>
//
// @param value the variable to check.
// @return <code>true</code> if value contains an integer, otherwise <code>false</code>.
--%>
function _isInteger(value) {
    var digits = "1234567890";
    for (var i = 0; i < value.length; ++i) {
<%--
        // if we found any non-digit character, return false
--%>
        if (digits.indexOf(value.charAt(i)) == -1) {
            return false;
        }
    }
    return true;
}

<%--
// <p>Helper method to extract an integer of at least minimum width from a string.</p>
//
// @param string string variable to extract the integer from.
// @param start position to start from.
// @param minLength minimum string length to extract.
// @param maxLength maximum string length to extract.
// @return <code>true</code> if the string contains an integer of the specified
//     minimum string length at the start position, otherwise <code>false</code>.
--%>
function _getInteger(string, start, minLength, maxLength) {
    for (var x=maxLength; x >= minLength; --x) {
        var token = string.substring(start, start + x);
        if (token.length < minLength) {
            return null;
        }
        if (_isInteger(token)) {
            return token;
        }
    }
    return null;
}

<%--
// <p>This function takes a date string and a format string. It matches
//
// @param string the string to evaluate as a date.
// @param format the format of the string.
// @return if the date string matches the format string, it returns the
// getTime() of the date. If it does not match, it returns 0.
--%>
function getDateFromFormat(string, format) {
<%--
    // check we're working with strings
--%>
    string = string + "";
    format = format + "";
    var stringIndex = 0;
        var i=0;
    var index = 0;
    var ch = "";
    var token = "";
    var token2 = "";
    var x, y;
    var now = new Date();
    var year = now.getYear();
    var month = now.getMonth() + 1;
    var date = 1;
    var hh = now.getHours();
    var mm = now.getMinutes();
    var ss = now.getSeconds();
    var ampm="";

        var dayName = "";
        var monthName = "";

    while (index < format.length) {
        // Get next token from format string
        ch = format.charAt(index);
        // escape quotes
        if (ch == "'") {
            while((index < format.length)
                    && ((ch = format.charAt(++index)) != "'")) {
                ;
            }
        }

        token="";
        while ((index < format.length)
                && (format.charAt(index) == ch)) {
            token += format.charAt(index++);
        }

<%--
        // Extract contents of value based on format token
        // -- YEAR --
--%>
        if ((token == "yyyy")
                || (token == "yy")
                || (token == "y")) {
            if (token == "yyyy") {
                x = 4;
                y = 4;
            }
            if (token == "yy") {
                x = 2;
                y = 2;
            }
            if (token == "y") {
                x = 2;
                y = 4;
            }
            year = _getInteger(string, stringIndex, x, y);

            if (year == null) {
                return 0;
            }

            stringIndex += year.length;
            if (year.length == 2) {
                year = ((year > y2kCutOff) ? 1900 : 2000) + ( year -0 );
            }
<%--
        // -- MONTH NAME --
--%>
        } else if (token == "MMM"){
            month = 0;
            for (i = 0; i< MONTH_SHORT_NAMES.length; ++i) {
                monthName=MONTH_SHORT_NAMES[i];
                if (string.substring(stringIndex, stringIndex + monthName.length).toLowerCase() == monthName.toLowerCase()) {
                    month= i + 1;
                    if (month > 12) {
                        month -= 12;
                    }
                    stringIndex += monthName.length;
                    break;
                }
            }
            if ((month < 1)
                    || (month > 12)) {
                return 0;
            }
        } else if (token == "MMMM") {
            month = 0;
            for (i = 0; i< MONTH_LONG_NAMES.length; ++i) {
                monthName=MONTH_LONG_NAMES[i];
                if (string.substring(stringIndex, stringIndex + monthName.length).toLowerCase() == monthName.toLowerCase()) {
                    month = i + 1;
                    if (month > 12) {
                        month -= 12;
                    }

                    stringIndex += monthName.length;
                    break;
                }
            }
            if ((month < 1)
                    || (month > 12)) {
                return 0;
            }

<%--
        // -- WEEK-DAY --
--%>
        } else if (token == "EEE") {

                        for (i=0; i < DAY_SHORT_NAMES.length; ++i) {
                                dayName = DAY_SHORT_NAMES[i];
                if (string.substring(stringIndex, stringIndex + dayName.length).toLowerCase() == dayName.toLowerCase()) {
                    stringIndex += dayName.length;
                    break;
                }
            }
        } else if (token == "EEEE") {
            for (i=0; i < DAY_LONG_NAMES.length; ++i) {
                                dayName = DAY_LONG_NAMES[i];
                if (string.substring(stringIndex, stringIndex + dayName.length).toLowerCase() == dayName.toLowerCase()) {
                    stringIndex += dayName.length;
                    break;
                }
            }
<%--
        // -- MONTH NUMBER --
--%>
        } else if (token == "MM"||token == "M") {
            month = _getInteger(string, stringIndex, token.length, 2);
            if ((month == null)
                    || (month < 1)
                    || (month > 12)) {
                return 0;
            }
            stringIndex+=month.length;
<%--
        // -- DAY OF MONTH NUMBER --
--%>
        } else if (token == "dd"||token == "d") {
            date = _getInteger(string, stringIndex, token.length, 2);
            if ((date == null)
                    || (date < 1)
                    || (date > 31)) {
                return 0;
            }
            stringIndex+=date.length;
<%--
        // -- 12h HOUR --
--%>
        } else if (token == "hh"||token == "h") {
            hh = _getInteger(string, stringIndex, token.length, 2);
            if ((hh == null)
                    || (hh < 1)
                    || (hh > 12)) {
                return 0;
            }
            stringIndex+=hh.length;
<%--
        // -- 24h HOUR --
--%>
        } else if (token == "HH"||token == "H") {
            hh = _getInteger(string, stringIndex, token.length, 2);
            if ((hh == null)
                    || (hh < 0)
                    || (hh > 23)) {
                return 0;
            }
            stringIndex += hh.length;
<%--
        // -- 24h HOUR, starting with 1 --
--%>
        } else if (token == "KK"||token == "K") {
            hh = _getInteger(string, stringIndex, token.length, 2);
            if ((hh == null)
                    || (hh < 0)
                    || (hh > 11)) {
                return 0;
            }
            stringIndex += hh.length;
<%--
        // -- 12h HOUR, starting with 0 --
--%>
        } else if (token == "kk"||token == "k") {
            hh = _getInteger(string, stringIndex, token.length, 2);
            if ((hh == null)
                    || (hh < 1)
                    || (hh > 24)) {
                return 0;
            }
            stringIndex+=hh.length;hh--;
<%--
        // -- MINUTES --
--%>
        } else if (token == "mm"||token == "m") {
            mm = _getInteger(string, stringIndex, token.length, 2);
            if ((mm == null)
                    || (mm < 0)
                    || (mm > 59)) {
                return 0;
            }
            stringIndex += mm.length;
<%--
        // -- SECONDS --
--%>
        } else if (token == "ss"||token == "s") {
            ss = _getInteger(string, stringIndex, token.length, 2);
            if ((ss == null)
                    || (ss < 0)
                    || (ss > 59)) {
                return 0;
            }
            stringIndex+=ss.length;
<%--
        // -- AM/PM --
--%>
        } else if (token == "a") {
            if (string.substring(stringIndex, stringIndex + 2).toLowerCase() == "am") {
                ampm = "AM";
            } else if (string.substring(stringIndex, stringIndex + 2).toLowerCase() == "pm") {
                ampm = "PM";
            } else {
                return 0;
            }
            stringIndex+=2;
        } else {
            if (string.substring(stringIndex, stringIndex + token.length) != token) {
                return 0;
            } else {
                stringIndex += token.length;
            }
        }
    }

    // If there are any trailing characters left in the value, it doesn't match
    if (stringIndex != string.length) {
        return 0;
    }

<%--
    // check date for month - februrary first
--%>
    if (month == 2) {
        // Check for leap year
        if (((year %4 == 0)
                && (year %100 != 0))
                || (year %400 == 0)) { // leap year
            if (date > 29){
                return false;
            }
        } else {
            if (date > 28) {
                return false;
            }
        }
    }

<%--
    // 'Thirty days hath September, April, June and November. All the rest hath
    // thirty-one, except February...'
--%>
    if ((month == 4)
            || (month == 6)
            || (month == 9)
            || (month == 11)) {
        if (date > 30) {
            return false;
        }
    }

<%--
    // Correct hours value for AM/PM
--%>
    if ((hh < 12)
            && (ampm == "PM")) {
        hh = hh - 0 + 12;
    } else if ((hh > 11)
            && (ampm == "AM")) {
        hh -= 12;
    }

    var newDate = new Date(year, month-1, date, hh, mm, ss);
    return newDate.getTime();
}

<%--
// <p>This function takes a date field with a date formatted in dateInputFormat
// and converts its value to dateDisplayFormat which is more strict.</p>
//
// @param dateField the html element - field with a date in it
//
--%>
function reformatDateField(dateField, dateInputFormat, dateDisplayFormat) {
    var newDateMs = getDateFromFormat(dateField.value,
        dateInputFormat);
    if (newDateMs == 0) {
        return;
    }
    var newDate = new Date();
    newDate.setTime(newDateMs);
    dateField.value = formatDate(newDate, dateDisplayFormat);
}

<%--
// <p>This function takes a date field with a time formatted in timeInputFormat
// and converts its value to timeDisplayFormat which is more strict.</p>
//
// @param timeField the html element - field with a time in it
//
--%>
function reformatTimeField(timeField, timeInputFormat, timeDisplayFormat) {
    var newTimeMs = getDateFromFormat(timeField.value,
        timeInputFormat);
    if (newTimeMs == 0) {
        return;
    }
    var newTime = new Date();
    newTime.setTime(newTimeMs);
    timeField.value = formatDate(newTime, timeDisplayFormat);
}
