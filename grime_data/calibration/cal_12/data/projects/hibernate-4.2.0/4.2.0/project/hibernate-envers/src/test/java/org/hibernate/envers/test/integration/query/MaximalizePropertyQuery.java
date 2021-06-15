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
package org.hibernate.envers.test.integration.query;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.criteria.AuditDisjunction;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrIntTestEntity;
import org.hibernate.testing.TestForIssue;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@SuppressWarnings({"unchecked"})
public class MaximalizePropertyQuery extends BaseEnversJPAFunctionalTestCase {
    Integer id1;
    Integer id2;
    Integer id3;

    protected Class<?>[] getAnnotatedClasses() {
        return new Class[] { StrIntTestEntity.class };
    }

    @Test
    @Priority(10)
    public void initData() {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        StrIntTestEntity site1 = new StrIntTestEntity("a", 10);
        StrIntTestEntity site2 = new StrIntTestEntity("b", 15);
        StrIntTestEntity site3 = new StrIntTestEntity("c", 42);

        em.persist(site1);
        em.persist(site2);
        em.persist(site3);

        id1 = site1.getId();
        id2 = site2.getId();
        id3 = site3.getId();

        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();

        site1 = em.find(StrIntTestEntity.class, id1);
        site2 = em.find(StrIntTestEntity.class, id2);

        site1.setStr1("d");
        site2.setNumber(20);

        em.getTransaction().commit();

        // Revision 3
        em.getTransaction().begin();

        site1 = em.find(StrIntTestEntity.class, id1);
        site2 = em.find(StrIntTestEntity.class, id2);

        site1.setNumber(30);
        site2.setStr1("z");

        em.getTransaction().commit();

        // Revision 4
        em.getTransaction().begin();

        site1 = em.find(StrIntTestEntity.class, id1);
        site2 = em.find(StrIntTestEntity.class, id2);

        site1.setNumber(5);
        site2.setStr1("a");

        em.getTransaction().commit();
    }

    @Test
    public void testMaximizeWithIdEq() {
        List revs_id1 = getAuditReader().createQuery()
                .forRevisionsOfEntity(StrIntTestEntity.class, false, true)
                .addProjection(AuditEntity.revisionNumber())
                .add(AuditEntity.property("number").maximize()
                    .add(AuditEntity.id().eq(id2)))
                .getResultList();

        assert Arrays.asList(2, 3, 4).equals(revs_id1);
    }

    @Test
    public void testMinimizeWithPropertyEq() {
        List result = getAuditReader().createQuery()
                .forRevisionsOfEntity(StrIntTestEntity.class, false, true)
                .addProjection(AuditEntity.revisionNumber())
                .add(AuditEntity.property("number").minimize()
                    .add(AuditEntity.property("str1").eq("a")))
                .getResultList();

        assert Arrays.asList(1).equals(result);
    }

    @Test
    public void testMaximizeRevision() {
        List result = getAuditReader().createQuery()
                .forRevisionsOfEntity(StrIntTestEntity.class, false, true)
                .addProjection(AuditEntity.revisionNumber())
                .add(AuditEntity.revisionNumber().maximize()
                    .add(AuditEntity.property("number").eq(10)))
                .getResultList();

        assert Arrays.asList(2).equals(result);
    }

	@Test
	@TestForIssue(jiraKey = "HHH-7800")
	public void testMaximizeInDisjunction() {
		List<Integer> idsToQuery = Arrays.asList( id1, id3 );

		AuditDisjunction disjunction = AuditEntity.disjunction();

		for ( Integer id : idsToQuery ) {
			disjunction.add( AuditEntity.revisionNumber().maximize().add( AuditEntity.id().eq( id ) ) );
		}
		List result = getAuditReader().createQuery()
				.forRevisionsOfEntity( StrIntTestEntity.class, true, true )
				.add( disjunction )
				.getResultList();

		Set<Integer> idsSeen = new HashSet<Integer>();
		for ( Object o : result ) {
			StrIntTestEntity entity = (StrIntTestEntity) o;
			Integer id = entity.getId();
			Assert.assertTrue( "Entity with ID " + id + " returned but not queried for.", idsToQuery.contains( id ) );
			if ( !idsSeen.add( id ) ) {
				Assert.fail( "Multiple revisions returned with ID " + id + "; expected only one." );
			}
		}
	}
}