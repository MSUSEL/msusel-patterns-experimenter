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
package org.hibernate.test.instrument.cases;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.instrument.domain.Document;
import org.hibernate.test.instrument.domain.Folder;
import org.hibernate.test.instrument.domain.Owner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Rob.Hasselbaum
 */
public class TestCustomColumnReadAndWrite extends AbstractExecutable {
	public void execute() {
		Session s = getFactory().openSession();
		Transaction t = s.beginTransaction();
		final double SIZE_IN_KB = 20480;
		final double SIZE_IN_MB = SIZE_IN_KB / 1024d;
		Owner o = new Owner();
		Document doc = new Document();
		Folder fol = new Folder();
		o.setName("gavin");
		doc.setName("Hibernate in Action");
		doc.setSummary("blah");
		doc.updateText("blah blah");	
		fol.setName("books");
		doc.setOwner(o);
		doc.setFolder(fol);
		doc.setSizeKb(SIZE_IN_KB);
		fol.getDocuments().add(doc);
		s.persist(o);
		s.persist(fol);
		t.commit();
		s.close();

		s = getFactory().openSession();
		t = s.beginTransaction();
		
		// Check value conversion on insert
		// Value returned by Oracle native query is a Types.NUMERIC, which is mapped to a BigDecimalType;
		// Cast returned value to Number then call Number.doubleValue() so it works on all dialects.
		Double sizeViaSql =
				( (Number)s.createSQLQuery("select size_mb from documents").uniqueResult() )
						.doubleValue();
		assertEquals( SIZE_IN_MB, sizeViaSql, 0.01d );

		// Test explicit fetch of all properties
		doc = (Document) s.createQuery("from Document fetch all properties").uniqueResult();
		assertTrue( Hibernate.isPropertyInitialized( doc, "sizeKb" ) );
		assertEquals( SIZE_IN_KB, doc.getSizeKb() );
		t.commit();
		s.close();		

		// Test lazy fetch with custom read
		s = getFactory().openSession();
		t = s.beginTransaction();
		doc = (Document) s.get( Document.class, doc.getId() );
		assertFalse( Hibernate.isPropertyInitialized( doc, "sizeKb" ) );
		assertEquals( SIZE_IN_KB, doc.getSizeKb() );
		s.delete(doc);
		s.delete( doc.getOwner() );
		s.delete( doc.getFolder() );
		t.commit();
		s.close();
	}
}
