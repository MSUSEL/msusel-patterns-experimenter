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
public class DoubleFlushAddMod extends AbstractFlushTest {
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

        fe.setStr("y");

        em.flush();

        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();

        fe = em.find(StrTestEntity.class, fe.getId());

        fe.setStr("z");
        em.flush();

        em.getTransaction().commit();

        //

        id = fe.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(StrTestEntity.class, id));
    }

    @Test
    public void testHistoryOfId() {
        StrTestEntity ver1 = new StrTestEntity("y", id);
        StrTestEntity ver2 = new StrTestEntity("z", id);

        assert getAuditReader().find(StrTestEntity.class, id, 1).equals(ver1);
        assert getAuditReader().find(StrTestEntity.class, id, 2).equals(ver2);
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