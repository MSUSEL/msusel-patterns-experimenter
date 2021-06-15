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
package org.hibernate.test.annotations.filter.secondarytable;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Assert;
import org.junit.Test;

public class SecondaryTableTest extends BaseCoreFunctionalTestCase {

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] {User.class};
	}

	@Override
	protected void prepareTest() throws Exception {
		openSession();
		insertUser("q@s.com", 21, false, "a1", "b");
		insertUser("r@s.com", 22, false, "a2", "b");
		insertUser("s@s.com", 23, true, "a3", "b");
		insertUser("t@s.com", 24, false, "a4", "b");
		session.flush();
	}
	
	@Test
	public void testFilter(){
		Assert.assertEquals(Long.valueOf(4), session.createQuery("select count(u) from User u").uniqueResult());
		session.enableFilter("ageFilter").setParameter("age", 24);
		Assert.assertEquals(Long.valueOf(2), session.createQuery("select count(u) from User u").uniqueResult());
	}
	
	private void insertUser(String emailAddress, int age, boolean lockedOut, String username, String password){
		User user = new User();
		user.setEmailAddress(emailAddress);
		user.setAge(age);
		user.setLockedOut(lockedOut);
		user.setUsername(username);
		user.setPassword(password);
		session.persist(user);
	}

}
