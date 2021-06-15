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

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.mask.util.SystemException;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Apr 16, 2004
 * @version $Revision: 1.2 $
 */
public interface SettingsRights {
    /**
     * <p>this method sets a setting to be allowed for users to override the
     * system value</p>, {see canAmendSetting(name)}
     * @param name the name of the setting
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public void addAmendRightForSetting(final
        SecuritySession securitySession,
            final String settingName)
        throws SystemException;
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
    public boolean canAmendSetting(final
        SecuritySession securitySession,
            final String settingName)
        throws SystemException;
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
        throws SystemException;
    /**
     * <p>this method disables overriding the system value of one setting,
     * {see canAmendSetting(name)}
     * @param name the name of the setting
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public void removeAmendRightForSetting(final
        SecuritySession securitySession,
            final String settingName)
        throws SystemException;
}