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
package org.hibernate.envers.test.integration.onetomany.detached;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.onetomany.detached.ListJoinColumnBidirectionalInheritanceRefEdChildEntity;
import org.hibernate.envers.test.entities.onetomany.detached.ListJoinColumnBidirectionalInheritanceRefEdParentEntity;
import org.hibernate.envers.test.entities.onetomany.detached.ListJoinColumnBidirectionalInheritanceRefIngEntity;

import static org.hibernate.envers.test.tools.TestTools.checkList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for a "fake" bidirectional mapping where one side uses @OneToMany+@JoinColumn (and thus owns the relatin),
 * and the other uses a @ManyToOne(insertable=false, updatable=false).
 * @author Adam Warski (adam at warski dot org)
 */
public class JoinColumnBidirectionalListWithInheritance extends BaseEnversJPAFunctionalTestCase {
    private Integer ed1_id;
    private Integer ed2_id;

    private Integer ing1_id;
    private Integer ing2_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(ListJoinColumnBidirectionalInheritanceRefIngEntity.class);
        cfg.addAnnotatedClass(ListJoinColumnBidirectionalInheritanceRefEdChildEntity.class);
        cfg.addAnnotatedClass(ListJoinColumnBidirectionalInheritanceRefEdParentEntity.class);
    }

    @Test
    @Priority(10)
    public void createData() {
        EntityManager em = getEntityManager();

        ListJoinColumnBidirectionalInheritanceRefEdParentEntity ed1 = new ListJoinColumnBidirectionalInheritanceRefEdChildEntity("ed1", null, "ed1 child");
        ListJoinColumnBidirectionalInheritanceRefEdParentEntity ed2 = new ListJoinColumnBidirectionalInheritanceRefEdChildEntity("ed2", null, "ed2 child");

        ListJoinColumnBidirectionalInheritanceRefIngEntity ing1 = new ListJoinColumnBidirectionalInheritanceRefIngEntity("coll1", ed1);
        ListJoinColumnBidirectionalInheritanceRefIngEntity ing2 = new ListJoinColumnBidirectionalInheritanceRefIngEntity("coll1", ed2);

        // Revision 1 (ing1: ed1, ing2: ed2)
        em.getTransaction().begin();

        em.persist(ed1);
        em.persist(ed2);
        em.persist(ing1);
        em.persist(ing2);

        em.getTransaction().commit();

        // Revision 2 (ing1: ed1, ed2)
        em.getTransaction().begin();

        ing1 = em.find(ListJoinColumnBidirectionalInheritanceRefIngEntity.class, ing1.getId());
        ing2 = em.find(ListJoinColumnBidirectionalInheritanceRefIngEntity.class, ing2.getId());
        ed1 = em.find(ListJoinColumnBidirectionalInheritanceRefEdParentEntity.class, ed1.getId());
        ed2 = em.find(ListJoinColumnBidirectionalInheritanceRefEdParentEntity.class, ed2.getId());

        ing2.getReferences().remove(ed2);
        ing1.getReferences().add(ed2);

        em.getTransaction().commit();
        em.clear();

        //

        ing1_id = ing1.getId();
        ing2_id = ing2.getId();

        ed1_id = ed1.getId();
        ed2_id = ed2.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assertEquals(Arrays.asList(1, 2), getAuditReader().getRevisions(ListJoinColumnBidirectionalInheritanceRefIngEntity.class, ing1_id));
        assertEquals(Arrays.asList(1, 2), getAuditReader().getRevisions(ListJoinColumnBidirectionalInheritanceRefIngEntity.class, ing2_id));

        assertEquals(Arrays.asList(1), getAuditReader().getRevisions(ListJoinColumnBidirectionalInheritanceRefEdParentEntity.class, ed1_id));
        assertEquals(Arrays.asList(1, 2), getAuditReader().getRevisions(ListJoinColumnBidirectionalInheritanceRefEdParentEntity.class, ed2_id));
    }

    @Test
    public void testHistoryOfIng1() {
        ListJoinColumnBidirectionalInheritanceRefEdParentEntity ed1 = getEntityManager().find(ListJoinColumnBidirectionalInheritanceRefEdParentEntity.class, ed1_id);
        ListJoinColumnBidirectionalInheritanceRefEdParentEntity ed2 = getEntityManager().find(ListJoinColumnBidirectionalInheritanceRefEdParentEntity.class, ed2_id);

        ListJoinColumnBidirectionalInheritanceRefIngEntity rev1 = getAuditReader().find(ListJoinColumnBidirectionalInheritanceRefIngEntity.class, ing1_id, 1);
        ListJoinColumnBidirectionalInheritanceRefIngEntity rev2 = getAuditReader().find(ListJoinColumnBidirectionalInheritanceRefIngEntity.class, ing1_id, 2);

        assertTrue(checkList(rev1.getReferences(), ed1));
        assertTrue(checkList(rev2.getReferences(), ed1, ed2));
    }

    @Test
    public void testHistoryOfIng2() {
        ListJoinColumnBidirectionalInheritanceRefEdParentEntity ed2 = getEntityManager().find(ListJoinColumnBidirectionalInheritanceRefEdParentEntity.class, ed2_id);

        ListJoinColumnBidirectionalInheritanceRefIngEntity rev1 = getAuditReader().find(ListJoinColumnBidirectionalInheritanceRefIngEntity.class, ing2_id, 1);
        ListJoinColumnBidirectionalInheritanceRefIngEntity rev2 = getAuditReader().find(ListJoinColumnBidirectionalInheritanceRefIngEntity.class, ing2_id, 2);

        assertTrue(checkList(rev1.getReferences(), ed2));
        assertTrue(checkList(rev2.getReferences()));
    }

    @Test
    public void testHistoryOfEd1() {
        ListJoinColumnBidirectionalInheritanceRefIngEntity ing1 = getEntityManager().find(ListJoinColumnBidirectionalInheritanceRefIngEntity.class, ing1_id);

        ListJoinColumnBidirectionalInheritanceRefEdParentEntity rev1 = getAuditReader().find(ListJoinColumnBidirectionalInheritanceRefEdParentEntity.class, ed1_id, 1);
        ListJoinColumnBidirectionalInheritanceRefEdParentEntity rev2 = getAuditReader().find(ListJoinColumnBidirectionalInheritanceRefEdParentEntity.class, ed1_id, 2);

        assertTrue(rev1.getOwner().equals(ing1));
        assertTrue(rev2.getOwner().equals(ing1));
    }

    @Test
    public void testHistoryOfEd2() {
        ListJoinColumnBidirectionalInheritanceRefIngEntity ing1 = getEntityManager().find(ListJoinColumnBidirectionalInheritanceRefIngEntity.class, ing1_id);
        ListJoinColumnBidirectionalInheritanceRefIngEntity ing2 = getEntityManager().find(ListJoinColumnBidirectionalInheritanceRefIngEntity.class, ing2_id);

        ListJoinColumnBidirectionalInheritanceRefEdParentEntity rev1 = getAuditReader().find(ListJoinColumnBidirectionalInheritanceRefEdParentEntity.class, ed2_id, 1);
        ListJoinColumnBidirectionalInheritanceRefEdParentEntity rev2 = getAuditReader().find(ListJoinColumnBidirectionalInheritanceRefEdParentEntity.class, ed2_id, 2);

        assertTrue(rev1.getOwner().equals(ing2));
        assertTrue(rev2.getOwner().equals(ing1));
    }

}