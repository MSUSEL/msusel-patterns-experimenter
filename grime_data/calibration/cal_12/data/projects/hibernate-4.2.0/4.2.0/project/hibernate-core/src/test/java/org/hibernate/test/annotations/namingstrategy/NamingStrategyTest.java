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
// $Id$
package org.hibernate.test.annotations.namingstrategy;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import org.jboss.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.EJB3NamingStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.Mappings;
import org.hibernate.mapping.Table;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.testing.ServiceRegistryBuilder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test harness for ANN-716.
 *
 * @author Hardy Ferentschik
 */
public class NamingStrategyTest {
	private static final Logger log = Logger.getLogger( NamingStrategyTest.class );

	private ServiceRegistry serviceRegistry;

	@Before
    public void setUp() {
		serviceRegistry = ServiceRegistryBuilder.buildServiceRegistry( Environment.getProperties() );
	}

	@After
    public void tearDown() {
        if (serviceRegistry != null) ServiceRegistryBuilder.destroy(serviceRegistry);
	}
    @Test
	public void testWithCustomNamingStrategy() throws Exception {
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.setNamingStrategy(new DummyNamingStrategy());
			config.addAnnotatedClass(Address.class);
			config.addAnnotatedClass(Person.class);
			config.buildSessionFactory( serviceRegistry );
		}
		catch( Exception e ) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
            log.debug(writer.toString());
			fail(e.getMessage());
		}
	}
    @Test
	public void testWithEJB3NamingStrategy() throws Exception {
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.setNamingStrategy(EJB3NamingStrategy.INSTANCE);
			config.addAnnotatedClass(A.class);
			config.addAnnotatedClass(AddressEntry.class);
			config.buildSessionFactory( serviceRegistry );
			Mappings mappings = config.createMappings();
			boolean foundIt = false;

			for ( Iterator iter = mappings.iterateTables(); iter.hasNext();  ) {
				Table table = (Table) iter.next();
                log.info("testWithEJB3NamingStrategy table = " + table.getName());
				if ( table.getName().equalsIgnoreCase("A_ADDRESS")) {
					foundIt = true;
				}
				// make sure we use A_ADDRESS instead of AEC_address
				assertFalse("got table name mapped to: AEC_address (should be A_ADDRESS) which violates JPA-2 spec section 11.1.8 ([OWNING_ENTITY_NAME]_[COLLECTION_ATTRIBUTE_NAME])",table.getName().equalsIgnoreCase("AEC_address"));
			}
			assertTrue("table not mapped to A_ADDRESS which violates JPA-2 spec section 11.1.8",foundIt);
		}
		catch( Exception e ) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
            log.debug(writer.toString());
			fail(e.getMessage());
		}
	}
    @Test
	public void testWithoutCustomNamingStrategy() throws Exception {
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Address.class);
			config.addAnnotatedClass(Person.class);
			config.buildSessionFactory( serviceRegistry );
		}
		catch( Exception e ) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
            log.debug(writer.toString());
			fail(e.getMessage());
		}
	}
}
