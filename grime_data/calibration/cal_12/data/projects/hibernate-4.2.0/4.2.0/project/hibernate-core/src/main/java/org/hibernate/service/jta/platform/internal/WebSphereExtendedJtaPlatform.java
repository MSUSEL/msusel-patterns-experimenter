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
package org.hibernate.service.jta.platform.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import javax.transaction.xa.XAResource;

import org.hibernate.HibernateException;

/**
 * JTA platform implementation intended for use with WebSphere Application Server (WAS).
 * <p/>
 * WAS, unlike every other app server on the planet, does not allow direct access to the JTS TransactionManager.
 * Instead, for common transaction-related tasks users must utilize a proprietary API known as ExtendedJTATransaction.
 * <p/>
 * Even more unfortunate, the exact TransactionManagerLookup to use inside of WAS is highly dependent upon<ul>
 *     <li>WAS version</li>
 *     <li>the WAS container in which Hibernate will be utilized</li>
 * </ul>
 * <p/>
 * This class is reported to work on WAS version 6 in any of the standard J2EE/JEE component containers.
 *
 * @author Gavin King
 * @author <a href="mailto:jesper@udby.com>Jesper Udby</a>
 * @author Steve Ebersole
 */
public class WebSphereExtendedJtaPlatform extends AbstractJtaPlatform {
	public static final String UT_NAME = "java:comp/UserTransaction";

	@Override
	protected boolean canCacheTransactionManager() {
		return true;
	}

	@Override
	protected TransactionManager locateTransactionManager() {
		return new TransactionManagerAdapter();
	}

	@Override
	protected UserTransaction locateUserTransaction() {
		return (UserTransaction) jndiService().locate( UT_NAME );
	}

	@Override
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public Object getTransactionIdentifier(Transaction transaction) {
		// WebSphere, however, is not a sane JEE/JTA container...
		return Integer.valueOf( transaction.hashCode() );
	}

	public class TransactionManagerAdapter implements TransactionManager {
		private final Class synchronizationCallbackClass;
		private final Method registerSynchronizationMethod;
		private final Method getLocalIdMethod;
		private Object extendedJTATransaction;

		private TransactionManagerAdapter() throws HibernateException {
			try {
				synchronizationCallbackClass = Class.forName( "com.ibm.websphere.jtaextensions.SynchronizationCallback" );
				Class extendedJTATransactionClass = Class.forName( "com.ibm.websphere.jtaextensions.ExtendedJTATransaction" );
				registerSynchronizationMethod = extendedJTATransactionClass.getMethod(
						"registerSynchronizationCallbackForCurrentTran",
						new Class[] { synchronizationCallbackClass }
				);
				getLocalIdMethod = extendedJTATransactionClass.getMethod( "getLocalId", (Class[]) null );
			}
			catch ( ClassNotFoundException cnfe ) {
				throw new HibernateException( cnfe );
			}
			catch ( NoSuchMethodException nsme ) {
				throw new HibernateException( nsme );
			}
		}

		@Override
		public void begin() throws NotSupportedException, SystemException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void commit() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getStatus() throws SystemException {
			return getTransaction() == null ? Status.STATUS_NO_TRANSACTION : getTransaction().getStatus();
		}

		@Override
		public Transaction getTransaction() throws SystemException {
			return new TransactionAdapter();
		}

		@Override
		public void resume(Transaction txn) throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void rollback() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRollbackOnly() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTransactionTimeout(int i) throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Transaction suspend() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		public class TransactionAdapter implements Transaction {

			private TransactionAdapter() {
				if ( extendedJTATransaction == null ) {
					extendedJTATransaction = jndiService().locate( "java:comp/websphere/ExtendedJTATransaction" );
				}
			}

			@Override
			public void registerSynchronization(final Synchronization synchronization)
					throws RollbackException, IllegalStateException,
					SystemException {

				final InvocationHandler ih = new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if ( "afterCompletion".equals( method.getName() ) ) {
							int status = args[2].equals(Boolean.TRUE) ?
									Status.STATUS_COMMITTED :
									Status.STATUS_UNKNOWN;
							synchronization.afterCompletion(status);
						}
						else if ( "beforeCompletion".equals( method.getName() ) ) {
							synchronization.beforeCompletion();
						}
						else if ( "toString".equals( method.getName() ) ) {
							return synchronization.toString();
						}
						return null;
					}

				};

				final Object synchronizationCallback = Proxy.newProxyInstance(
						getClass().getClassLoader(),
						new Class[] {synchronizationCallbackClass},
						ih
				);

				try {
					registerSynchronizationMethod.invoke( extendedJTATransaction, synchronizationCallback );
				}
				catch (Exception e) {
					throw new HibernateException(e);
				}

			}

			@Override
			public int hashCode() {
				return getLocalId().hashCode();
			}

			@Override
			public boolean equals(Object other) {
				if ( !(other instanceof TransactionAdapter) ) return false;
				TransactionAdapter that = (TransactionAdapter) other;
				return getLocalId().equals( that.getLocalId() );
			}

			private Object getLocalId() throws HibernateException {
				try {
					return getLocalIdMethod.invoke( extendedJTATransaction, (Object[]) null );
				}
				catch ( Exception e ) {
					throw new HibernateException( e );
				}
			}

			@Override
			public void commit() throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean delistResource(XAResource resource, int i) throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean enlistResource(XAResource resource) throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}

			@Override
			public int getStatus() {
				return Integer.valueOf( 0 ).equals( getLocalId() ) ?
						Status.STATUS_NO_TRANSACTION : Status.STATUS_ACTIVE;
			}

			@Override
			public void rollback() throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}

			@Override
			public void setRollbackOnly() throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}
		}
	}

}
