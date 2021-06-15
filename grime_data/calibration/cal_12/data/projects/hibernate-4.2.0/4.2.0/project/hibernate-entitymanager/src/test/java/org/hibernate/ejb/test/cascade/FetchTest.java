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
package org.hibernate.ejb.test.cascade;

import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Emmanuel Bernard
 */
public class FetchTest extends BaseEntityManagerFunctionalTestCase {
	@Test
	public void testCascadeAndFetchCollection() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Troop disney = new Troop();
		disney.setName( "Disney" );
		Soldier mickey = new Soldier();
		mickey.setName( "Mickey" );
		disney.addSoldier( mickey );
		em.persist( disney );
		em.getTransaction().commit();
		em.close();

		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Troop troop = em.find( Troop.class, disney.getId() );
		assertFalse( Hibernate.isInitialized( troop.getSoldiers() ) );
		em.getTransaction().commit();
		assertFalse( Hibernate.isInitialized( troop.getSoldiers() ) );
		em.close();

		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		troop = em.find( Troop.class, disney.getId() );
		em.remove( troop );
		//Fail because of HHH-1187
		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void testCascadeAndFetchEntity() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Troop disney = new Troop();
		disney.setName( "Disney" );
		Soldier mickey = new Soldier();
		mickey.setName( "Mickey" );
		disney.addSoldier( mickey );
		em.persist( disney );
		em.getTransaction().commit();
		em.close();

		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Soldier soldier = em.find( Soldier.class, mickey.getId() );
		assertFalse( Hibernate.isInitialized( soldier.getTroop() ) );
		em.getTransaction().commit();
		assertFalse( Hibernate.isInitialized( soldier.getTroop() ) );
		em.close();
		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Troop troop = em.find( Troop.class, disney.getId() );
		em.remove( troop );
		//Fail because of HHH-1187
		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void testTwoLevelDeepPersist() throws Exception {
		EntityTransaction tx;

		EntityManager em = getOrCreateEntityManager();
		tx = em.getTransaction();
		tx.begin();
		Conference jbwBarcelona = new Conference();
		jbwBarcelona.setDate( new Date() );
		ExtractionDocumentInfo info = new ExtractionDocumentInfo();
		info.setConference( jbwBarcelona );
		jbwBarcelona.setExtractionDocument( info );
		info.setLastModified( new Date() );
		ExtractionDocument doc = new ExtractionDocument();
		doc.setDocumentInfo( info );
		info.setDocuments( new ArrayList<ExtractionDocument>() );
		info.getDocuments().add( doc );
		doc.setBody( new byte[]{'c', 'f'} );
		em.persist( jbwBarcelona );
		tx.commit();
		em.close();

		em = getOrCreateEntityManager();
		tx = em.getTransaction();
		tx.begin();
		jbwBarcelona = em.find( Conference.class, jbwBarcelona.getId() );
		assertTrue( Hibernate.isInitialized( jbwBarcelona ) );
		assertTrue( Hibernate.isInitialized( jbwBarcelona.getExtractionDocument() ) );
		assertFalse( Hibernate.isInitialized( jbwBarcelona.getExtractionDocument().getDocuments() ) );
		em.flush();
		assertTrue( Hibernate.isInitialized( jbwBarcelona ) );
		assertTrue( Hibernate.isInitialized( jbwBarcelona.getExtractionDocument() ) );
		assertFalse( Hibernate.isInitialized( jbwBarcelona.getExtractionDocument().getDocuments() ) );
		em.remove( jbwBarcelona );
		tx.commit();
		em.close();
	}

	@Test
	public void testTwoLevelDeepPersistOnManyToOne() throws Exception {
		EntityTransaction tx;
		EntityManager em = getOrCreateEntityManager();
		tx = em.getTransaction();
		tx.begin();
		Grandson gs = new Grandson();
		gs.setParent( new Son() );
		gs.getParent().setParent( new Parent() );
		em.persist( gs );
		tx.commit();
		em.close();
		em = getOrCreateEntityManager();
		tx = em.getTransaction();
		tx.begin();
		gs = em.find( Grandson.class, gs.getId() );
		em.flush();
		assertTrue( Hibernate.isInitialized( gs.getParent() ) );
		assertFalse( Hibernate.isInitialized( gs.getParent().getParent() ) );
		em.remove( gs );
		tx.commit();
		em.close();
	}

	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[]{
				Troop.class,
				Soldier.class,
				Conference.class,
				ExtractionDocument.class,
				ExtractionDocumentInfo.class,
				Parent.class,
				Son.class,
				Grandson.class
		};
	}
}
