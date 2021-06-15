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
package org.hibernate.test.math;

import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * @author Brett Meyer
 */
@RequiresDialect( value = { Oracle8iDialect.class, H2Dialect.class } )
public class MathTest extends BaseCoreFunctionalTestCase {

	@Override
	public String[] getMappings() {
		return new String[]{"math/Math.hbm.xml"};
	}
	
	@Test
	public void testBitAnd() {
		MathEntity me = new MathEntity();
		me.setValue( 5 );
		
		Session s = openSession();
		s.beginTransaction();
		Long id = (Long) s.save( me );
		s.getTransaction().commit();
		s.close();
		
		s = openSession();
		s.beginTransaction();
		int value1 = ((Integer) s.createQuery( "select bitand(m.value,0) from MathEntity m where m.id=" + id ).uniqueResult()).intValue();
		int value2 = ((Integer) s.createQuery( "select bitand(m.value,2) from MathEntity m where m.id=" + id ).uniqueResult()).intValue();
		int value3 = ((Integer )s.createQuery( "select bitand(m.value,3) from MathEntity m where m.id=" + id ).uniqueResult()).intValue();
		s.getTransaction().commit();
		s.close();

		assertEquals(value1, 0);
		assertEquals(value2, 0);
		assertEquals(value3, 1);
	}

}
