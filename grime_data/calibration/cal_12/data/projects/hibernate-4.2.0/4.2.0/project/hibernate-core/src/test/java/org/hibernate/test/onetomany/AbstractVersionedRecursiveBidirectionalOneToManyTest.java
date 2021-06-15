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
package org.hibernate.test.onetomany;

import org.hibernate.Session;
import org.hibernate.Transaction;

import static org.junit.Assert.assertEquals;

/**
 * @author Burkhard Graves
 * @author Gail Badner
 */
public abstract class AbstractVersionedRecursiveBidirectionalOneToManyTest extends AbstractRecursiveBidirectionalOneToManyTest {
	@Override
	public String[] getMappings() {
		return new String[] { "onetomany/VersionedNode.hbm.xml" };
	}

	@Override
	@SuppressWarnings( {"UnnecessaryBoxing"})
	void check(boolean simplePropertyUpdated) {
		super.check( simplePropertyUpdated );
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Node node1 = ( Node ) s.get( Node.class, Integer.valueOf( 1 ) );
		Node node2 = ( Node ) s.get( Node.class, Integer.valueOf( 2 ) );
		Node node3 = ( Node ) s.get( Node.class, Integer.valueOf( 3 ) );
		assertEquals( 1, node1.getVersion() );
		assertEquals( 1, node2.getVersion() );
		assertEquals( 1, node3.getVersion() );
		tx.commit();
		s.close();
	}
}
