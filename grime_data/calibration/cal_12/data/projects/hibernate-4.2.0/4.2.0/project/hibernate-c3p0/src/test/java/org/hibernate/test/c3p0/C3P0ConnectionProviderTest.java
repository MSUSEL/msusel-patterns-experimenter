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
package org.hibernate.test.c3p0;

import java.lang.management.ManagementFactory;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.junit.Test;

import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Strong Liu
 */
public class C3P0ConnectionProviderTest extends BaseCoreFunctionalTestCase {

    @Test
    public void testC3P0isDefaultWhenThereIsC3P0Properties() {
        JdbcServices jdbcServices = serviceRegistry().getService( JdbcServices.class );
        ConnectionProvider provider = jdbcServices.getConnectionProvider();
        assertTrue( provider instanceof C3P0ConnectionProvider );

    }

    @Test
    public void testHHH6635() throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> set = mBeanServer.queryNames( null, null );
        boolean mbeanfound = false;
        for ( ObjectName obj : set ) {
            if ( obj.getKeyPropertyListString().indexOf( "PooledDataSource" ) > 0 ) {
                mbeanfound = true;

                // see according c3p0 settings in META-INF/persistence.xml

                int actual_minPoolSize = (Integer) mBeanServer.getAttribute( obj, "minPoolSize" );
                assertEquals( 50, actual_minPoolSize );

                int actual_maxPoolSize = (Integer) mBeanServer.getAttribute( obj, "maxPoolSize" );
                assertEquals( 800, actual_maxPoolSize );

                int actual_maxStatements = (Integer) mBeanServer.getAttribute( obj, "maxStatements" );
                assertEquals( 50, actual_maxStatements );

                int actual_maxIdleTime = (Integer) mBeanServer.getAttribute( obj, "maxIdleTime" );
                assertEquals( 300, actual_maxIdleTime );

                int actual_idleConnectionTestPeriod = (Integer) mBeanServer.getAttribute(
                        obj,
                        "idleConnectionTestPeriod"
                );
                assertEquals( 3000, actual_idleConnectionTestPeriod );
                break;
            }
        }

        assertTrue( "PooledDataSource BMean not found, please verify version of c3p0", mbeanfound );
    }
}
