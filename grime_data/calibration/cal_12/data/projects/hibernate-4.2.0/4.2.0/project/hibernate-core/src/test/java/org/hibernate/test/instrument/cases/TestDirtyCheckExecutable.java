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
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.instrument.domain.Folder;

/**
 * @author Steve Ebersole
 */
public class TestDirtyCheckExecutable extends AbstractExecutable {
	public void execute() {
		Session s = getFactory().openSession();
		Transaction t = s.beginTransaction();
		Folder pics = new Folder();
		pics.setName("pics");
		Folder docs = new Folder();
		docs.setName("docs");
		s.persist(docs);
		s.persist(pics);
		t.commit();
		s.close();

		s = getFactory().openSession();
		t = s.beginTransaction();
		List list = s.createCriteria(Folder.class).list();
		for ( Iterator iter = list.iterator(); iter.hasNext(); ) {
			Folder f = (Folder) iter.next();
			Assert.assertFalse( f.nameWasread );
		}
		t.commit();
		s.close();

		for ( Iterator iter = list.iterator(); iter.hasNext(); ) {
			Folder f = (Folder) iter.next();
			Assert.assertFalse( f.nameWasread );
		}

		s = getFactory().openSession();
		t = s.beginTransaction();
		s.createQuery("delete from Folder").executeUpdate();
		t.commit();
		s.close();
	}
}
