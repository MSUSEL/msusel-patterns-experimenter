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
package com.ivata.groupware.admin.setting.right;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.util.SystemException;


/**
 * <p>This session bean manages rigths for changing settings.</p>
 *
 * @since 2003-02-11
 * @author Peter Illes
 * @version $Revision: 1.3 $
 *
 * @ejb.bean
 *      name="SettingsRights"
 *      display-name="SettingRights"
 *      type="Stateless"
 *      view-type="both"
 *      local-jndi-name="SettingsRightsLocal"
 *      jndi-name="SettingsRightsRemote"
 *
 *
 *  @ejb.home
 *      generate="false"
 *      remote-class="com.ivata.groupware.admin.setting.right.SettingsRightsRemoteHome"
 *
 *  @ejb.interface
 *      remote-class="com.ivata.groupware.admin.setting.right.SettingsRightsRemote"
 */
public class SettingsRightsBean implements SessionBean {


    /**
     * <p>Provides the session bean with container-specific information.</p>
     */
    SessionContext sessionContext;

    /**
     * <p>this method sets a setting to be allowed for users to override the
     * system value</p>, {see canAmendSetting(name)}
     * @param name the name of the setting
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public void addAmendRightForSetting(final SecuritySession securitySession,
            final String settingName)
            throws SystemException {
        getSettingsRights().addAmendRightForSetting(securitySession, settingName);
    }

    /**
     * <p>the method finds out whether a setting can be changed (overriden) by
     * a user</p>
     * @param name the name of the setting
     * @return <code>true</code> when this setting can be overridden by user,
     * <code>false</code> otherwise
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public boolean canAmendSetting(final SecuritySession securitySession,
            final String settingName)
            throws SystemException {
        return getSettingsRights().canAmendSetting(securitySession, settingName);
    }

    /**
     * <p>the method tells whether a user can amend system settings</p>
     * @param userName the name of the user
     * @return <code>true</code> when this user can amend system settings or
     * <code>false</code> when he can't
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public boolean canAmendSystemSettings(final SecuritySession securitySession)
            throws SystemException {
        return getSettingsRights().canAmendSystemSettings(securitySession);
    }

    /**
     * <p>Called by the container to notify an entity object it has been
     * activated.</p>
     */
    public void ejbActivate() {}

    /**
     * <p>Called by the container just after the bean has been created.</p>
     *
     * @throws CreateException if any error occurs. Never thrown by this class.
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
     * Get the settings rights implementation.
     *
     * @return valid settings rights implementation.
     */
    private SettingsRights getSettingsRights() throws SystemException {
        PicoContainer container = PicoContainerFactory.getInstance()
            .getGlobalContainer();
        return (SettingsRights) container.getComponentInstance(SettingsRights.class);
    }

    /**
     * <p>this method disables overriding the system value of one setting,
     * {see canAmendSetting(name)}
     * @param name the name of the setting
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public void removeAmendRightForSetting(final SecuritySession securitySession,
            final String settingName)
            throws SystemException {
        getSettingsRights().removeAmendRightForSetting(securitySession, settingName);
    }

    /**
     * <p>Set up the context for this entity object. The session bean stores the
     * context for later use.</p>
     *
     * @param sessionContext the new context which the session object should store.
     */
    public final void setSessionContext(final SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }
}
