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
package org.hibernate.test.connections;
import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.internal.util.SerializationHelper;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Common test cases relating to session management and how the sessions
 * manages its underlying jdbc connection across different config
 * scenarios.  The different config scenarios are controlled by the
 * individual test subclasses.
 * <p/>
 * In general, all the tests required are defined here in templated fashion.
 * Subclassed then override various hook methods specific to their given
 * scneario being tested.
 *
 * @author Steve Ebersole
 */

public abstract class ConnectionManagementTestCase extends BaseCoreFunctionalTestCase {
	@Override
	public final String[] getMappings() {
		return new String[] { "connections/Silly.hbm.xml" };
	}


	// hooks ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Used to prepare the environment for testing (e.g., starting a
	 * JTA transaction or obtaining a user-supplied connection).
	 *
	 * @throws Throwable indicates problems preparing
	 */
	protected void prepare() throws Throwable {
	}

	/**
	 * Used to cleanup the environment after testing (e.g., ending a JTA
	 * transaction or closing a user-supplied connection).
	 *
	 * @throws Throwable indicates problems cleaning up
	 */
	protected void done() throws Throwable {
	}

	/**
	 * Used to get a session configured based on the config scenario being
	 * tested.
	 *
	 * @return The session to be used in testing.
	 * @throws Throwable Indicates problems building a test session fixture.
	 */
	protected abstract Session getSessionUnderTest() throws Throwable;

	/**
	 * Used to release a {@link #getSessionUnderTest fixture session}.
	 * Overridden to perform session releasing/testing specific to the given
	 * config scenario being tested.
	 *
	 * @param session The session to be released.
	 */
	protected void release(Session session) {
		if ( session != null && session.isOpen() ) {
			try {
				session.close();
			}
			catch( Throwable ignore ) {
			}
		}
	}

	protected void disconnect(Session session) throws Throwable {
		session.disconnect();
	}

	/**
	 * Perform any steps needed to reconnect a fixture session.
	 *
	 * @param session The fixture session to be reconnected.
	 * @throws Throwable Indicates problems reconnecting.
	 */
	protected abstract void reconnect(Session session) throws Throwable;

	/**
	 * Check the state of a fixture session after serialization, as well
	 * as validate the environmental state after session serialization.
	 *
	 * @param session The fixture session that was serialized.
	 */
	protected void checkSerializedState(Session session) {
	}

	/**
	 * Check the state of a fixture session after deserialization, as well
	 * as validate the environmental state after session deserialization.
	 *
	 * @param session The fixture session that was deserialized.
	 */
	protected void checkDeserializedState(Session session) {
	}


	// tests ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests to validate that a session holding JDBC resources will not
	 * be allowed to serialize.
	 */
	@Test
	public final void testConnectedSerialization() throws Throwable {
		prepare();
		Session sessionUnderTest = getSessionUnderTest();

		// force the connection to be retained
		sessionUnderTest.createQuery( "from Silly" ).scroll();

		try {
			SerializationHelper.serialize( sessionUnderTest );

			fail( "Serialization of connected session allowed!" );
		}
		catch( IllegalStateException e ) {
			// expected behaviour
		}
		finally {
			release( sessionUnderTest );
			done();
		}
	}

	/**
	 * Tests to validate that a session holding JDBC resources will not
	 * be allowed to serialize.
	 */
	@Test
	public final void testEnabledFilterSerialization() throws Throwable {
		prepare();
		Session sessionUnderTest = getSessionUnderTest();

		sessionUnderTest.enableFilter( "nameIsNull" );
		assertNotNull( sessionUnderTest.getEnabledFilter( "nameIsNull" ) );
		disconnect( sessionUnderTest );
		assertNotNull( sessionUnderTest.getEnabledFilter( "nameIsNull" ) );

		byte[] bytes = SerializationHelper.serialize( sessionUnderTest );
		checkSerializedState( sessionUnderTest );
		assertNotNull( sessionUnderTest.getEnabledFilter( "nameIsNull" ) );
		reconnect( sessionUnderTest );
		assertNotNull( sessionUnderTest.getEnabledFilter( "nameIsNull" ) );
		disconnect( sessionUnderTest );
		assertNotNull( sessionUnderTest.getEnabledFilter( "nameIsNull" ) );

		Session s2 = ( Session ) SerializationHelper.deserialize( bytes );
		checkDeserializedState( s2 );
		assertNotNull( sessionUnderTest.getEnabledFilter( "nameIsNull" ) );
		reconnect( s2 );
		assertNotNull( sessionUnderTest.getEnabledFilter( "nameIsNull" ) );

		disconnect( s2 );
		assertNotNull( sessionUnderTest.getEnabledFilter( "nameIsNull" ) );
		reconnect( s2 );
		assertNotNull( sessionUnderTest.getEnabledFilter( "nameIsNull" ) );

		release( sessionUnderTest );
		release( s2 );
		done();
	}

	/**
	 * Test that a session which has been manually disconnected will be allowed
	 * to serialize.
	 */
	@Test
	public final void testManualDisconnectedSerialization() throws Throwable {
		prepare();
		Session sessionUnderTest = getSessionUnderTest();

		disconnect( sessionUnderTest );

		SerializationHelper.serialize( sessionUnderTest );
		checkSerializedState( sessionUnderTest );

		release( sessionUnderTest );
		done();
	}

	/**
	 * Test that the legacy manual disconnect()/reconnect() chain works as
	 * expected in the given environment.
	 */
	@Test
	public final void testManualDisconnectChain() throws Throwable {
		prepare();
		Session sessionUnderTest = getSessionUnderTest();

		disconnect( sessionUnderTest );

		byte[] bytes = SerializationHelper.serialize( sessionUnderTest );
		checkSerializedState( sessionUnderTest );
		Session s2 = ( Session ) SerializationHelper.deserialize( bytes );
		checkDeserializedState( s2 );

		reconnect( s2 );

		disconnect( s2 );
		reconnect( s2 );

		release( sessionUnderTest );
		release( s2 );
		done();
	}

	/**
	 * Test that the legacy manual disconnect()/reconnect() chain works as
	 * expected in the given environment.  Similar to {@link #testManualDisconnectChain}
	 * expect that here we force the session to acquire and hold JDBC resources
	 * prior to disconnecting.
	 */
	@Test
	public final void testManualDisconnectWithOpenResources() throws Throwable {
		prepare();
		Session sessionUnderTest = getSessionUnderTest();

		Silly silly = new Silly( "tester" );
		sessionUnderTest.save( silly );
		sessionUnderTest.flush();

		sessionUnderTest.createQuery( "from Silly" ).iterate();

		disconnect( sessionUnderTest );
		SerializationHelper.serialize( sessionUnderTest );
		checkSerializedState( sessionUnderTest );

		reconnect( sessionUnderTest );
		sessionUnderTest.createQuery( "from Silly" ).scroll();

		disconnect( sessionUnderTest );
		SerializationHelper.serialize( sessionUnderTest );
		checkSerializedState( sessionUnderTest );

		reconnect( sessionUnderTest );
		sessionUnderTest.delete( silly );
		sessionUnderTest.flush();

		release( sessionUnderTest );
		done();
	}

	/**
	 * Test that the basic session usage template works in all environment
	 * scenarios.
	 */
	@Test
	public void testBasicSessionUsage() throws Throwable {
		prepare();
		Session s = null;
		Transaction txn = null;
		try {
			s = getSessionUnderTest();
			txn = s.beginTransaction();
			s.createQuery( "from Silly" ).list();
			txn.commit();
		}
		catch( Throwable t ) {
			if ( txn != null ) {
				try {
					txn.rollback();
				}
				catch( Throwable ignore ) {
				}
			}
		}
		finally {
			if ( s != null && s.isOpen() ) {
				try {
					s.close();
				}
				catch( Throwable ignore ) {
				}
			}
		}
		done();
	}

	/**
	 * Test that session-closed protections work properly in all environments.
	 */
	@Test
	public void testSessionClosedProtections() throws Throwable {
		prepare();
		Session s = getSessionUnderTest();
		release( s );
		done();
		assertFalse( s.isOpen() );
		assertFalse( s.isConnected() );
		assertNotNull( s.getStatistics() );
		assertNotNull( s.toString() );

		try {
			s.createQuery( "from Silly" ).list();
			fail( "allowed to create query on closed session" );
		}
		catch( Throwable ignore ) {
		}

		try {
			s.getTransaction();
			fail( "allowed to access transaction on closed session" );
		}
		catch( Throwable ignore ) {
		}

		try {
			s.close();
			fail( "allowed to close already closed session" );
		}
		catch( Throwable ignore ) {
		}

		try {
			s.isDirty();
			fail( "allowed to check dirtiness of closed session" );
		}
		catch( Throwable ignore ) {
		}
	}
}
