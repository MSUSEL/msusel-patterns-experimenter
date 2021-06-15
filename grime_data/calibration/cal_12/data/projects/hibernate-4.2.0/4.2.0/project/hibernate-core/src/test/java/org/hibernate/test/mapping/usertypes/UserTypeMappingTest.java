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
package org.hibernate.test.mapping.usertypes;


import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.testing.ServiceRegistryBuilder;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseUnitTestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for read-order independent resolution of user-defined types
 * Testcase for issue HHH-7300
 * @author Stefan Schulze
 */
@TestForIssue(jiraKey = "HHH-7300")
public class UserTypeMappingTest extends BaseUnitTestCase{

private Configuration cfg;
private ServiceRegistry serviceRegistry;

	@Before
	public void setup(){
		cfg=new Configuration();
		Properties p = new Properties();
		p.put( Environment.DIALECT, "org.hibernate.dialect.HSQLDialect" );
		p.put( "hibernate.connection.driver_class", "org.h2.Driver" );
		p.put( "hibernate.connection.url", "jdbc:h2:mem:" );
		p.put( "hibernate.connection.username", "sa" );
		p.put( "hibernate.connection.password", "" );
		cfg.setProperties(p);
		serviceRegistry = ServiceRegistryBuilder.buildServiceRegistry( cfg.getProperties() );
	}
	
	@Test
	public void testFirstTypeThenEntity(){
		cfg.addResource("org/hibernate/test/mapping/usertypes/TestEnumType.hbm.xml")
		   .addResource("org/hibernate/test/mapping/usertypes/TestEntity.hbm.xml");
		SessionFactory sessions=cfg.buildSessionFactory(serviceRegistry);
		Assert.assertNotNull(sessions);
	}
	
	@Test
	public void testFirstEntityThenType(){
		cfg.addResource("org/hibernate/test/mapping/usertypes/TestEntity.hbm.xml")
		   .addResource("org/hibernate/test/mapping/usertypes/TestEnumType.hbm.xml");
		
		SessionFactory sessions=cfg.buildSessionFactory(serviceRegistry);
		Assert.assertNotNull(sessions);
	}

}
