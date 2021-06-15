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
package org.hibernate.envers.test.integration.naming;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.envers.test.tools.TestTools;
import org.hibernate.mapping.Column;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class VersionsJoinTableNaming extends BaseEnversJPAFunctionalTestCase {
    private Integer uni1_id;
    private Integer str1_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(VersionsJoinTableTestEntity.class);
        cfg.addAnnotatedClass(StrTestEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        VersionsJoinTableTestEntity uni1 = new VersionsJoinTableTestEntity(1, "data1");
        StrTestEntity str1 = new StrTestEntity("str1");

        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        uni1.setCollection(new HashSet<StrTestEntity>());
        em.persist(uni1);
        em.persist(str1);

        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();

        uni1 = em.find(VersionsJoinTableTestEntity.class, uni1.getId());
        str1 = em.find(StrTestEntity.class, str1.getId());
        uni1.getCollection().add(str1);

        em.getTransaction().commit();

        //

        uni1_id = uni1.getId();
        str1_id = str1.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(VersionsJoinTableTestEntity.class, uni1_id));
        assert Arrays.asList(1).equals(getAuditReader().getRevisions(StrTestEntity.class, str1_id));
    }

    @Test
    public void testHistoryOfUniId1() {
        StrTestEntity str1 = getEntityManager().find(StrTestEntity.class, str1_id);

        VersionsJoinTableTestEntity rev1 = getAuditReader().find(VersionsJoinTableTestEntity.class, uni1_id, 1);
        VersionsJoinTableTestEntity rev2 = getAuditReader().find(VersionsJoinTableTestEntity.class, uni1_id, 2);

        assert rev1.getCollection().equals(TestTools.makeSet());
        assert rev2.getCollection().equals(TestTools.makeSet(str1));

        assert "data1".equals(rev1.getData());
        assert "data1".equals(rev2.getData());
    }

    private final static String MIDDLE_VERSIONS_ENTITY_NAME = "VERSIONS_JOIN_TABLE_TEST";
    
    @Test
    public void testTableName() {
        assert MIDDLE_VERSIONS_ENTITY_NAME.equals(
                getCfg().getClassMapping(MIDDLE_VERSIONS_ENTITY_NAME).getTable().getName());
    }

    @SuppressWarnings({"unchecked"})
    @Test
    public void testJoinColumnName() {
        Iterator<Column> columns =
                getCfg().getClassMapping(MIDDLE_VERSIONS_ENTITY_NAME).getTable().getColumnIterator();

        boolean id1Found = false;
        boolean id2Found = false;

        while (columns.hasNext()) {
            Column column = columns.next();
            if ("VJT_ID".equals(column.getName())) {
                id1Found = true;
            }

            if ("STR_ID".equals(column.getName())) {
                id2Found = true;
            }
        }

        assert id1Found && id2Found;
    }
}