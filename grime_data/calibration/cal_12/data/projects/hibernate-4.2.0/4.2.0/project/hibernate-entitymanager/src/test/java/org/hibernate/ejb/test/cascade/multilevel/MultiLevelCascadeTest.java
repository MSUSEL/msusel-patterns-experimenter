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
package org.hibernate.ejb.test.cascade.multilevel;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Test;

import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;
import org.hibernate.testing.TestForIssue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class MultiLevelCascadeTest extends BaseEntityManagerFunctionalTestCase {

	@TestForIssue( jiraKey = "HHH-5299" )
    @Test
    public void test() {
		EntityManager em = getOrCreateEntityManager( );
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Top top = new Top();
        em.persist( top );
        // Flush 1
        em.flush();
 
        Middle middle = new Middle( 1l );
        top.addMiddle( middle );
		middle.setTop( top );
		Bottom bottom = new Bottom();
		middle.setBottom( bottom );
		bottom.setMiddle( middle );
 
        Middle middle2 = new Middle( 2l );
		top.addMiddle(middle2);
		middle2.setTop( top );
		Bottom bottom2 = new Bottom();
        middle2.setBottom( bottom2 );
		bottom2.setMiddle( middle2 );
        // Flush 2
        em.flush();
        tx.commit();
        em.close();

        em = getOrCreateEntityManager();
        tx = em.getTransaction();
        tx.begin();

        top = em.find(Top.class, top.getId());

        assertEquals(2, top.getMiddles().size());
        for (Middle loadedMiddle : top.getMiddles()) {
            assertSame(top, loadedMiddle.getTop());
            assertNotNull(loadedMiddle.getBottom());
        }
		em.remove( top );
        em.close();
    }

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] { Top.class, Middle.class, Bottom.class };
	}

}
