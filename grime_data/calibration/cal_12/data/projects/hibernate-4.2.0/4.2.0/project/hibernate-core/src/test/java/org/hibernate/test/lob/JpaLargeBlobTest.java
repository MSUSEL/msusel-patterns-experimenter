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
package org.hibernate.test.lob;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Random;

import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * @author Brett Meyer
 */
@TestForIssue( jiraKey = "HHH-7698" )
@RequiresDialect( value = H2Dialect.class, jiraKey = "HHH-7724" )
public class JpaLargeBlobTest extends BaseCoreFunctionalTestCase {
    
    @Override
    protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { LobEntity.class };
	}

    @Override
    protected void configure(Configuration configuration) {
    	super.configure( configuration );
        configuration.setProperty(Environment.USE_STREAMS_FOR_BINARY, "true");
    }

    @Test
    public void jpaBlobStream() throws Exception {
        Session session = openSession();
        LobEntity o = new LobEntity();

        LobHelper lh = session.getLobHelper();
        LobInputStream lis = new LobInputStream();

        session.getTransaction().begin();

        Blob blob = lh.createBlob(lis, LobEntity.BLOB_LENGTH);
        o.setBlob(blob);
        
        // Regardless if NON_CONTEXTUAL_LOB_CREATION is set to true,
        // ContextualLobCreator should use a NonContextualLobCreator to create
        // a blob Proxy.  If that's the case, the InputStream will not be read
        // until it's persisted with the JDBC driver.
        // Although HHH-7698 was about high memory consumption, this is the best
        // way to test that the high memory use is being prevented.
        assertFalse( lis.wasRead() );

        session.persist(o);
        session.getTransaction().commit();
        
        assertTrue( lis.wasRead() );
        
        session.close();

        lis.close();
    }
    
    private class LobInputStream extends InputStream {
    	private boolean read = false;
    	private Long count = (long) 200 * 1024 * 1024;

        @Override
        public int read() throws IOException {
        	read = true;
            if (count > 0) {
                count--;
                return new Random().nextInt();
            }
            return -1;
        }
        
        @Override
        public int available() throws IOException {
        	return 1;
        }
        
        public boolean wasRead() {
        	return read;
        }
    }
}