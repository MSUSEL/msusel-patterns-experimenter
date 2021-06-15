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
package org.hibernate.test.flush;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * Thanks to Jan Hodac and Laurent Almeras for providing test cases for this
 * issue.
 * 
 * @author Guillaume Smet
 */
@TestForIssue(jiraKey = "HHH-7821")
public class TestClearBatchFetchQueueAfterFlush extends BaseCoreFunctionalTestCase {

	public void configure(Configuration cfg) {
		cfg.setProperty( Environment.DEFAULT_BATCH_FETCH_SIZE, "10" );
	}

	@Test
	public void testClearBatchFetchQueueAfterFlush() {
		Session s = openSession();
		s.beginTransaction();

		Author author1 = new Author( "David Lodge" );
		author1.getBooks().add( new Book( "A Man of Parts", author1 ) );
		author1.getBooks().add( new Book( "Thinks...", author1 ) );
		author1.getBooks().add( new Book( "Therapy", author1 ) );
		s.save( author1 );

		Iterator<Book> bookIterator = author1.getBooks().iterator();

		BookStore bookStore1 = new BookStore( "Passages" );
		bookStore1.getBooks().add( bookIterator.next() );
		s.save( bookStore1 );

		BookStore bookStore2 = new BookStore( "Librairie du Tramway" );
		bookStore2.getBooks().add( bookIterator.next() );
		s.save( bookStore2 );

		BookStore bookStore3 = new BookStore( "Le Bal des Ardents" );
		bookStore3.getBooks().add( bookIterator.next() );
		s.save( bookStore3 );

		s.flush();
		s.getTransaction().commit();
		s.clear();

		bookStore1 = (BookStore) s.load( BookStore.class, bookStore1.getId() );
		bookStore2 = (BookStore) s.load( BookStore.class, bookStore2.getId() );
		bookStore3 = (BookStore) s.load( BookStore.class, bookStore3.getId() );

		s.beginTransaction();
		s.delete( bookStore2 );
		s.getTransaction().commit();

		s.flush();

		bookStore1.getBooks().size();
		bookStore3.getBooks().size();
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { Author.class, Book.class, Publisher.class, BookStore.class };
	}

}
