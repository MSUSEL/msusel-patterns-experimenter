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
import java.util.Collections;

import javax.ejb.EJBException;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.BusinessLogic;
import com.ivata.groupware.container.persistence.QueryPersistenceManager;
import com.ivata.groupware.navigation.menu.item.MenuItemDO;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.SystemException;


/**
 * <p>Lets you access the menues and folders in the system and generally get
 * around.</p>
 *
 * @since 2002-05-07
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class NavigationImpl extends BusinessLogic implements Navigation {
    /**
     * Persistence manger used to store/retrieve data objects, or retrieve a
     * new persistence session.
     */
    private QueryPersistenceManager persistenceManager;

    /**
     * Construct and initialize the navigation implementation.
     *
     * @param persistenceManager persistence manager used to store/retrieve data
     *     objects.
     */
    public NavigationImpl(QueryPersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    /**
     * <p>Add a new menu item, with no image associated with it initially.</p>
     *
     * @param userName the user for whom to insert the new menu item, or
     * <code>null</code> if everyone should see it.
     * @param menuId the unique identifier of the menu into which the new item
     * will be inserted.
     * @param text human-readable english language text for the menu item.
     * Should be unique within the menu it is in though this is not enforced
     * server-side.
     * @param URL the <code>URL</code> the new menu item links to.
     *
     * @ejb.interface-method
     *      view-type="remote"
     */
    public void addMenuItem(final SecuritySession securitySession,
            final MenuItemDO menuItem)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            persistenceManager.add(persistenceSession, menuItem);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
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
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            persistenceManager.amend(persistenceSession, menuItem);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>Find all the menues for a given user.</p>
     *
     * @param userName the user to search for
     * @return a <code>Collection</code>Containing all the user's menues, as
     * instances of {@link com.ivata.groupware.menu.MenuDO MenuDO}
     * @see com.ivata.groupware.menu.MenuDOHome#findByUserName( String sUserName )
     * @throws EJBException if there is a <code>FinderException</code> calling
     * <code>MenuDOHome.findByUserName</code>
     * @throws EJBException if there is a <code>NamingException</code> setting
     * looking up the MenuHome
     *
     * @ejb.interface-method
     *      view-type="remote"
     */
    public Collection findMenues(final SecuritySession securitySession)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return Collections.synchronizedCollection(persistenceManager.find(
                    persistenceSession,
                    "navigationMenuByUserNameOrderByPriority",
                    new Object[] {securitySession.getUser().getName()}));
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
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
            final String menuItemId)
            throws SystemException {
        PersistenceSession persistenceSession =
            persistenceManager.openSession(securitySession);
        try {
            persistenceManager.remove(persistenceSession,
                    MenuItemDO.class, menuItemId);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
}
