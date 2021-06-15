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
package org.hibernate.test.stateless;

import java.util.Date;

import org.junit.Test;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

/**
 * @author Gavin King
 */
public class StatelessSessionTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "stateless/Document.hbm.xml" };
	}

	@Test
	public void testCreateUpdateReadDelete() {
		StatelessSession ss = sessionFactory().openStatelessSession();
		Transaction tx = ss.beginTransaction();
		Document doc = new Document("blah blah blah", "Blahs");
		ss.insert(doc);
		assertNotNull( doc.getName() );
		Date initVersion = doc.getLastModified();
		assertNotNull( initVersion );
		tx.commit();
		
		tx = ss.beginTransaction();
		doc.setText("blah blah blah .... blah");
		ss.update(doc);
		assertNotNull( doc.getLastModified() );
		assertNotSame( doc.getLastModified(), initVersion );
		tx.commit();
		
		tx = ss.beginTransaction();
		doc.setText("blah blah blah .... blah blay");
		ss.update(doc);
		tx.commit();
		
		Document doc2 = (Document) ss.get(Document.class.getName(), "Blahs");
		assertEquals("Blahs", doc2.getName());
		assertEquals(doc.getText(), doc2.getText());
				
		doc2 = (Document) ss.createQuery("from Document where text is not null").uniqueResult();
		assertEquals("Blahs", doc2.getName());
		assertEquals(doc.getText(), doc2.getText());
		
		ScrollableResults sr = ss.createQuery("from Document where text is not null")
			.scroll(ScrollMode.FORWARD_ONLY);
		sr.next();
		doc2 = (Document) sr.get(0);
		sr.close();
		assertEquals("Blahs", doc2.getName());
		assertEquals(doc.getText(), doc2.getText());
				
		doc2 = (Document) ss.createSQLQuery("select * from Document")
			.addEntity(Document.class)
			.uniqueResult();
		assertEquals("Blahs", doc2.getName());
		assertEquals(doc.getText(), doc2.getText());
				
		doc2 = (Document) ss.createCriteria(Document.class).uniqueResult();
		assertEquals("Blahs", doc2.getName());
		assertEquals(doc.getText(), doc2.getText());
		
		sr = ss.createCriteria(Document.class).scroll(ScrollMode.FORWARD_ONLY);
		sr.next();
		doc2 = (Document) sr.get(0);
		sr.close();
		assertEquals("Blahs", doc2.getName());
		assertEquals(doc.getText(), doc2.getText());

		tx = ss.beginTransaction();
		ss.delete(doc);
		tx.commit();
		ss.close();
	}

	@Test
	public void testHqlBulk() {
		StatelessSession ss = sessionFactory().openStatelessSession();
		Transaction tx = ss.beginTransaction();
		Document doc = new Document("blah blah blah", "Blahs");
		ss.insert(doc);
		Paper paper = new Paper();
		paper.setColor( "White" );
		ss.insert(paper);
		tx.commit();

		tx = ss.beginTransaction();
		int count = ss.createQuery( "update Document set name = :newName where name = :oldName" )
				.setString( "newName", "Foos" )
				.setString( "oldName", "Blahs" )
				.executeUpdate();
		assertEquals( "hql-update on stateless session", 1, count );
		count = ss.createQuery( "update Paper set color = :newColor" )
				.setString( "newColor", "Goldenrod" )
				.executeUpdate();
		assertEquals( "hql-update on stateless session", 1, count );
		tx.commit();

		tx = ss.beginTransaction();
		count = ss.createQuery( "delete Document" ).executeUpdate();
		assertEquals( "hql-delete on stateless session", 1, count );
		count = ss.createQuery( "delete Paper" ).executeUpdate();
		assertEquals( "hql-delete on stateless session", 1, count );
		tx.commit();
		ss.close();
	}

	@Test
	public void testInitId() {
		StatelessSession ss = sessionFactory().openStatelessSession();
		Transaction tx = ss.beginTransaction();
		Paper paper = new Paper();
		paper.setColor( "White" );
		ss.insert(paper);
		assertNotNull( paper.getId() );
		tx.commit();

		tx = ss.beginTransaction();
		ss.delete( ss.get( Paper.class, paper.getId() ) );
		tx.commit();
		ss.close();
	}

	@Test
	public void testRefresh() {
		StatelessSession ss = sessionFactory().openStatelessSession();
		Transaction tx = ss.beginTransaction();
		Paper paper = new Paper();
		paper.setColor( "whtie" );
		ss.insert( paper );
		tx.commit();
		ss.close();

		ss = sessionFactory().openStatelessSession();
		tx = ss.beginTransaction();
		Paper p2 = ( Paper ) ss.get( Paper.class, paper.getId() );
		p2.setColor( "White" );
		ss.update( p2 );
		tx.commit();
		ss.close();

		ss = sessionFactory().openStatelessSession();
		tx = ss.beginTransaction();
		assertEquals( "whtie", paper.getColor() );
		ss.refresh( paper );
		assertEquals( "White", paper.getColor() );
		ss.delete( paper );
		tx.commit();
		ss.close();
	}

}

