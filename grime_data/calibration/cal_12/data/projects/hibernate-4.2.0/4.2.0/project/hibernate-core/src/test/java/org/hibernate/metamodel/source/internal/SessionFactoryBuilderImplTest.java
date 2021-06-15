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
package org.hibernate.metamodel.source.internal;

import java.io.Serializable;
import java.util.Iterator;

import org.junit.Test;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.metamodel.MetadataSources;
import org.hibernate.metamodel.SessionFactoryBuilder;
import org.hibernate.proxy.EntityNotFoundDelegate;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.testing.junit4.BaseUnitTestCase;
import org.hibernate.type.Type;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

/**
 * @author Gail Badner
 */
public class SessionFactoryBuilderImplTest extends BaseUnitTestCase {

	@Test
	public void testGettingSessionFactoryBuilder() {
		SessionFactoryBuilder sessionFactoryBuilder = getSessionFactoryBuilder();
		assertNotNull( sessionFactoryBuilder );
		assertTrue( SessionFactoryBuilderImpl.class.isInstance( sessionFactoryBuilder ) );
	}

	@Test
	public void testBuildSessionFactoryWithDefaultOptions() {
		SessionFactoryBuilder sessionFactoryBuilder = getSessionFactoryBuilder();
		SessionFactory sessionFactory = sessionFactoryBuilder.buildSessionFactory();
		assertSame( EmptyInterceptor.INSTANCE, sessionFactory.getSessionFactoryOptions().getInterceptor() );
		assertTrue( EntityNotFoundDelegate.class.isInstance(
				sessionFactory.getSessionFactoryOptions().getEntityNotFoundDelegate()
		) );
		sessionFactory.close();
	}

	@Test
	public void testBuildSessionFactoryWithUpdatedOptions() {
		SessionFactoryBuilder sessionFactoryBuilder = getSessionFactoryBuilder();
		Interceptor interceptor = new AnInterceptor();
		EntityNotFoundDelegate entityNotFoundDelegate = new EntityNotFoundDelegate() {
			@Override
			public void handleEntityNotFound(String entityName, Serializable id) {
				throw new ObjectNotFoundException( id, entityName );
			}
		};
		sessionFactoryBuilder.with( interceptor );
		sessionFactoryBuilder.with( entityNotFoundDelegate );
		SessionFactory sessionFactory = sessionFactoryBuilder.buildSessionFactory();
		assertSame( interceptor, sessionFactory.getSessionFactoryOptions().getInterceptor() );
		assertSame( entityNotFoundDelegate, sessionFactory.getSessionFactoryOptions().getEntityNotFoundDelegate() );
		sessionFactory.close();
	}

	private SessionFactoryBuilder getSessionFactoryBuilder() {
		MetadataSources sources = new MetadataSources( new ServiceRegistryBuilder().buildServiceRegistry() );
		sources.addAnnotatedClass( SimpleEntity.class );
		MetadataImpl metadata = (MetadataImpl) sources.buildMetadata();
		return  metadata.getSessionFactoryBuilder();
	}

	private static class AnInterceptor implements Interceptor {
		private static final Interceptor INSTANCE = EmptyInterceptor.INSTANCE;

		@Override
		public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
				throws CallbackException {
			return INSTANCE.onLoad( entity, id, state, propertyNames, types );
		}

		@Override
		public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types)
				throws CallbackException {
			return INSTANCE.onFlushDirty( entity, id, currentState, previousState, propertyNames, types );
		}

		@Override
		public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
				throws CallbackException {
			return INSTANCE.onSave( entity, id, state, propertyNames, types );
		}

		@Override
		public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
				throws CallbackException {
			INSTANCE.onDelete( entity, id, state, propertyNames, types );
		}

		@Override
		public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {
			INSTANCE.onCollectionRecreate( collection, key );
		}

		@Override
		public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
			INSTANCE.onCollectionRemove( collection, key );
		}

		@Override
		public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
			INSTANCE.onCollectionUpdate( collection, key );
		}

		@Override
		public void preFlush(Iterator entities) throws CallbackException {
			INSTANCE.preFlush( entities );
		}

		@Override
		public void postFlush(Iterator entities) throws CallbackException {
			INSTANCE.postFlush( entities );
		}

		@Override
		public Boolean isTransient(Object entity) {
			return INSTANCE.isTransient( entity );
		}

		@Override
		public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
			return INSTANCE.findDirty( entity, id, currentState, previousState, propertyNames, types );
		}

		@Override
		public Object instantiate(String entityName, EntityMode entityMode, Serializable id)
				throws CallbackException {
			return INSTANCE.instantiate( entityName, entityMode, id );
		}

		@Override
		public String getEntityName(Object object) throws CallbackException {
			return INSTANCE.getEntityName( object );
		}

		@Override
		public Object getEntity(String entityName, Serializable id) throws CallbackException {
			return INSTANCE.getEntity( entityName, id );
		}

		@Override
		public void afterTransactionBegin(Transaction tx) {
			INSTANCE.afterTransactionBegin( tx );
		}

		@Override
		public void beforeTransactionCompletion(Transaction tx) {
			INSTANCE.beforeTransactionCompletion( tx );
		}

		@Override
		public void afterTransactionCompletion(Transaction tx) {
			INSTANCE.afterTransactionCompletion( tx );
		}

		@Override
		public String onPrepareStatement(String sql) {
			return INSTANCE.onPrepareStatement( sql );
		}
	}
}


