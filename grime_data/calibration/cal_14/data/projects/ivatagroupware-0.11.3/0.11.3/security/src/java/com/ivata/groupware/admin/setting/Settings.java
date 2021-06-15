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

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationErrors;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Apr 3, 2004
 * @version $Revision: 1.2 $
 */
public interface Settings {
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
    void amendSetting(final SecuritySession securitySession,
            final String name,
            final Object value,
            final UserDO user)
            throws SystemException;

    /**
     * <p>Get a setting of class <code>Boolean</code>.</p>
     *
     * @return the setting if set, or null if not
     * @param name the name of the setting to return the value for
     * @param userName the user to search for. If null is specified, the
     *     default setting is searched for and returned if found.
     * @exception SettingsDataTypeException if the setting has any class other
     *     than Boolean
     * @return a setting of class <code>Boolean</code> for the setting
     *      name provided.
     * @see #getSetting
     * @see #getIntegerSetting
     * @see #getStringSetting
     * @throws SettingNullException if this setting does not exist.
     */
    Boolean getBooleanSetting(final SecuritySession securitySession,
            final String name,
            final UserDO userDO)
        throws SystemException;

    /**
     * <p>Get a setting of class <code>Integer</code>.</p>
     *
     * @exception SettingsDataTypeException if the setting has any class other
     *     than Integer.
     * @return the setting if set, or null if not
     * @param name the name of the setting to return the value for.
     * @param userName the user to search for. If null is specified, the
     *     default setting is searched for and returned if found.
     * @return a setting of class <code>Integer</code> for the setting
     *      name provided.
     * @see #getSetting
     * @see #getStringSetting
     * @see #getBooleanSetting
     * @throws SettingNullException if this setting does not exist.
     */
    Integer getIntegerSetting(final SecuritySession securitySession,
            final String name,
            final UserDO userDO)
        throws SystemException;

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
     * @param userName the user to search for. If null is specified, the
     *     default setting is searched for and returned if found.
     * @return a setting with the setting name provided. The type of the
     *     returned object depends on the <code>type</code> field of the
     *     setting.
     * @see #getIntegerSetting
     * @see #getStringSetting
     * @see #getBooleanSetting
     * @throws SettingNullException if this setting does not exist.
     */
    Object getSetting(final SecuritySession securitySession,
            final String name,
            final UserDO userDO)
        throws SystemException;

    /**
     * <p>Get the type of a setting</p>
     * @param name the name of the setting
     * @return one of the static fields of <code>SettingConstants</code>
     * @throws SettingNullException if this setting does not exist.
     */
    int getSettingType(final SecuritySession securitySession,
            final String name)
        throws SystemException;

    /**
     * <p>Get a setting of class String.</p>
     *
     * @return the setting if set, or null if not
     * @param name the name of the setting to return the value for
     * @param userName the user to search for. If null is specified, the
     *     default setting is searched for and returned if found.
     * @exception SettingsDataTypeException if the setting has any class other
     *     than String
     * @return a setting of class <code>String</code> for the setting
     *      name provided.
     * @see #getSetting
     * @see #getIntegerSetting
     * @see #getBooleanSetting
     * @throws SettingNullException if this setting does not exist.
     */
    String getStringSetting(final SecuritySession securitySession,
            final String name,
            final UserDO userDO)
        throws SystemException;

    /**
     * <p>
     * Find out whether or not a setting is enabled.
     * </p>
     *
     * @param securitySession valid security session.
     * @param name name of the setting to check.
     * @return <code>true</code> if the setting exists and is enabled.
     * @throws SystemException if it don't work out in some way :-)
     */
    boolean isSettingEnabled(final SecuritySession securitySession,
            final String
        name)
        throws SystemException;

    /**
     * <p>
     * Revert a user setting back to the general value.
     * </p>
     *
     * @param name the name of the setting to revert
     * @param user the setting for this user is reverted
     */
    void revertSetting(final SecuritySession securitySession,
            final String name,
            final UserDO user)
            throws SystemException;

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
     */
    ValidationErrors validate(final SecuritySession securitySession,
            final Map settings,
            final Locale locale,
            final int settingType)
        throws SystemException;
}