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
package net.sourceforge.ganttproject.time.gregorian;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.language.GanttLanguage.Event;
import net.sourceforge.ganttproject.time.TextFormatter;
import net.sourceforge.ganttproject.time.TimeUnitText;

public class MonthTextFormatter extends CachingTextFormatter implements
        TextFormatter {
    public MonthTextFormatter() {
        initFormats();
    }

    protected TimeUnitText createTimeUnitText(Date adjustedLeft) {
        TimeUnitText result;
        String longText = MessageFormat.format("{0}",
                new Object[] { myLongFormat.format(adjustedLeft) });
        String mediumText = MessageFormat.format("{0}",
                new Object[] { myMediumFormat.format(adjustedLeft) });
        String shortText = MessageFormat.format("{0}",
                new Object[] { myShortFormat.format(adjustedLeft) });
        result = new TimeUnitText(longText, mediumText, shortText);
        return result;
    }

    private void initFormats() {
        myLongFormat = GanttLanguage.getInstance()
                .createDateFormat("MMMM yyyy");
        myMediumFormat = GanttLanguage.getInstance().createDateFormat(
                "MMM - yy");
        myShortFormat = GanttLanguage.getInstance().createDateFormat("MM - yy");
    }

    public void languageChanged(Event event) {
        super.languageChanged(event);
        initFormats();
    }

    private SimpleDateFormat myLongFormat;

    private SimpleDateFormat myMediumFormat;

    private SimpleDateFormat myShortFormat;
}
