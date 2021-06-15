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
package net.sourceforge.ganttproject.io;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

import javax.xml.transform.sax.TransformerHandler;

import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.calendar.GPCalendar;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class CalendarSaver extends SaverBase {
    private SimpleDateFormat myShortFormat = new SimpleDateFormat("EEE",
            Locale.ENGLISH);

    private Calendar myCalendar = GregorianCalendar.getInstance(Locale.ENGLISH);

    void save(IGanttProject project, TransformerHandler handler)
            throws SAXException {
        AttributesImpl attrs = new AttributesImpl();
        startElement("calendars", attrs, handler);
        startElement("day-types", attrs, handler);
        //
        addAttribute("id", "0", attrs);
        emptyElement("day-type", attrs, handler);
        addAttribute("id", "1", attrs);
        emptyElement("day-type", attrs, handler);
        //
        addAttribute("id", "1", attrs);
        addAttribute("name", "default", attrs);
        startElement("calendar", attrs, handler);
        for (int i = 1; i <= 7; i++) {
            boolean holiday = project.getActiveCalendar().getWeekDayType(i) == GPCalendar.DayType.WEEKEND;
            addAttribute(getShortDayName(i), holiday ? "1" : "0", attrs);
        }
        emptyElement("default-week", attrs, handler);
        emptyElement("overriden-day-types", attrs, handler);
        emptyElement("days", attrs, handler);
        endElement("calendar", handler);
        //
        endElement("day-types", handler);
        Collection publicHoliday = project.getActiveCalendar()
                .getPublicHolidays();
        for (Iterator iter = publicHoliday.iterator(); iter.hasNext();) {
            Date d = (Date) iter.next();
            if (d.getYear() == 1 - 1900)
                addAttribute("year", "", attrs);
            else
                addAttribute("year", (d.getYear() + 1900) + "", attrs);
            addAttribute("month", (d.getMonth() + 1) + "", attrs);
            addAttribute("date", d.getDate() + "", attrs);
            emptyElement("date", attrs, handler);
        }

        endElement("calendars", handler);
    }

    private String getShortDayName(int i) {
        myCalendar.set(Calendar.DAY_OF_WEEK, i);
        return myShortFormat.format(myCalendar.getTime()).toLowerCase();
    }

}
