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
package com.ivata.groupware.admin.setting;

import java.util.Locale;
import java.util.Map;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationErrors;


/**
 * <p>This class controls/sets the global preferences. It's might make sense to
 * make it a stateful bean so it can contais a reference to the current
 * personUser</p>
 *
 * <p>This Session Bean has been extended from a class,
 * <code>com.ivata.groupware.admin.settings</code>, in the original JSP intranet
 * prototype.</p>
 *
 * @since 2001-09-06
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @ejb.bean
 *      name="Settings"
 *      display-name="Settings"
 *      type="Stateless"
 *      view-type="both"
 *      local-jndi-name="SettingsLocal"
 *      jndi-name="SettingsRemote"
 *
 * @ejb.transaction
 *      type = "Required"
 *
 *  @ejb.home
 *      generate="false"
 *      remote-class="com.ivata.groupware.admin.setting.SettingsRemoteHome"
 *
 *  @ejb.interface
 *      remote-class="com.ivata.groupware.admin.setting.SettingsRemote"
 */
public class SettingsBean implements SessionBean, Settings {


    /**
     * <p>Provides the session bean with container-specific information.</p>
     */
    SessionContext sessionContext;

    /**
     * <p>
     * Change the value of an existing setting.
     * </p>
     *
     * @param name the name of the setting to set
     * @param value new value to be set.
     * @param user if not <code>null</code>, then the setting for this user
     * is set, otherwise the general setting is changed.
     */
    public void amendSetting(final SecuritySession securitySession,
            final String name,
            final Object value,
            final UserDO user) throws SystemException {
        getSettings().amendSetting(securitySession, name, value, user);
    }

    /**
     * <p>Called by the container to notify a stateful session object it has been
     * activated.</p>
     */
    public void ejbActivate() {}

    /**
     * <p>Mandatory, parameter-less <code>ejbCreate</code> is required by the
     * stateless bean interface.</p>
     *
     * <p>This method will be called after a client calls the <code>create</code>
     * of the remote/local home interface.</p>
     *
     * @ejb.create-method
     */
    public void ejbCreate() {}

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
     * <p>Get a setting of class <code>Boolean</code>.</p>
     *
     * @return the setting if set, or null if not
     * @param name the name of the setting to return the value for
     * @param userDO the user to search for. If null is specified, the
     *     default setting is searched for and returned if found.
     * @exception SettingsDataTypeException if the setting has any class other
     *     than Boolean
     * @return a setting of class <code>Boolean</code> for the setting
     *      name provided.
     * @see #getSetting
     * @see #getIntegerSetting
     * @see #getStringSetting
     * @throws SettingNullException if this setting does not exist.
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public final Boolean getBooleanSetting(final SecuritySession securitySession,
            final String name,
            final UserDO userDO) throws SystemException {
        return getSettings().getBooleanSetting(securitySession, name, userDO);
    }

    /**
     * <p>Get a setting of class <code>Integer</code>.</p>
     *
     * @exception SettingsDataTypeException if the setting has any class other
     *     than Integer.
     * @return the setting if set, or null if not
     * @param name the name of the setting to return the value for.
     * @param userDO the user to search for. If null is specified, the
     *     default setting is searched for and returned if found.
     * @return a setting of class <code>Integer</code> for the setting
     *      name provided.
     * @see #getSetting
     * @see #getStringSetting
     * @see #getBooleanSetting
     * @throws SettingNullException if this setting does not exist.
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public final Integer getIntegerSetting(final SecuritySession securitySession,
            final String name,
            final UserDO userDO) throws SystemException {
        return getSettings().getIntegerSetting(securitySession, name, userDO);
    }

    /**
     * <p>Get a setting for a given user. The class of the returned object will
     * depend on the <code>type</code> field of the EJB with this name and can
     * be one of:</br/>
     * <ul>
     * <li><code>Integer</code></li>
     * <li><code>String</code></li>
     * <li><code>Boolean</code></li></p>
     *
     * @return the setting if set, or null if not
     * @param name the name of the setting to return the value for
     * @param userDO the user to search for. If null is specified, the
     *     default setting is searched for and returned if found.
     * @return a setting with the setting name provided. The type of the
     *     returned object depends on the <code>type</code> field of the
     *     setting.
     * @see #getIntegerSetting
     * @see #getStringSetting
     * @see #getBooleanSetting
     * @throws SettingNullException if this setting does not exist.
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public final Object getSetting(final SecuritySession securitySession,
            final String name,
            final UserDO userDO) throws SystemException {
        return getSettings().getSetting(securitySession, name, userDO);
    }
    /**
     * Get the implementation which does all the hard work.
     */
    private Settings getSettings() throws SystemException {
        PicoContainer container = PicoContainerFactory.getInstance()
            .getGlobalContainer();
        return (Settings) container.getComponentInstance(Settings.class);
    }

    /**
     * <p>Get the type of a setting</p>
     * @param name the name of the setting
     * @return one of the static fields of <code>SettingConstants</code>
     * @throws SettingNullException if this setting does not exist.
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public final int getSettingType(final SecuritySession securitySession,
            final String name) throws SystemException {
        return getSettings().getSettingType(securitySession, name);
    }

    /**
     * <p>Get a setting of class String.</p>
     *
     * @return the setting if set, or null if not
     * @param name the name of the setting to return the value for
     * @param userDO the user to search for. If null is specified, the
     *     default setting is searched for and returned if found.
     * @exception SettingsDataTypeException if the setting has any class other
     *     than String
     * @return a setting of class <code>String</code> for the setting
     *      name provided.
     * @see #getSetting
     * @see #getIntegerSetting
     * @see #getBooleanSetting
     * @throws SettingNullException if this setting does not exist.
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public final String getStringSetting(final SecuritySession securitySession,
            final String name,
            final UserDO userDO) throws SystemException {
        return getSettings().getStringSetting(securitySession, name, userDO);
    }
    /**
     * <p>
     * Find out whether or not a setting is enabled.
     * </p>
     *
     * @param securitySession valid security session.
     * @param name name of the setting to check.
     * @return <code>true</code> if the setting exists and is enabled.
     * @throws SystemException if it don't work out in some way :-)
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public boolean isSettingEnabled(final SecuritySession securitySession,
            final String
            name)
            throws SystemException {
        return getSettings().isSettingEnabled(securitySession, name);
    }

    /**
     * <p>
     * Revert a user setting back to the general value.
     * </p>
     *
     * @param name the name of the setting to revert
     * @param user the setting for this user is reverted
     */
    public void revertSetting(final SecuritySession securitySession,
            final String name,
            final UserDO user) throws SystemException {
        getSettings().revertSetting(securitySession, name, user);
    }

    /**
     * <p>Provides access to the runtime properties of the context in which this
     * session bean is running.</p>
     *
     * <p>Is usually stored by the bean internally.</p>
     *
     * @param sessionContext new value for the session context. Is usually
     *     stored internally
     */
    public final void setSessionContext(final SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    /**
     * <p>Confirm all of the settings passed are correct.</p>
     *
     * @param settings a <code>Map</code> with setting names as keys and setting
     *  values as values
     * @param the <code>Locale</code> to get localised error messages
     * @param settingType one of the constants in <code>SettingConstants</code>:
     *  <code>SETTING_USER</code> or  <code>SETTING_SYSTEM</code>
     * @return a collection of validation errors if any of the settings contains
     *  invalid value.
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public ValidationErrors validate(final SecuritySession securitySession,
            final Map settings,
            final Locale locale,
            final int settingType) throws SystemException {
        return getSettings().validate(securitySession, settings, locale, settingType);
    }

}
