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
package com.ivata.groupware.business.calendar.event.meeting.agendapoint;


import com.ivata.groupware.business.calendar.event.meeting.category.MeetingCategoryDO;
import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p>Each meeting may have many agenda points. These will appear as points on
 * the agenda before the meeting, and as headings in the library item of the
 * minutes after the meeting.</p>
 *
 * @since 2002-06-18
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @hibernate.class
 *      table="meeting_agenda_point"
 * @hibernate.cache
 *      usage="read-write"
 */
public class AgendaPointDO  extends BaseDO {

    /**
     * <p>Get category which contains this agenda point.</p>
     */
    private MeetingCategoryDO category;

    /**
     * <p>The text of the minutes describing the meeting.</p>
     */
    private String minutesText;
    /**
     * <p>The name of this agenda point.</p>
     */
    private String name;
    /**
     * <p>Get the category which contains this agenda point.</p>
     *
     * @return category which contains this agenda point.
     *
     * @hibernate.many-to-one
     */
    public final MeetingCategoryDO getCategory() {
        return category;
    }
    /**
     * <p>Get the text of the minutes describing the meeting.</p>
     *
     * @return text of the minutes describing the meeting.
     * @hibernate.property
     */
    public final String getMinutesText() {
        return minutesText;
    }
    /**
     * <p>Get the name of this agenda point.</p>
     *
     * @return the agenda point name.
     *
     * @hibernate.property
     */
    public final String getName() {
        return name;
    }

    /**
     * <p>Set the category which contains this agenda point.</p>
     *
     * @param category new value of category which contains this agenda point.
     */
    public final void setCategory(final MeetingCategoryDO category) {
        this.category = category;
    }

    /**
     * <p>Set the text of the minutes describing the meeting.</p>
     *
     * @param minutesText new text of the minutes describing the meeting.
     */
    public final void setMinutesText(final String minutesText) {
        this.minutesText = minutesText;
    }

    /**
     * <p>Set the name of this agenda point.</p>
     *
     * @param name the new agenda point name.
     */
    public final void setName(final String name) {
        this.name = name;
    }
}
