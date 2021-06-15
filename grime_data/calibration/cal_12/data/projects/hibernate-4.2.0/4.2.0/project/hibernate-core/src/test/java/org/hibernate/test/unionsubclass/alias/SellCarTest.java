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
package org.hibernate.test.unionsubclass.alias;

import org.junit.Test;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Strong Liu <stliu@redhat.com>
 */
@TestForIssue( jiraKey = "HHH-4825" )
public class SellCarTest extends BaseCoreFunctionalTestCase {
    public String[] getMappings() {
        return new String[] { "unionsubclass/alias/mapping.hbm.xml" };
    }

	@Test
    public void testSellCar() throws Exception {
        prepareData();
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery( "from Seller" );
        Seller seller = (Seller) query.uniqueResult();
        assertNotNull( seller );
        assertEquals( 1, seller.getBuyers().size() );
        tx.commit();
        session.close();
    }

    private void prepareData() {
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        session.save( createData() );
        tx.commit();
        session.close();
    }

    @SuppressWarnings( {"unchecked"})
	private Object createData() {
        Seller stliu = new Seller();
        stliu.setId( createID( "stliu" ) );
        CarBuyer zd = new CarBuyer();
        zd.setId( createID( "zd" ) );
        zd.setSeller( stliu );
        zd.setSellerName( stliu.getId().getName() );
        stliu.getBuyers().add( zd );
        return stliu;
    }

    @SuppressWarnings( {"UnnecessaryBoxing"})
	private PersonID createID( String name ) {
        PersonID id = new PersonID();
        id.setName( name );
        id.setNum( Long.valueOf( 100 ) );
        return id;
    }
}
