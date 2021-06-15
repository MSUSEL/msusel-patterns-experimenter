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
package org.hibernate.ejb.criteria.components;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;
import org.hibernate.testing.TestForIssue;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author alan.oleary
 */
public class ComponentCriteriaTest extends BaseEntityManagerFunctionalTestCase {
	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[] { Client.class };
	}

	@Test
	public void testEmbeddableInPath() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Client client = new Client( 111, "steve", "ebersole" );
		em.persist(client);
		em.getTransaction().commit();
		em.close();

		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Client> cq = cb.createQuery(Client.class);
		Root<Client> root = cq.from(Client.class);
		cq.where(cb.equal(root.get("name").get("firstName"), client.getName().getFirstName()));
		List<Client> list = em.createQuery(cq).getResultList();
		Assert.assertEquals( 1, list.size() );
		em.getTransaction().commit();
		em.close();
		
		// HHH-5792
		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		TypedQuery< Client > q = em.createQuery(
				"SELECT c FROM Client c JOIN c.name n WHERE n.firstName = '"
						+ client.getName().getFirstName() + "'",
                 Client.class );
		Assert.assertEquals( 1, q.getResultList().size() );
		em.getTransaction().commit();
		em.close();

		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		em.createQuery( "delete Client" ).executeUpdate();
		em.getTransaction().commit();
		em.close();
	}

	@Test
	@TestForIssue( jiraKey = "HHH-4586" )
	public void testParameterizedFunctions() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		// lower
		CriteriaQuery<Client> cq = cb.createQuery( Client.class );
		Root<Client> root = cq.from( Client.class );
		cq.where( cb.equal( cb.lower( root.get( Client_.name ).get( Name_.lastName ) ),"test" ) );
		em.createQuery( cq ).getResultList();
		// upper
		cq = cb.createQuery( Client.class );
		root = cq.from( Client.class );
		cq.where( cb.equal( cb.upper( root.get( Client_.name ).get( Name_.lastName ) ),"test" ) );
		em.createQuery( cq ).getResultList();
		em.getTransaction().commit();
		em.close();
	}
}
