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
package com.ivata.groupware.business.library.item;


/**
 * <p>Stores constant values for the {@link LibraryItemBean library item}.</p>
 *
 * @since   2002-06-14
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 * @see     LibraryItemBean
 */
public class LibraryItemConstants {
    /**
 * <p>Represents a general document or report type.</p>
 */
    public final static Integer ITEM_DOCUMENT = new Integer(0);

    /**
 * <p>Represents a memo from one person to a group.</p>
 */
    public final static Integer ITEM_MEMO = new Integer(1);

    /**
 * <p>Represents frequently asked questions.</p>
 */
    public final static Integer ITEM_FAQ = new Integer(2);

    /**
 * <p>Represents minutes/agenda of a meeting.</p>
 */
    public final static Integer ITEM_MEETING = new Integer(3);

    /**
 * <p>Represents a short note, with just a summary.</p>
 */
    public final static Integer ITEM_NOTE = new Integer(4);

    /**
 * <p>Represents a news item, usually linking externally.</p>
 */
    public final static Integer ITEM_NEWS = new Integer(5);
}
