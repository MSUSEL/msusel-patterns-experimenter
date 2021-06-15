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
//$Id$
package org.hibernate.test.annotations.backquotes;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.jboss.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.testing.ServiceRegistryBuilder;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Testcase for ANN-718 - @JoinTable / @JoinColumn fail when using backquotes in PK field name.
 *
 * @author Hardy Ferentschik
 *
 */
public class BackquoteTest extends BaseUnitTestCase {
	private static final Logger log = Logger.getLogger( BackquoteTest.class );

	private ServiceRegistry serviceRegistry;
    private SessionFactory sessionFactory;

	@Before
    public void setUp() {
		serviceRegistry = ServiceRegistryBuilder.buildServiceRegistry( Environment.getProperties() );
	}

	@After
    public void tearDown() {
        if(sessionFactory !=null) sessionFactory.close();
        if (serviceRegistry != null) ServiceRegistryBuilder.destroy(serviceRegistry);
	}

	@Test
	@TestForIssue( jiraKey = "ANN-718" )
	public void testBackquotes() {
		try {
			Configuration config = new Configuration();
			config.addAnnotatedClass(Bug.class);
			config.addAnnotatedClass(Category.class);
			sessionFactory = config.buildSessionFactory( serviceRegistry );
		}
		catch( Exception e ) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
            log.debug(writer.toString());
			fail(e.getMessage());
		}
	}

	/**
	 *  HHH-4647 : Problems with @JoinColumn referencedColumnName and quoted column and table names
	 *
	 *  An invalid referencedColumnName to an entity having a quoted table name results in an
	 *  infinite loop in o.h.c.Configuration$MappingsImpl#getPhysicalColumnName().
	 *  The same issue exists with getLogicalColumnName()
	 */
	@Test
	@TestForIssue( jiraKey = "HHH-4647" )
	public void testInvalidReferenceToQuotedTableName() {
    	try {
    		Configuration config = new Configuration();
    		config.addAnnotatedClass(Printer.class);
    		config.addAnnotatedClass(PrinterCable.class);
    		sessionFactory = config.buildSessionFactory( serviceRegistry );
    		fail("expected MappingException to be thrown");
    	}
    	//we WANT MappingException to be thrown
        catch( MappingException e ) {
        	assertTrue("MappingException was thrown", true);
        }
        catch(Exception e) {
        	StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
            log.debug(writer.toString());
        	fail(e.getMessage());
        }
	}
}
