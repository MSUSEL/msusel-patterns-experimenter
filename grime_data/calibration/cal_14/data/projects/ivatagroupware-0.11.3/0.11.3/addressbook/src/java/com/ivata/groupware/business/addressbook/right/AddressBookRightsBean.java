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
package com.ivata.groupware.business.addressbook.right;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.person.employee.EmployeeDO;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.util.SystemException;


/**
 * <p>Address book rights determine what each user can and cannot do within the
 * address book subsystem.</p>
 *
 * @since 2002-07-22
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @ejb.bean
 *      name="AddressBookRights"
 *      display-name="AddressBookRights"
 *      type="Stateless"
 *      view-type="both"
 *      local-jndi-name="AddressBookRightsLocal"
 *      jndi-name="AddressBookRightsRemote"
 *
 * @ejb.transaction
 *      type = "Required"
 *
 *  @ejb.home
 *      generate="false"
 *      remote-class="com.ivata.groupware.business.addressbook.right.AddressBookRightsRemoteHome"
 *
 *  @ejb.interface
 *      remote-class="com.ivata.groupware.business.addressbook.right.AddressBookRightsRemote"
 */
public class AddressBookRightsBean implements SessionBean {


    /**
     * <p>Provides the session bean with container-specific information.</p>
     */
    SessionContext sessionContext;

    /**
     * <p>Change user rights for group.</p>
     *
     * @param id of group
     * @param userName user which trying to change rights
     * @param rights collection of group ids which will have ACCESS right to that group
     * @param set to one of the <code>ACCESS_...</code> constants in <code>RightConstants</code>.
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public void amendRightsForGroup(final SecuritySession securitySession,
            final GroupDO group,
            final Collection rights,
            final Integer access) throws SystemException {
        getAddressBookRights().amendRightsForGroup(securitySession, group, rights, access);
    }

    /**
     * <p>TODO: add a comment here.</p>
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public boolean canAddEmployeeToPerson(final SecuritySession securitySession,
            final PersonDO person) throws SystemException {
        return getAddressBookRights().canAddEmployeeToPerson(securitySession, person);
    }

    /**
     * <p>Find out if a used is allowed to add entries to a given group.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param groupId the unique identifier of the group to check.
     * @return <code>true</code> if the user is entitled to add to the group,
     *     otherwise <code>false</code>.
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public boolean canAddToGroup(final SecuritySession securitySession,
            final GroupDO group) throws SystemException {
        return getAddressBookRights().canAddToGroup(securitySession, group);
    }

    /**
     * <p>TODO: add a comment here.</p>
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public boolean canAmendEmployee(final SecuritySession securitySession,
            final EmployeeDO employeeDO) throws SystemException {
        return getAddressBookRights().canAmendEmployee(securitySession, employeeDO);
    }

    /**
     * <p>Find out if a used is allowed to amend entries in a given group.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param groupId the unique identifier of the group to check.
     * @return <code>true</code> if the user is entitled to amend entries in the
     *      group, otherwise <code>false</code>.
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public boolean canAmendInGroup(final SecuritySession securitySession,
            final GroupDO group) throws SystemException {
        return getAddressBookRights().canAmendInGroup(securitySession, group);
    }

    /**
     * <p>TODO: add a comment here.</p>
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public boolean canRemoveEmployee(final SecuritySession securitySession,
            final EmployeeDO employeeDO) throws SystemException {
        return getAddressBookRights().canRemoveEmployee(securitySession, employeeDO);
    }

    /**
     * <p>Find out if a used is allowed to remove entries from a given group.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param groupId the unique identifier of the group to check.
     * @return <code>true</code> if the user is entitled to remove from the
     *     group, otherwise <code>false</code>.
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public boolean canRemoveFromGroup(final SecuritySession securitySession,
            final GroupDO group) throws SystemException {
        return getAddressBookRights().canRemoveFromGroup(securitySession, group);
    }


    /**
     * <p>Internal helper method. Find out if a user is allowed to access
     * entries in a given group.</p>
     *
     * @param userName the name of the user to check the user rights for.
     * @param groupId the unique identifier of the group to check.
     * @param access the access level as defined in {@link
     *      com.ivata.groupware.business.addressbook.person.group.right.RightConstants
     *      RightConstants}.
     * @return <code>true</code> if the user is entitled to access entries in the
     *      group, otherwise <code>false</code>.
     */
    public boolean canUser(final SecuritySession securitySession,
            final GroupDO group,
            final Integer access) throws SystemException {
        return getAddressBookRights().canUser(securitySession, group, access);
    }

    /**
     * <p>Called by the container to notify an entity object it has been
     * activated.</p>
     */
    public void ejbActivate() {}

    /**
     * <p>Called by the container just after the bean has been created.</p>
     *
     * @exception CreateException if any error occurs. Never thrown by this
     *     class.
     *
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {}

    /**
     * <p>Called by the container to notify the entity object it will be
     * deactivated. Called just before deactivation.</p>
     */
    public void ejbPassivate() {}

    /**
     * <p>This method is called by the container when the bean is about
     * to be removed.</p>
     *
     * <p>This method will be called after a client calls the <code>remove</code>
     * method of the remote/local home interface.</p>
     *
     * @throws RemoveException if any error occurs. Currently never thrown by
     *     this class.
     */
    public void ejbRemove() {}
    /**
     * <p>Find the unique identifiers of all addressBooks which can be accessed by the
     * group specified, with the access level given.</p>
     *
     * @param groupId unique identifier of the group for which to search for
     *    other groups.
     * @param access the access level as defined in {@link
     *      com.ivata.groupware.business.addressbook.person.group.right.RightConstants
     *      RightConstants}.
     * @return a <code>Collection</code> of <code>Integer</code> instances,
     *      matching all groups which can be access with this level of access
     *      by the group specified.
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public Collection findAddressBooksByGroupAccess(final SecuritySession securitySession,
            final GroupDO group,
            final Integer access) throws SystemException {
        return getAddressBookRights().findAddressBooksByGroupAccess(securitySession, group, access);
    }

    /**
     * <p>Find groups which have <code>access</code> to group.
     * Return only those groups which can be see by that user.</p>
     *
     * @param userName user which is trying find rights
     * @param id of group which we are interesting
     * @param access find rights with this access
     * @return Collection of IDS of groups which have <code>access</code> to that group
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public Collection findRightsForGroup(final SecuritySession securitySession,
            final GroupDO group,
            final Integer access) throws SystemException {
        return getAddressBookRights().findRightsForGroup(securitySession, group, access);
    }
    /**
     * Get the addressbook implementation from the <code>PicoContainer</code>.
     */
    private AddressBookRights getAddressBookRights()
            throws SystemException {
        PicoContainer container = PicoContainerFactory.getInstance()
            .getGlobalContainer();
        return (AddressBookRights) container.getComponentInstance(AddressBookRights.class);
    }

    /**
     * <p>Set up the context for this entity object. The session bean stores the
     * context for later use.</p>
     *
     * @param sessionContext the new context which the session object should
     *     store.
     */
    public final void setSessionContext(final SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }
}
