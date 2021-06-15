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
package org.hibernate.ejb;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author Gavin King
 * @author Emmanuel Bernard
 */
public class TransactionImpl implements EntityTransaction {

	private HibernateEntityManagerImplementor entityManager;
	private Transaction tx;
	private boolean rollbackOnly;

	public TransactionImpl(AbstractEntityManagerImpl entityManager) {
		this.entityManager = entityManager;
	}

	private Session getSession() {
		return entityManager.getSession();
	}

	public void begin() {
		try {
			rollbackOnly = false;
			if ( tx != null && tx.isActive() ) {
				throw new IllegalStateException( "Transaction already active" );
			}
			//entityManager.adjustFlushMode();
			tx = getSession().beginTransaction();
		}
		catch (HibernateException he) {
			entityManager.throwPersistenceException( he );
		}
	}

	public void commit() {
		if ( tx == null || !tx.isActive() ) {
			throw new IllegalStateException( "Transaction not active" );
		}
		if ( rollbackOnly ) {
			tx.rollback();
			throw new RollbackException( "Transaction marked as rollbackOnly" );
		}
		try {
			tx.commit();
		}
		catch (Exception e) {
			Exception wrappedException;
			if (e instanceof HibernateException) {
				wrappedException = entityManager.convert( (HibernateException)e );
			}
			else {
				wrappedException = e;
			}
			try {
				//as per the spec we should rollback if commit fails
				tx.rollback();
			}
			catch (Exception re) {
				//swallow
			}
			throw new RollbackException( "Error while committing the transaction", wrappedException );
		}
		finally {
			rollbackOnly = false;
		}
		//if closed and we commit, the mode should have been adjusted already
		//if ( entityManager.isOpen() ) entityManager.adjustFlushMode();
	}

	public void rollback() {
		if ( tx == null || !tx.isActive() ) {
			throw new IllegalStateException( "Transaction not active" );
		}
		try {
			tx.rollback();
		}
		catch (Exception e) {
			throw new PersistenceException( "unexpected error when rollbacking", e );
		}
		finally {
			try {
				if (entityManager !=  null) {
					Session session = getSession();
					if ( session != null && session.isOpen() ) session.clear();
				}
			}
			catch (Throwable t) {
				//we don't really care here since it's only for safety purpose
			}
			rollbackOnly = false;
		}
	}

	public void setRollbackOnly() {
		if ( ! isActive() ) throw new IllegalStateException( "Transaction not active" );
		this.rollbackOnly = true;
	}

	public boolean getRollbackOnly() {
		if ( ! isActive() ) throw new IllegalStateException( "Transaction not active" );
		return rollbackOnly;
	}

	public boolean isActive() {
		try {
			return tx != null && tx.isActive();
		}
		catch (RuntimeException e) {
			throw new PersistenceException( "unexpected error when checking transaction status", e );
		}
	}

}
