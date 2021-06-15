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
package org.hibernate.test.pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertFalse;

/**
 * HHH-5715 bug test case: Duplicated entries when using select distinct with join and pagination. The bug has to do
 * with new {@link SQLServerDialect} that uses row_number function for pagination
 * 
 * @author Valotasios Yoryos
 */
@TestForIssue( jiraKey = "HHH-5715" )
public class DistinctSelectTest extends BaseCoreFunctionalTestCase {
	private static final int NUM_OF_USERS = 30;

	@Override
	public String[] getMappings() {
		return new String[] { "pagination/EntryTag.hbm.xml" };
	}

	public void feedDatabase() {
		List<Tag> tags = new ArrayList<Tag>();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		for (int i = 0; i < 5; i++) {
			Tag tag = new Tag("Tag: " + UUID.randomUUID().toString());
			tags.add(tag);
			s.save(tag);
		}

		for (int i = 0; i < NUM_OF_USERS; i++) {
			Entry e = new Entry("Entry: " + UUID.randomUUID().toString());
			e.getTags().addAll(tags);
			s.save(e);
		}
		t.commit();
		s.close();
	}

	@SuppressWarnings( {"unchecked"})
	@Test
	public void testDistinctSelectWithJoin() {
		feedDatabase();

		Session s = openSession();

		List<Entry> entries = s.createQuery("select distinct e from Entry e join e.tags t where t.surrogate != null order by e.name").setFirstResult(10).setMaxResults(5).list();

		// System.out.println(entries);
		Entry firstEntry = entries.remove(0);
		assertFalse("The list of entries should not contain dublicated Entry objects as we've done a distinct select", entries.contains(firstEntry));

		s.close();
	}
}
