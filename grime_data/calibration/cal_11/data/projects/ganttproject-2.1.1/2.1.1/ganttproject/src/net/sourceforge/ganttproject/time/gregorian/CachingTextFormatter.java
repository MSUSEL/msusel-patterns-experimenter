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
 * Created on 06.03.2005
 */
package net.sourceforge.ganttproject.time.gregorian;

import java.util.Date;
import java.util.HashMap;

import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.language.GanttLanguage.Event;
import net.sourceforge.ganttproject.language.GanttLanguage.Listener;
import net.sourceforge.ganttproject.time.DateFrameable;
import net.sourceforge.ganttproject.time.TimeUnit;
import net.sourceforge.ganttproject.time.TimeUnitText;

/**
 * @author bard
 */
public abstract class CachingTextFormatter implements Listener {
    private final HashMap myTextCache = new HashMap();

    protected CachingTextFormatter() {
        GanttLanguage.getInstance().addListener(this);
    }

    public TimeUnitText format(TimeUnit timeUnit, Date baseDate) {
        TimeUnitText result = null;
        Date adjustedLeft = ((DateFrameable) timeUnit).adjustLeft(baseDate);
        result = getCachedText(adjustedLeft);
        if (result == null) {
            result = createTimeUnitText(adjustedLeft);
            myTextCache.put(adjustedLeft, result);
        }

        return result;
    }

    protected TimeUnitText getCachedText(Date startDate) {
        return (TimeUnitText) myTextCache.get(startDate);
    }

    public void languageChanged(Event event) {
        myTextCache.clear();
    }

    protected abstract TimeUnitText createTimeUnitText(Date adjustedLeft);

}
