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
package com.ivata.groupware.navigation;


import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.groupware.navigation.menu.item.MenuItemDO;
import com.ivata.mask.util.SystemException;


/**
 * <p><code>SessionBean</code> to let you access the menues and folders in
 * the system and generally get around.</p>
 *
 * @since 2002-05-07
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @ejb.bean
 *      name="Navigation"
 *      display-name="Navigation"
 *      type="Stateless"
 *      view-type="remote"
 *      jndi-name="NavigationRemote"
 *
 * @ejb.transaction
 *      type = "Required"
 *
 *  @ejb.home
 *      generate="false"
 *      remote-class="com.ivata.groupware.navigation.NavigationRemoteHome"
 *
 *  @ejb.interface
 *      remote-class="com.ivata.groupware.navigation.NavigationRemote"
 */
public class NavigationBean implements SessionBean, Navigation {


    /**
     * <p>Provides the session bean with container-specific information.</p>
     */
    SessionContext sessionContext;

    /**
     * <p>Add a new menu item, with no image associated with it initially.</p>
     *
     * @param menuId the unique identifier of the menu into which the new item
     * will be inserted.
     * @param text human-readable english language text for the menu item.
     * Should be unique within the menu it is in though this is not enforced
     * server-side.
     * @param URL the <code>URL</code> the new menu item links to.
     * @param userName the user for whom to insert the new menu item, or
     * <code>null</code> if everyone should see it.
     *
     * @ejb.interface-method
     *      view-type="remote"
     */
    public void addMenuItem(final SecuritySession securitySession,
            final MenuItemDO menuItem)
            throws SystemException {
        getNavigation().addMenuItem(securitySession, menuItem);
    }

    /**
     * <p>changes a menu item, if it belongs to the given user</p>
     *
     * @param menuItemId the unique identifier of the menu item to change.
     * @param text human-readable english language text for the menu item.
     * Should be unique within the menu it is in though this is not enforced
     * server-side.
     * @param URL the <code>URL</code> the new menu item links to.
     * @param userName the user for whom the menu item should belong
     *
     * @ejb.interface-method
     *      view-type="remote"
     */
    public void amendMenuItem(final SecuritySession securitySession,
            final MenuItemDO menuItem)
            throws SystemException {
        getNavigation().addMenuItem(securitySession, menuItem);
    }

    /**
     * <p>Called by the container to notify a stateful session object it has been
     * activated.</p>
     */
    public void ejbActivate() {}

    /**
     * <p>Provides the session bean with container-specific information.</p>
     *
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {}

    /**
     * <p>Called by the container to notify a stateful session object it will be
     * deactivated. Called just before deactivation.</p>
     */
    public void ejbPassivate() {}

    /**
     * <p>This method is called by the container when the session bean is about
     * to be removed.</p>
     *
     * <p>This method will be called after a client calls the <code>remove</code>
     * method of the remote/local home interface.</p>
     */
    public void ejbRemove() {}

    /**
     * <p>Find all the menues for a given user.</p>
     *
     * @param userName the user to search for
     * @return a <code>Collection</code>Containing all the user's menues, as
     * instances of {@link com.ivata.groupware.menu.MenuDO MenuDO}
     * @see com.ivata.groupware.menu.MenuLocalHome#findByUserName( String sUserName )
     * @throws EJBException if there is a <code>FinderException</code> calling
     * <code>MenuLocalHome.findByUserName</code>
     * @throws EJBException if there is a <code>NamingException</code> setting
     * looking up the MenuHome
     *
     * @ejb.interface-method
     *      view-type="remote"
     */
    public Collection findMenues(final SecuritySession securitySession)
            throws SystemException {
        return getNavigation().findMenues(securitySession);
    }
    /**
     * Get the navigation implementation.
     *
     * @return valid security implementation.
     */
    private Navigation getNavigation() throws SystemException {
        PicoContainer container = PicoContainerFactory.getInstance()
            .getGlobalContainer();
        return (Navigation) container.getComponentInstance(Navigation.class);
    }

    /**
     * <p>removes a menu item, if it belongs to the given user</p>
     *
     * @param menuItemId the unique identifier of the menu item to remove.
     * @param userName the user for whom the menu item should belong
     *
     * @ejb.interface-method
     *      view-type="remote"
     */
    public void removeMenuItem(final SecuritySession securitySession,
            final String id)
            throws SystemException {
        getNavigation().removeMenuItem(securitySession, id);
    }

    /**
     * <p>Provides access to the runtime properties of the context in which this
     * session bean is running.</p>
     *
     * <p>Is usually stored by the bean internally.</p>
     *
     * @param sessionContext new value for the session context. Is usually stored internally
     */
    public final void setSessionContext(final SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }
}
