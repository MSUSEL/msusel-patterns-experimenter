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
package com.ivata.groupware.admin.security.right;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.BusinessLogic;
import com.ivata.groupware.business.addressbook.person.group.right.RightConstants;
import com.ivata.groupware.container.persistence.QueryPersistenceManager;
import com.ivata.mask.util.SystemException;


/**
 * <p>Security rights determine what each user can and cannot do within the
 * security subsystem. If you need to know where a user has sufficient rights
 * to add, change or remove another user, then  this is the class to tell you.</p>
 *
 *
 * @since 2002-09-08
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public class SecurityRightsImpl extends BusinessLogic implements SecurityRights {
    /**
     * Persistence manger used to store/retrieve data objects.
     */
    private QueryPersistenceManager persistenceManager;

    /**
     * Construct a new address book rights instance.
     *
     * @param persistenceManager used to store objects in db.
     */
    public SecurityRightsImpl(QueryPersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    /**
     * <p>See if a user has sufficient rights to add user to the system - it's meen to everyOne group.</p>
     *
     * @param userName the user who wants to add another user.
     * @param personId the unique identifier of the person who will be added.
     * @return <code>true</code> if this action is authorized by the system,
     * otherwise <code>false</code>.
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public boolean canAddUser(final SecuritySession securitySession)
            throws SystemException {
        return canUser(securitySession, RightConstants.ACCESS_ADD);
    }

    /**
     * <p>See if a user has sufficient rights to amend user in the
     * system - it's meen in everyone group.</p>
     *
     * @param userName the user who wants to add another user.
     * @param userNameAmend the user who should be amended.
     * @return <code>true</code> if this action is authorized by the system,
     * otherwise <code>false</code>.
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public boolean canAmendUser(final SecuritySession securitySession)
            throws SystemException {
        return canUser(securitySession, RightConstants.ACCESS_AMEND);
    }

    /**
     * <p>See if a user has sufficient rights to remove user from the
     * system - it's meen from everone group.</p>
     *
     * @param userName the user who wants to add another user.
     * @param userNameRemove the user who should be removed.
     * @return <code>true</code> if this action is authorized by the system,
     * otherwise <code>false</code>.
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public boolean canRemoveUser(final SecuritySession securitySession)
            throws SystemException {
        return canUser(securitySession, RightConstants.ACCESS_REMOVE);
    }
    /**
     * <p>Internal helper method. Find out if a user is allowed to access
     * entries in a given group.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param groupId the unique identifier of the group to check.
     * @param access the access level as defined in {@link
     *      com.ivata.groupware.security.person.group.right.RightConstants
     *      RightConstants}.
     * @return <code>true</code> if the user is entitled to access entries in the
     *      group, otherwise <code>false</code>.
     */
    public boolean canUser(final SecuritySession securitySession,
            final Integer access)
            throws SystemException {
        // for now, everyone can do everything!
        return true;
        /* TODO:
        PersistenceSession persistenceSession =
            persistenceManager.openSession(securitySession);
        // see if we're allowed to insert this group into the parent
        try {
            Collection tmp = persistenceManager.find(persistenceSession,
                "rightByUserNameAccessDetailTargetId",
                new Object [] {
                    securitySession.getUser().getName(),
                    access,
                    RightConstants.DETAIL_PERSON_GROUP_MEMBER,
                    GroupConstants.USER_GROUP
                });
            if (tmp.size() == 0) {
                return false;
            }
        } catch (FinderException e) {
            // oops
            return false;
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
        // only return true if we get this far :- )
        return true;
        */
    }
}
