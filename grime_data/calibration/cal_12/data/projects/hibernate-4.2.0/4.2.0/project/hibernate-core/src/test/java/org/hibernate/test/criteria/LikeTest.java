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
package org.hibernate.test.criteria;
import java.util.List;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;


public class LikeTest extends BaseCoreFunctionalTestCase {
    public String[] getMappings() {
        return new String[]{"criteria/TestObject.hbm.xml"};
    }

	@Test
    public void testLike(){
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        TestObject obj = new TestObject();
        String uniq = "uniq" + System.currentTimeMillis();
        obj.setText( "XyZ " + uniq + " blablabla" );
        session.save( obj );
        session.flush();
        tx.commit();
        session.close();
        String pattern = "XyZ " + uniq + "%";
        // retrieve object - case sensitive - works ok
        session = openSession();
        tx = session.beginTransaction();
        List objects = session.createCriteria( TestObject.class ).add(
                Restrictions.like( "text", pattern ) ).list();
        assertEquals( 1, objects.size() );
        session.clear();

        // retrieve object - case insensitive - works ok
        objects = session.createCriteria( TestObject.class ).add(
                Restrictions.like( "text", pattern ).ignoreCase() ).list();

        assertEquals( 1, objects.size() );
        session.clear();
        if ( !( getDialect() instanceof MySQLDialect ) && ! ( getDialect() instanceof PostgreSQLDialect ) && ! ( getDialect() instanceof PostgreSQL81Dialect )) {
            // retrieve object - case insensitive via custom expression - works
            // ok
            objects = session.createCriteria( TestObject.class ).add(
                    StringExpression.stringExpression( "text", pattern, true ) )
                    .list();

            assertEquals( 1, objects.size() );
            session.clear();

            // retrieve object - case sensitive via custom expression - not
            // working
            objects = session.createCriteria( TestObject.class )
                    .add(
                            StringExpression.stringExpression( "text", pattern,
                                    false ) ).list();
            assertEquals( 1, objects.size() );
        }
        tx.rollback();
        session.close();
        
    }
}
