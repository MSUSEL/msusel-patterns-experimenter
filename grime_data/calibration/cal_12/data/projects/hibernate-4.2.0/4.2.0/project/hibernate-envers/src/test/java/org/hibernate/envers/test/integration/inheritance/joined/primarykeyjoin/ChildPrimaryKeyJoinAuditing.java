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
package org.hibernate.envers.test.integration.inheritance.joined.primarykeyjoin;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.integration.inheritance.joined.ParentEntity;
import org.hibernate.mapping.Column;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class ChildPrimaryKeyJoinAuditing extends BaseEnversJPAFunctionalTestCase {
    private Integer id1;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(ChildPrimaryKeyJoinEntity.class);
        cfg.addAnnotatedClass(ParentEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        id1 = 1;

        // Rev 1
        em.getTransaction().begin();
        ChildPrimaryKeyJoinEntity ce = new ChildPrimaryKeyJoinEntity(id1, "x", 1l);
        em.persist(ce);
        em.getTransaction().commit();

        // Rev 2
        em.getTransaction().begin();
        ce = em.find(ChildPrimaryKeyJoinEntity.class, id1);
        ce.setData("y");
        ce.setNumVal(2l);
        em.getTransaction().commit();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(ChildPrimaryKeyJoinEntity.class, id1));
    }

    @Test
    public void testHistoryOfChildId1() {
        ChildPrimaryKeyJoinEntity ver1 = new ChildPrimaryKeyJoinEntity(id1, "x", 1l);
        ChildPrimaryKeyJoinEntity ver2 = new ChildPrimaryKeyJoinEntity(id1, "y", 2l);

        assert getAuditReader().find(ChildPrimaryKeyJoinEntity.class, id1, 1).equals(ver1);
        assert getAuditReader().find(ChildPrimaryKeyJoinEntity.class, id1, 2).equals(ver2);

        assert getAuditReader().find(ParentEntity.class, id1, 1).equals(ver1);
        assert getAuditReader().find(ParentEntity.class, id1, 2).equals(ver2);
    }

    @Test
    public void testPolymorphicQuery() {
        ChildPrimaryKeyJoinEntity childVer1 = new ChildPrimaryKeyJoinEntity(id1, "x", 1l);

        assert getAuditReader().createQuery().forEntitiesAtRevision(ChildPrimaryKeyJoinEntity.class, 1).getSingleResult()
                .equals(childVer1);

        assert getAuditReader().createQuery().forEntitiesAtRevision(ParentEntity.class, 1).getSingleResult()
                .equals(childVer1);
    }

    @Test
    public void testChildIdColumnName() {
        Assert.assertEquals("other_id",
                ((Column) getCfg()
                        .getClassMapping("org.hibernate.envers.test.integration.inheritance.joined.primarykeyjoin.ChildPrimaryKeyJoinEntity_AUD")
                        .getKey().getColumnIterator().next()).getName());
    }
}