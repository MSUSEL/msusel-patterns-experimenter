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
package org.hibernate.ejb.test.callbacks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;
import org.hibernate.ejb.test.Cat;
import org.hibernate.ejb.test.Kitten;
import org.hibernate.testing.FailureExpected;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Emmanuel Bernard
 */
@SuppressWarnings("unchecked")
public class CallbacksTest extends BaseEntityManagerFunctionalTestCase {
	@Test
	public void testCallbackMethod() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		Cat c = new Cat();
		c.setName( "Kitty" );
		c.setDateOfBirth( new Date( 90, 11, 15 ) );
		em.getTransaction().begin();
		em.persist( c );
		em.getTransaction().commit();
		em.clear();
		em.getTransaction().begin();
		c = em.find( Cat.class, c.getId() );
		assertFalse( c.getAge() == 0 );
		c.setName( "Tomcat" ); //update this entity
		em.getTransaction().commit();
		em.clear();
		em.getTransaction().begin();
		c = em.find( Cat.class, c.getId() );
		assertEquals( "Tomcat", c.getName() );
		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void testEntityListener() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		Cat c = new Cat();
		c.setName( "Kitty" );
		c.setLength( 12 );
		c.setDateOfBirth( new Date( 90, 11, 15 ) );
		em.getTransaction().begin();
		int previousVersion = c.getManualVersion();
		em.persist( c );
		em.getTransaction().commit();
		em.getTransaction().begin();
		c = em.find( Cat.class, c.getId() );
		assertNotNull( c.getLastUpdate() );
		assertTrue( previousVersion < c.getManualVersion() );
		assertEquals( 12, c.getLength() );
		previousVersion = c.getManualVersion();
		c.setName( "new name" );
		em.getTransaction().commit();
		em.getTransaction().begin();
		c = em.find( Cat.class, c.getId() );
		assertTrue( previousVersion < c.getManualVersion() );
		em.getTransaction().commit();

		em.close();
	}

	@Test
	public void testPostPersist() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		Cat c = new Cat();
		em.getTransaction().begin();
		c.setLength( 23 );
		c.setAge( 2 );
		c.setName( "Beetle" );
		c.setDateOfBirth( new Date() );
		em.persist( c );
		em.getTransaction().commit();
		em.close();
		List ids = Cat.getIdList();
		Object id = Cat.getIdList().get( ids.size() - 1 );
		assertNotNull( id );
	}

	//Not a test since the spec did not make the proper change on listeners
	public void listenerAnnotation() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		Translation tl = new Translation();
		em.getTransaction().begin();
		tl.setInto( "France" );
		em.persist( tl );
		tl = new Translation();
		tl.setInto( "Bimboland" );
		try {
			em.persist( tl );
			em.flush();
			fail( "Annotations annotated by a listener not used" );
		}
		catch (IllegalArgumentException e) {
			//success
		}
		finally {
			em.getTransaction().rollback();
			em.close();
		}
	}

	@Test
	public void testPrePersistOnCascade() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Television tv = new Television();
		RemoteControl rc = new RemoteControl();
		em.persist( tv );
		em.flush();
		tv.setControl( rc );
		tv.init();
		em.flush();
		assertNotNull( rc.getCreationDate() );
		em.getTransaction().rollback();
		em.close();
	}

	@Test
	public void testCallBackListenersHierarchy() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Television tv = new Television();
		em.persist( tv );
		tv.setName( "Myaio" );
		tv.init();
		em.flush();
		assertEquals( 1, tv.counter );
		em.getTransaction().rollback();
		em.close();
		assertEquals( 5, tv.communication );
		assertTrue( tv.isLast );
	}

	@Test
	public void testException() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Rythm r = new Rythm();
		try {
			em.persist( r );
			em.flush();
			fail("should have raised an ArythmeticException:");
		}
		catch (ArithmeticException e) {
			//success
		}
		catch( Exception e ) {
			fail("should have raised an ArythmeticException:" + e.getClass() );
		}

		em.getTransaction().rollback();
		em.close();

	}

	@Test
	public void testIdNullSetByPrePersist() throws Exception {
		Plant plant = new Plant();
		plant.setName( "Origuna plantula gigantic" );
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		em.persist( plant );
		em.flush();
		em.getTransaction().rollback();
		em.close();
	}
	
	@Test
	@FailureExpected(message = "collection change does not trigger an event", jiraKey = "EJB-288")
	public void testPostUpdateCollection() throws Exception {
		// create a cat
		EntityManager em = getOrCreateEntityManager();
		Cat cat = new Cat();
		em.getTransaction().begin();
		cat.setLength( 23 );
		cat.setAge( 2 );
		cat.setName( "Beetle" );
		cat.setDateOfBirth( new Date() );
		em.persist( cat );
		em.getTransaction().commit();

		// assert it is persisted
		List ids = Cat.getIdList();
		Object id = Cat.getIdList().get( ids.size() - 1 );
		assertNotNull( id );

		// add a kitten to the cat - triggers PostCollectionRecreateEvent
		int postVersion = Cat.postVersion;
		em.getTransaction().begin();
		Kitten kitty = new Kitten();
		kitty.setName("kitty");
		List kittens = new ArrayList<Kitten>();
		kittens.add(kitty);
		cat.setKittens(kittens);
		em.getTransaction().commit();
		assertEquals("Post version should have been incremented.", postVersion + 1, Cat.postVersion);

		// add another kitten - triggers PostCollectionUpdateEvent.
		postVersion = Cat.postVersion;
		em.getTransaction().begin();
		Kitten tom = new Kitten();
		tom.setName("Tom");
		cat.getKittens().add(tom);
		em.getTransaction().commit();
		assertEquals("Post version should have been incremented.", postVersion + 1, Cat.postVersion);

		// delete a kitty - triggers PostCollectionUpdateEvent
		postVersion = Cat.postVersion;
		em.getTransaction().begin();
		cat.getKittens().remove(tom);
		em.getTransaction().commit();
		assertEquals("Post version should have been incremented.", postVersion + 1, Cat.postVersion);

		// delete and recreate kittens - triggers PostCollectionRemoveEvent and PostCollectionRecreateEvent)
		postVersion = Cat.postVersion;
		em.getTransaction().begin();
		cat.setKittens(new ArrayList<Kitten>());
		em.getTransaction().commit();
		assertEquals("Post version should have been incremented.", postVersion + 2, Cat.postVersion);

		em.close();
	}

	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[]{
				Cat.class,
				Translation.class,
				Television.class,
				RemoteControl.class,
				Rythm.class,
				Plant.class,
				Kitten.class
		};
	}
}
