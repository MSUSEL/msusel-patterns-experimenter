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
package net.sourceforge.ganttproject.parser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import net.sourceforge.ganttproject.calendar.GPCalendar;

import org.xml.sax.Attributes;

public class DefaultWeekTagHandler implements TagHandler {

    private GPCalendar myGPCalendar;

    private Calendar myCalendar = GregorianCalendar.getInstance(Locale.ENGLISH);

    private SimpleDateFormat myShortFormat = new SimpleDateFormat("EEE",
            Locale.ENGLISH);

    public DefaultWeekTagHandler(GPCalendar calendar) {
        myGPCalendar = calendar;
        for (int i = 1; i <= 7; i++) {
            myGPCalendar.setWeekDayType(i, GPCalendar.DayType.WORKING);
        }
    }

    public void startElement(String namespaceURI, String sName, String qName,
            Attributes attrs) throws FileFormatException {
        if ("default-week".equals(qName)) {
            loadCalendar(attrs);
        }
    }

    private void loadCalendar(Attributes attrs) {
        for (int i = 1; i <= 7; i++) {
            String nextDayName = getShortDayName(i);
            String nextEncodedType = attrs.getValue(nextDayName);
            if ("1".equals(nextEncodedType)) {
                myGPCalendar.setWeekDayType(i, GPCalendar.DayType.WEEKEND);
            }
        }

    }

    private String getShortDayName(int i) {
        myCalendar.set(Calendar.DAY_OF_WEEK, i);
        return myShortFormat.format(myCalendar.getTime()).toLowerCase();
    }

    public void endElement(String namespaceURI, String sName, String qName) {
    }

}
