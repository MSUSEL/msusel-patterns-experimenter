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
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.exception.RevisionDoesNotExistException;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrTestEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class CustomBoxed extends BaseEnversJPAFunctionalTestCase {
    private Integer id;
    private long timestamp1;
    private long timestamp2;
    private long timestamp3;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(StrTestEntity.class);
        cfg.addAnnotatedClass(CustomBoxedRevEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() throws InterruptedException {
        timestamp1 = System.currentTimeMillis();

        Thread.sleep(100);

        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        StrTestEntity te = new StrTestEntity("x");
        em.persist(te);
        id = te.getId();
        em.getTransaction().commit();

        timestamp2 = System.currentTimeMillis();

        Thread.sleep(100);

        // Revision 2
        em.getTransaction().begin();
        te = em.find(StrTestEntity.class, id);
        te.setStr("y");
        em.getTransaction().commit();

        timestamp3 = System.currentTimeMillis();
    }

    @Test(expected = RevisionDoesNotExistException.class)
    public void testTimestamps1() {
        getAuditReader().getRevisionNumberForDate(new Date(timestamp1));
    }

    @Test
    public void testTimestamps() {
        assert getAuditReader().getRevisionNumberForDate(new Date(timestamp2)).intValue() == 1;
        assert getAuditReader().getRevisionNumberForDate(new Date(timestamp3)).intValue() == 2;
    }

    @Test
    public void testDatesForRevisions() {
        AuditReader vr = getAuditReader();
        assert vr.getRevisionNumberForDate(vr.getRevisionDate(1)).intValue() == 1;
        assert vr.getRevisionNumberForDate(vr.getRevisionDate(2)).intValue() == 2;
    }

    @Test
    public void testRevisionsForDates() {
        AuditReader vr = getAuditReader();

        assert vr.getRevisionDate(vr.getRevisionNumberForDate(new Date(timestamp2))).getTime() <= timestamp2;
        assert vr.getRevisionDate(vr.getRevisionNumberForDate(new Date(timestamp2)).intValue()+1).getTime() > timestamp2;

        assert vr.getRevisionDate(vr.getRevisionNumberForDate(new Date(timestamp3))).getTime() <= timestamp3;
    }

    @Test
    public void testFindRevision() {
        AuditReader vr = getAuditReader();

        long rev1Timestamp = vr.findRevision(CustomBoxedRevEntity.class, 1).getCustomTimestamp();
        assert rev1Timestamp > timestamp1;
        assert rev1Timestamp <= timestamp2;

        long rev2Timestamp = vr.findRevision(CustomBoxedRevEntity.class, 2).getCustomTimestamp();
        assert rev2Timestamp > timestamp2;
        assert rev2Timestamp <= timestamp3;
    }

    @Test
    public void testFindRevisions() {
        AuditReader vr = getAuditReader();

        Set<Number> revNumbers = new HashSet<Number>();
        revNumbers.add(1);
        revNumbers.add(2);
        
        Map<Number, CustomBoxedRevEntity> revisionMap = vr.findRevisions(CustomBoxedRevEntity.class, revNumbers);
        assert(revisionMap.size() == 2);
        assert(revisionMap.get(1).equals(vr.findRevision(CustomBoxedRevEntity.class, 1)));
        assert(revisionMap.get(2).equals(vr.findRevision(CustomBoxedRevEntity.class, 2)));
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(StrTestEntity.class, id));
    }

    @Test
    public void testHistoryOfId1() {
        StrTestEntity ver1 = new StrTestEntity("x", id);
        StrTestEntity ver2 = new StrTestEntity("y", id);

        assert getAuditReader().find(StrTestEntity.class, id, 1).equals(ver1);
        assert getAuditReader().find(StrTestEntity.class, id, 2).equals(ver2);
    }
}
