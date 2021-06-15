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
package org.hibernate.test.annotations.filter.subclass.joined;

import junit.framework.Assert;

import org.hibernate.test.annotations.filter.subclass.SubClassTest;
import org.junit.Test;

public class JoinedSubClassTest extends SubClassTest{

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[]{Animal.class, Mammal.class, Human.class, Club.class};
	}
	
	@Override
	protected void cleanupTest() throws Exception {
		super.cleanupTest();
		openSession();
		session.beginTransaction();
		
		session.createQuery("delete from Club").executeUpdate();
		
		session.getTransaction().commit();
		session.close();
	}	
	
	@Override
	protected void persistTestData() {
		Club club = new Club();
		club.setName("Mensa applicants");
		club.getMembers().add(createHuman(club, false, 90));
		club.getMembers().add(createHuman(club, false, 100));
		club.getMembers().add(createHuman(club, true, 110));
		session.persist(club);
	}

	@Test
	public void testClub(){
		openSession();
		session.beginTransaction();

		Club club =  (Club) session.createQuery("from Club").uniqueResult();
		Assert.assertEquals(3, club.getMembers().size());
		session.clear();
		
		session.enableFilter("pregnantMembers");
		club =  (Club) session.createQuery("from Club").uniqueResult();
		Assert.assertEquals(1, club.getMembers().size());
		session.clear();
		
		session.enableFilter("iqMin").setParameter("min", 148);
		club =  (Club) session.createQuery("from Club").uniqueResult();
		Assert.assertEquals(0, club.getMembers().size());
		
		session.getTransaction().commit();
		session.close();
	}
	
	private Human createHuman(Club club, boolean pregnant, int iq){
		Human human = new Human();
		human.setClub(club);
		human.setName("Homo Sapiens");
		human.setPregnant(pregnant);
		human.setIq(iq);
		session.persist(human);
		return human;
	}
	

}
