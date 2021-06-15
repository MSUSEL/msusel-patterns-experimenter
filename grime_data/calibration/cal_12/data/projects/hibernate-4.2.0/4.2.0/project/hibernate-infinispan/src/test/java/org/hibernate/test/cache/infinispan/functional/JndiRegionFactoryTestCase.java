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
package org.hibernate.test.cache.infinispan.functional;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.infinispan.Cache;
import org.infinispan.lifecycle.ComponentStatus;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;
import org.jboss.util.naming.NonSerializableFactory;
import org.jnp.server.Main;
import org.jnp.server.SingletonNamingServer;
import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.cache.infinispan.InfinispanRegionFactory;
import org.hibernate.cache.infinispan.JndiInfinispanRegionFactory;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.Mappings;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.stat.Statistics;

import static org.junit.Assert.assertEquals;

/**
 * // TODO: Document this
 *
 * @author Galder Zamarreño
 * @since // TODO
 */
public class JndiRegionFactoryTestCase extends SingleNodeTestCase {
	private static final Log log = LogFactory.getLog( JndiRegionFactoryTestCase.class );
	private static final String JNDI_NAME = "java:CacheManager";
	private Main namingMain;
	private SingletonNamingServer namingServer;
	private Properties props;
	private boolean bindToJndi = true;
	private EmbeddedCacheManager manager;

	@Override
	protected void cleanupTest() throws Exception {
		Context ctx = new InitialContext( props );
		unbind( JNDI_NAME, ctx );
		namingServer.destroy();
		namingMain.stop();
		manager.stop(); // Need to stop cos JNDI region factory does not stop it.
	}

	@Override
	protected Class<? extends RegionFactory> getCacheRegionFactory() {
		return JndiInfinispanRegionFactory.class;
	}

	@Override
	public void afterConfigurationBuilt(Mappings mappings, Dialect dialect) {
		if ( bindToJndi ) {
			try {
				// Create an in-memory jndi
				namingServer = new SingletonNamingServer();
				namingMain = new Main();
				namingMain.setInstallGlobalService( true );
				namingMain.setPort( -1 );
				namingMain.start();
				props = new Properties();
				props.put( "java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory" );
				props.put( "java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces" );

				manager = new DefaultCacheManager( InfinispanRegionFactory.DEF_INFINISPAN_CONFIG_RESOURCE, false );
				Context ctx = new InitialContext( props );
				bind( JNDI_NAME, manager, EmbeddedCacheManager.class, ctx );
			}
			catch (Exception e) {
				throw new RuntimeException( "Failure to set up JNDI", e );
			}
		}
	}

	@Override
	public void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( JndiInfinispanRegionFactory.CACHE_MANAGER_RESOURCE_PROP, JNDI_NAME );
		cfg.setProperty( Environment.JNDI_CLASS, "org.jnp.interfaces.NamingContextFactory" );
		cfg.setProperty( "java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces" );
	}

	@Test
	public void testRedeployment() throws Exception {
		addEntityCheckCache( sessionFactory() );
		sessionFactory().close();
		bindToJndi = false;

		SessionFactoryImplementor sessionFactory = (SessionFactoryImplementor) configuration().buildSessionFactory( serviceRegistry() );
		addEntityCheckCache( sessionFactory );
		JndiInfinispanRegionFactory regionFactory = (JndiInfinispanRegionFactory) sessionFactory.getSettings().getRegionFactory();
		Cache cache = regionFactory.getCacheManager().getCache( "org.hibernate.test.cache.infinispan.functional.Item" );
		assertEquals( ComponentStatus.RUNNING, cache.getStatus() );
	}

	private void addEntityCheckCache(SessionFactoryImplementor sessionFactory) throws Exception {
		Item item = new Item( "chris", "Chris's Item" );
		beginTx();
		try {
			Session s = sessionFactory.openSession();
			s.getTransaction().begin();
			s.persist( item );
			s.getTransaction().commit();
			s.close();
		}
		catch (Exception e) {
			setRollbackOnlyTx( e );
		}
		finally {
			commitOrRollbackTx();
		}

		beginTx();
		try {
			Session s =	sessionFactory.openSession();
			Item found = (Item) s.load( Item.class, item.getId() );
			Statistics stats = sessionFactory.getStatistics();
			log.info( stats.toString() );
			assertEquals( item.getDescription(), found.getDescription() );
			assertEquals( 0, stats.getSecondLevelCacheMissCount() );
			assertEquals( 1, stats.getSecondLevelCacheHitCount() );
			s.delete( found );
			s.close();
		}
		catch (Exception e) {
			setRollbackOnlyTx( e );
		}
		finally {
			commitOrRollbackTx();
		}

	}

	/**
	 * Helper method that binds the a non serializable object to the JNDI tree.
	 *
	 * @param jndiName Name under which the object must be bound
	 * @param who Object to bind in JNDI
	 * @param classType Class type under which should appear the bound object
	 * @param ctx Naming context under which we bind the object
	 * @throws Exception Thrown if a naming exception occurs during binding
	 */
	private void bind(String jndiName, Object who, Class<?> classType, Context ctx) throws Exception {
		// Ah ! This service isn't serializable, so we use a helper class
		NonSerializableFactory.bind( jndiName, who );
		Name n = ctx.getNameParser( "" ).parse( jndiName );
		while ( n.size() > 1 ) {
			String ctxName = n.get( 0 );
			try {
				ctx = (Context) ctx.lookup( ctxName );
			}
			catch (NameNotFoundException e) {
				log.debug( "creating Subcontext " + ctxName );
				ctx = ctx.createSubcontext( ctxName );
			}
			n = n.getSuffix( 1 );
		}

		// The helper class NonSerializableFactory uses address type nns, we go on to
		// use the helper class to bind the service object in JNDI
		StringRefAddr addr = new StringRefAddr( "nns", jndiName );
		Reference ref = new Reference( classType.getName(), addr, NonSerializableFactory.class.getName(), null );
		ctx.rebind( n.get( 0 ), ref );
	}

	private void unbind(String jndiName, Context ctx) throws Exception {
		NonSerializableFactory.unbind( jndiName );
//      ctx.unbind(jndiName);
	}

}
