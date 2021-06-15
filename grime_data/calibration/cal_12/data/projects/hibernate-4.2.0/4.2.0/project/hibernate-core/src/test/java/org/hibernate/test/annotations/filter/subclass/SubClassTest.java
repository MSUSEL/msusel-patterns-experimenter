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
package org.hibernate.test.annotations.filter.subclass;

import junit.framework.Assert;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

public abstract class SubClassTest extends BaseCoreFunctionalTestCase{
	
	@Override
	protected void prepareTest() throws Exception {
		openSession();
		session.beginTransaction();
		
		persistTestData();
		
		session.getTransaction().commit();
		session.close();
	}
	
	protected abstract void persistTestData();
	
	@Override
	protected void cleanupTest() throws Exception {
		openSession();
		session.beginTransaction();
		
		session.createQuery("delete from Human").executeUpdate();
		
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testIqFilter(){
		openSession();
		session.beginTransaction();
		
		assertCount(3);	
		session.enableFilter("iqRange").setParameter("min", 101).setParameter("max", 140);
		assertCount(1);	
		
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testPregnantFilter(){
		openSession();
		session.beginTransaction();
		
		assertCount(3);	
		session.enableFilter("pregnantOnly");
		assertCount(1);	
		
		session.getTransaction().commit();
		session.close();
	}
	@Test
	public void testNonHumanFilter(){
		openSession();
		session.beginTransaction();
		
		assertCount(3);	
		session.enableFilter("ignoreSome").setParameter("name", "Homo Sapiens");
		assertCount(0);	
		
		session.getTransaction().commit();
		session.close();
	}
	
	
	private void assertCount(long expected){
		long count = (Long) session.createQuery("select count(h) from Human h").uniqueResult();	
		Assert.assertEquals(expected, count);
	}

}
