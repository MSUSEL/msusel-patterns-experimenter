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
package com.ivata.groupware.admin.security.addressbook;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.mask.util.SystemException;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Mar 29, 2004
 * @version $Revision: 1.3 $
 */
public interface AddressBookSecurity extends Security {

    /**
     * <p>Add a new user to the system. The person's details must first have
     * been entered into the address book.</p>
     *
     * <p><strong>Note:</strong> This routine will fail if the person has no email
     * address!</p>
     *
     * @param userName the name of the user who is adding the new user to the
     *     system. <strong>This is not the user name of the new user!</strong>
     * @param person the person, with the user object set in it.
     * @throws InvalidFieldValueException if any of the fields supplied are
     *     <code>null</code>.
     */
    public void addUserToPerson(final
        SecuritySession securitySession,
            final PersonDO person)
        throws SystemException;
}