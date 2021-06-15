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
/*
 LICENSE:
 
 This program is free software; you can redistribute it and/or modify  
 it under the terms of the GNU General Public License as published by  
 the Free Software Foundation; either version 2 of the License, or     
 (at your option) any later version.                                   
 
 Copyright (C) 2004, GanttProject Development Team
 */
package net.sourceforge.ganttproject.time.gregorian;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import net.sourceforge.ganttproject.calendar.CalendarFactory;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.time.DateFrameable;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author bard
 */
public class FramerImpl implements DateFrameable {
    private final int myCalendarField;

    public FramerImpl(int calendarField) {
        myCalendarField = calendarField;
    }

    public Date adjustRight(Date baseDate) {
        Calendar c = CalendarFactory.newCalendar();
        c.setTime(baseDate);
        clearFields(c);
        c.add(myCalendarField, 1);
        return c.getTime();
    }

    private void clearFields(Calendar c) {
        for (int i = myCalendarField + 1; i <= Calendar.MILLISECOND; i++) {
            c.clear(i);
        }
    }

    public Date adjustLeft(Date baseDate) {
        Calendar c = CalendarFactory.newCalendar();
        c.setTime(baseDate);
        Date beforeClear = c.getTime();
        clearFields(c);
        // if (beforeClear.compareTo(c.getTime())==0) {
        // c.add(Calendar.MILLISECOND, -1);
        // }
        Date result = c.getTime();
        return result;
    }

    public Date jumpLeft(Date baseDate) {
        Calendar c = CalendarFactory.newCalendar();
        c.setTime(baseDate);
        c.add(myCalendarField, -1);
        return c.getTime();
    }
    
    public static void main(String[] args) {
        GanttLanguage.getInstance().setLocale(null);
        Calendar c = (Calendar) GanttLanguage.getInstance().newCalendar();
        Date now = GregorianCalendar.getInstance().getTime();
        c.setTime(now);
        for (int i = Calendar.DAY_OF_MONTH + 1; i <= Calendar.MILLISECOND; i++) {
            c.clear(i);
        }
        Date result = c.getTime();
        System.err.println(result);
    }
}
