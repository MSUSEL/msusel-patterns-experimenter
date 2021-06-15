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
package com.ivata.groupware.business.addressbook;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.cfg.Configuration;

import com.ivata.groupware.admin.AdminTestCase;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.SettingsImpl;
import com.ivata.groupware.business.addressbook.address.AddressDO;
import com.ivata.groupware.business.addressbook.address.country.CountryDO;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.person.employee.EmployeeDO;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.groupware.business.addressbook.right.AddressBookRightsImpl;
import com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressConstants;
import com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO;
import com.ivata.groupware.container.persistence.hibernate.HibernateManager;
import com.ivata.mask.DefaultMaskFactory;
import com.ivata.mask.field.DefaultFieldValueConvertorFactory;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationErrors;

/**
 * <p>
 * Unit test for the address book implementation.
 * </p>
 *
 * @see AddressBookImpl
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Jun 2, 2004
 * @version $Revision: 1.4 $
 */
public class AddressBookImplTest extends AdminTestCase {
    // TODO: remove hard-coded value here!
    private final static String DEFAULT_GROUP_ID = "7";
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

//        hibernateConfiguration.configure(new File("src/test/hibernate.cfg.xml"));
        return hibernateConfiguration;
    }

    /**
     * <p>
     * This is the implementation instance which will be tested.
     * </p>
     */
    private AddressBookImpl addressBook;
    GroupDO defaultGroup;
    /**
     * <p>
     * If a test group needs to be created, it is created here.
     * </p>
     */
    List newGroups = new ArrayList();

    /**
     * <p>
     * If a test person needs to be created, (s)he is created here.
     * </p>
     */
    List newPeople = new ArrayList();

    /**
     * Constructor for AddressBookImplTest.
     * @param arg0
     */
    public AddressBookImplTest(String arg0) throws HibernateException {
        super(arg0);
    }


    /**
     * <p>
     * Private helper. Create a test group.
     * </p>
     *
     * @param number number of the group to be created - just use 0 if you're
     * not sure.
     */
    private GroupDO createTestGroup(final GroupDO parent,
            final int number) throws SystemException {
        SecuritySession securitySession = getSecuritySession();

        PersonDO groupHead = createTestPerson(number);
        groupHead.setFileAs("head of group " + number);
        addressBook.amendPerson(securitySession, groupHead);

        Set groupPeople = new HashSet();
        groupPeople.add(groupHead);

        GroupDO group = new GroupDO();
        // get the default address book group
        group.setDescription("test description " + number);
        group.setParent(parent);
        group.setPeople(groupPeople);
        group.setName("test group " + number);
        group.setHead(groupHead);

        group = addressBook.addGroup(getSecuritySession(), group);
        assertNotNull(group.getId());
        group = addressBook.findGroupByPrimaryKey(securitySession, group.getId());
        newGroups.add(group);

        return group;
    }


    /**
     * <p>
     * Private helper. Create a test person.
     * </p>
     *
     * @param number number of the person to be created - just use 0 if you're
     * not sure.
     */
    private PersonDO createTestPerson(final int number) throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        PersonDO person = new PersonDO();
        person.setUser(null);
        person.setGroup(defaultGroup);
        person.setCompany("test company " + number);
        person.setFileAs("file as " + number);
        person.setFirstNames("first names " + number);
        person.setLastName("last name " + number);
        person.setSalutation("Mr " + number);

        person = addressBook.addPerson(getSecuritySession(), person);
        assertNotNull(person.getId());
        
        //person = addressBook.findPersonByPrimaryKey(securitySession, person.getId());
        /*
         * QualitaCorpus.class: we removed the line above
         * due to type error. We replace it with the following
         * statement:
         */
        person = addressBook.findPersonByPrimaryKey(securitySession, person.getIdString());
        
        newPeople.add(person);

        return person;
    }
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected synchronized void setUp() throws Exception {
        super.setUp();
        SecuritySession securitySession = getSecuritySession();
        addressBook = new AddressBookImpl(getHibernateManager(),
                new DefaultMaskFactory(new DefaultFieldValueConvertorFactory()),
                new AddressBookRightsImpl(getHibernateManager()),
                new SettingsImpl(getHibernateManager()));
        //defaultGroup = addressBook.findGroupByPrimaryKey(securitySession, DEFAULT_GROUP_ID);
        /*
         * QualitaCorpus.class: we removed the line above
         * due to type error. We replace it with the following
         * statement:
         */
        defaultGroup = addressBook.findGroupByPrimaryKey(securitySession, new Integer(DEFAULT_GROUP_ID));
        
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        SecuritySession securitySession = getSecuritySession();

        // if there was a group created, remove!
        Iterator groupIterator = newGroups.iterator();
        while(groupIterator.hasNext()) {
            GroupDO group = (GroupDO) groupIterator.next();
            if (group.getId() == null) {
                throw new NullPointerException("Group "
                    + group
                    + " has a null id");
            } else {
                addressBook.removeGroup(securitySession, group.getId());
            }
        }
    }

    public void testAddAddressBook() {
    }

    /**
     * <p>
     * Test the <code>addGroup</code> method. A group is added to the default
     * public group and, if successful, deleted again afterwards.
     * </p>
     *
     * @see AddressBookImpl#addGroup
     * @throws SystemException
     */
    public void testAddGroup() throws SystemException {
        SecuritySession securitySession = getSecuritySession();

        // add 3 groups - a parend and two sub-groups
        int totalGroups = 3;
        GroupDO parentGroup = defaultGroup;
        Set groupPeople = new HashSet();

        for (int i = 0; i < totalGroups; ++i) {
            GroupDO group = createTestGroup(parentGroup, i);

            assertNotNull(group.getCreated());
            assertNotNull(group.getModified());
            assertNotNull(group.getCreatedBy());
            assertNotNull(group.getModifiedBy());
            assertEquals(group.getCreated(), group.getModified());
            assertEquals(group.getCreatedBy(), group.getModifiedBy());
            assertEquals("test description " + i, group.getDescription());
            assertEquals("test group " + i, group.getName());
            assertNotNull(group.getHead());
            assertTrue(group.getPeople().size() == 1);
            assertTrue(group.getPeople().contains(group.getHead()));

            // first group is parent of the others
            if (i==0) {
                parentGroup = group;
            }
        }
    }

    /**
     * <p>
     * Test the <code>addPerson</code> method. A person is added and, if
     * successful, deleted again afterwards.
     * </p>
     *
     * @see AddressBookImpl#addPerson
     * @throws SystemException
     */
    public void testAddPerson() throws SystemException {
        // get the default address book group
        SecuritySession securitySession = getSecuritySession();


        PersonDO person = createTestPerson(0);
        assertNotNull(person.getCreated());
        assertNotNull(person.getModified());
        assertNotNull(person.getCreatedBy());
        assertNotNull(person.getModifiedBy());
        assertEquals(person.getCreated(), person.getModified());
        assertEquals(person.getCreatedBy(), person.getModifiedBy());
        assertEquals("test company 0", person.getCompany());
        assertEquals("file as 0", person.getFileAs());
        assertEquals("first names 0", person.getFirstNames());
        assertEquals("last name 0", person.getLastName());
        assertEquals("Mr 0", person.getSalutation());
    }

    public void testAddUserGroup() {
        // TODO
    }

    public void testAmendGroup() throws SystemException {
        SecuritySession securitySession = getSecuritySession();

        // first test the basic group fields
        GroupDO group = createTestGroup(defaultGroup.getParent(), 0);
        group.setDescription("new test description");
        group.setName("new name");

        //String id = group.getId();
        /*
         * QualitaCorpus.class: we removed the line above
         * due to type error. We replace it with the following
         * statement:
         */
        Integer id = group.getId();
        
        
        group = addressBook.amendGroup(securitySession, group);
        assertEquals(id, group.getId());
        group = addressBook.findGroupByPrimaryKey(securitySession, id);
        assertEquals("new test description", group.getDescription());
        assertEquals("new name", group.getName());

        Set people = new HashSet();
        PersonDO person0 = createTestPerson(0);
        people.add(person0);
        group.setHead(person0);
        group.setPeople(people);
        group = addressBook.amendGroup(securitySession, group);
        group = addressBook.findGroupByPrimaryKey(securitySession, id);
        assertEquals(person0, group.getHead());
        assertTrue(group.getPeople().size() == 1);
        assertTrue(group.getPeople().contains(person0));

        // TODO: amend a user group too!
    }

    public void testAmendPerson() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        HibernateManager hibernateManager = getHibernateManager();
        PersistenceSession persistenceSession = hibernateManager.openSession(securitySession);
        try {
            // get a test person to play with!
            PersonDO person = createTestPerson(0);

            // set/test the basic things here
            person.setCompany("new company");
            person.setFileAs("another file as");
            person.setFirstNames("new first names");
            person.setJobTitle("another title");
            person.setLastName("another last name");
            person.setSalutation("Mister");
            //String id = person.getId();
            /*
             * QualitaCorpus.class: we removed the line above
             * due to type error. We replace it with the following
             * statement:
             */
            String id = person.getIdString();

            person = addressBook.amendPerson(securitySession, person);
            assertEquals(id, person.getId());
            person = addressBook.findPersonByPrimaryKey(securitySession, id);
            assertEquals("new company", person.getCompany());
            assertEquals("another file as", person.getFileAs());
            assertEquals("new first names", person.getFirstNames());
            assertEquals("another title", person.getJobTitle());
            assertEquals("another last name", person.getLastName());
            assertEquals("Mister", person.getSalutation());

            // countries - for addresses and employee record
            CountryDO unitedKingdom = (CountryDO) hibernateManager.findInstance(persistenceSession,
                "addressBookCountryByCode",
                new Object[]{"UK"});
            CountryDO slovakia = (CountryDO) hibernateManager.findInstance(persistenceSession,
                "addressBookCountryByCode",
                new Object[]{"SK"});

            // addresses
            AddressDO address = new AddressDO();
            address.setCountry(slovakia);
            address.setPostCode("346436");
            address.setRegion("Niekeho");
            address.setStreetAddress("Sturova 47");
            address.setTown("Banska Bystrica");
            person.setAddress(address);
            addressBook.amendPerson(securitySession, person);
            person = addressBook.findPersonByPrimaryKey(securitySession, id);
            assertNotNull(person.getAddress());
            address = person.getAddress();
            assertEquals(slovakia, address.getCountry());
            assertEquals("346436", address.getPostCode());
            assertEquals("Niekeho", address.getRegion());
            assertEquals("Sturova 47", address.getStreetAddress());
            assertEquals("Banska Bystrica", address.getTown());

            // date of birth
            Date now = new Date();
            person.setDateOfBirth(now);
            person = addressBook.amendPerson(securitySession, person);
            person = addressBook.findPersonByPrimaryKey(securitySession, id);
            assertEquals(now, person.getDateOfBirth());

            // employee
            EmployeeDO employee = new EmployeeDO();
            employee.setCountry(unitedKingdom);
            employee.setNumber("pp12344667");
            employee.setVacationDays(new Integer(22));
            person.setEmployee(employee);
            person = addressBook.amendPerson(securitySession, person);
            person = addressBook.findPersonByPrimaryKey(securitySession, id);
            assertNotNull(person.getEmployee());
            assertEquals(employee, person.getEmployee());
            assertEquals(unitedKingdom, person.getEmployee().getCountry());
            assertEquals("pp12344667", person.getEmployee().getNumber());
            assertEquals(new Integer(22), person.getEmployee().getVacationDays());

            // change the group
            GroupDO newGroup = createTestGroup(defaultGroup, 99);
            person.setGroup(newGroup);
            person = addressBook.amendPerson(securitySession, person);
            person = addressBook.findPersonByPrimaryKey(securitySession, id);
            assertNotNull(person.getGroup());
            assertEquals(newGroup, person.getGroup());

            // add some telecom addresses
            // needs to come first - you need an email address for a user!!
            Set telecomAddresses = new HashSet();
            TelecomAddressDO telecomAddress = new TelecomAddressDO();
            telecomAddress.setAddress("(020)12334545");
            telecomAddress.setType(TelecomAddressConstants.TYPE_HOME);
            telecomAddress.setNumber(0);
            telecomAddresses.add(telecomAddress);
            telecomAddress = new TelecomAddressDO();
            telecomAddress.setAddress("nobody@home.com");
            telecomAddress.setType(TelecomAddressConstants.TYPE_EMAIL);
            telecomAddress.setNumber(1);
            telecomAddresses.add(telecomAddress);
            person.setTelecomAddresses(telecomAddresses);
            person = addressBook.amendPerson(securitySession, person);
            person = addressBook.findPersonByPrimaryKey(securitySession, id);
            assertNotNull(person.getTelecomAddresses());
            assertTrue(person.getTelecomAddresses().size() == 2);
            Iterator telecomAddressIterator = person.getTelecomAddresses().iterator();
            while (telecomAddressIterator.hasNext()) {
                telecomAddress = (TelecomAddressDO) telecomAddressIterator.next();
                int number = telecomAddress.getNumber();
                assertTrue((number >= 0) && (number < 2));
                switch (number) {
                    case 0:
                        assertEquals("(020)12334545", telecomAddress.getAddress());
                        assertTrue(telecomAddress.getType() == TelecomAddressConstants.TYPE_HOME);
                        break;
                    case 1:
                        assertEquals("nobody@home.com", telecomAddress.getAddress());
                        assertTrue(telecomAddress.getType() == TelecomAddressConstants.TYPE_EMAIL);
                        break;
                    default:
                        // CAN'T HAPPEN!
                        assertTrue(false);
                        break;
                }
            }

            // change the user
            UserDO user = new UserDO();
            user.setDeleted(false);
            user.setEnabled(true);
            user.setName("test user");
            user.setPassword("test password");
            person.setUser(user);
            person = addressBook.amendPerson(securitySession, person);
            person = addressBook.findPersonByPrimaryKey(securitySession, id);
            assertNotNull(person.getUser());
            assertFalse(user.isDeleted());
            assertTrue(user.isEnabled());
            assertEquals("test user", person.getUser().getName());
            assertEquals("test password", person.getUser().getPassword());
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    public void testFindAddressBooks() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        HibernateManager hibernateManager = getHibernateManager();
        PersistenceSession persistenceSession = hibernateManager.openSession(securitySession);

        try {
            GroupDO privateAddressBook = (GroupDO)
                hibernateManager.findByPrimaryKey(persistenceSession,
                GroupDO.class, new Integer(501));
            List allAddressBooks = addressBook.findAddressBooks(securitySession, true);
            assertTrue(allAddressBooks.size() > 0);
            assertTrue(allAddressBooks.contains(privateAddressBook));
            allAddressBooks = addressBook.findAddressBooks(securitySession, false);
            assertTrue(allAddressBooks.size() > 0);
            assertFalse(allAddressBooks.contains(privateAddressBook));
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    public void testFindAllCountries() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        HibernateManager hibernateManager = getHibernateManager();
        PersistenceSession persistenceSession = hibernateManager.openSession(securitySession);

        try {
            CountryDO germany = (CountryDO) hibernateManager.findInstance(persistenceSession,
                "addressBookCountryByCode",
                new Object[]{"DE"});
            CountryDO slovakia = (CountryDO) hibernateManager.findInstance(persistenceSession,
                "addressBookCountryByCode",
                new Object[]{"SK"});
            CountryDO unitedKingdom = (CountryDO) hibernateManager.findInstance(persistenceSession,
                "addressBookCountryByCode",
                new Object[]{"UK"});

            List allCountries = addressBook.findAllCountries(securitySession);
            assertTrue(allCountries.size() > 0);
            assertTrue(allCountries.contains(germany));
            assertTrue(allCountries.contains(slovakia));
            assertTrue(allCountries.contains(unitedKingdom));
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    public void testFindAllPeopleInGroup() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        PersonDO adminPerson = addressBook.findPersonByUserName(securitySession,
                securitySession.getUser().getName());

        List allPeople = addressBook.findAllPeopleInGroup(securitySession, defaultGroup, null);
        assertTrue(allPeople.size() > 0);
        assertTrue(allPeople.contains(adminPerson));

        allPeople = addressBook.findAllPeopleInGroup(securitySession, defaultGroup, "a");
        assertTrue(allPeople.size() > 0);
        assertTrue(allPeople.contains(adminPerson));
    }

    public void testFindCountryByCode() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        HibernateManager hibernateManager = getHibernateManager();
        PersistenceSession persistenceSession = hibernateManager.openSession(securitySession);
        try {
            CountryDO germany = (CountryDO) hibernateManager.findInstance(persistenceSession,
                "addressBookCountryByCode",
                new Object[]{"DE"});
            CountryDO slovakia = (CountryDO) hibernateManager.findInstance(persistenceSession,
                "addressBookCountryByCode",
                new Object[]{"SK"});
            CountryDO unitedKingdom = (CountryDO) hibernateManager.findInstance(persistenceSession,
                "addressBookCountryByCode",
                new Object[]{"UK"});

            CountryDO germanyCheck = addressBook.findCountryByCode(securitySession, "DE");
            assertNotNull(germanyCheck);
            assertEquals(germany, germanyCheck);
            assertEquals("country.DE", germanyCheck.getName());
            CountryDO slovakiaCheck = addressBook.findCountryByCode(securitySession, "SK");
            assertNotNull(slovakiaCheck);
            assertEquals(slovakia, slovakiaCheck);
            assertEquals("country.SK", slovakiaCheck.getName());
            CountryDO unitedKingdomCheck = addressBook.findCountryByCode(securitySession, "UK");
            assertNotNull(unitedKingdomCheck);
            assertEquals(unitedKingdom, unitedKingdomCheck);
            assertEquals("country.UK", unitedKingdomCheck.getName());
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    public void testFindGroupByPrimaryKey() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        GroupDO checkGroup = addressBook.findGroupByPrimaryKey(securitySession, defaultGroup.getId());
        assertEquals(defaultGroup, checkGroup);
    }

    public void testFindGroupsByParent() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        List children = addressBook.findGroupsByParent(securitySession, defaultGroup.getParent().getId());
        assertTrue(children.size() > 0);
        assertTrue(children.contains(defaultGroup));
    }

    public void testFindPersonalAddressBook() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        HibernateManager hibernateManager = getHibernateManager();
        PersistenceSession persistenceSession = hibernateManager.openSession(securitySession);
        try {
            GroupDO privateAddressBook = (GroupDO)
                hibernateManager.findByPrimaryKey(persistenceSession,
                GroupDO.class, new Integer(501));
            GroupDO checkAddressBook = addressBook.findPersonalAddressBook(securitySession);
            assertEquals(privateAddressBook, checkAddressBook);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    public void testFindPersonByPrimaryKey() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        PersonDO userPerson = addressBook.findPersonByUserName(securitySession,
                securitySession.getUser().getName());
        
        //PersonDO checkPerson = addressBook.findGroupByPrimaryKey(securitySession, userPerson.getId());
        /*
         * QualitaCorpus.class: we removed the line above
         * due to type error. We replace it with the following
         * statement:
         */
        PersonDO checkPerson = addressBook.findPersonByPrimaryKey(securitySession, userPerson.getIdString());
        
        assertEquals(userPerson, checkPerson);
    }

    public void testFindUserGroups() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        HibernateManager hibernateManager = getHibernateManager();
        PersistenceSession persistenceSession = hibernateManager.openSession(securitySession);
        try {
            List userGroups = addressBook.findUserGroups(securitySession, true);

            // add in the private user group to compare all user groups
            GroupDO privateUserGroup = (GroupDO)
                hibernateManager.findByPrimaryKey(persistenceSession,
                GroupDO.class, new Integer(500));
            assertTrue(userGroups.size() > 0);
            userGroups.add(privateUserGroup);
            assertTrue(userGroups.containsAll(securitySession.getUser().getGroups()));
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /*
     * Test for ValidationErrors validate(SecuritySession, AddressDO)
     */
    public void testValidateSecuritySessionAddressDO() throws SystemException {
        SecuritySession securitySession = getSecuritySession();
        HibernateManager hibernateManager = getHibernateManager();
        PersistenceSession persistenceSession = hibernateManager.openSession(securitySession);
        try {
            AddressDO testAddress = new AddressDO();
            ValidationErrors validationErrors = addressBook.validate(securitySession,
                testAddress);
            assertNotNull(validationErrors);
            assertFalse(validationErrors.isEmpty());

            /* TODO: need to convert these old-style errors
            assertTrue(validationErrors.contains(new ValidationError("errors.required",
                        new ValidationField("address.field.country",
                        AddressBook.BUNDLE_PATH))));
            assertTrue(validationErrors.contains(new ValidationError("errors.required",
                        new ValidationField("address.field.streetAddress",
                        AddressBook.BUNDLE_PATH))));
            assertTrue(validationErrors.contains(new ValidationError("errors.required",
                        new ValidationField("address.field.town",
                        AddressBook.BUNDLE_PATH))));
            */

            CountryDO germany = (CountryDO) hibernateManager.findInstance(persistenceSession,
                "addressBookCountryByCode",
                new Object[]{"DE"});
            testAddress.setCountry(germany);
            testAddress.setStreetAddress("nothing");
            testAddress.setTown("test");

            validationErrors = addressBook.validate(securitySession,
                        testAddress);
            assertTrue((validationErrors == null) || validationErrors.isEmpty());
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /*
     * Test for ValidationErrors validate(SecuritySession, GroupDO)
     */
    public void testValidateSecuritySessionGroupDO() throws SystemException {
        SecuritySession securitySession = getSecuritySession();

        GroupDO testGroup = new GroupDO();
        ValidationErrors validationErrors = addressBook.validate(securitySession,
            testGroup);
        assertNotNull(validationErrors);
        assertFalse(validationErrors.isEmpty());

        /* TODO: need to convert these old-style errors
        assertTrue(validationErrors.contains(new ValidationError("errors.required",
                    new ValidationField("group.field.name",
                    AddressBook.BUNDLE_PATH))));
        */

        testGroup.setName("test");
        validationErrors = addressBook.validate(securitySession,
            testGroup);
        assertTrue((validationErrors == null) || validationErrors.isEmpty());
    }

    /*
     * Test for ValidationErrors validate(SecuritySession, PersonDO)
     */
    public void testValidateSecuritySessionPersonDO() {
        SecuritySession securitySession = getSecuritySession();

        PersonDO testPerson = new PersonDO();
        // user errors only show if the persoon is a user
        testPerson.setUser(new UserDO());
        ValidationErrors validationErrors = addressBook.validate(securitySession,
            testPerson);
        assertNotNull(validationErrors);
        assertFalse(validationErrors.isEmpty());

        /* TODO: need to convert these old-style errors
        assertTrue(validationErrors.contains(new ValidationError("errors.required",
                    new ValidationField("personDetails.field.fileAs",
                    AddressBook.BUNDLE_PATH))));
        assertTrue(validationErrors.contains(new ValidationError("errors.required",
                    new ValidationField("personDetails.field.firstNames",
                    AddressBook.BUNDLE_PATH))));
        assertTrue(validationErrors.contains(new ValidationError("errors.required",
                    new ValidationField("personDetails.field.lastName",
                    AddressBook.BUNDLE_PATH))));
        assertTrue(validationErrors.contains(new ValidationError("errors.required",
                    new ValidationField("personDetails.field.addressBookName",
                    AddressBook.BUNDLE_PATH))));
        //  this user has no email address
        assertTrue(validationErrors.contains(new ValidationError("errors.required",
                    new ValidationField("user.field.requiredEmailAddress",
                    AddressBook.BUNDLE_PATH))));
        assertTrue(validationErrors.contains(new ValidationError("errors.required",
                    new ValidationField("user.field.userName",
                    AddressBook.BUNDLE_PATH))));
        */


        testPerson.setFileAs("test");
        testPerson.setFirstNames("test");
        testPerson.setLastName("test");
        testPerson.setGroup(defaultGroup);
        testPerson.getUser().setName("test");
        Set telecomAddresses = new HashSet();
        TelecomAddressDO emailAddress = new TelecomAddressDO();
        emailAddress.setAddress("test@test.com");
        emailAddress.setType(TelecomAddressConstants.TYPE_EMAIL);
        telecomAddresses.add(emailAddress);
        testPerson.setTelecomAddresses(telecomAddresses);

        validationErrors = addressBook.validate(securitySession,
            testPerson);
        assertTrue((validationErrors == null) || validationErrors.isEmpty());
    }

}