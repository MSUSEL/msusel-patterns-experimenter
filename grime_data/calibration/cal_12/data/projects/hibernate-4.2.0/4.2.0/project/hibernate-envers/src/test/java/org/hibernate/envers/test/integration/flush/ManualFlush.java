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
package org.hibernate.envers.test.integration.flush;

import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.FlushMode;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrTestEntity;

import static org.junit.Assert.assertEquals;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class ManualFlush extends AbstractFlushTest {
    private Integer id;

    public FlushMode getFlushMode() {
        return FlushMode.MANUAL;
    }

    @Test
    @Priority(10)
    public void initData() {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        StrTestEntity fe = new StrTestEntity("x");
        em.persist(fe);
        em.flush();

        em.getTransaction().commit();

        // No revision - we change the data, but do not flush the session
        em.getTransaction().begin();

        fe = em.find(StrTestEntity.class, fe.getId());
        fe.setStr("y");

        em.getTransaction().commit();

        // Revision 2 - only the first change should be saved
        em.getTransaction().begin();

        fe = em.find(StrTestEntity.class, fe.getId());
        fe.setStr("z");
        em.flush();

        fe = em.find(StrTestEntity.class, fe.getId());
        fe.setStr("z2");

        em.getTransaction().commit();

        //

        id = fe.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assertEquals(Arrays.asList(1, 2), getAuditReader().getRevisions(StrTestEntity.class, id));
    }

    @Test
    public void testHistoryOfId() {
        StrTestEntity ver1 = new StrTestEntity("x", id);
        StrTestEntity ver2 = new StrTestEntity("z", id);

        assertEquals(ver1, getAuditReader().find(StrTestEntity.class, id, 1));
        assertEquals(ver2, getAuditReader().find(StrTestEntity.class, id, 2));
    }

    @Test
    public void testCurrent() {
        assertEquals(new StrTestEntity("z", id), getEntityManager().find(StrTestEntity.class, id));
    }

    @Test
    public void testRevisionTypes() {
        @SuppressWarnings({"unchecked"}) List<Object[]> results =
                getAuditReader().createQuery()
                        .forRevisionsOfEntity(StrTestEntity.class, false, true)
                        .add(AuditEntity.id().eq(id))
                        .getResultList();

        assertEquals(results.get(0)[2], RevisionType.ADD);
        assertEquals(results.get(1)[2], RevisionType.MOD);
    }
}
