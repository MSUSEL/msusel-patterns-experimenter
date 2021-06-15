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
package com.ivata.groupware.business.addressbook.person.group.right;


/**
 * <p>Store all the constants for person group rights. You need to use these
 * constants to identify the 'detail' of right you are applying, or the access
 * value.</p>
 *
 * @since   2002-05-19
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public class RightConstants {

    /**
     * <p>This right allows a group to add, view or remove group memberships.</p>
     */
    public final static Integer DETAIL_PERSON_GROUP_MEMBER = new Integer(1);

    /**
     * <p>This right allows a group to add, view or remove library items, based
     * on their topic.</p>
     */
    public final static Integer DETAIL_LIBRARY_ITEM_TOPIC = new Integer(2);

    /**
     * <p>This right allows a group to add, amend or remove topics.</p>
     */
    public final static Integer DETAIL_LIBRARY_TOPIC = new Integer(3);

    /**
     * <p>This right allows a group to add, amend or remove comment, based on
     * the topics of their items.</p>
     */
    public final static Integer DETAIL_LIBRARY_COMMENT_TOPIC = new Integer(4);

    /**
     * <p>This right allows a group to amend a setting on user level.</p>
     */
    public final static Integer DETAIL_SETTING_USER = new Integer(5);

    /**
     * <p>This right allows a group to amend settings on system level.</p>
     */
    public final static Integer DETAIL_SETTING_SYSTEM = new Integer(6);

    /**
     * <p>This right allows a group to add to, amend and remove a directory.</p>
     */
    public final static Integer DETAIL_DIRECTORY = new Integer(8);

    /**
     * <p>Gives the group the right to view/select an element.</p>
     */
    public final static Integer ACCESS_VIEW = new Integer(0);

    /**
     * <p>Gives the group the right to add/insert an element.</p>
     */
    public final static Integer ACCESS_ADD = new Integer(1);

    /**
     * <p>Gives the group the right to amend/update an element.</p>
     */
    public final static Integer ACCESS_AMEND = new Integer(2);

    /**
     * <p>Gives the group the right to remove/delete an element.</p>
     */
    public final static Integer ACCESS_REMOVE = new Integer(3);
}
