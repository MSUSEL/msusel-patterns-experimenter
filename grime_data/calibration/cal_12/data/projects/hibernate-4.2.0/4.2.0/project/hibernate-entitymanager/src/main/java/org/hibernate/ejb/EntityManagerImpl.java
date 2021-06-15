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
package org.hibernate.ejb;

import java.util.Map;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceUnitTransactionType;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionOwner;
import org.hibernate.annotations.common.util.ReflectHelper;
import org.hibernate.ejb.internal.EntityManagerMessageLogger;
import org.hibernate.engine.spi.SessionBuilderImplementor;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * Hibernate implementation of {@link javax.persistence.EntityManager}.
 *
 * @author Gavin King
 */
public class EntityManagerImpl extends AbstractEntityManagerImpl implements SessionOwner {

    public static final EntityManagerMessageLogger LOG = Logger.getMessageLogger(EntityManagerMessageLogger.class,
                                                                          EntityManagerImpl.class.getName());

	protected Session session;
	protected boolean open;
	protected boolean discardOnClose;
	private Class sessionInterceptorClass;

	public EntityManagerImpl(
			EntityManagerFactoryImpl entityManagerFactory,
			PersistenceContextType pcType,
			PersistenceUnitTransactionType transactionType,
			boolean discardOnClose,
			Class sessionInterceptorClass,
			Map properties) {
		super( entityManagerFactory, pcType, transactionType, properties );
		this.open = true;
		this.discardOnClose = discardOnClose;
		Object localSessionInterceptor = null;
		if (properties != null) {
			localSessionInterceptor = properties.get( AvailableSettings.SESSION_INTERCEPTOR );
		}
		if ( localSessionInterceptor != null ) {
			if (localSessionInterceptor instanceof Class) {
				sessionInterceptorClass = (Class) localSessionInterceptor;
			}
			else if (localSessionInterceptor instanceof String) {
				try {
					sessionInterceptorClass =
							ReflectHelper.classForName( (String) localSessionInterceptor, EntityManagerImpl.class );
				}
				catch (ClassNotFoundException e) {
					throw new PersistenceException("Unable to instanciate interceptor: " + localSessionInterceptor, e);
				}
			}
			else {
				throw new PersistenceException("Unable to instanciate interceptor: " + localSessionInterceptor);
			}
		}
		this.sessionInterceptorClass = sessionInterceptorClass;
		postInit();
	}

	@Override
    public Session getSession() {
		if ( !open ) {
			throw new IllegalStateException( "EntityManager is closed" );
		}
		return getRawSession();
	}

	@Override
    protected Session getRawSession() {
		if ( session == null ) {
			SessionBuilderImplementor sessionBuilder = getEntityManagerFactory().getSessionFactory().withOptions();
			sessionBuilder.owner( this );
			if (sessionInterceptorClass != null) {
				try {
					Interceptor interceptor = (Interceptor) sessionInterceptorClass.newInstance();
					sessionBuilder.interceptor( interceptor );
				}
				catch (InstantiationException e) {
					throw new PersistenceException("Unable to instanciate session interceptor: " + sessionInterceptorClass, e);
				}
				catch (IllegalAccessException e) {
					throw new PersistenceException("Unable to instanciate session interceptor: " + sessionInterceptorClass, e);
				}
				catch (ClassCastException e) {
					throw new PersistenceException("Session interceptor does not implement Interceptor: " + sessionInterceptorClass, e);
				}
			}
			sessionBuilder.autoJoinTransactions( getTransactionType() != PersistenceUnitTransactionType.JTA );
			session = sessionBuilder.openSession();
			if ( persistenceContextType == PersistenceContextType.TRANSACTION ) {
				( (SessionImplementor) session ).setAutoClear( true );
			}
		}
		return session;
	}

	public void close() {
		checkEntityManagerFactory();
		if ( !open ) {
			throw new IllegalStateException( "EntityManager is closed" );
		}
		if ( discardOnClose || !isTransactionInProgress() ) {
			//close right now
			if ( session != null ) {
				session.close();
			}
		}
		// Otherwise, session auto-close will be enabled by shouldAutoCloseSession().
		open = false;
	}

	public boolean isOpen() {
		//adjustFlushMode(); //don't adjust, can't be done on closed EM
		checkEntityManagerFactory();
		try {
			if ( open ) {
				getSession().isOpen(); //to force enlistment in tx
			}
			return open;
		}
		catch (HibernateException he) {
			throwPersistenceException( he );
			return false;
		}
	}

	@Override
	public boolean shouldAutoCloseSession() {
		return !isOpen();
	}

	private void checkEntityManagerFactory() {
		if (! getEntityManagerFactory().isOpen()) {
			open = false;
		}
	}
}
