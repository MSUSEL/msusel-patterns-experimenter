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
import junit.framework.TestCase;

import org.hibernate.CacheMode;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.test.instrument.domain.Document;
import org.hibernate.test.instrument.domain.Folder;
import org.hibernate.test.instrument.domain.Owner;

/**
 * @author Steve Ebersole
 */
public class TestLazyExecutable extends AbstractExecutable {
	public void execute() {
		// The following block is repeated 100 times to reproduce HHH-2627.
		// Without the fix, Oracle will run out of cursors using 10g with
		// a default installation (ORA-01000: maximum open cursors exceeded).
		// The number of loops may need to be adjusted depending on the how
		// Oracle is configured.
		// Note: The block is not indented to avoid a lot of irrelevant differences.
		for ( int i=0; i<100; i++ ) {

		SessionFactory factory = getFactory();
		Session s = factory.openSession();
		Transaction t = s.beginTransaction();
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
		fol.getDocuments().add(doc);
		s.save(o);
		s.save(fol);
		t.commit();
		s.close();

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		doc = ( Document ) s.get( Document.class, doc.getId() );
		TestCase.assertTrue( Hibernate.isPropertyInitialized(doc, "weirdProperty"));
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "name"));
		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "text"));
		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "upperCaseName"));
		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "folder"));
		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "owner"));
		doc.getUpperCaseName();  // should force initialization
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "text"));
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "weirdProperty"));
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "upperCaseName"));
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "folder"));
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "owner"));
		t.commit();
		s.close();

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		doc.getName();
		TestCase.assertEquals( doc.getText(), "blah blah" );
		t.commit();
		s.close();

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		doc.getName();
		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "text"));
		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "summary"));
		TestCase.assertEquals( doc.getText(), "blah blah" );
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "text"));
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "summary"));
		t.commit();
		s.close();

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		doc.setName("HiA");
		t.commit();
		s.close();

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		TestCase.assertEquals( doc.getName(), "HiA" );
		TestCase.assertEquals( doc.getText(), "blah blah" );
		t.commit();
		s.close();

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		doc.getText();
		doc.setName("HiA second edition");
		t.commit();
		s.close();

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "weirdProperty"));
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "name"));
		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "text"));
		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "upperCaseName"));
		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "owner"));
		TestCase.assertEquals( doc.getName(), "HiA second edition" );
		TestCase.assertEquals( doc.getText(), "blah blah" );
		TestCase.assertEquals( doc.getUpperCaseName(), "HIA SECOND EDITION" );
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "text"));
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "weirdProperty"));
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "upperCaseName"));
		t.commit();
		s.close();

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		t.commit();
		s.close();

		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "text"));

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		s.lock(doc, LockMode.NONE);
		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "text"));
		TestCase.assertEquals( doc.getText(), "blah blah" );
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "text"));
		t.commit();
		s.close();

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		t.commit();
		s.close();

		doc.setName("HiA2");

		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "text"));

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		s.saveOrUpdate(doc);
		s.flush();
		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "text"));
		TestCase.assertEquals( doc.getText(), "blah blah" );
		TestCase.assertTrue(Hibernate.isPropertyInitialized(doc, "text"));
		doc.updateText("blah blah blah blah");
		t.commit();
		s.close();

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		doc = ( Document ) s.createQuery("from Document").uniqueResult();
		TestCase.assertEquals( doc.getName(), "HiA2" );
		TestCase.assertEquals( doc.getText(), "blah blah blah blah" );
		t.commit();
		s.close();

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		doc = (Document) s.load( Document.class, doc.getId() );
		doc.getName();
		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "text"));
		TestCase.assertFalse(Hibernate.isPropertyInitialized(doc, "summary"));
		t.commit();
		s.close();

		s = factory.openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		//s.delete(doc);
		s.delete( doc.getFolder() );
		s.delete( doc.getOwner() );
		s.flush();
		t.commit();
		s.close();

		}

	}

}
