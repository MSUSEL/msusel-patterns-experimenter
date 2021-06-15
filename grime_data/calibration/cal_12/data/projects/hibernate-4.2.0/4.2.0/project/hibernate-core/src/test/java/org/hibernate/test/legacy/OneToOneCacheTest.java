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
package org.hibernate.test.legacy;
import java.io.Serializable;

import org.junit.Test;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import static org.junit.Assert.assertNotNull;

/**
 * Simple testcase to illustrate HB-992
 *
 * @author Wolfgang Voelkl, michael
 */
public class OneToOneCacheTest extends LegacyTestCase {
	private Serializable generatedId;

	@Override
	public String[] getMappings() {
		return new String[] { "legacy/Object2.hbm.xml", "legacy/MainObject.hbm.xml" };
	}

	@Test
	public void testOneToOneCache() throws HibernateException {

		//create a new MainObject
		createMainObject();
		// load the MainObject
		readMainObject();

		//create and add Ojbect2
		addObject2();

		//here the newly created Object2 is written to the database
		//but the MainObject does not know it yet
		MainObject mainObject = readMainObject();

		assertNotNull( mainObject.getObj2() );

		// after evicting, it works.
		sessionFactory().evict( MainObject.class );

		mainObject = readMainObject();

		assertNotNull( mainObject.getObj2() );

	}

	/**
	 * creates a new MainObject
	 * <p/>
	 * one hibernate transaction !
	 */
	private void createMainObject() throws HibernateException {
		Session session = openSession();
		Transaction tx = session.beginTransaction();

		MainObject mo = new MainObject();
		mo.setDescription( "Main Test" );

		generatedId = session.save( mo );

		tx.commit();
		session.close();
	}

	/**
	 * loads the newly created MainObject
	 * and adds a new Object2 to it
	 * <p/>
	 * one hibernate transaction
	 */
	private void addObject2() throws HibernateException {
		Session session = openSession();
		Transaction tx = session.beginTransaction();

		MainObject mo =
				( MainObject ) session.load( MainObject.class, generatedId );

		Object2 toAdd = new Object2();
		toAdd.setDummy( "test" );

		//toAdd should now be saved by cascade
		mo.setObj2( toAdd );

		tx.commit();
		session.close();
	}

	/**
	 * reads the newly created MainObject
	 * and its Object2 if it exists
	 * <p/>
	 * one hibernate transaction
	 */
	private MainObject readMainObject() throws HibernateException {
		Long returnId = null;
		Session session = openSession();
		Transaction tx = session.beginTransaction();

		Serializable id = generatedId;

		MainObject mo = ( MainObject ) session.load( MainObject.class, id );

		tx.commit();
		session.close();

		return mo;
	}
}
