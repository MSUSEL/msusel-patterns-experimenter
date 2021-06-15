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
package org.hibernate.test.annotations.various.readwriteexpression;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Emmanuel Bernard
 */
public class ColumnTransformerTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testCustomColumnReadAndWrite() throws Exception{
		Session s = openSession();
		Transaction t = s.beginTransaction();
		final double HEIGHT_INCHES = 73;
		final double HEIGHT_CENTIMETERS = HEIGHT_INCHES * 2.54d;

		Staff staff = new Staff(HEIGHT_INCHES, HEIGHT_INCHES, HEIGHT_INCHES*2, 1);
		s.persist( staff );
		s.flush();

		// Test value conversion during insert
		// Value returned by Oracle native query is a Types.NUMERIC, which is mapped to a BigDecimalType;
		// Cast returned value to Number then call Number.doubleValue() so it works on all dialects.
		double heightViaSql =
				( (Number)s.createSQLQuery("select size_in_cm from t_staff where t_staff.id=1").uniqueResult() )
						.doubleValue();
		assertEquals(HEIGHT_CENTIMETERS, heightViaSql, 0.01d);

		heightViaSql =
				( (Number)s.createSQLQuery("select radiusS from t_staff where t_staff.id=1").uniqueResult() )
						.doubleValue();
		assertEquals(HEIGHT_CENTIMETERS, heightViaSql, 0.01d);

		heightViaSql =
				( (Number)s.createSQLQuery("select diamet from t_staff where t_staff.id=1").uniqueResult() )
						.doubleValue();
		assertEquals(HEIGHT_CENTIMETERS*2, heightViaSql, 0.01d);

		// Test projection
		Double heightViaHql = (Double)s.createQuery("select s.sizeInInches from Staff s where s.id = 1").uniqueResult();
		assertEquals(HEIGHT_INCHES, heightViaHql, 0.01d);

		// Test restriction and entity load via criteria
		staff = (Staff)s.createCriteria(Staff.class)
			.add( Restrictions.between("sizeInInches", HEIGHT_INCHES - 0.01d, HEIGHT_INCHES + 0.01d))
			.uniqueResult();
		assertEquals(HEIGHT_INCHES, staff.getSizeInInches(), 0.01d);
		
		// Test predicate and entity load via HQL
		staff = (Staff)s.createQuery("from Staff s where s.sizeInInches between ? and ?")
			.setDouble(0, HEIGHT_INCHES - 0.01d)
			.setDouble(1, HEIGHT_INCHES + 0.01d)
			.uniqueResult();
		assertEquals(HEIGHT_INCHES, staff.getSizeInInches(), 0.01d);

		// Test update
		staff.setSizeInInches(1);
		s.flush();
		heightViaSql =
				( (Number)s.createSQLQuery("select size_in_cm from t_staff where t_staff.id=1").uniqueResult() )
						.doubleValue();
		assertEquals(2.54d, heightViaSql, 0.01d);
		s.delete(staff);
		t.commit();
		s.close();
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				Staff.class
		};
	}
}
