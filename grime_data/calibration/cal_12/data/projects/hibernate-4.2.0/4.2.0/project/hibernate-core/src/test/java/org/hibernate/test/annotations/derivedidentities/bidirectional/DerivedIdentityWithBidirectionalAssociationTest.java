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
package org.hibernate.test.annotations.derivedidentities.bidirectional;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.test.util.SchemaUtil;
import org.hibernate.testing.Skip;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@Skip( condition = Skip.AlwaysSkip.class,message = "sdf")
public class DerivedIdentityWithBidirectionalAssociationTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testBidirectionalAssociation() throws Exception {
		assertTrue( SchemaUtil.isColumnPresent( "Dependent", "emp_empId", configuration() ) );
		assertTrue( !SchemaUtil.isColumnPresent( "Dependent", "empPK", configuration() ) );
		Employee e = new Employee();
		e.empId = 1;
		e.empName = "Emmanuel";

		Session s = openSession();
		s.getTransaction().begin();
		s.persist( e );
		Dependent d = new Dependent();
		d.emp = e;
		s.persist( d );
		s.flush();
		s.clear();
		d = getDerivedClassById( e, s, Dependent.class );
		assertEquals( e.empId, d.emp.empId );
		s.getTransaction().rollback();
		s.close();
	}

	private <T> T getDerivedClassById(Employee e, Session s, Class<T> clazz) {
		return clazz.cast( s.createQuery( "from " + clazz.getName() + " d where d.emp.empId = :empId" )
						.setParameter( "empId", e.empId ).uniqueResult() );
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				Dependent.class,
				Employee.class
		};
	}
}