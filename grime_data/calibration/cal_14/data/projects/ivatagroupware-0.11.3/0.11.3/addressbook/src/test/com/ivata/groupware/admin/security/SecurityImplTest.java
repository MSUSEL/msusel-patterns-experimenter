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
package com.ivata.groupware.admin.security;
import net.sf.hibernate.HibernateException;

import com.ivata.groupware.admin.AdminTestCase;
import com.ivata.groupware.admin.security.server.PlainTextSecurityServer;
import com.ivata.groupware.admin.security.server.SecurityServer;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.container.persistence.QueryPersistenceManager;
import com.ivata.mask.DefaultMaskFactory;
import com.ivata.mask.field.DefaultFieldValueConvertorFactory;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.SystemException;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Jun 3, 2004
 * @version $Revision: 1.4 $
 */
public class SecurityImplTest extends AdminTestCase {

    QueryPersistenceManager persistenceManager;
    PersistenceSession persistenceSession;

    PersonDO person = null;

    /**
     * <p>
     * This is the implementation instance which will be tested.
     * </p>
     */
    private SecurityImpl security;

    /**
     * Constructor for SecurityImplTest.
     * @param arg0
     */
    public SecurityImplTest(String arg0)  throws HibernateException {
        super(arg0);
    }
    SecuritySession securitySession;

    /*
     * @see GroupwareTestCase#setUp()
     */
    protected synchronized void setUp() throws Exception {
        super.setUp();
        securitySession = getSecuritySession();
        persistenceManager = getHibernateManager();
        persistenceSession = getHibernateSession();
        SecurityServer server = new PlainTextSecurityServer(persistenceManager);
        //security = new SecurityImpl(persistenceManager, server, new DefaultMaskFactory(), Boolean.FALSE);
        /*
         * QualitaCorpus.class: we removed the line above
         * due to a missing parameter. We replace it with the following
         * statement:
         */
        security = new SecurityImpl(persistenceManager, server, new DefaultMaskFactory(null), Boolean.FALSE);
    }

    /*
     * @see GroupwareTestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        if (person != null) {
            persistenceManager.remove(persistenceSession, person);
        }
    }

    private PersonDO createTestPerson() throws SystemException {
        person = new PersonDO();
        person.setCompany("test company");
        person.setFileAs("file as");
        person.setFirstNames("first names");
        person.setLastName("security test");
        persistenceManager.add(persistenceSession, person);
        return person;
    }


    public void testAddUserToPerson() throws SystemException {
/* TODO
        PersonDO person = createTestPerson();

        security.addUserToPerson(securitySession, person, "security test user", true);

        String id = person.getId();
        person = (PersonDO)
            persistenceManager.findByPrimaryKey(persistenceSession, PersonDO.class,
                    id);
        assertEquals(id, person.getId());
        UserDO user = (UserDO)
            persistenceManager.findInstance(persistenceSession, "adminUserByName",
                    new Object [] {"security test user"});
        assertNotNull(user.getId());
        assertEquals(person.getUser(), user);
        assertEquals("security test user", user.getName());
        assertTrue(user.isEnabled());
        assertFalse(user.isDeleted());
*/
    }

    public void testAmendUserName() throws SystemException {
    }

    public void testCheckPassword() {
    }

    public void testCreatePrivateGroups() {
    }

    public void testEnableUser() {
    }

    public void testFindUserByName() {
    }

    public void testGetSystemUserName() {
    }

    public void testGetUserNameFromSystemUserName() {
    }

    public void testIsUserEnabled() {
    }

    public void testLogin() {
    }

    public void testLoginAgain() {
    }

    public void testLoginGuest() {
    }

    public void testRemoveUser() {
    }

    public void testRestoreUser() {
    }

    public void testSetPassword() {
    }

}