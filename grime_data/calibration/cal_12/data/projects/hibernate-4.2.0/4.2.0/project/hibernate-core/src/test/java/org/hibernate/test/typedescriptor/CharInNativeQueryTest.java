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
 package org.hibernate.test.typedescriptor;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

 /**
  * @author Strong Liu
  */
public class CharInNativeQueryTest extends BaseCoreFunctionalTestCase {
    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[] {
                Issue.class
        };
    }
    @Test
    @TestForIssue(jiraKey = "HHH-2304")
    public void testNativeQuery() {
        Issue issue = new Issue();
        issue.setIssueNumber( "HHH-2304" );
        issue.setDescription( "Wrong type detection for sql type char(x) columns" );

        Session session = openSession();
        session.beginTransaction();
        session.persist( issue );
        session.getTransaction().commit();
        session.close();

        session = openSession(  );
        session.beginTransaction();
        Object issueNumber = session.createSQLQuery( "select issue.issueNumber from Issue issue" ).uniqueResult();
        session.getTransaction().commit();
        session.close();

        assertNotNull( issueNumber );
        assertTrue( issueNumber instanceof String );
        assertEquals( "HHH-2304", issueNumber );


    }

 }