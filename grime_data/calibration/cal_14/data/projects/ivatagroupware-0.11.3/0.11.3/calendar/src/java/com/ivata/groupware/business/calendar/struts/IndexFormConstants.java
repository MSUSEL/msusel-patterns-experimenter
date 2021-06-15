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
package com.ivata.groupware.business.calendar.struts;


/**
 * <p>Constants used to identify view styles for calendar.</p>
 *
 * @since 2003-02-02
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 *
 */
public class IndexFormConstants {
    /**
     * <p>Represents day view.</p>
     */
    public static final Integer VIEW_DAY = new Integer(0);
    /**
     * <p>Represents working week, or 5 day view.</p>
     */
    public static final Integer VIEW_WORK_WEEK = new Integer(1);
    /**
     * <p>Represents week, or 7 day view.</p>
     */
    public static final Integer VIEW_WEEK = new Integer(2);
    /**
     * <p>Represents month view.</p>
     */
    public static final Integer VIEW_MONTH = new Integer(3);
    /**
     * <p>Represents year view.</p>
     */
    public static final Integer VIEW_YEAR = new Integer(4);
    /**
     * <p>Represents a list view displaying recent and future events as a list.</p>
     */
    public static final Integer VIEW_LIST = new Integer(15);

}
