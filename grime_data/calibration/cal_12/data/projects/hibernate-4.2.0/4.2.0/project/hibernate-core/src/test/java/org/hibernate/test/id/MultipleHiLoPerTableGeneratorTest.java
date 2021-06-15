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
package org.hibernate.test.id;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Emmanuel Bernard
 */
public class MultipleHiLoPerTableGeneratorTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[]{ "id/Car.hbm.xml", "id/Plane.hbm.xml", "id/Radio.hbm.xml" };
	}

	@Test
	public void testDistinctId() throws Exception {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		int testLength = 8;
		Car[] cars = new Car[testLength];
		Plane[] planes = new Plane[testLength];
		for (int i = 0; i < testLength ; i++) {
			cars[i] = new Car();
			cars[i].setColor("Color" + i);
			planes[i] = new Plane();
			planes[i].setNbrOfSeats(i);
			s.persist(cars[i]);
			//s.persist(planes[i]);
		}
		tx.commit();
		s.close();
		for (int i = 0; i < testLength ; i++) {
			assertEquals(i+1, cars[i].getId().intValue());
			//assertEquals(i+1, planes[i].getId().intValue());
		}
		
		s = openSession();
		tx = s.beginTransaction();
		s.createQuery( "delete from Car" ).executeUpdate();
		tx.commit();
		s.close();
	}

	@Test
	public void testRollingBack() throws Throwable {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		int testLength = 3;
		Long lastId = null;
		for (int i = 0; i < testLength ; i++) {
			Car car = new Car();
			car.setColor( "color " + i );
			s.save( car );
			lastId = car.getId();
		}
		tx.rollback();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		Car car = new Car();
		car.setColor( "blue" );
		s.save( car );
		s.flush();
		tx.commit();
		s.close();

		assertEquals( "id generation was rolled back", lastId.longValue() + 1, car.getId().longValue() );

		s = openSession();
		tx = s.beginTransaction();
		s.createQuery( "delete Car" ).executeUpdate();
		tx.commit();
		s.close();
	}

	@Test
	public void testAllParams() throws Exception {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Radio radio = new Radio();
		radio.setFrequency("32 MHz");
		s.persist(radio);
		assertEquals( new Integer(1), radio.getId() );
		radio = new Radio();
		radio.setFrequency("32 MHz");
		s.persist(radio);
		assertEquals( new Integer(2), radio.getId() );
		tx.commit();
		s.close();
		
		s = openSession();
		tx = s.beginTransaction();
		s.createQuery( "delete from Radio" ).executeUpdate();
		tx.commit();
		s.close();
	}
}
