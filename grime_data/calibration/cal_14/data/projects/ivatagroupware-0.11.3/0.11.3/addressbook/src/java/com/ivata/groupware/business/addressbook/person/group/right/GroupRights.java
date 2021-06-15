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

import java.io.Serializable;
import java.util.List;

import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.person.group.GroupConstants;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.mask.persistence.right.PersistenceRights;
import com.ivata.mask.valueobject.ValueObject;

import com.ivata.groupware.admin.security.server.SecurityServer;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.mask.util.SystemException;

/**
 * This class will implement ivata groupware rights, by checking against the
 * group right table/entities.
 * To keep things simple for this release, however, it returns <code>true</code>
 * for <em>almost</em> everything - it doesn't let you delete major things like
 * the system-wide address book, but that is about it.
 *
 * @since ivata groupware 0.10 (2005-01-14)
 * @author Colin MacLeod
 * <a href="mailto:colin.macleod@ivata.com">colin.macleod@ivata.com</a>
 * @version $Revision: 1.4.2.1 $
 */

public class GroupRights implements PersistenceRights, Serializable {
    private AddressBook addressBook;
    private SecurityServer securityServer;
    public GroupRights (AddressBook addressBook, SecurityServer securityServer) {
        this.addressBook = addressBook;
        this.securityServer = securityServer;
    }
    /**
     * Refer to {@link PersistenceRights#canAdd}.
     * @param valueObjectClassParam Refer to {@link PersistenceRights#canAdd}.
     *
     * @return Refer to {@link PersistenceRights#canAdd}.
     */
    public boolean canAdd(String userName,
            Class valueObjectClassParam) {
        return true;
    }

    /**
     * Refer to
     * {@link PersistenceRights#canAmend(String, ValueObject)}.
     * @param valueObjectParam Refer to
     * {@link PersistenceRights#canAmend(String, ValueObject)}.
     *
     * @return Refer to
     * {@link PersistenceRights#canAmend(String, ValueObject)}.
     */
    public boolean canAmend(String userNameParam,
            ValueObject valueObjectParam) {
        return true;
    }

    /**
     * Refer to {@link PersistenceRights#canAmend(String,
     * ValueObject, String)}.
     * @param valueObjectParam Refer to
     * {@link PersistenceRights#canAmend(String,
     * ValueObject, String)}.
     * @param fieldNameParam Refer to
     * {@link PersistenceRights#canAmend(String,
     * ValueObject, String)}.
     * @return Refer to
     * {@link PersistenceRights#canAmend(String,
     * ValueObject, String)}.
     * @see PersistenceRights#canAmend(String, com.ivata.mask.valueobject.ValueObject, java.lang.String)
     */
    public boolean canAmend(String userNameParam,
            ValueObject valueObjectParam, String fieldNameParam) {
        // you can only change the name of an address book if it is not
        // private
        if (valueObjectParam instanceof GroupDO) {
            GroupDO group = (GroupDO)valueObjectParam;
            GroupDO parent = group.getParent();
            // you can't rename a private address book
            if ((parent != null)
                    && "name".equals(fieldNameParam)
                    && (GroupConstants.equals(
                            parent.getId(),
                            GroupConstants.ADDRESS_BOOK_PRIVATE))) {
                return false;
            }
        }

        // everything else goes...
        return true;
    }

    /**
     * Refer to {@link PersistenceRights#canRemove}.
     * @param valueObjectParam Refer to {@link
     * PersistenceRights#canRemove}.
     *
     * @return Refer to {@link PersistenceRights#canRemove}.
     */
    public boolean canRemove(String userNameParam,
            ValueObject valueObjectParam) {
        // only return false if this is a major component
        if (valueObjectParam instanceof GroupDO) {
            GroupDO group = (GroupDO)valueObjectParam;
            // you can't delete the default, system-wide address book
            if (GroupConstants.equals(
                    group.getId(),
                    GroupConstants.ADDRESS_BOOK_DEFAULT)) {
                return false;
            }
            GroupDO parent = group.getParent();
            // you can't delete a private address book
            if ((parent == null)
                    || (GroupConstants.equals(
                            parent.getId(),
                            GroupConstants.ADDRESS_BOOK_PRIVATE))) {
                return false;
            }
            // you can't delete a group which has users, people
            if ((group.getPeople().size() > 0)
                    || (group.getUsers().size() > 0)) {
                return false;
            }
            // you can't delete a group which has children groups
            try {
                SecuritySession guestSession = securityServer.loginGuest();
                List children = addressBook.findGroupsByParent(guestSession,
                    group.getId());
                if ((children != null)
                        && (children.size() > 0)) {
                    return false;
                }
            } catch (SystemException e) {
                throw new RuntimeException(e);
            }
        }
        // everything else goes...
        return true;
    }
}
