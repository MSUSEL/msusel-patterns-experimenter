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
package org.hibernate.test.annotations.derivedidentities.e1.a;

import java.util.List;

import org.junit.Test;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.test.util.SchemaUtil;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Emmanuel Bernard
 */
public class DerivedIdentitySimpleParentIdClassDepTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testManyToOne() throws Exception {
		assertTrue( SchemaUtil.isColumnPresent( "Dependent", "emp_empId", configuration() ) );
		assertTrue( ! SchemaUtil.isColumnPresent( "Dependent", "emp", configuration() ) );

		Session s = openSession();
		s.getTransaction().begin();
		Employee e = new Employee( 1L, "Emmanuel", "Manu" );
		Dependent d = new Dependent( "Doggy", e );
		s.persist( d );
		s.persist( e );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.getTransaction().begin();
		DependentId dId = new DependentId( d.getName(), d.getEmp().empId );
		d = (Dependent) s.get( Dependent.class, dId );
		assertEquals( e.empId, d.getEmp().empId );
		assertEquals( e.empName, d.getEmp().empName );
		assertEquals( e.nickname, d.getEmp().nickname );
		s.delete( d );
		s.delete( d.getEmp() );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testQueryNewEntityInPC() throws Exception {
		Session s = openSession();
		s.getTransaction().begin();
		Employee e = new Employee( 1L, "Paula", "P" );
		Dependent d = new Dependent( "LittleP", e );
		d.setEmp(e);
		s.persist( d );
		s.persist( e );

		// find the entity added above
		Query query = s.createQuery("Select d from Dependent d where d.name='LittleP' and d.emp.empName='Paula'");
		List depList = query.list();
		assertEquals( 1, depList.size() );
		Object newDependent = (Dependent) depList.get(0);
		assertSame( d, newDependent );
		s.getTransaction().rollback();
		s.close();
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				Dependent.class,
				Employee.class
		};
	}
}