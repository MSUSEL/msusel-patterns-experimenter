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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.SecurityImpl;
import com.ivata.groupware.admin.security.server.SecurityServer;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.person.group.GroupConstants;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.groupware.container.persistence.QueryPersistenceManager;
import com.ivata.groupware.container.persistence.TimestampDOHandling;
import com.ivata.mask.Mask;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.field.Field;
import com.ivata.mask.persistence.FinderException;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationError;
import com.ivata.mask.validation.ValidationException;


/**
 * <p>The security bean is responsible for adding, removing and amending users
 * to the system, and for logging in in the first place.</p>
 *
 * @since 2002-09-08
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4.2.1 $
 * TODO: finish user right implementation
 */
public class AddressBookSecurityImpl extends SecurityImpl implements AddressBookSecurity {
    private MaskFactory maskFactory;
    /**
     * Persistence manger used to store/retrieve data objects, or retrieve a
     * new persistence session.
     */
    private QueryPersistenceManager persistenceManager;

    /**
     * Security server - used for logging in users.
     */
    private SecurityServer securityServer;

    /**
     * Construct and initialize the Security implementation.
     *
     * @param persistenceManager persistence manager used to store/retrieve data
     *     objects.
     */
    public AddressBookSecurityImpl(QueryPersistenceManager persistenceManager,
            SecurityServer securityServer,
            final MaskFactory maskFactory,
            final Boolean demoVersion) {
        super(persistenceManager, securityServer, maskFactory, demoVersion);
        this.persistenceManager = persistenceManager;
        this.securityServer = securityServer;
        this.maskFactory = maskFactory;
    }

    /**
     * <p>Add a new user to the system. The person's details must first have
     * been entered into the address book.</p>
     *
     * <p><strong>Note:</strong> This routine will fail if the person has no email
     * address!</p>
     *
     * @param securitySession current security session.
     * @param personId the id of the person EJB, giving name and address
     * @param userNameAdd the name the user will use to log into the system. Case
     *     sensitive.
     * @param enable if <code>true</code>, the user will be initially enabled,
     *     otherwise the user is disabled initially.
     * @throws InvalidFieldValueException if any of the fields supplied are
     *     <code>null</code>.
     */
    public void addUserToPerson(final SecuritySession securitySession,
            final PersonDO person)
            throws SystemException {
        if (isDemoVersion()) {
            return;
        }
        // check we have field values
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        Mask userMask = maskFactory.getMask(UserDO.class);
        Field userNameField = userMask.getField("name");
        Mask personMask = maskFactory.getMask(PersonDO.class);
        try {
            // TODO: there is duplication here between this and the addUser
            // method - we should have a validation method for user names

            // check we have field values
            if ((person.getUser() == null)
                    || StringHandling.isNullOrEmpty(person
                            .getUser().getName())) {
                throw new ValidationException(
                        new ValidationError(
                                "user",
                                Security.BUNDLE_PATH,
                                userNameField,
                                "errors.required"));
            }

            // check the user has an email address
            // only if user is enabled
            UserDO user = person.getUser();
            // if the user is deleted, it is naturally also disabled!
            if (user.isDeleted()) {
                user.setEnabled(false);
            }

            if (user.isEnabled() &&
                    StringHandling.isNullOrEmpty(person.getEmailAddress())) {
                throw new ValidationException(
                        new ValidationError(
                                "errors.addressBook.user.emailAddress",
                                Arrays.asList(new Object [] {
                                        person.getFileAs()
                                })));

            }
            // first check this user name is not already taken
            if (isUser(securitySession, user.getName())) {
                throw new ValidationException(
                            new ValidationError(
                                    "user",
                                    Security.BUNDLE_PATH,
                                    userNameField,
                                    "errors.unique"
                                            ));
            }
            // user id is always the same as person id (1-1 relationship)
            user.setId(person.getId());
            persistenceManager.add(persistenceSession, user);

            // if the user is enabled, add it to the security server
            if (user.isEnabled()) {
                securityServer.addUser(securitySession,
                        user.getName(), getRealName(
                        persistenceSession, user));
            }

            // each user has to be in USER group
            // so put this new user to USER group
            GroupDO userGroup = (GroupDO) persistenceManager.findByPrimaryKey(
                    persistenceSession,
                    GroupDO.class,
                    GroupConstants.USER_GROUP);

            // each user is a member of userGroup
            user.getGroups().add(userGroup);
            persistenceManager.amend(persistenceSession, user);

            // create group in private_user group for this new user
            createPrivateGroups(persistenceSession, securitySession, user);

            TimestampDOHandling.amend(securitySession, person);
            persistenceManager.amend(persistenceSession, person);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
    /**
     * <p>This method creates private userGroup and private AddressBook group.
     * It sets rights for those new groups.</p>
     *
     * @param securitySession current security session.
     * @param user user for whom to set the new groups
     */
    public void createPrivateGroups(
            final PersistenceSession persistenceSession,
            final SecuritySession securitySession,
            final UserDO user)
            throws SystemException {
        if (isDemoVersion()) {
            return;
        }
        // find private_user group which contain all private user groups
        GroupDO privateUser = (GroupDO) persistenceManager.findByPrimaryKey(
                persistenceSession,
                GroupDO.class,
                GroupConstants.USER_GROUP_PRIVATE);
        GroupDO privateAddressBook = (GroupDO) persistenceManager
            .findByPrimaryKey(persistenceSession,
                    GroupDO.class,
                    GroupConstants.ADDRESS_BOOK_PRIVATE);

        // create user's private group
        GroupDO privateUserGroup = new GroupDO();
        privateUserGroup.setName(user.getName());
        privateUserGroup.setParent(privateUser);

        // user is a member of her private group, only she is a member
        privateUserGroup.getUsers().add(user);
        privateUserGroup.setDescription("Private group for user id "
            + user.getId());
        privateUserGroup = (GroupDO) persistenceManager.add(persistenceSession,
                privateUserGroup);


        // create user's private addressBook - it's the user's private group in
        // group address_book/private - where she stores her contacts
        GroupDO privateUserAddressBookGroup = new GroupDO();
        privateUserAddressBookGroup.setParent(privateAddressBook);
        privateUserAddressBookGroup.setName(user.getName());
        privateUserAddressBookGroup.setDescription(
                "Private address book for user id "
                + user.getId());
        privateUserAddressBookGroup = (GroupDO) persistenceManager.add(persistenceSession,
            privateUserAddressBookGroup);

    }

    /**
     * <p>
     * Overriden to provide the real life name for the user.
     * </p>
     *
     * @param persistenceSession Valid persistence session.
     * @param user The user for whom to return the name.
     * @return Real-life name for this user.
     * @throws SystemException
     */
    protected String getRealName(final PersistenceSession persistenceSession,
            final UserDO user)
            throws SystemException {
        // TODO: should really be file as
        return user.getName();
    }
    /**
     * Refer to {@link }.
     *
     * @param securitySessionParam
     * @param userNameParam
     * @return
     * @throws SystemException
     * @see com.ivata.groupware.admin.security.Security#isUser(com.ivata.groupware.admin.security.server.SecuritySession, java.lang.String)
     */
    public boolean isUser(SecuritySession securitySessionParam,
            String userNameParam) throws SystemException {
        if (isDemoVersion()) {
            return false;
        }
        boolean isUser = super.isUser(securitySessionParam, userNameParam);
        if (!isUser) {
            PersistenceSession persistenceSession = persistenceManager
                .openSession(securitySessionParam);
            try {
                persistenceManager.findInstance(persistenceSession,
                    "securityUserByName",
                    new Object[] { userNameParam });
                isUser = true;
            } catch (FinderException thatsGood) {
                // that's good!
            } finally {
                persistenceSession.close();
            }
        }
        return isUser;
    }

    /**
     * <p>if userName emergency is trying login -> find first admin .</p>
     * @param userName
     * @return
     */
    public String loginAgain(final SecuritySession securitySession,
            final String userNameParam)
            throws SystemException {
        String userNameReturn = userNameParam;
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            if (userNameReturn.equals("emergency")) {
                GroupDO group = (GroupDO) persistenceManager.findByPrimaryKey(
                        persistenceSession,
                        GroupDO.class,
                        GroupConstants.GROUP_ADMINISTRATOR);
                Collection admins = group.getUsers();
                Iterator iterator = admins.iterator();
                UserDO firstAdmin = (UserDO) iterator.next();
                userNameReturn = firstAdmin.getName();
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }

        return userNameReturn;
    }
    protected void onAmendUserName(final SecuritySession securitySession,
            final PersistenceSession persistenceSession,
            final UserDO user,
            final String oldUserName)
            throws SystemException {
        if (isDemoVersion()) {
            return;
        }
        // if everyThing is OK update users private groups
        // if user has some contacts in Personal addressBook don't delete those
        GroupDO group = (GroupDO) persistenceManager.findInstance(persistenceSession,
            "addressBookGroupsInGroupByName",
            new Object[] { GroupConstants.ADDRESS_BOOK_PRIVATE, oldUserName });
        group.setName(user.getName());
        persistenceManager.amend(persistenceSession, group);

        group = (GroupDO) persistenceManager.findInstance(persistenceSession,
            "addressBookGroupsInGroupByName",
            new Object[] { GroupConstants.USER_GROUP_PRIVATE, oldUserName });
        group.setName(user.getName());
        persistenceManager.amend(persistenceSession, group);
        super.onAmendUserName(securitySession,
                persistenceSession, user, oldUserName);
    }
}
