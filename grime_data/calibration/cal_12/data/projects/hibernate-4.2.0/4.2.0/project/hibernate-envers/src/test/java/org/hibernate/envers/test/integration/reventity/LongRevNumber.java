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
package org.hibernate.envers.test.integration.reventity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrTestEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class LongRevNumber extends BaseEnversJPAFunctionalTestCase {
    private Integer id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(StrTestEntity.class);
        cfg.addAnnotatedClass(LongRevNumberRevEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() throws InterruptedException {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        StrTestEntity te = new StrTestEntity("x");
        em.persist(te);
        id = te.getId();
        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();
        te = em.find(StrTestEntity.class, id);
        te.setStr("y");
        em.getTransaction().commit();
    }

    @Test
    public void testFindRevision() {
        AuditReader vr = getAuditReader();

        assert vr.findRevision(LongRevNumberRevEntity.class, 1l).getCustomId() == 1l;
        assert vr.findRevision(LongRevNumberRevEntity.class, 2l).getCustomId() == 2l;
    }

    @Test
    public void testFindRevisions() {
        AuditReader vr = getAuditReader();

        Set<Number> revNumbers = new HashSet<Number>();
        revNumbers.add(1l);
        revNumbers.add(2l);
        
        Map<Number, LongRevNumberRevEntity> revisionMap = vr.findRevisions(LongRevNumberRevEntity.class, revNumbers);
        assert(revisionMap.size() == 2);
        assert(revisionMap.get(1l).equals(vr.findRevision(LongRevNumberRevEntity.class, 1l)));
        assert(revisionMap.get(2l).equals(vr.findRevision(LongRevNumberRevEntity.class, 2l)));
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1l, 2l).equals(getAuditReader().getRevisions(StrTestEntity.class, id));
    }

    @Test
    public void testHistoryOfId1() {
        StrTestEntity ver1 = new StrTestEntity("x", id);
        StrTestEntity ver2 = new StrTestEntity("y", id);

        assert getAuditReader().find(StrTestEntity.class, id, 1l).equals(ver1);
        assert getAuditReader().find(StrTestEntity.class, id, 2l).equals(ver2);
    }
}
