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
package com.ivata.groupware;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.hibernate.Interceptor;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.server.TestSecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.container.persistence.hibernate.HibernateInterceptor;
import com.ivata.groupware.container.persistence.hibernate.HibernateManager;
import com.ivata.groupware.container.persistence.hibernate.HibernateQueryFactory;
import com.ivata.groupware.container.persistence.hibernate.HibernateSession;
import com.ivata.mask.persistence.right.DefaultPersistenceRights;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Jun 2, 2004
 * @version $Revision: 1.3 $
 */
public class GroupwareTestCase extends TestCase {

    /**
     * <p>
     * Hibernate persistence manager.
     * </p>
     */
    private static HibernateManager hibernateManager;
    /**
     * <p>
     * Hibernate session - used to access hibernate.
     * </p>
     */
    private static HibernateSession hibernateSession;

    /**
     * <p>
     * Fake security session - gives you admin rights.
     * </p>
     */
    private static SecuritySession securitySession;
    /**
     * @return
     */
    protected static HibernateManager getHibernateManager() {
        return hibernateManager;
    }
    /**
     * @return
     */
    protected static HibernateSession getHibernateSession() {
        return hibernateSession;
    }

    /**
     * @return
     */
    protected static SecuritySession getSecuritySession() {
        return securitySession;
    }

    /**
     * <p>
     * Hibernate configuration to use to configure the hibernate session.
     * </p>
     */
    Configuration hibernateConfiguration;

    /**
     * Constructor for AddressBookImplTest.
     * @param arg0
     */
    public GroupwareTestCase(Configuration hibernateConfiguration, String arg0) {
        super(arg0);
        this.hibernateConfiguration = hibernateConfiguration;
    }

    /*
     * @see TestCase#setUp()
     */
    protected synchronized void setUp() throws Exception {
        super.setUp();

        if (hibernateSession == null) {
            // set up the hibernate session
            SessionFactory sessionFactory = hibernateConfiguration.buildSessionFactory();

            Map queryMap = new HashMap();
            Map queryArgumentsMap = new HashMap();

            hibernateManager = new HibernateManager(sessionFactory,
                        new HibernateQueryFactory(queryMap, queryArgumentsMap),
                        new DefaultPersistenceRights());
            Interceptor interceptor = new HibernateInterceptor(hibernateManager,
                    sessionFactory);
            Session wrappedSession = sessionFactory.openSession(interceptor);
            hibernateSession = new HibernateSession(wrappedSession,
                    wrappedSession.beginTransaction(), null);

            // create a fake security session for admin
            UserDO adminUser = (UserDO)hibernateManager.findByPrimaryKey(hibernateSession,
                UserDO.class, new Integer(1));
            securitySession = new TestSecuritySession(adminUser);
        }
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
