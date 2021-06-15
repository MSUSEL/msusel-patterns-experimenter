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
package com.ivata.groupware.admin;

import java.io.File;
import java.util.Properties;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.cfg.Configuration;

import com.ivata.groupware.GroupwareTestCase;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Jun 7, 2004
 * @version $Revision: 1.2 $
 */
public abstract class AdminTestCase extends GroupwareTestCase {

    /**
     * @param hibernateConfiguration
     * @param arg0
     */
    public AdminTestCase(String arg0) throws HibernateException {
        super(getHibernateConfiguration(), arg0);
    }

    /**
     * <p>
     * Helper called by the constructor to create a valid hibernate
     * configuration for all the address book's functionality.
     * </p>
     *
     * @return valid hibernate configuration.
     * @throws HibernateException
     */
    private static Configuration getHibernateConfiguration() throws HibernateException {
        Configuration hibernateConfiguration = new Configuration();
        hibernateConfiguration.addFile(new File("../hibernate/target/xdoclet/hibernatedoclet/com/ivata/groupware/admin/setting/SettingDO.hbm.xml"));
        hibernateConfiguration.addFile(new File("../hibernate/target/xdoclet/hibernatedoclet/com/ivata/groupware/admin/security/user/UserDO.hbm.xml"));
        hibernateConfiguration.addFile(new File("../hibernate/target/xdoclet/hibernatedoclet/com/ivata/groupware/business/addressbook/address/AddressDO.hbm.xml"));
        hibernateConfiguration.addFile(new File("../hibernate/target/xdoclet/hibernatedoclet/com/ivata/groupware/business/addressbook/address/country/CountryDO.hbm.xml"));
        hibernateConfiguration.addFile(new File("../hibernate/target/xdoclet/hibernatedoclet/com/ivata/groupware/business/addressbook/person/PersonDO.hbm.xml"));
        hibernateConfiguration.addFile(new File("../hibernate/target/xdoclet/hibernatedoclet/com/ivata/groupware/business/addressbook/person/group/right/RightDO.hbm.xml"));
        hibernateConfiguration.addFile(new File("../hibernate/target/xdoclet/hibernatedoclet/com/ivata/groupware/business/addressbook/person/group/right/detail/RightDetailDO.hbm.xml"));
        hibernateConfiguration.addFile(new File("../hibernate/target/xdoclet/hibernatedoclet/com/ivata/groupware/business/addressbook/person/group/GroupDO.hbm.xml"));
        hibernateConfiguration.addFile(new File("../hibernate/target/xdoclet/hibernatedoclet/com/ivata/groupware/business/addressbook/person/employee/EmployeeDO.hbm.xml"));
        hibernateConfiguration.addFile(new File("../hibernate/target/xdoclet/hibernatedoclet/com/ivata/groupware/business/addressbook/telecomaddress/TelecomAddressDO.hbm.xml"));
        hibernateConfiguration.addFile(new File("../hibernate/target/xdoclet/hibernatedoclet/com/ivata/groupware/navigation/menu/MenuDO.hbm.xml"));
        hibernateConfiguration.addFile(new File("../hibernate/target/xdoclet/hibernatedoclet/com/ivata/groupware/navigation/menu/item/MenuItemDO.hbm.xml"));

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        hibernateProperties.setProperty("hibernate.connection.URL", "jdbc:postgresql://localhost:5432/portal");
        hibernateProperties.setProperty("hibernate.connection.username", "postgres");
        hibernateProperties.setProperty("hibernate.connection.password", "");
        hibernateProperties.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateConfiguration.setProperties(hibernateProperties);

        return hibernateConfiguration;
    }


}
