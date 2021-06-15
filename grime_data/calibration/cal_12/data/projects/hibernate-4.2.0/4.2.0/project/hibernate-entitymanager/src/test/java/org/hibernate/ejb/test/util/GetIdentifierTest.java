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
package org.hibernate.ejb.test.util;


import org.junit.Test;
import javax.persistence.EntityManager;
import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.testing.TestForIssue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Emmanuel Bernard
 */
public class GetIdentifierTest extends BaseEntityManagerFunctionalTestCase {
	@Test
	public void testSimpleId() {
		EntityManager em = entityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Book book = new Book();
		em.persist( book );
		em.flush();
		assertEquals( book.getId(), em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier( book ) );
		em.getTransaction().rollback();
		em.close();
	}

	@Test
	@TestForIssue(jiraKey = "HHH-7561")
	public void testProxyObject() {
		EntityManager em = entityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Book book = new Book();
		em.persist( book );
		em.flush();
		em.clear(); // Clear persistence context to receive proxy object below.
		Book proxy = em.getReference( Book.class, book.getId() );
		assertTrue( proxy instanceof HibernateProxy );
		assertEquals( book.getId(), em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier( proxy ) );
		em.getTransaction().rollback();
		em.close();

		em = entityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Author author = new Author();
		Article article = new Article( author );
		em.persist( author );
		em.persist( article );
		em.flush();
		em.clear(); // Clear persistence context to receive proxy relation below.
		article = em.find( Article.class, article.getId() );
		assertTrue( article.getAuthor() instanceof HibernateProxy );
		assertEquals( author.getId(), em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier( article.getAuthor() ) );
		em.getTransaction().rollback();
		em.close();
	}

	@Test
	public void testEmbeddedId() {
		EntityManager em = entityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Umbrella umbrella = new Umbrella();
		umbrella.setId( new Umbrella.PK() );
		umbrella.getId().setBrand( "Burberry" );
		umbrella.getId().setModel( "Red Hat" );
		em.persist( umbrella );
		em.flush();
		assertEquals( umbrella.getId(), em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier( umbrella ) );
		em.getTransaction().rollback();
		em.close();
	}

	@Test
	public void testIdClass() {
		EntityManager em = entityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Sickness sick = new Sickness();

		sick.setClassification( "H1N1" );
		sick.setType("Flu");
		em.persist( sick );
		em.flush();
		Sickness.PK id = new Sickness.PK();
		id.setClassification( sick.getClassification() );
		id.setType( sick.getType() );
		assertEquals( id, em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier( sick ) );
		em.getTransaction().rollback();
		em.close();
	}

	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[] {
				Book.class,
				Umbrella.class,
				Sickness.class,
				Author.class,
				Article.class
		};
	}
}
