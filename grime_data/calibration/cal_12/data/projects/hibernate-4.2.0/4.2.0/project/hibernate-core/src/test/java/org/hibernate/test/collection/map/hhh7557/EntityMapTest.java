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
package org.hibernate.test.collection.map.hhh7557;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Elizabeth Chatman
 * @author Steve Ebersole
 */
public class EntityMapTest extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] {MapValue.class, MapKey.class, MapHolder.class};
	}

	@Test
	public void testInsertIntoMap() throws Exception {
		{
			// Session 1: Insert 3 values into the map
			Session session = openSession();
			session.beginTransaction();
			MapHolder mapHolder = new MapHolder();
			mapHolder.setMap( new HashMap<MapKey, MapValue>() );
			addMapEntry( session, mapHolder, "A", "1" );
			addMapEntry( session, mapHolder, "B", "2" );
			addMapEntry( session, mapHolder, "C", "3" );
			session.save( mapHolder );
			// Verify there are 3 entries in the map
			Assert.assertEquals( 3, mapHolder.getMap().size() );
			session.getTransaction().commit();
			session.close();
		}

		{
			// Session 2: Add a 4th value to the map
			Session session = openSession();
			session.beginTransaction();
			MapHolder mapHolder = getMapHolder( session );
			System.out.println( "Got MapHolder; checking map size -----" );
			Assert.assertEquals( 3, mapHolder.getMap().size() );
			System.out.println( "Got MapHolder; checked map size -----" );
			addMapEntry( session, mapHolder, "D", "4" );
			// Verify there are 4 entries in the map
			Assert.assertEquals( 4, mapHolder.getMap().size() );
			session.getTransaction().commit();
			session.close();
		}

		{
			// Session 3: Count the entries in the map
			Session session = openSession();
			session.beginTransaction();
			MapHolder mapHolder = getMapHolder( session );
			// Fails here (expected:<4> but was:<1>)
			Assert.assertEquals( 4, mapHolder.getMap().size() );
			session.getTransaction().commit();
			session.close();
		}
	}

	private void addMapEntry(Session session, MapHolder mapHolder, String key, String value) {
		System.out.println( "Inserting (" + key + "," + value + ") into map" );
		MapValue entityValue = new MapValue( value );
		session.save( entityValue );
		MapKey entityKey = new MapKey( key, entityValue );
		session.save( entityKey );
		mapHolder.getMap().put( entityKey, entityValue );
	}

	private MapHolder getMapHolder(Session session) {
		List mapHolders = session.createQuery( "select distinct mh from MapHolder mh" ).list();
		Assert.assertEquals( 1, mapHolders.size() );
		return (MapHolder) mapHolders.get( 0 );
	}
}
