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
package org.hibernate.test.resulttransformer;

import java.util.List;

import org.junit.Test;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.hibernate.transform.ResultTransformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Sharath Reddy
 */
public class ResultTransformerTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "resulttransformer/Contract.hbm.xml" };
	}

	@Test
	@TestForIssue( jiraKey = "HHH-3694" )
	public void testResultTransformerIsAppliedToScrollableResults() throws Exception
	{
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		PartnerA a = new PartnerA();
		a.setName("Partner A");
		PartnerB b = new PartnerB();
		b.setName("Partner B");
		Contract obj1 = new Contract();
		obj1.setName("Contract");
		obj1.setA(a);
		obj1.setB(b);
		s.save(a);
		s.save(b);
		s.save(obj1);

		tx.commit();
		s.close();

		s = openSession();

		Query q = s.getNamedQuery(Contract.class.getName() + ".testQuery");
		q.setFetchSize(100);
		q.setResultTransformer(new ResultTransformer() {

			private static final long serialVersionUID = -5815434828170704822L;

			public Object transformTuple(Object[] arg0, String[] arg1)
			{
				// return only the PartnerA object from the query
				return arg0[1];
			}

			@SuppressWarnings("unchecked")
			public List transformList(List arg0)
			{
				return arg0;
			}
		});
		ScrollableResults sr = q.scroll();
		sr.first();
		Object[] row = sr.get();
		assertEquals(1, row.length);
		Object obj = row[0];
		assertTrue(obj instanceof PartnerA);
		PartnerA obj2 = (PartnerA) obj;
		assertEquals("Partner A", obj2.getName());
		s.close();
	}
}


