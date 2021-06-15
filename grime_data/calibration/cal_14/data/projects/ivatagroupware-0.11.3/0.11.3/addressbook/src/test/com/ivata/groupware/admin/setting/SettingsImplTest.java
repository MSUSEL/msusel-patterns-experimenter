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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;

import com.ivata.groupware.admin.AdminTestCase;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.container.persistence.hibernate.HibernateManager;
import com.ivata.groupware.container.persistence.hibernate.HibernateSession;
import com.ivata.mask.util.SystemException;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Jun 7, 2004
 * @version $Revision: 1.2 $
 */
public class SettingsImplTest extends AdminTestCase {
    /**
     * <p>
     * This is the implementation instance which will be tested.
     * </p>
     */
    private SettingsImpl settings;

    private List testSettings = new ArrayList();

    /**
     * Constructor for SettingsImplTest.
     * @param arg0
     */
    public SettingsImplTest(String arg0) throws HibernateException {
        super(arg0);
    }

    private SettingDO createStringSetting(final String name) throws SystemException {
        HibernateManager hibernateManager = getHibernateManager();
        HibernateSession hibernateSession = getHibernateSession();

        SettingDO setting = new SettingDO();
        setting.setDescription("any description");
        setting.setEnabled(true);
        setting.setName("name");
        setting.setType(SettingConstants.DATA_TYPE_STRING);
        setting.setUser(null);
        setting = (SettingDO) hibernateManager.add(hibernateSession, setting);
        assertNotNull(setting.getId());
        testSettings.add(setting);
        return setting;
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        SecuritySession securitySession = getSecuritySession();
        settings = new SettingsImpl(getHibernateManager());
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        HibernateManager hibernateManager = getHibernateManager();
        HibernateSession hibernateSession = getHibernateSession();

        Iterator testSettingIterator = testSettings.iterator();
        while (testSettingIterator.hasNext()) {
            SettingDO setting = (SettingDO) testSettingIterator.next();
            hibernateManager.remove(hibernateSession, setting);
        }
    }

    public void testAmendSetting() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        HibernateManager hibernateManager = getHibernateManager();
        HibernateSession hibernateSession = getHibernateSession();

        SettingDO setting = createStringSetting("amend_test");
        settings.amendSetting(securitySession, "amend_test", "new_value", null);
        setting = (SettingDO)
            hibernateManager.findInstance(hibernateSession, "adminSettingName",
            new Object[] {setting.getName()});
        assertEquals("new_value", setting.getValue());
        UserDO user = securitySession.getUser();
        settings.amendSetting(securitySession, "amend_test", "user_value", user);
        SettingDO userSetting = (SettingDO)
            hibernateManager.findInstance(hibernateSession, "adminSettingNameUserName",
            new Object[] {setting.getName(), user.getName()});
        setting = (SettingDO)
            hibernateManager.findInstance(hibernateSession, "adminSettingName",
            new Object[] {setting.getName()});
        // check the general setting hasn't changed!
        assertEquals("user_value", userSetting.getValue());
        // check the general setting hasn't changed!
        assertEquals("new_value", setting.getValue());
    }

    public void testGetBooleanSetting() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        HibernateManager hibernateManager = getHibernateManager();
        HibernateSession hibernateSession = getHibernateSession();

        SettingDO testSetting = createStringSetting("boolean_test");
        testSetting.setType(SettingConstants.DATA_TYPE_BOOLEAN);
        testSetting.setValue("false");
        hibernateManager.amend(hibernateSession, testSetting);

        Boolean test = settings.getBooleanSetting(securitySession, "boolean_test", null);
        assertFalse(test.booleanValue());
    }

    public void testGetIntegerSetting() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        HibernateManager hibernateManager = getHibernateManager();
        HibernateSession hibernateSession = getHibernateSession();

        SettingDO testSetting = createStringSetting("integer_test");
        testSetting.setType(SettingConstants.DATA_TYPE_BOOLEAN);
        testSetting.setValue("202");
        hibernateManager.amend(hibernateSession, testSetting);

        Integer test = settings.getIntegerSetting(securitySession, "integer_test", null);
        assertEquals(new Integer(202), test);
    }

    public void testGetSetting() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        HibernateManager hibernateManager = getHibernateManager();
        HibernateSession hibernateSession = getHibernateSession();

        SettingDO testSetting = createStringSetting("get_test");
        testSetting.setValue("get_test_value");
        hibernateManager.amend(hibernateSession, testSetting);

        Object test = settings.getSetting(securitySession, "get_test", null);
        assertEquals("get_test_value", test);
    }

    public void testGetSettingType() {
    }

    public void testGetStringSetting() {
    }

    public void testIsSettingEnabled() {
    }

    public void testRemoveSetting() {
    }

    public void testValidate() {
    }

}
