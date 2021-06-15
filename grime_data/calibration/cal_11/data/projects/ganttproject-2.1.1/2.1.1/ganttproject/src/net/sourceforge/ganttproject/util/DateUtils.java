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
package net.sourceforge.ganttproject.util;

import java.text.AttributedCharacterIterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * This class groups static methods together to handle dates.
 * 
 * @author bbaranne (Benoit Baranne)
 */
public class DateUtils {
    /**
     * This method tries to parse the given date according to the given locale.
     * Actually, this method tries to parse the given string with several
     * DateFormat : Short, Medium, Long and Full. Normally if you give an
     * appropriate locale in relation with the string, this method will return
     * the correct date.
     * 
     * @param date
     *            String representation of a date.
     * @param locale
     *            Locale use to parse the date with.
     * @return A date object.
     * @throws ParseException
     *             Exception thrown if parsing is fruitless.
     */
    public static Date parseDate(String date)
            throws ParseException {
        DateFormat[] formats = new DateFormat[] {
        		GanttLanguage.getInstance().getShortDateFormat(),
        		GanttLanguage.getInstance().getMediumDateFormat(),
        		GanttLanguage.getInstance().getLongDateFormat()
        };
        //DateFormat dfFull = DateFormat.getDateInstance(DateFormat.FULL, locale);
        for (int i=0; i<formats.length; i++) {
        	try {
        		return formats[i].parse(date);
        	}
        	catch (ParseException e) {
        		if (i+1 == formats.length) {
        			throw e;
        		}
        	}
        	catch (IllegalArgumentException e) {
        		if (i+1 == formats.length) {
        			throw e;
        		}
        		
        	}
        }
        throw new ParseException("Failed to parse date="+date, 0);
    }
}
