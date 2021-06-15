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

import java.util.Collection;
import java.util.Iterator;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.setting.SettingDO;
import com.ivata.groupware.business.BusinessLogic;
import com.ivata.groupware.business.addressbook.person.group.GroupConstants;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.groupware.business.addressbook.person.group.right.RightConstants;
import com.ivata.groupware.business.addressbook.person.group.right.RightDO;
import com.ivata.groupware.business.addressbook.person.group.right.detail.RightDetailDO;
import com.ivata.groupware.container.persistence.QueryPersistenceManager;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.SystemException;


/**
 * <p>This session bean manages rigths for changing settings.</p>
 *
 * @since 2003-02-11
 * @author Peter Illes
 * @version $Revision: 1.4 $
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
public class SettingsRightsImpl extends BusinessLogic implements SettingsRights {
    private QueryPersistenceManager persistenceManager;
    public SettingsRightsImpl(QueryPersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

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
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // get the system setting - we always refer to system setting, as
            // they can't be removed
            SettingDO setting = (SettingDO)
                persistenceManager.findInstance(persistenceSession,
                "adminSettingByName", new Object [] {settingName});
            Integer settingId = setting.getId();

            // detail with meaning amend setting - user level
            RightDetailDO amendDetail = (RightDetailDO)
                    persistenceManager.findByPrimaryKey(persistenceSession,
                    RightDetailDO.class, RightConstants.DETAIL_SETTING_USER);

            Collection amendRight =
                persistenceManager.find(persistenceSession,
                            "rightByAccessDetailTargetId",
                            new Object [] {RightConstants.ACCESS_AMEND,
                                RightConstants.DETAIL_SETTING_USER,
                                settingId});

            // if there were no such rights set for this setting, do it now
            if (amendRight.isEmpty()) {
                GroupDO userGroup = (GroupDO)
                    persistenceManager.findByPrimaryKey(persistenceSession,
                    GroupDO.class, GroupConstants.USER_GROUP);

                RightDO right = new RightDO();
                right.setAccess(RightConstants.ACCESS_AMEND);
                right.setDetail(amendDetail);

                right.setGroup(userGroup);
                right.setTargetId(settingId);

                persistenceManager.add(persistenceSession, right);
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
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
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // get the system setting - we always refer to system setting, as
            // they can't be removed
            SettingDO setting = (SettingDO)
                persistenceManager.findInstance(persistenceSession,
                "adminSettingByName", new Object [] {settingName});
            Integer settingId = setting.getId();


            Collection amendRight =
                persistenceManager.find(persistenceSession,
                            "rightByAccessDetailTargetId",
                            new Object [] {RightConstants.ACCESS_AMEND,
                                RightConstants.DETAIL_SETTING_USER,
                                settingId});

            // if there were rights, they must be ours
            return !amendRight.isEmpty();
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
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
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            Collection targetIds = persistenceManager.find(persistenceSession,
                "rightByUserNameAccessDetail",
                new Object []
                {
                    securitySession.getUser().getName(),
                    RightConstants.ACCESS_AMEND,
                    RightConstants.DETAIL_SETTING_SYSTEM
                });

            // if the collection is nonEmpty, the user has right to change
            // system settings
            return !targetIds.isEmpty();
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
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
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // get the system setting - we always refer to system setting, as
            // they can't be removed
            SettingDO setting = (SettingDO)
                persistenceManager.findInstance(persistenceSession,
                "adminSettingByName", new Object [] {settingName});
            Integer settingId = setting.getId();

            // detail with meaning amend setting - user level
            RightDetailDO amendDetail = (RightDetailDO)
                    persistenceManager.findByPrimaryKey(persistenceSession,
                    RightDetailDO.class, RightConstants.DETAIL_SETTING_USER);
            Collection amendRight =
                persistenceManager.find(persistenceSession,
                            "rightByAccessDetailTargetId",
                            new Object [] {RightConstants.ACCESS_AMEND,
                                RightConstants.DETAIL_SETTING_USER,
                                settingId});

            // remove all rights found, there should be only one though...
            for (Iterator i = amendRight.iterator(); i.hasNext();) {
                RightDO currentRight = (RightDO) i.next();
                persistenceManager.remove(persistenceSession, currentRight);
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
}
