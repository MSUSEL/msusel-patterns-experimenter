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
/**
 * 
 */
package net.sourceforge.ganttproject.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.ganttproject.GanttCalendar;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.gui.DateIntervalListEditor.DateInterval;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author nbohn
 */
public class GanttDialogPublicHoliday {
    private GanttLanguage lang = GanttLanguage.getInstance();

    private DateIntervalListEditor publicHolidayBean;


    private DateIntervalListEditor.DateIntervalModel publicHolidays;

    private boolean isChanged = false;

    private UIFacade myUIFacade;
    
    public GanttDialogPublicHoliday(IGanttProject project, UIFacade uiFacade) {
        publicHolidays = new DateIntervalListEditor.DefaultDateIntervalModel();
        for (Iterator iter = project.getActiveCalendar().getPublicHolidays().iterator(); iter.hasNext();) {
            Date d = (Date)iter.next();
            publicHolidays.add(new DateIntervalListEditor.DateInterval(d,d));
        }

        //publicHolidayBean = new GanttPublicHolidayBean(publicHolidays);
        publicHolidayBean = new DateIntervalListEditor(publicHolidays);
        myUIFacade = uiFacade;

        //publicHolidayBean.addActionListener(this);

    }

    public Component getContentPane() {
        return publicHolidayBean;
    }
    
    public List getHolidays() {
        //return Arrays.asList(publicHolidays.toArray());
    	List result =new ArrayList();
    	DateInterval[] intervals = publicHolidays.getIntervals();
    	for (int i=0; i<intervals.length; i++) {
    		result.add(new GanttCalendar(intervals[i].start));
    	}
    	return result;
    }
}
