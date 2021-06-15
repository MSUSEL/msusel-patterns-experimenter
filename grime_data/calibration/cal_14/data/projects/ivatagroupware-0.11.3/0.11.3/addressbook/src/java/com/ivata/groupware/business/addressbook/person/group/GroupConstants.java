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
package com.ivata.groupware.business.addressbook.person.group;

/**
 * <p>Store all the constants for groups.</p>
 *
 * @since   2002-09-26
 * @author  jano
 * @version $Revision: 1.5 $
 */
public class GroupConstants {

    /**
     * <p>Administrator group, this group can not be deleted.</p>
     */
    public final static Integer GROUP_ADMINISTRATOR = new Integer(1);
    /**
     * <p>User group. Each USER is int this group, this group can not be deleted.</p>
     */
    public final static Integer USER_GROUP = new Integer(2);

    /**
     * <p>each user has privete group, so those user groups are under this group</p>
     */
    public final static Integer USER_GROUP_PRIVATE = new Integer(3);

    /**
     * <p>This group contain addressBooks groups - private and public.</p>
     */
    public final static Integer ADDRESS_BOOK = new Integer(4);
    /**
     * <p>This group contain private addressBooks - one for each user.</p>
     */
    public final static Integer ADDRESS_BOOK_PRIVATE = new Integer(5);
    /**
     * <p>This group contain public addressBooks</p>
     */
    public final static Integer ADDRESS_BOOK_PUBLIC = new Integer(6);
    /**
     * <p>This is a default address book, present when the system is initially
     * created. Everyone can see this address book.</p>
     */
    public final static Integer ADDRESS_BOOK_DEFAULT = new Integer(7);
    public static boolean equals(
            final Integer compareID,
            final Integer constantGroupID) {
        // none of the constants are null!
        assert (constantGroupID != null);
        if (compareID == null) {
            return false;
        }
        return constantGroupID.equals(compareID);
    }
}
