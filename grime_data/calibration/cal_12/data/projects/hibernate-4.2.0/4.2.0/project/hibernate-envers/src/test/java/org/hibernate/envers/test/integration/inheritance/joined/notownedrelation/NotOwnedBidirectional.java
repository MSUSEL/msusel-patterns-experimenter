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
package org.hibernate.envers.test.integration.inheritance.joined.notownedrelation;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.tools.TestTools;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class NotOwnedBidirectional extends BaseEnversJPAFunctionalTestCase {
    private Long pc_id;
    private Long a1_id;
    private Long a2_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(Address.class);
        cfg.addAnnotatedClass(Contact.class);
        cfg.addAnnotatedClass(PersonalContact.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        pc_id = 1l;
        a1_id = 10l;
        a2_id = 100l;

        // Rev 1
        em.getTransaction().begin();

        PersonalContact pc = new PersonalContact(pc_id, "e", "f");

        Address a1 = new Address(a1_id, "a1");
        a1.setContact(pc);

        em.persist(pc);
        em.persist(a1);

        em.getTransaction().commit();

        // Rev 2
        em.getTransaction().begin();

        pc = em.find(PersonalContact.class, pc_id);

        Address a2 = new Address(a2_id, "a2");
        a2.setContact(pc);

        em.persist(a2);

        em.getTransaction().commit();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(Contact.class, pc_id));
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(PersonalContact.class, pc_id));

        assert Arrays.asList(1).equals(getAuditReader().getRevisions(Address.class, a1_id));
        assert Arrays.asList(1).equals(getAuditReader().getRevisions(Address.class, a1_id));

        assert Arrays.asList(2).equals(getAuditReader().getRevisions(Address.class, a2_id));
        assert Arrays.asList(2).equals(getAuditReader().getRevisions(Address.class, a2_id));
    }

    @Test
    public void testHistoryOfContact() {
        assert getAuditReader().find(Contact.class, pc_id, 1).getAddresses().equals(
                TestTools.makeSet(new Address(a1_id, "a1")));

        assert getAuditReader().find(Contact.class, pc_id, 2).getAddresses().equals(
                TestTools.makeSet(new Address(a1_id, "a1"), new Address(a2_id, "a2")));
    }

    @Test
    public void testHistoryOfPersonalContact() {
        System.out.println(getAuditReader().find(PersonalContact.class, pc_id, 1).getAddresses());
        assert getAuditReader().find(PersonalContact.class, pc_id, 1).getAddresses().equals(
                TestTools.makeSet(new Address(a1_id, "a1")));

        assert getAuditReader().find(PersonalContact.class, pc_id, 2).getAddresses().equals(
                TestTools.makeSet(new Address(a1_id, "a1"), new Address(a2_id, "a2")));
    }
}