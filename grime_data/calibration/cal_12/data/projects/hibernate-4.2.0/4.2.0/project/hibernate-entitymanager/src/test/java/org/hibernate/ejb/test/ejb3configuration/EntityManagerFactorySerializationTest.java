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
package org.hibernate.ejb.test.ejb3configuration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Test;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;
import org.hibernate.ejb.test.Cat;
import org.hibernate.ejb.test.Distributor;
import org.hibernate.ejb.test.Item;
import org.hibernate.ejb.test.Kitten;
import org.hibernate.ejb.test.Wallet;

import static org.junit.Assert.assertTrue;

/**
 * @author Emmanuel Bernard
 */
public class EntityManagerFactorySerializationTest extends BaseEntityManagerFunctionalTestCase {
	@Test
	public void testSerialization() throws Exception {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream( stream );
		out.writeObject( entityManagerFactory() );
		out.close();
		byte[] serialized = stream.toByteArray();
		stream.close();
		ByteArrayInputStream byteIn = new ByteArrayInputStream( serialized );
		ObjectInputStream in = new ObjectInputStream( byteIn );
		EntityManagerFactory serializedFactory = (EntityManagerFactory) in.readObject();
		in.close();
		byteIn.close();
		EntityManager em = serializedFactory.createEntityManager();
		//em.getTransaction().begin();
		//em.setFlushMode( FlushModeType.NEVER );
		Cat cat = new Cat();
		cat.setAge( 3 );
		cat.setDateOfBirth( new Date() );
		cat.setLength( 22 );
		cat.setName( "Kitty" );
		em.persist( cat );
		Item item = new Item();
		item.setName( "Train Ticket" );
		item.setDescr( "Paris-London" );
		em.persist( item );
		//em.getTransaction().commit();
		//em.getTransaction().begin();
		item.setDescr( "Paris-Bruxelles" );
		//em.getTransaction().commit();

		//fake the in container work
		( (HibernateEntityManager) em ).getSession().disconnect();
		stream = new ByteArrayOutputStream();
		out = new ObjectOutputStream( stream );
		out.writeObject( em );
		out.close();
		serialized = stream.toByteArray();
		stream.close();
		byteIn = new ByteArrayInputStream( serialized );
		in = new ObjectInputStream( byteIn );
		em = (EntityManager) in.readObject();
		in.close();
		byteIn.close();
		//fake the in container work
		em.getTransaction().begin();
		item = em.find( Item.class, item.getName() );
		item.setDescr( item.getDescr() + "-Amsterdam" );
		cat = (Cat) em.createQuery( "select c from " + Cat.class.getName() + " c" ).getSingleResult();
		cat.setLength( 34 );
		em.flush();
		em.remove( item );
		em.remove( cat );
		em.flush();
		em.getTransaction().commit();

		em.close();
	}

	@Test
	public void testEntityManagerFactorySerialization() throws Exception {
		EntityManagerFactory entityManagerFactory = entityManagerFactory();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream( stream );
		out.writeObject( entityManagerFactory );
		out.close();
		byte[] serialized = stream.toByteArray();
		stream.close();
		ByteArrayInputStream byteIn = new ByteArrayInputStream( serialized );
		ObjectInputStream in = new ObjectInputStream( byteIn );
		EntityManagerFactory entityManagerFactory2 = (EntityManagerFactory) in.readObject();
		in.close();
		byteIn.close();

		assertTrue("deserialized EntityManagerFactory should be the same original EntityManagerFactory instance",
				entityManagerFactory2 == entityManagerFactory);
	}


	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[]{
				Item.class,
				Distributor.class,
				Wallet.class,
				Cat.class,
				Kitten.class
		};
	}
}
