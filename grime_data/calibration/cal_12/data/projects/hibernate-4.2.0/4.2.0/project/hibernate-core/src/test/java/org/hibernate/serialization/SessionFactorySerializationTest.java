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
package org.hibernate.serialization;


import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.junit.Test;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryRegistry;
import org.hibernate.internal.util.SerializationHelper;
import org.hibernate.testing.junit4.BaseUnitTestCase;
import org.hibernate.type.SerializationException;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * @author Steve Ebersole
 */
public class SessionFactorySerializationTest extends BaseUnitTestCase {
	public static final String NAME = "mySF";

	@Test
	public void testNamedSessionFactorySerialization() throws Exception {
		Configuration cfg = new Configuration()
				.setProperty( AvailableSettings.SESSION_FACTORY_NAME, NAME )
				.setProperty( AvailableSettings.SESSION_FACTORY_NAME_IS_JNDI, "false" ); // default is true
		SessionFactory factory = cfg.buildSessionFactory();

		// we need to do some tricking here so that Hibernate thinks the deserialization happens in a
		// different VM
		Reference reference = factory.getReference();
		StringRefAddr refAddr = (StringRefAddr) reference.get( "uuid" );
		String uuid = (String) refAddr.getContent();
		// deregister under this uuid...
		SessionFactoryRegistry.INSTANCE.removeSessionFactory( uuid, NAME, false, null );
		// and then register under a different uuid...
		SessionFactoryRegistry.INSTANCE.addSessionFactory( "some-other-uuid", NAME, false, factory, null );

		SessionFactory factory2 = (SessionFactory) SerializationHelper.clone( factory );
		assertSame( factory, factory2 );
		factory.close();
	}

	@Test
	public void testUnNamedSessionFactorySerialization() throws Exception {
		// IMPL NOTE : this test is a control to testNamedSessionFactorySerialization
		// 		here, the test should fail based just on attempted uuid resolution
		Configuration cfg = new Configuration()
				.setProperty( AvailableSettings.SESSION_FACTORY_NAME_IS_JNDI, "false" ); // default is true
		SessionFactory factory = cfg.buildSessionFactory();

		// we need to do some tricking here so that Hibernate thinks the deserialization happens in a
		// different VM
		Reference reference = factory.getReference();
		StringRefAddr refAddr = (StringRefAddr) reference.get( "uuid" );
		String uuid = (String) refAddr.getContent();
		// deregister under this uuid...
		SessionFactoryRegistry.INSTANCE.removeSessionFactory( uuid, NAME, false, null );
		// and then register under a different uuid...
		SessionFactoryRegistry.INSTANCE.addSessionFactory( "some-other-uuid", NAME, false, factory, null );

		try {
			SerializationHelper.clone( factory );
			fail( "Expecting an error" );
		}
		catch ( SerializationException expected ) {

		}
		factory.close();
	}
}
