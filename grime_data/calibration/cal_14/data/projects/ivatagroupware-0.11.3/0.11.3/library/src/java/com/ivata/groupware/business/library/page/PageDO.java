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
package com.ivata.groupware.business.library.page;

import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p>Represents a page of text within the library. Items with the following
 * types can have pages associated with them:<br/>
 * <ul>
 *   <li>document</li>
 *   <li>memo</li>
 *   <li>news item</li>
 *   <li>meeting agenda/minutes</li>
 * </ul></p>
 *
 * @since 2002-06-14
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @hibernate.class
 *      table="library_page"
 */
public class PageDO  extends BaseDO {

    /**
     * <p>Get the page number of this page within the library item. The first
     * page in the item will have the number <code>0</code>.</p>
     *
     * @return an <code>Integer</code> beginning with 0 describing the
     *   page's significance within it's library item. Lower numbers have higher
     *   significance.
     */
    private Integer number;

    /**
     * <p>
     * Contains the contents of the page.
     * </p>
     */
    private String text;

    /**
     * <p>Get the page number of this page within the library item. The first
     * page in the item will have the number <code>0</code>.</p>
     *
     * @return an <code>Integer</code> beginning with 0 describing the
     *   page's significance within it's library item. Lower numbers have higher
     *   significance.
     * @hibernate.property
     */
    public final Integer getNumber() {
        return number;
    }
    /**
     * <p>
     * Contains the contents of the page.
     * </p>
     *
     * @return Human-readable, clear text contents of the page.
     * @hibernate.property
     */
    public final String getText() {
        return text;
    }
    /**
     * <p>Set the page number of this page within the library item. The first
     * page in the item will have the number <code>0</code>.</p>
     *
     * @param number An <code>Integer</code> beginning with 0 describing the
     *   page's significance within it's library item. Lower numbers have higher
     *   significance.
     */
    public final void setNumber(final Integer number) {
        this.number = number;
    }
    /**
     * <p>
     * Contains the contents of the page.
     * </p>
     *
     * @param text Human-readable, clear text contents of the page.
     */
    public final void setText(final String text) {
        this.text = text;
    }
}
