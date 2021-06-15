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
package org.hibernate.test.annotations.derivedidentities.e1.b;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.test.util.SchemaUtil;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Emmanuel Bernard
 */
public class DerivedIdentitySimpleParentEmbeddedIdDepTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testManyToOne() throws Exception {
		assertTrue( SchemaUtil.isColumnPresent( "Dependent", "emp_empId", configuration() ) );
		assertTrue( ! SchemaUtil.isColumnPresent( "Dependent", "empPK", configuration() ) );
		Employee e = new Employee();
		e.empId = 1;
		e.empName = "Emmanuel";
		Session s = openSession(  );
		s.getTransaction().begin();

		Dependent d = new Dependent();
		d.emp = e;
		d.id = new DependentId();
		d.id.name = "Doggy";
		s.persist( d );
		s.persist( e );
		s.flush();
		s.clear();
		d = (Dependent) s.get( Dependent.class, d.id );
		assertEquals( d.id.empPK, d.emp.empId );
		s.getTransaction().rollback();
		s.close();
	}

	@Test
	public void testOneToOne() throws Exception {
		assertTrue( SchemaUtil.isColumnPresent( "ExclusiveDependent", "FK", configuration() ) );
		assertTrue( ! SchemaUtil.isColumnPresent( "ExclusiveDependent", "empPK", configuration() ) );
		Employee e = new Employee();
		e.empId = 1;
		e.empName = "Emmanuel";
		Session s = openSession(  );
		s.getTransaction().begin();
		s.persist( e );
		ExclusiveDependent d = new ExclusiveDependent();
		d.emp = e;
		d.id = new DependentId();
		d.id.name = "Doggy";
		//d.id.empPK = e.empId; //FIXME not needed when foreign is enabled
		s.persist( d );
		s.flush();
		s.clear();
		d = (ExclusiveDependent) s.get( ExclusiveDependent.class, d.id );
		assertEquals( d.id.empPK, d.emp.empId );
		s.getTransaction().rollback();
		s.close();
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				Dependent.class,
				Employee.class,
				ExclusiveDependent.class
		};
	}
}
